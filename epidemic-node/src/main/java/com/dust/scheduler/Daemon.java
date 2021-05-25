package com.dust.scheduler;

import java.util.concurrent.ThreadFactory;

/**
 * 守护线程基础实现
 */
public class Daemon extends Thread {

    {
        //一定是守护线程
        setDaemon(true);
    }

    /**
     * 一个Daemon构造工厂，方便在ExecutorServices中使用
     */
    public static class DaemonFactory extends Daemon implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            return new Daemon(r);
        }
    }

    Runnable runnable = null;

    /* 构造一个守护线程 */
    public Daemon() {
        super();
    }

    /* 构造一个守护线程 */
    public Daemon(Runnable runnable) {
        super(runnable);
        this.runnable = runnable;
        this.setName(runnable.toString());
    }

    /* 构造一个守护线程使它成为某个线程组的一部分 */
    public Daemon(ThreadGroup group, Runnable runnable) {
        super(group, runnable);
        this.runnable = runnable;
        this.setName(runnable.toString());
    }

    public Runnable getRunnable() {
        return runnable;
    }
}
