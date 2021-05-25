package com.dust.scheduler;

import com.dust.NodeConfig;
import com.dust.fundation.EpidemicUtils;
import com.dust.logs.LogFormat;
import com.dust.logs.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RePublishing操作线程
 */
public class RePublishing extends Daemon {

    private long rePublishingPeriod;    //单位：秒

    private Queue<RePublishingTask> queue;

    private boolean shouldRun;

    private ThreadPoolExecutor executor;

    public RePublishing(NodeConfig config) {
        try {
            initialize(config);
        } catch (Exception e) {
            shutdown();
            throw e;
        }
    }

    /**
     * 工作循环
     */
    @Override
    public void run() {
        long periodMSec = rePublishingPeriod * 1000;
        long lastPublishTime = 0;
        while (shouldRun) {
            try {
                long now = EpidemicUtils.now();
                boolean shouldPush = now >= lastPublishTime + periodMSec;
                if (shouldPush) {
                    doRePublishing();
                    lastPublishTime = now;
                }
            } catch (Exception e) {
                Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "RePublishing出现异常", e.getMessage());
            } catch (Throwable t) {
                Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "RePublishing出现错误", t.getMessage());
                shutdown();     //出现错误要结束进程
            }

            try {
                sleep(periodMSec);
            } catch (InterruptedException e) {
                //不做任何事
            }
        }
    }

    /**
     * 将某个文件push任务推送到队列中，等待Re_Publishing操作
     * @param task 要push的任务
     */
    public void push(RePublishingTask task) {
        queue.add(task);
    }

    /**
     * 初始化
     */
    private void initialize(NodeConfig config) {
        this.shouldRun = true;
        this.queue = new ConcurrentLinkedQueue<>();
        this.rePublishingPeriod = config.getRePublishingTime();

        int threadPoolSize = config.getRePublishingThreadPoolSize();
        this.executor = new ThreadPoolExecutor(threadPoolSize, threadPoolSize,
                0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    /**
     * 停止程序
     */
    private void shutdown() {
        this.shouldRun = false;
        this.executor.shutdown();
    }

    /**
     * 执行Re-Publishing操作
     */
    private void doRePublishing() {
        while (!queue.isEmpty()) {
            var task = queue.poll();
            executor.execute(task);
        }
    }

}
