package braindustry.annotations;

import mindustry.annotations.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class BDAnnotations {
    @Target({ElementType.FIELD,ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BackgroundStyleSources {
        boolean setting()default false;
        boolean keyOnly()default false;
    }
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WritableObject {

    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EntityConfig {
        Class[] value() default {};
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WritableObjectsConfig {
        Class[] value() default {};

        int offset() default 0;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DefaultValue {
        String value();

        Class[] imports() default {};
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RulesTable {
        String value();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Rules {

    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EntityInterface {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Import {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EntitySuperClass {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EntitySuperInterface {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CashAnnotation1 {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CashAnnotation2 {
    }
}

/*

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface WritableObject {

}

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface EntityConfig {
    Class[] value() default {};
}

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface WritableObjectsConfig {
    Class[] value() default {};

    int offset() default 0;
}

@Retention(RetentionPolicy.SOURCE)
public @interface DefaultValue {
    String value();

    Class[] imports() default {};
}

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface RulesTable {
    String value();
}

@Retention(RetentionPolicy.SOURCE)
public @interface Rules {

}

@Retention(RetentionPolicy.SOURCE)
public @interface EntityInterface {
}


@Retention(RetentionPolicy.SOURCE)
public @interface EntitySuperClass {
}

@Retention(RetentionPolicy.SOURCE)
public @interface EntitySuperInterface {
}

@Retention(RetentionPolicy.SOURCE)
public @interface CashAnnotation1 {
}

@Retention(RetentionPolicy.SOURCE)
public @interface CashAnnotation2 {
}
*
* */