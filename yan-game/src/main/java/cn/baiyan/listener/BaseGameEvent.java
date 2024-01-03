package cn.baiyan.listener;

/**
 * 监听器监听的事件抽象类
 */
public class BaseGameEvent {
    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 事件类型
     */
    private final EventType eventType;

    public BaseGameEvent(EventType eventType) {
        this.createTime = System.currentTimeMillis();
        this.eventType = eventType;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    /**
     * 是否在消息主线同步执行
     */
    public boolean isSynchronized() {
        return true;
    }
}
