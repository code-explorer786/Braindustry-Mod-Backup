package braindustry.annotations.backgroundMenu;

import arc.files.Fi;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.struct.StringMap;
import arc.util.Log;
import arc.util.serialization.Json;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import mindustry.annotations.BaseProcessor;
import mindustry.annotations.util.TypeIOResolver.ClassSerializer;

import javax.lang.model.element.Modifier;

import static mindustry.annotations.BaseProcessor.instanceOf;

public class BackgroundIO {
    final static Json json = new Json();
    //suffixes for sync fields
    //replacements after refactoring
    final static StringMap replacements = StringMap.of("mindustry.entities.units.BuildRequest", "mindustry.entities.units.BuildPlan");

    final ClassSerializer serializer;
    final String name;
    final TypeSpec.Builder type;
    final Fi directory;
    final Seq<Revision> revisions = new Seq<>();

    boolean write;
    MethodSpec.Builder method;
    ObjectSet<String> presentFields = new ObjectSet<>();

    BackgroundIO(String name, TypeSpec.Builder type, Seq<FieldSpec> typeFields, ClassSerializer serializer, Fi directory){
        this.directory = directory;
        this.type = type;
        this.serializer = serializer;
        this.name = name;

        json.setIgnoreUnknownFields(true);

        directory.mkdirs();

        //load old revisions
        for(Fi fi : directory.list()){
            revisions.add(json.fromJson(Revision.class, fi));
        }

        revisions.sort(r -> r.version);

        //next revision to be used
        int nextRevision = revisions.isEmpty() ? 0 : revisions.max(r -> r.version).version + 1;

        //resolve preferred field order based on fields that fit
        Seq<FieldSpec> fields = typeFields.select(spec ->
                !spec.hasModifier(Modifier.TRANSIENT) &&
                        !spec.hasModifier(Modifier.STATIC) &&
                        !spec.hasModifier(Modifier.FINAL)/* &&
            (spec.type.isPrimitive() || serializer.has(spec.type.toString()))*/);

        //sort to keep order
        fields.sortComparing(f -> f.name);

        //keep track of fields present in the entity
        presentFields.addAll(fields.map(f -> f.name));

        Revision previous = revisions.isEmpty() ? null : revisions.peek();

        //add new revision if it doesn't match or there are no revisions
        if(revisions.isEmpty() || !revisions.peek().equal(fields)){
            revisions.add(new Revision(nextRevision,
                    fields.map(f -> new RevisionField(f.name, f.type.toString()))));
            Log.warn("Adding new revision @ for @.\nPre = @\nNew = @\n", nextRevision, name, previous == null ? null : previous.fields.toString(", ", f -> f.name + ":" + f.type), fields.toString(", ", f -> f.name + ":" + f.type.toString()));
            //write revision
            directory.child(nextRevision + ".json").writeString(json.toJson(revisions.peek()));
        }
    }

