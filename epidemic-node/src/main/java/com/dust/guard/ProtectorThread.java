package com.dust.guard;

import com.dust.logs.LogFormat;
import com.dust.logs.Logger;

import java.util.Queue;

/**
 * 守护者线程
 * 主要用于执行各种定时计划，比如路由信息的持久化、网络节点定时的ping等
 */
public class ProtectorThread implements Runnable {

    private volatile boolean running = false;

    private Queue<Task> taskQueue;

    public static ProtectorThread create(Queue<Task> queue) {
        return new ProtectorThread(queue);
    }

    private ProtectorThread(Queue<Task> queue) {
        this.running = true;
        this.taskQueue = queue;
    }

    @Override
    public void run() {
        Logger.systemLog.error(LogFormat.SYSTEM_INFO_FORMAT, "守护线程启动");
        while (running) {
            if (taskQueue.isEmpty()) {
                continue;
            }
            while (!taskQueue.isEmpty()) {
                Task task = taskQueue.poll();
                try {
                    task.process();
                } catch (Exception e) {
                    Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "任务执行异常", e.getMessage());
                }
            }
        }
        Logger.systemLog.error(LogFormat.SYSTEM_INFO_FORMAT, "守护线程停止");
    }

    public void stop() {
        this.running = false;
    }
}
