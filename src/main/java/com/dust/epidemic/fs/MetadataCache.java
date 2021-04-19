package com.dust.epidemic.fs;

/**
 * 元数据缓存
 * 缓存的是针对.data的元数据对象，这个对象保存着.data相关的随机读写对象以及版本信息
 */
public class MetadataCache {


    public FileMetadata getFileInfo(String fileId) {
        //TODO 根据文件id返回缓存的元数据对象
        return null;
    }

    public void setFileInfo(String fileId, FileMetadata metadata) {
        //TODO 根据文件id将其对应的元数据对象加入缓存
    }

}
