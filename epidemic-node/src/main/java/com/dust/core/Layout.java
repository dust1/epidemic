package com.dust.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Objects;

/**
 * 对外模块的抽象类
 * 由于存储与节点管理都会用到元数据快照。
 * 对于路由模块来说，需要快照保存的是文件的路由节点信息
 */
public abstract class Layout {

//    /**
//     * 快照文件存储路径
//     */
//    private String snapshotPath;
//
//    /**
//     * 快照文件的存储名称
//     */
//    private String snapshotName;
//
    /**
     * 对快照文件进行随机读写的对象
     * 默认不会初始化
     */
    protected RandomAccessFile snapshot;

//    private void initSnapshot() throws FileNotFoundException {
//        if (Objects.nonNull(snapshot)) {
//            return;
//        }
//
//        File dir = new File(snapshotPath);
//        dir.mkdirs();
//        File file = new File(snapshotPath, snapshotName);
//        snapshot = new RandomAccessFile(file, "rw");
//    }
//
//    /**
//     * 加载元数据
//     */
//    protected boolean loadSnapshot() {
//        try {
//            initSnapshot();
//        } catch (FileNotFoundException e) {
//            //TODO 元数据加载失败，将重新
//        }
//    }

}
