package cn.baiyan.thread;

import java.lang.annotation.*;

/**
 * 标识一个容器组件为线程安全的
 * @author kinson
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ThreadSafe {

}
