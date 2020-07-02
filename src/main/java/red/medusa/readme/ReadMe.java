package red.medusa.readme;

import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Test
public @interface ReadMe {

    // 等价于locTit
    String value() default "";

    String locTit() default "";

    int order() default 0;

    int listLevel() default 0;

    String usage() default "";
}