    void write(MethodSpec.Builder method, boolean write) throws Exception{
        this.method = method;
        this.write = write;

        //subclasses *have* to call this method
//        method.addAnnotation(BDAnnotations.CallSuper.class);

        if(write){
            //write short revision
            st("write.s($L)", revisions.peek().version);
            //write uses most recent revision
            for(RevisionField field : revisions.peek().fields){
                io(field.type, "this." + field.name);
            }
        }else{
            //read revision
            st("short REV = read.s()");

            for(int i = 0; i < revisions.size; i++){
                //check for the right revision
                Revision rev = revisions.get(i);
                if(i == 0){
                    cont("if(REV == $L)", rev.version);
                }else{
                    ncont("else if(REV == $L)", rev.version);
                }

                //add code for reading revision
                for(RevisionField field : rev.fields){
                    //if the field doesn't exist, the result will be an empty string, it won't get assigned
                    io(field.type, presentFields.contains(field.name) ? "this." + field.name + " = " : "");
                }
            }

            //throw exception on illegal revisions
            ncont("else");
            st("throw new IllegalArgumentException(\"Unknown revision '\" + REV + \"' for entity type '" + name + "'\")");
            econt();
        }
    }
    private void io(String type, String field) throws Exception{
        type = type.replace("mindustry.gen.", "");
        type = replacements.get(type, type);

        if(BaseProcessor.isPrimitive(type)){
            s(type.equals("boolean") ? "bool" : type.charAt(0) + "", field);
        }else if(serializer.writers.containsKey(type) && write){
            st("$L(write, $L)", serializer.writers.get(type), field);
        }else if(serializer.mutatorReaders.containsKey(type) && !write && !field.replace(" = ", "").contains(" ") && !field.isEmpty()){
            st("$L$L(read, $L)", field, serializer.mutatorReaders.get(type), field.replace(" = ", ""));
        }else if(serializer.readers.containsKey(type) && !write){
            st("$L$L(read)", field, serializer.readers.get(type));
        }else if(instanceOf(type, "mindustry.ctype.Content") && false){
            if(write){
                s("s", field + ".id");
            }else{
                String simpleName = BaseProcessor.simpleName(type);
                String contentType =  simpleName.toLowerCase().replace("type", "");
                if (simpleName.contains("Unit")) {
                    contentType = "unit";
                } else if (contentType.equals("gas")){
                    contentType="typeid_UNUSED";
                } else if (contentType.equals("floor")||contentType.equals("staticwall")){
                    contentType="block";
                }
                st(field + "mindustry.Vars.content.getByID(mindustry.ctype.ContentType.$L, read.s())", contentType);
            }
        }else if(type.endsWith("[]")){ //it's a 1D array
            String rawType = type.substring(0, type.length() - 2);

            if(write){
                s("i", field + ".length");
                cont("for(int INDEX = 0; INDEX < $L.length; INDEX ++)", field);
                io(rawType, field + "[INDEX]");
            }else{
                String fieldName = field.replace(" = ", "").replace("this.", "");
                String lenf = fieldName + "_LENGTH";
                s("i", "int " + lenf + " = ");
                if(!field.isEmpty()){
                    st("$Lnew $L[$L]", field, type.replace("[]", ""), lenf);
                }
                cont("for(int INDEX = 0; INDEX < $L; INDEX ++)", lenf);
                io(rawType, field.replace(" = ", "[INDEX] = "));
            }

            econt();
        }else if(type.startsWith("arc.struct") && type.contains("<")){ //it's some type of data structure
            String struct = type.substring(0, type.indexOf("<"));
            String generic = type.substring(type.indexOf("<") + 1, type.indexOf(">"));

            if(struct.equals("arc.struct.Queue") || struct.equals("arc.struct.Seq")){
                if(write){
                    s("i", field + ".size");
                    cont("for(int INDEX = 0; INDEX < $L.size; INDEX ++)", field);
                    io(generic, field + ".get(INDEX)");
                }else{
                    String fieldName = field.replace(" = ", "").replace("this.", "");
                    String lenf = fieldName + "_LENGTH";
                    s("i", "int " + lenf + " = ");
                    if(!field.isEmpty()){
                        st("$L.clear()", field.replace(" = ", ""));
                    }
                    cont("for(int INDEX = 0; INDEX < $L; INDEX ++)", lenf);
                    io(generic, field.replace(" = ", "_ITEM = ").replace("this.", generic + " "));
                    if(!field.isEmpty()){
                        String temp = field.replace(" = ", "_ITEM").replace("this.", "");
                        st("if($L != null) $L.add($L)", temp, field.replace(" = ", ""), temp);
                    }
                }

                econt();
            }else{
                Log.warn("Missing serialization code for collection '@' in '@'", type, name);
            }
        }else{
            Log.warn("Missing serialization code for type '@' in '@'", type, name);
        }
    }

    private void cont(String text, Object... fmt){
        method.beginControlFlow(text, fmt);
    }

    private void econt(){
        method.endControlFlow();
    }

    private void ncont(String text, Object... fmt){
        method.nextControlFlow(text, fmt);
    }

    private void st(String text, Object... args){
        method.addStatement(text, args);
    }

    private void s(String type, String field){
        if(write){
            method.addStatement("write.$L($L)", type, field);
        }else{
            method.addStatement("$Lread.$L()", field, type);
        }
    }

    public static class Revision{
        int version;
        Seq<RevisionField> fields;

        Revision(int version, Seq<RevisionField> fields){
            this.version = version;
            this.fields = fields;
        }

        Revision(){}

        /** @return whether these two revisions are compatible */
        boolean equal(Seq<FieldSpec> specs){
            if(fields.size != specs.size) return false;

            for(int i = 0; i < fields.size; i++){
                RevisionField field = fields.get(i);
                FieldSpec spec = specs.get(i);
                if(!field.type.replace("mindustry.gen.", "").equals(spec.type.toString().replace("mindustry.gen.", ""))){
                    return false;
                }
            }
            return true;
        }
    }

    public static class RevisionField{
        String name, type;

        RevisionField(String name, String type){
            this.name = name;
            this.type = type;
        }

        RevisionField(){}
    }
}
