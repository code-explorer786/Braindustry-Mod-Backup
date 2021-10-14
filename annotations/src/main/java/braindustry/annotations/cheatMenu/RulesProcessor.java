package braindustry.annotations.cheatMenu;

import arc.scene.ui.TextArea;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.struct.StringMap;
import arc.util.Log;
import arc.util.Strings;
import braindustry.annotations.BDAnnotations;

import com.squareup.javapoet.*;
import mindustry.annotations.util.Selement;
import mindustry.annotations.util.Smethod;
import mindustry.annotations.util.Stype;
import mindustry.annotations.util.Svar;
import mindustry.game.Rules;
import mindustry.type.Sector;
import mma.annotations.ModBaseProcessor;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Modifier;


@SupportedAnnotationTypes({
        "braindustry.annotations.BDAnnotations.Rules",
        "braindustry.annotations.BDAnnotations.RulesTable",
})
public class RulesProcessor extends ModBaseProcessor {

    protected CodeBlock.Builder cashBuilder = null;
    private Seq<Selement> elements;
    private Stype methodsCollector;
    private BDAnnotations.RulesTable methodsCollectorAnnotations;
    private ObjectSet<String> erroredTypeNames = new ObjectSet<>();

    {
        rounds = 1;
    }

    protected void addCode(CodeBlock.Builder builder, String text, Object... args) {
        builder.addStatement(Strings.format(text, args));
    }

    protected void addCode(String text, Object... args) {
        addCode(cashBuilder, text, args);
    }

    protected void setBuilder(CodeBlock.Builder builder) {
        cashBuilder = builder;
    }

    private void addColorEdit(CodeBlock.Builder builder, Svar field) {

        String name = "rules." + field.name();
        addCode("table.label(()->\"@\").growX()", field.name());
        addCode("table.button(\"edit\",()-> ModVars.modVars.modUI.colorPicker.show(@,c->@=c)).growX()", name, name);

    }

    protected void addSlider(CodeBlock.Builder builder, Svar field, String cast) {
        builder.beginControlFlow(Strings.format("table.add(\"\").update(label->"));
        setBuilder(builder);
        addCode("label.setText(\"@: \"+rules.@)", field.name(), field.name());
        builder.endControlFlow(")");
        addCode("textArea = table.area(\"\"+rules.@, text -> rules.@=@Strings.parseFloat(text,0f)).width(100).get()", field.name(), field.name(), cast);
        addCode("textArea.setMaxLength((Float.MAX_VALUE + \"\").length())");
        addCode("textArea.setFilter(@.floatsOnly)", ClassName.get(TextField.TextFieldFilter.class));
//        addCode(builder,"table.slider(0f,10000f,1f,rules.@,(f)->rules.@=@f).growX().width(240f);",field.name(),field.name(),cast);
    }

    protected void build(CodeBlock.Builder builder, Svar field) {
        if (check(field.tname().box().toString())) {
            String methodName = methodsCollectorAnnotations.value();
            Seq<Smethod> methods = methodsCollector.methods().select(method -> method.name().equals(methodName));
            for (Smethod method : methods) {
                if (method.params().size != 4)continue;
                Svar param = method.params().get(2);
                String name = param.tname().toString();
                boolean prov = name.startsWith("arc.func.Prov<") && name.endsWith(">") &&
                            name.substring("arc.func.Prov<".length(), name.length() - ">".length()).equals(field.tname().box().toString());
                boolean func = name.startsWith("arc.func.Func<java.lang.Integer, ") && name.endsWith(">") &&
                            name.substring("arc.func.Func<java.lang.Integer, ".length(), name.length() - ">".length()).equals(field.tname().box().toString());
                boolean struck = prov ||
                                 func
                                     ;
                if (name.startsWith("arc.func.Func<")){
//                    Log.info("func: @",name);
                }
                boolean valid= struck || param.tname().equals(field.tname());
                if (valid) {
                  if (!struck) {
                      builder.addStatement("$L.$L(table,$S,rules.$L,($T)(var->rules.$L=var))", methodsCollector.fullName(), methodName, field.name(), field.name(), method.params().peek().tname(), field.name());
                  } else if (prov){
                      builder.addStatement("$L.$L(table,$S,()->rules.$L,($T)(var->rules.$L=var))", methodsCollector.fullName(), methodName, field.name(), field.name(), method.params().peek().tname(), field.name());
                  } else {
                      builder.addStatement("$L.$L(table,$S,(zero)->rules.$L,($T)(var->rules.$L=var))", methodsCollector.fullName(), methodName, field.name(), field.name(), method.params().peek().tname(), field.name());
                  }
                    builder.addStatement("table.row()");
                    return;
                }
            }
            String name = field.tname().toString();
            if (erroredTypeNames.add(name)) {
                Log.err("Cannot fnd method for @\t@", name,field.name());
            }
        }
    }


    protected boolean check(String c) {
        Seq<Class> with = Seq.<Class>with(ObjectSet.class, Seq.class, StringMap.class, Sector.class, Rules.TeamRules.class);
        return !with.map((Class::getName)).contains(c.split("<")[0]);
//        return !with.contains(c);
    }

    public void process(RoundEnvironment env) throws Exception {
        elements = elements(BDAnnotations.Rules.class);
        methodsCollector = types(BDAnnotations.RulesTable.class).first();
        methodsCollectorAnnotations = methodsCollector.annotation(BDAnnotations.RulesTable.class);
        Stype type = this.elements.first().asType().superclass();
        Seq<Svar> fields = type.fields();
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("CheatModRulesTable").addModifiers(Modifier.PUBLIC);
//        classBuilder.addJavadoc(RemoteProcess.autogenWarning);
        MethodSpec.Builder buildMethod = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Table.class, "table") //buffer to read form
                .addParameter(Rules.class, "rules") //ID of method type to read
                .returns(void.class);
        CodeBlock.Builder buildBlock = CodeBlock.builder();

        buildBlock.addStatement(ClassName.get(TextArea.class).reflectionName() + " textArea");
//        buildBlock.addStatement(ClassName.get(Strings.class).reflectionName()+" Strings=new "+ClassName.get(Strings.class).reflectionName()+"()");
        for (Svar field : fields) {
            build(buildBlock, field);
//            System.out.println(field.tname().box().toString());
//            System.out.println((field.tname().toString() + "-" + field.name()));
        }
//        classBuilder.alwaysQualifiedNames.add(ClassName.get(parentName,className.replace("Mod","")).reflectionName());
//        classBuilder.originatingElements.add();
        buildMethod.addCode(buildBlock.build());
        classBuilder.addMethod(buildMethod.build());

        TypeSpec spec = classBuilder.build();
        Seq<ClassName> imports = new Seq<>();
        imports.add(ClassName.get(Strings.class));
        write(classBuilder, imports.map(c->"import "+c.reflectionName()+";"));
        imports.clear();
//        JavaFile.builder(packageName, spec).build().writeTo(BaseProcessor.filer);
    }
}
