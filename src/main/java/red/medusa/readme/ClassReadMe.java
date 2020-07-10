package red.medusa.readme;

import red.medusa.readme.model.ReadMeFlag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassReadMe {

    // 信息
    String msg() default "";

    // 等价于msg
    String value() default "";

    // 模块级别
    int moduleLevel() default 3;

    int order() default 0;

    // 默认解析
    ReadMeFlag flag() default ReadMeFlag.PROCESS;

    // 拆分文件
    boolean split() default false;

    // 指定组
    Class<?> group() default Object.class;

    // 与group互斥
    // 明确指定组名
    String groupName() default "";

}
