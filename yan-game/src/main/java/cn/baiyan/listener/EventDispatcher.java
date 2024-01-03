package cn.baiyan.listener;

import cn.baiyan.logger.LoggerUtils;
import cn.baiyan.thread.NamedThreadFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;

public class EventDispatcher {
    private static EventDispatcher instance = new EventDispatcher();

    private EventDispatcher() {
        new NamedThreadFactory("event-dispatch").newThread(new EventWorker()).start();
    }

    public static EventDispatcher getInstance() {
        return instance;
    }

    /**
     * 事件类型与事件监听器列表的映射关系
     */
    private final Map<EventType, Set<Object>> observers = new HashMap<>();

    /**
     * 异步执行的事件队列
     */
    private LinkedBlockingQueue<BaseGameEvent> eventQueue = new LinkedBlockingQueue<>();


    /**
     * 注册事件监听器
     */
    public void registerEvent(EventType eventType, Object listener) {
        Set<Object> listeners = observers.get(eventType);
        if (listeners == null) {
            listeners = new CopyOnWriteArraySet<>();
            observers.put(eventType, listeners);
        }
        listeners.add(listener);
    }

    /**
     * 分发事件
      */
    public void fireEvent(BaseGameEvent event){
        if(event == null){
            throw new NullPointerException("event cannot be null");
        }
        // 如果事件是同步的，那么就在消息主线程执行逻辑，否则就丢到事件线程异步执行
        if(event.isSynchronized()){
            triggerEvent(event);
        }else{
            eventQueue.add(event);
        }

    }

    /**
     * 触发事件
     */
    private void triggerEvent(BaseGameEvent event) {
        EventType eventType = event.getEventType();
        Set<Object> listeners = observers.get(eventType);
        if (listeners != null) {
            listeners.forEach(listener -> {
                try {
                    ListenerManager.INSTANCE.fireEvent(listener, event);
                } catch (Exception e) {
                    // 防止其中一个listener异常而中断其他逻辑
                    LoggerUtils.error("triggerEvent failed", e);
                }
            });
        }
    }


    private class EventWorker implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    BaseGameEvent event = eventQueue.take();
                    triggerEvent(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
