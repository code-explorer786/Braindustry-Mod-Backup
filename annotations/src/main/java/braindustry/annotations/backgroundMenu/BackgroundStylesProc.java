package braindustry.annotations.backgroundMenu;

import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.BDAnnotations;

import com.squareup.javapoet.*;
import mindustry.annotations.util.*;
import mma.annotations.ModBaseProcessor;
import mma.annotations.remote.ModTypeIOResolver;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes({
        "braindustry.annotations.BDAnnotations.BackgroundStyleSources",
        "mindustry.annotations.Annotations.TypeIOHandler",
})
public class BackgroundStylesProc extends ModBaseProcessor {
    ObjectSet<String> imports = new ObjectSet<>();
    TypeIOResolver.ClassSerializer serializer;
    Seq<FieldSpec> allFieldSpecs = new Seq<>();
    MethodSpec.Builder setMethodBuilder = MethodSpec.methodBuilder("set");
    ClassName backgroundStyleClass, writesClass, readsClass, backgroundSettingsClass, seqClass;
    String lastUsedKey = "braindustry.styles.lastUsed";

    public static boolean instanceOf(String type, String other) {
        TypeElement a = elementu.getTypeElement(type);
        TypeElement b = elementu.getTypeElement(other);
        return a != null && b != null && typeu.isSubtype(a.asType(), b.asType());
    }

    @Override
    public void process(RoundEnvironment env) throws Exception {
        backgroundStyleClass = ClassName.bestGuess("BackgroundStyle");
        backgroundSettingsClass = ClassName.bestGuess("BackgroundSettings");
        writesClass = ClassName.get("braindustry.io", "ZelWrites");
        readsClass = ClassName.get("braindustry.io", "ZelReads");
        seqClass = ClassName.get("arc.struct", "Seq");
        imports.clear();
        allFieldSpecs.clear();
        serializer = ModTypeIOResolver. resolve(this);

        setMethodBuilder.addParameter(backgroundStyleClass, "other");


        Seq<Selement> elements = elements(BDAnnotations.BackgroundStyleSources.class);
        Seq<Svar> fields = elements.select(Selement::isVar).map(Selement::asVar);
        Seq<Stype> classes = elements.select(Selement::isType).map(Selement::asType);
        Seq<Smethod> methods = elements.select(Selement::isMethod).map(Selement::asMethod);

        makeSettingsClass(fields, methods);
        makeBackgroundStyleClass(fields, methods);
    }

    private void makeSettingsClass(Seq<Svar> fields, Seq<Smethod> methods) throws Exception {
        TypeSpec.Builder settingsBuilder = TypeSpec.classBuilder("BackgroundSettings").addModifiers(Modifier.PUBLIC);

        settingsBuilder.addField(FieldSpec.builder(backgroundStyleClass, "lastStyle", Modifier.STATIC).initializer("$T.load(\"\"+arc.Core.settings.get($S,$S))", backgroundStyleClass, lastUsedKey, "default").build());

        settingsBuilder.addMethod(MethodSpec.methodBuilder("setLastStyle")
                .addParameter(String.class, "name")
                .addStatement("lastStyle=$T.load(name)", backgroundStyleClass)
                .addStatement("arc.Core.settings.put($S,name)", lastUsedKey)
                .build()
        );

        for (Smethod method : methods) {
            MethodSpec.Builder builder = MethodSpec.methodBuilder(method.name())
                    .addCode(method.tree().getBody().toString())
                    .returns(method.retn())
                    .addModifiers(method.tree().getModifiers().getFlags());
            for (Svar param : method.params()) {
                builder.addParameter(param.tname(), param.name());
            }
            settingsBuilder.addMethod(builder.build());
        }
        for (Svar field : fields) {
            BDAnnotations.BackgroundStyleSources sources = field.annotation(BDAnnotations.BackgroundStyleSources.class);

            handleSettingField(settingsBuilder, imports, field, sources);
        }/*
        Seq<MethodSpec> settets = Seq.with(settingsBuilder.methodSpecs).select(m -> m.returnType.equals(TypeName.VOID));
        Seq<MethodSpec> getters = Seq.with(settingsBuilder.methodSpecs).select(m -> !m.returnType.equals(TypeName.VOID));
        settets.sort(methodSpec -> methodSpec.parameters.size());
        MethodSpec.methodBuilder("get").addParameter();*/

        write(settingsBuilder, imports.asArray());
    }

