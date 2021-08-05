package braindustry.annotations.backgroundMenu;

import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import braindustry.annotations.ModAnnotations;
import braindustry.annotations.ModBaseProcessor;
import com.squareup.javapoet.*;
import mindustry.annotations.util.Svar;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

@SupportedAnnotationTypes("braindustry.annotations.ModAnnotations.BackgroundStyleSources")
public class BackgroundStylesProc extends ModBaseProcessor {
    @Override
    public void process(RoundEnvironment env) throws Exception {
        Seq<Svar> fields = fields(ModAnnotations.BackgroundStyleSources.class);
        TypeSpec.Builder builder = TypeSpec.classBuilder("BackgroundSettings").addModifiers(Modifier.PUBLIC);
        ObjectSet<String> imports = new ObjectSet<>();
        for (Svar field : fields) {
            imports.addAll(getImports(field.enclosingType().e));
            ModAnnotations.BackgroundStyleSources sources = field.annotation(ModAnnotations.BackgroundStyleSources.class);
            String key = "background.style." + field.name();
            String defValue = field.tree().getInitializer().toString();
            //add stringName
            builder.addField(FieldSpec.builder(TypeName.get(String.class), field.name()+"Key")
                    .initializer("$S", key)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .build()
            );
            boolean extraMethods = !sources.castMethodSet().equals("\n");
            //add getter
            String hardGetter = "get" + Strings.capitalize(field.name());
            builder.addMethod(MethodSpec.methodBuilder(extraMethods ? hardGetter : field.name())
                    .returns(field.tname())
                    .addStatement("return ($T)arc.Core.settings.get($S,$L)", field.tname(), key, defValue)
                    .addModifiers(extraMethods ? Modifier.PRIVATE : Modifier.PUBLIC, Modifier.STATIC)
                    .build());
            //add setter
            String hardSetter = "set" + Strings.capitalize(field.name());
            builder.addMethod(MethodSpec.methodBuilder(extraMethods ? hardSetter : field.name())
                    .addParameter(field.tname(), "value")
                    .addStatement("arc.Core.settings.put($S,value)", key)
                    .addModifiers(extraMethods ? Modifier.PRIVATE : Modifier.PUBLIC, Modifier.STATIC)
                    .build());
            if (extraMethods) {
                String methodGet = sources.castMethodGet();
                String preGet = methodGet.substring(0, methodGet.indexOf("@"));
                String postGet = methodGet.substring(methodGet.indexOf("@") + 1);
                String methodSet = sources.castMethodSet();
                String preSet = methodSet.substring(0, methodSet.indexOf("@"));
                String postSet = methodSet.substring(methodSet.indexOf("@") + 1);
                String clazz = sources.clazz();
                ClassName type = ClassName.bestGuess(clazz);

                boolean getCast=preGet.startsWith("("+clazz+")");
                String getter = Strings.format("@@()@",!getCast?preGet: preGet.substring(("(" + clazz + ")").length()), hardGetter, postGet);
                //add getter
                builder.addMethod(MethodSpec.methodBuilder(field.name())
                        .returns(type)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addStatement("return $L()==$L || !($L instanceof $L)?null:($L)$L", hardGetter, sources.noDefError()?-1:defValue, getter, clazz, clazz,getter)
                        .build());
                //add setter
                builder.addMethod(MethodSpec.methodBuilder(field.name())
                        .addParameter(type, "value")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addStatement("$L(value==null?$L:$Lvalue$L)", hardSetter, defValue, preSet, postSet)
                        .build());

            }
        }
        write(builder, imports.asArray());
    }

    Seq<String> getImports(Element elem) {
        return Seq.with(trees.getPath(elem).getCompilationUnit().getImports()).map(Object::toString);
    }
}
