package cn.baiyan.listener;

import java.lang.annotation.*;

/**
 * 监听器注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {
}
