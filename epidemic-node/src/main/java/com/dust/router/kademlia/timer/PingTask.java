package com.dust.router.kademlia.timer;

import com.dust.guard.Task;
import com.dust.router.kademlia.KademliaBucket;

/**
 * 测试所有路由节点网络情况的任务
 */
public class PingTask implements Task {

    public PingTask(KademliaBucket bucket) {

    }

    @Override
    public void process() {
//        System.out.println("ping");
    }
}