    private void makeBackgroundStyleClass(Seq<Svar> fields, Seq<Smethod> methods) throws Exception {
        TypeSpec.Builder styleBuilder = TypeSpec.classBuilder("BackgroundStyle")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).addParameter(String.class, "name").addStatement("this.name=name")
                        .build())
                .addField(String.class, "name", Modifier.PRIVATE);
        styleBuilder.addField(FieldSpec.builder(writesClass, "write", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).initializer("new $T()", writesClass).build());
        styleBuilder.addField(FieldSpec.builder(readsClass, "read", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).initializer("new $T()", readsClass).build());

        //add name getter
        styleBuilder.addMethod(MethodSpec.methodBuilder("name")
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return name")
                .build());

        for (Svar field : fields) {
            BDAnnotations.BackgroundStyleSources sources = field.annotation(BDAnnotations.BackgroundStyleSources.class);
            if (sources.setting() || sources.keyOnly()) continue;
            handleStyleField(styleBuilder, field, sources);
        }
        BackgroundIO io = new BackgroundIO("BackgroundStyle", styleBuilder, allFieldSpecs, serializer, rootDirectory.child("annotations/src/main/resources/revisions").child("BackgroundStyle"));

        //add read method
        styleBuilder.addMethod(MethodSpec.methodBuilder("readBytes")
                .addParameter(byte[].class, "bytes")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("read.setBytes(bytes)")
                .addStatement("read(read)")
                .build());
        MethodSpec.Builder readBuilder = MethodSpec.methodBuilder("read")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Reads.class, "read");
        ;
        io.write(readBuilder, false);
        styleBuilder.addMethod(readBuilder.build());

        //add write method
        styleBuilder.addMethod(MethodSpec.methodBuilder("writeBytes")
                .returns(byte[].class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("write.reset()")
                .addStatement("write(write)")
                .addStatement("return write.getBytes()")
                .build());
        MethodSpec.Builder writeBuilder = MethodSpec.methodBuilder("write")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Writes.class, "write");
        io.write(writeBuilder, true);
        styleBuilder.addMethod(writeBuilder.build());
        ;

        //load and save methods
        styleBuilder.addMethod(MethodSpec.methodBuilder("save").addModifiers(Modifier.PUBLIC)
                .addStatement("save(this)")
                .build());
        styleBuilder.addMethod(MethodSpec.methodBuilder("save")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(backgroundStyleClass, "style")
                .addStatement("if (style==null)return")
                .addStatement("$T<$T> seq=$T.getStyles()", seqClass, backgroundStyleClass, backgroundSettingsClass)
                .beginControlFlow("if (seq.contains(style))")
                .addStatement("seq.set(seq.indexOf(style),style)")
                .nextControlFlow("else")
                .addStatement("seq.add(style)")
                .endControlFlow()
                .addStatement("$T.saveStyles(seq)", backgroundSettingsClass)
//                .addStatement("arc.Core.settings.put(\"braindustry.background.styles.\"+name,bytes)")
                .build());
        styleBuilder.addMethod(MethodSpec.methodBuilder("load")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String.class, "name")
                .returns(backgroundStyleClass)
                .addStatement("$T<$T> seq=$T.getStyles()", seqClass, backgroundStyleClass, backgroundSettingsClass)
                .addStatement("$T style=seq.find(s->s.name.equals(name))", backgroundStyleClass)
                .beginControlFlow("if (style==null)")
                .addStatement("style=new $T(name)", backgroundStyleClass)
                .addStatement("seq.add(style)")
                .addStatement("$T.saveStyles(seq)", backgroundSettingsClass)
                .endControlFlow()
                .addStatement("return style")
                .build());
        styleBuilder.addMethod(MethodSpec.methodBuilder("read")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String.class, "name")
                .addParameter(Reads.class, "read")
                .returns(backgroundStyleClass)
                .addStatement("$T style=new $T(name)", backgroundStyleClass, backgroundStyleClass)
                .addStatement("style.read(read)")
                .addStatement("return style")
                .build());

        //override equals
        styleBuilder.addMethod(MethodSpec.methodBuilder("equals")
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(Object.class, "obj")
                .addAnnotation(Override.class)
                .addStatement("return obj instanceof $T && (($T)obj).name.equals(name)", backgroundStyleClass, backgroundStyleClass)
                .build());
        //add rename method
        styleBuilder.addMethod(MethodSpec.methodBuilder("rename")
                .addParameter(String.class, "name")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$T<$T> seq=$T.getStyles()", seqClass, backgroundStyleClass, backgroundSettingsClass)
                .addStatement("if($T.lastStyle.equals(this))arc.Core.settings.put($S,name)",backgroundSettingsClass,"braindustry.styles.lastUsed")
                .beginControlFlow("if (seq.contains(this))")
                .addStatement("int index=seq.indexOf(this)")
                .addStatement("this.name=name")
                .addStatement("seq.set(index,this)")
                .nextControlFlow("else")
                .addStatement("this.name=name")
                .addStatement("seq.add(this)")
                .endControlFlow()
                .addStatement("$T.saveStyles(seq)", backgroundSettingsClass)
                .build());


        write(styleBuilder, imports.asArray());
    }

    private void handleSettingField(TypeSpec.Builder builder, ObjectSet<String> imports, Svar field, BDAnnotations.BackgroundStyleSources sources) {
        imports.addAll(getImports(field.enclosingType().e));
        String key = "background.style." + field.name();

        //add stringName
        builder.addField(FieldSpec.builder(TypeName.get(String.class), field.name() + "Key")
                .initializer("$S", key)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .build()
        );
        if (sources.keyOnly()) return;
        TypeName typeName = field.tname();
        String defValue = field.tree().getInitializer().toString();
        if (!field.tname().isBoxedPrimitive() && !field.tname().isPrimitive()) {
            imports.add("import " + field.tname() + ";");
        }
        if (sources.setting() && (field.tname().isPrimitive() || field.tname().isBoxedPrimitive())) {
            typeName = typeName.isBoxedPrimitive() ? typeName.unbox() : typeName;
            TypeName boxed = typeName.isPrimitive() ? typeName.box() : typeName;
            //add getter
            builder.addMethod(MethodSpec.methodBuilder(field.name())
                    .returns(typeName)
                    .addStatement("if(!(arc.Core.settings.get($S,$L) instanceof $T))$L($L)", key, defValue, boxed, field.name(), defValue)
                    .addStatement("return ($T)arc.Core.settings.get($S,$L)", typeName, key, defValue)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build());
            //add setter
            builder.addMethod(MethodSpec.methodBuilder(field.name())
                    .addParameter(typeName, "value")
                    .addStatement("arc.Core.settings.put($S,value)", key)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build());
        } else {
            //add getter
            builder.addMethod(MethodSpec.methodBuilder(field.name())
                    .returns(typeName)
                    .addStatement("return lastStyle==null?$L:lastStyle.$L()", defValue, field.name())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build());
            //add setter
            builder.addMethod(MethodSpec.methodBuilder(field.name())
                    .addParameter(typeName, "value")
                    .addStatement("if (lastStyle!=null)lastStyle.$L(value)", field.name())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build());
        }
    }

    private void handleStyleField(TypeSpec.Builder styleBuilder, Svar field, BDAnnotations.BackgroundStyleSources sources) {
        FieldSpec fieldSpec = FieldSpec.builder(field.tname(), field.name(), Modifier.PRIVATE).initializer(field.tree().getInitializer().toString()).build();
        styleBuilder.addField(fieldSpec);
        allFieldSpecs.add(fieldSpec);
        setMethodBuilder.addStatement("$L=$L", field.name(), field.tree().getInitializer());
        styleBuilder.addMethod(MethodSpec.methodBuilder(field.name())
                .returns(field.tname())
                .addStatement("return $L", field.name())
                .addModifiers(Modifier.PUBLIC)
                .build()
        );
        styleBuilder.addMethod(MethodSpec.methodBuilder(field.name())
                .addParameter(field.tname(), field.name())
                .addStatement("this.$L=$L", field.name(), field.name())
                .addStatement("save()")
                .addModifiers(Modifier.PUBLIC)
                .build()
        );
    }

    Seq<String> getImports(Element elem) {
        return Seq.with(trees.getPath(elem).getCompilationUnit().getImports()).map(Object::toString);
    }
}
