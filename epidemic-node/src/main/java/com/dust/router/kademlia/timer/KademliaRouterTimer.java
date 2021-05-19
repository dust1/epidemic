package com.dust.router.kademlia.timer;

import com.dust.NodeConfig;
import com.dust.guard.Task;
import com.dust.logs.LogFormat;
import com.dust.logs.Logger;
import com.dust.router.kademlia.KademliaBucket;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * 路由定时对象
 * 设置持久化定时任务，使的在一段时间后将内存的路由节点信息持久化到磁盘中
 *
 */
public class KademliaRouterTimer {

    private int maxSize;

    /**
     * 添加任务计时器，记录已经添加了多少个任务
     */
    private volatile int count;

    /**
     * 上一次添加节点计时，如果超过maxTime，则执行持久化
     */
    private volatile int prevTime;

    private volatile int maxTime;

    /**
     * 桶对象
     */
    private KademliaBucket bucket;

    /**
     * 定时任务
     */
    private Thread work;

    /**
     * 两个线程之间通信的队列
     */
    private Queue<Task> queue;

    private ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);

    public KademliaRouterTimer(NodeConfig config) {
        this.maxSize = config.getLayoutSaveMaxSize();
        this.maxTime = config.getLayoutSaveMaxTime();

        this.count = 0;
        this.prevTime = -1;
    }

    /**
     * 开启定时任务
     */
    public void start(KademliaBucket bucket) {
        this.bucket = bucket;
        this.queue = new ConcurrentLinkedQueue<>();

        pool.scheduleAtFixedRate(() -> {
            queue.add(new PingTask(bucket));
        }, 10, 10, TimeUnit.SECONDS);

        this.work = new Thread(() -> {
            Logger.systemLog.info(LogFormat.SYSTEM_INFO_FORMAT, "守护线程启动");
            while (true) {
                while (!queue.isEmpty()) {
                    var task = queue.poll();
                    try {
                        task.process();
                    } catch (Exception e) {
                        Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "定时任务执行失败", e.getMessage());
                    }
                }

                int nowTime = (int) (System.currentTimeMillis() / 1000);
                if ((nowTime - prevTime) >= maxTime && count > 0) {
                    queue.add(new SaveDiskTask());
                }
            }
        });
        this.work.setDaemon(true);
        this.work.start();
    }

    /**
     * 一个添加任务信息
     */
    public void add() {
        if (++count >= maxSize) {
            count = 0;
            queue.add(new SaveDiskTask());
        }

        prevTime = (int) (System.currentTimeMillis() / 1000);
    }

}
