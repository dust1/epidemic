package com.dust.router.kademlia.timer;

import com.dust.NodeConfig;
import com.dust.guard.Task;
import com.dust.logs.LogFormat;
import com.dust.logs.Logger;
import com.dust.router.kademlia.KademliaBucket;

import java.util.Objects;
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

    private volatile int pingCount;

    private final int startPingCount;

    /**
     * 桶对象
     */
    private KademliaBucket bucket;

    private BlockingQueue<Task> saveQueue;

    private BlockingQueue<Task> pingQueue;

    private ScheduledThreadPoolExecutor pool;

    public KademliaRouterTimer(NodeConfig config) {
        this.maxSize = config.getLayoutSaveMaxSize();
        this.maxTime = config.getLayoutSaveMaxTime();
        this.startPingCount = config.getStartPingCount();
        this.count = 0;
        this.prevTime = -1;
    }

    private void init(KademliaBucket bucket) {
        this.bucket = bucket;
        this.saveQueue = new LinkedBlockingQueue<>();
        this.pingQueue = new LinkedBlockingQueue<>();
        this.pool = new ScheduledThreadPoolExecutor(1);
    }

    /**
     * 开启定时任务
     */
    public void start(KademliaBucket bucket) {
        init(bucket);

        var saveWork = new Thread(new WorkingTask(saveQueue, "持久化"));
        var pingWork = new Thread(new WorkingTask(pingQueue, "ping"));

        saveWork.setDaemon(true);
        pingWork.setDaemon(true);
        saveWork.start();
        pingWork.start();

        pool.scheduleAtFixedRate(() -> {
            int nowTime = (int) (System.currentTimeMillis() / 1000);
            if ((nowTime - prevTime) >= maxTime && count > 0) {
                count = 0;
                saveQueue.add(new SaveDiskTask(bucket));
            }

            if (++pingCount >= startPingCount) {
                pingCount = 0;
                pingQueue.add(new PingTask(bucket));
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * 工作线程
     */
    static class WorkingTask implements Runnable {

        private BlockingQueue<Task> queue;

        private String threadName;

        public WorkingTask(BlockingQueue<Task> queue,
                           String threadName) {
            this.queue = queue;
            this.threadName = threadName;
        }

        @Override
        public void run() {
            Logger.systemLog.info(LogFormat.SYSTEM_INFO_FORMAT, threadName + "线程启动");
            while (true) {
                try {
                    //将其阻塞，减少CPU开销
                    var task = queue.take();
                    task.process();
                } catch (InterruptedException e) {
                    Logger.systemLog.info(LogFormat.SYSTEM_INFO_FORMAT, threadName + "线程中断");
                } catch (Exception e) {
                    Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, threadName + "线程任务执行失败", e.getMessage());
                }
            }
        }
    }

    /**
     * 一个添加任务信息
     */
    public void add() {
        if (Objects.isNull(bucket)) {
            return;
        }

        if (++count >= maxSize) {
            count = 0;
            saveQueue.add(new SaveDiskTask(bucket));
        }
    }

}
