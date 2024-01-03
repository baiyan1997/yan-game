package cn.baiyan.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private ThreadGroup threadGroup;

    private String groupName;

    private AtomicInteger idGenerator = new AtomicInteger(1);

    private final boolean daemo;

    public NamedThreadFactory(String group) {
        this(group, false);
    }

    public NamedThreadFactory(String groupName, boolean daemo) {
        this.groupName = groupName;
        this.daemo = daemo;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = getNextThreadName();
        Thread thread = new Thread(threadGroup, runnable, name, 0);
        return thread;
    }

    private String getNextThreadName() {
        return this.groupName + "-thread-" + this.idGenerator.getAndIncrement();
    }
}
