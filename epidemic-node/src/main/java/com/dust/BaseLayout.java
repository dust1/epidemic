package com.dust;

import com.dust.fundation.EpidemicUtils;
import com.dust.logs.LogFormat;
import com.dust.logs.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.function.Function;

/**
 * Layout基础组件
 */
public abstract class BaseLayout {

    /**
     * Layout所管理的文件夹路径
     */
    public final String path;

    /**
     * Layout所配置的配置文件信息
     */
    public final NodeConfig config;

    /**
     * 当前Layout的版本号
     */
    public final long version;

    /**
     * 当前Layout保存版本号的文件名称
     */
    public final String versionFileName;

    /**
     * 当前Layout用于持久化数据所传入的校验头文件
     */
    public final byte[] head;

    protected BaseLayout(String path, long version,
                      String versionFileName, byte[] head,
                      NodeConfig config) throws IOException {
        this.path = path;
        this.config = config;
        this.version = version;
        this.versionFileName = versionFileName;
        this.head = head;

        checkAndWriteVersion();
//        before();
    }

    /*
     * 在指定目录检查给定版本文件名的版本信息并确定当前版本是否兼容。
     * @throws IOException
     *      如果读取文件失败
     */
    private void checkAndWriteVersion() throws IOException {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean mkdirCheck = dir.mkdirs();
            if (!mkdirCheck) {
                Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "创建文件夹失败:" + dir.getPath(), "原因未知");
                throw new IOException("创建文件夹失败：" + dir.getPath());
            }
        }
        if (!dir.isDirectory()) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "版本文件路径不是一个文件夹目录:" + path, "-");
            throw new IOException("版本文件路径不是一个文件夹目录:" + path);
        }

        File versionFile = new File(path, versionFileName);
        if (versionFile.exists()) {
            RandomAccessFile raf = new RandomAccessFile(versionFile, "r");
            try (raf) {
                long version = raf.readLong();
                if (!isCompatibleVersion(version)) {
                    Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "本地的版本文件与当前系统并不兼容:" + versionFileName, "-");
                    throw new IOException("本地的版本文件与当前系统并不兼容！" + versionFileName);
                }
            }
        }

        final File tmpFile = new File(path, versionFileName + "_tmp");
        try (var out = new RandomAccessFile(tmpFile, "rw")) {
            out.writeLong(version);
        }
        boolean renameResult = tmpFile.renameTo(versionFile);
        if (!renameResult) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "版本文件重命名失败:", "原因未知");
            System.out.println("版本文件重命名失败");
        }
    }

    /**
     * 检查当前Layout是否兼容传入的版本号
     * @param version 要校验的版本号
     * @return 如果兼容则返回true
     */
    public abstract boolean isCompatibleVersion(long version);

    /**
     * 当节点有一个新的节点接入的时候通知Layout执行相应逻辑
     * @param nodeId 节点id
     * @param host 节点host
     * @param port 节点端口号
     */
    public abstract void haveNewNode(String nodeId, String host, int port);

    /**
     * 当Layout启动的时候调用该方法进行前置初始化
     * @throws IOException 读取文件异常
     */
    protected abstract void before() throws IOException;

}
