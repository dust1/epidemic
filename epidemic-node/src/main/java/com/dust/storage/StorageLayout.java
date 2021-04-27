package com.dust.storage;

import java.io.IOException;

public abstract class StorageLayout {

    /**
     * 加载元数据文件并构建文件目录
     * 文件目录由B+树组成
     * @throws IOException
     *      读取元数据文件失败
     */
    public abstract void load() throws IOException;

}
