package cn.baiyan.thread;

public class Test {
    public static void main(String[] args) {
        String currentName = Thread.currentThread().getName();
        String groupName = Thread.currentThread().getThreadGroup().getName();
        System.out.println("线程名称: " + currentName + "所在线程组: " + groupName);
        System.out.println();

        ThreadGroup threadGroup = new ThreadGroup("root线程组");
        Thread t1 = new Thread(threadGroup, new MyTask(), "线程-1");
        Thread t2 = new Thread(threadGroup, new MyTask(), "线程-2");
        t1.start();
        t2.start();


    }

    static class MyTask implements  Runnable{

        @Override
        public void run() {
            String currentName = Thread.currentThread().getName();
            String groupName = Thread.currentThread().getThreadGroup().getName();
            System.out.println("线程名称: " + currentName + "所在线程组：" + groupName);
        }
    }
}
