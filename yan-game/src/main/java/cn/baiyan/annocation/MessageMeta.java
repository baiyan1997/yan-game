package cn.baiyan.annocation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageMeta {
    short module() default 0;

    byte cmd() default 0;
}
