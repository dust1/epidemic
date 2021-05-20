package com.dust.router.kademlia.timer;

import com.dust.guard.Task;
import com.dust.logs.LogFormat;
import com.dust.logs.Logger;
import com.dust.router.RouterLayout;
import com.dust.router.kademlia.KademliaBucket;
import com.dust.router.kademlia.KademliaRouterLayout;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * 保存到本地磁盘的任务
 */
public class SaveDiskTask implements Task {

    private KademliaBucket bucket;

    public SaveDiskTask(KademliaBucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public void process() {
        String saveName = RouterLayout.SNAPSHOT_FILENAME;
        String savePath = bucket.config.getRouterPath();
        var nodes = bucket.cloneBucket();
        File temp = new File(savePath, saveName + "_tmp");
        try (var raf = new RandomAccessFile(temp, "rw");
             var channel = raf.getChannel();) {
            raf.seek(0);
            raf.write(KademliaRouterLayout.HEAD);
            raf.write(bucket.myNode.getBytes(StandardCharsets.UTF_8));
            for (var node : nodes) {
                channel.write(node.toBuffer());
            }
        } catch (IOException e) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "持久化路由节点信息失败", e.getMessage());
        }

        if (!temp.renameTo(new File(savePath, saveName))) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "持久化路由节点信息失败", "temp文件重命名失败");
        }
    }
}
