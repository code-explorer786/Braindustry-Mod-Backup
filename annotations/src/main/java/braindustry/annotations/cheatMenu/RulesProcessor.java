package braindustry.annotations.cheatMenu;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import braindustry.annotations.*;
import braindustry.annotations.BDAnnotations.*;
import com.squareup.javapoet.*;
import mindustry.annotations.util.*;
import mindustry.type.*;
import mma.annotations.*;
import org.jetbrains.annotations.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;


@SupportedAnnotationTypes({
"braindustry.annotations.BDAnnotations.WritableObjectsConfig"
})
public class RulesProcessor extends ModBaseProcessor{

    private Seq<Selement> elements;
    private Stype methodsCollector;
    private Seq<Smethod> fieldViewers;
    private BDAnnotations.RulesTable methodsCollectorAnnotations;
    private ObjectSet<String> erroredTypeNames = new ObjectSet<>();

    {
        rounds = 1;
    }



    public void process(@NotNull RoundEnvironment env) throws Exception{
        elements = elements(BDAnnotations.Rules.class);
        methodsCollector = types(BDAnnotations.RulesTable.class).first();
        methodsCollectorAnnotations = methodsCollector.annotation(BDAnnotations.RulesTable.class);
        fieldViewers=methods(RulesFieldViewer.class);
        Stype type = this.elements.first().asType().superclass();
        Seq<Svar> fields = type.fields();
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("CheatModRulesTable").addModifiers(Modifier.PUBLIC);
//        classBuilder.addJavadoc(RemoteProcess.autogenWarning);
        MethodSpec.Builder buildMethod = MethodSpec.methodBuilder("build")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(Table.class, "table") //buffer to read form
        .addParameter(mindustry.game.Rules.class, "rules") //ID of method type to read
        .returns(void.class);
        CodeBlock.Builder buildBlock = CodeBlock.builder();

        buildBlock.addStatement(ClassName.get(TextArea.class).reflectionName() + " textArea");
        for(Svar field : fields){
            build(buildBlock, field);
        }
        buildMethod.addCode(buildBlock.build());
        classBuilder.addMethod(buildMethod.build());

        Seq<ClassName> imports = new Seq<>();
        imports.add(ClassName.get(Strings.class));
        write(classBuilder, imports.map(c -> "import " + c.reflectionName() + ";"));
        imports.clear();
//        JavaFile.builder(packageName, spec).build().writeTo(BaseProcessor.filer);
    }

    protected void build(CodeBlock.Builder builder, @NotNull Svar field){
        if(check(field.tname().box().toString())){
            String methodName = methodsCollectorAnnotations.value();
            Seq<Smethod> methods = methodsCollector.methods().select(method -> method.name().equals(methodName));
            Smethod viewer = fieldViewers.find(m -> m.annotation(RulesFieldViewer.class).fieldName().equals(field.name()));
            if (viewer!=null){
                if(processMethod(builder, field, methodName(viewer), viewer)) return;
                throw new RuntimeException("Cannot use viewer for Rules."+field.name()+" named "+viewer.fullName());
            }
            for(Smethod method : methods){
                if(method.params().size != 4) continue;
                if(processMethod(builder, field,methodsCollector.fullName()+"."+ methodName, method)) return;
            }
            String name = field.tname().toString();
            if(erroredTypeNames.add(name)){
                Log.err("Cannot fnd method for @\t@", name, field.name());
            }
        }
    }
private String methodName(Smethod smethod){
        return smethod.up().asType().toString() + "." + smethod.name();
}
    private boolean processMethod(CodeBlock.Builder builder, @NotNull Svar field, String fullMethodName, Smethod method){
        Svar param = method.params().get(2);
        String name = param.tname().toString();
                /*boolean prov = name.startsWith("arc.func.Prov<") && name.endsWith(">") &&
                            name.substring("arc.func.Prov<".length(), name.length() - ">".length()).equals(field.tname().box().toString());*/
        boolean prov = name.matches("^(arc\\.func\\.Prov<)BOX_TYPE(>)$".replace("BOX_TYPE", field.tname().box().toString()));
                /*boolean func = name.startsWith("arc.func.Func<java.lang.Integer, ") && name.endsWith(">") &&
                               name.substring("arc.func.Func<java.lang.Integer, ".length(), name.length() - ">".length()).equals(field.tname().box().toString());*/
        boolean func = name.matches("^(arc\\.func\\.Func<java\\.lang\\.Integer,\\s?)BOX_TYPE(>)$".replace("BOX_TYPE", field.tname().box().toString()));
        boolean struck = prov || func;
        if(name.startsWith("arc.func.Func<")){
//                    Log.info("func: @",name);
        }
        boolean valid = struck || param.tname().equals(field.tname());
        if(valid){
            if(!struck){
                builder.addStatement("$L(table,$S,rules.$L,($T)(var->rules.$L=var))",fullMethodName, field.name(), field.name(), method.params().peek().tname(), field.name());
            }else if(prov){
                builder.addStatement("$L(table,$S,()->rules.$L,($T)(var->rules.$L=var))",fullMethodName, field.name(), field.name(), method.params().peek().tname(), field.name());
            }else{
                builder.addStatement("$L(table,$S,(zero)->rules.$L,($T)(var->rules.$L=var))", fullMethodName, field.name(), field.name(), method.params().peek().tname(), field.name());
            }
            builder.addStatement("table.row()");
            return true;
        }
        return false;
    }

    protected boolean check(@NotNull String c){
        Seq<Class<?>> with = Seq.with(ObjectSet.class, Seq.class, StringMap.class, Sector.class, mindustry.game.Rules.TeamRules.class);
        return !with.map((Class::getName)).contains(c.split("<")[0]);
//        return !with.contains(c);
    }

}
