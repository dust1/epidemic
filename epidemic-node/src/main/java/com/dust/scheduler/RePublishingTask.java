package com.dust.scheduler;

/**
 * 要执行RePublishing操作的任务封装
 * 主要任务为推送文件，需要获取文件块所在的文件名称、偏移量、文件大小、推送的目标地址信息
 */
public class RePublishingTask implements Runnable{

    private String blockName;

    private long offset;

    private long size;

    private String targetHost;

    private int targetPort;

    public static RePublishingTask create(String blockName,
                                          long offset, long size,
                                          String targetHost,
                                          int targetPort) {
        return new RePublishingTask(blockName, offset, size, targetHost, targetPort);
    }

    private RePublishingTask(String blockName,
                             long offset, long size,
                             String targetHost,
                             int targetPort) {
        this.blockName = blockName;
        this.offset = offset;
        this.size = size;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }

    @Override
    public void run() {

    }

}
