package cn.baiyan.listener;

import java.lang.annotation.*;

/**
 * 事件处理器
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    /**
     * 绑定的事件类型列表
     */
    EventType[] value();
}
