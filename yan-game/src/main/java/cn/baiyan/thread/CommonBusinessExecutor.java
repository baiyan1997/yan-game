package cn.baiyan.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommonBusinessExecutor {

    private Logger logger = LoggerFactory.getLogger(CommonBusinessExecutor.class);

    /**
     * task worker pool
     */
    private final List<TaskWorker> workerPool = new ArrayList<>();


    private final AtomicBoolean run = new AtomicBoolean(true);


    /**
     * 任务队列总线(这里不要用public修饰，限制业务逻辑直接访问！！)
     */

    LinkedBlockingQueue<Runnable> ROOT_QUEUE = new LinkedBlockingQueue<>();

    public CommonMailGroup commonMailGroup(String name, int workerSize) {
        return new CommonMailGroup(name, workerSize);
    }

    public CommonBusinessExecutor(String name, int size) {
        for (int i = 0; i < size; i++) {
            TaskWorker worker = new TaskWorker();
            workerPool.add(worker);
            new NamedThreadFactory(name + "-" + i).newThread(worker).start();
        }
    }


    private class TaskWorker implements Runnable {

        @Override
        public void run() {
            while (run.get()) {
                try {
                    Runnable task = ROOT_QUEUE.take();
                    task.run();

                } catch (Exception e) {
                }
            }
        }
    }
}
