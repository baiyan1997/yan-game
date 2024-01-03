package cn.baiyan.net;

import java.lang.annotation.*;

/**
 * indicates that an annotation type is used be an executor of messageTask
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

}
