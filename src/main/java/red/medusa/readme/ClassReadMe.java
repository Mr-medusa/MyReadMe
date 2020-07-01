package red.medusa.readme;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassReadMe {

    // 信息
    String msg() default "";

    String value() default "";

    // 模块级别
    int moduleLevel() default 3;

    // 第一位位置从0开始计算
    int order() default -1;

    // 默认解析
    ReadMeFlag flag() default ReadMeFlag.PROCESS;
}
