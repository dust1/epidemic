package com.dust.epidemic.fs;


import com.dust.epidemic.core.EpidemicConfig;
import com.dust.epidemic.foundation.buffer.ReusableBuffer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * 数据访问抽象类
 * 在提供数据访问的同时还保存着存储过程中的通用参数与方法
 */
public abstract class StorageLayout {

    /**
     * 版本文件名称
     */
    private static final String VERSION_FILENAME = ".version";

    /**
     * 存储文件所在的目录
     */
    protected final String saveDir;

    /**
     * 保存元数据的缓存
     */
    protected final MetadataCache cache;

//    /**
//     * 目前对于工厂模式都是直接在接口类或者抽象类提供，而不是通过专门的工厂类
//     */
//    public static StorageLayout getInstance() {
//
//    }

    StorageLayout(EpidemicConfig config, MetadataCache cache) throws IOException {
        this.cache = cache;

        String dirPath = config.getSaveDir();
        if (!dirPath.endsWith("/")) {
            dirPath += "/";
        }
        this.saveDir = dirPath;


        //检查存储系统版本
        File versionFile = new File(saveDir, VERSION_FILENAME);
        if (versionFile.exists()) {
            //版本存在说明不是第一次启动
            char[] text;
            int len;
            FileReader reader = new FileReader(versionFile);
            try (reader) {
                text = new char[(int) versionFile.length()];
                len = reader.read(text);
            }
            if (len > 0) {
                int versionOnDisk = Integer.valueOf(String.valueOf(text));
                if (!isCompatibleVersion(versionOnDisk)) {
                    throw new IOException("当前存储系统版本无法兼容本地数据版本：" + versionOnDisk);
                }
            }
        }

        // 为什么要先写入.tmp文件在重命名成.version？
        // 是为了防止直接写入version过程中系统异常导致新的版本号写入到一半，
        // 如果这样的话当系统你重启后将无法读取到正确的版本号
        File versionTmpFile = new File(saveDir, ".tmp");
        FileWriter fileWriter = new FileWriter(versionTmpFile);
        fileWriter.write(Integer.toString(getLayoutVersionTag()));
        fileWriter.close();
        boolean renameResult = versionTmpFile.renameTo(versionFile);
        if (!renameResult) {
            throw new IOException("对版本文件.version的写入失败。路径：" + versionFile.getPath());
        }

    }

    /**
     * 获取这个文件id所属的元数据对象
     * @param fileId 文件id
     * @return {@link FileMetadata}
     * @throws IOException
     *         当读取元数据文件失败
     */
    public FileMetadata getFileMetadata(String fileId) throws IOException {
        FileMetadata metadata = getFileMetadataNoCaching(fileId);
        cache.setFileInfo(fileId, metadata);
        return metadata;
    }

    /**
     * 获取这个文件id所属的元数据对象，如果对象不存在则从文件中加载，但是不将其加入缓存
     * @param fileId 文件id
     * @return {@link FileMetadata}
     * @throws IOException
     *         当读取元数据文件失败
     */
    public FileMetadata getFileMetadataNoCaching(String fileId) throws IOException {
        FileMetadata metadata = cache.getFileInfo(fileId);
        if (Objects.isNull(metadata)) {
            metadata = loadFileMetadata(fileId);
        }
        return metadata;
    }

    /**
     * 当文件关闭的时候调用该方法
     * 这个方法并不是调用关闭文件这个逻辑，而是在关闭完文件后最后调用这个方法，用于一些后续的处理
     * @param fileMetadata 已经关闭的文件的元数据对象
     */
    public void closeFile(FileMetadata fileMetadata) {
        //...
    }

    /**
     * 根据文件id以及元数据读取给定编号的object中的给定偏移量的数据信息
     * @param fileId .data的文件id
     * @param md .data文件的元数据对象
     * @param objNo object对象编号
     * @param offset 要读取的数据在object中的偏移量，如果完整读取则offset = 0, length = object.length
     * @param length 要在object中读取的数据长度，如果完整读取则需要offset = 0, length = object.length
     * @param version 要读取的object的版本号
     * @return 读取到的object包装信息类
     * @throws IOException
     *      当读取.data文件失败时
     */
    public abstract ObjectInformation readObject(String fileId, FileMetadata md, long objNo, int offset,
                                                 int length, long version) throws IOException;

    /**
     * 将给定的{@link ReusableBuffer}数据写入到指定(fileId)文件的对应对象的某个位置
     * @param fileId 要写入的.data文件id
     * @param md 要写入的.data文件元数据
     * @param data 要写入的数据
     * @param objNo 写入的对象编号
     * @param offset 从对象的offset位置开始写入
     * @param newVersion 这次写入的版本号
     * @param sync 这次写入是否是同步的
     * @throws IOException
     *      当修改.data文件失败时
     */
    public abstract void writeObject(String fileId, FileMetadata md, ReusableBuffer data, long objNo,
                                     int offset, long newVersion, boolean sync) throws IOException;

    /**
     * 删除文件中对应版本的对象
     *
     * 有一种实现方法就是将已被删除的对象做标记，并在之后系统开启垃圾回收操作，将这些存储空间统一回收
     *
     * @param fileId 要操作的文件id
     * @param md 文件id对应的元数据
     * @param objNo 要被删除的对象编号
     * @param version 要被删除的对象版本
     * @throws IOException
     *      当修改.data文件失败时
     */
    public abstract void deleteObject(String fileId, FileMetadata md, long objNo, long version) throws IOException;

    /**
     * 将给定fileId对应的文件进行拆分
     * @param fileId 要拆分的文件id
     * @param md 要拆分的文件的元数据
     * @param splitOffset 从splitOffset位置开始拆分，通常来说之后的部位会拆分成一个新的文件
     * @param newVersion 拆分后两个文件的版本号
     * @param sync 本次拆分是否是同步
     * @return 拆分结束后原先元数据对象中文件的大小将会发生改变，拆分之后形成的新的元数据对象将会被返回
     * @throws IOException
     *      当修改.data文件失败时
     */
    public abstract FileMetadata splitFile(String fileId, FileMetadata md, long splitOffset, long newVersion,
                                           boolean sync) throws IOException;

    /**
     * 清空整个.data文件，将其中所有对象的所有版本删除
     *
     * 有一个方案就是将整个.data文件标记为【已删除】，这样就不需要OS做删除操作的开始，之后如果有新的数据写入则直接覆盖即可
     *
     * @param fileId 要删除的文件id
     * @param deleteMetadata 是否连同删除元数据文件
     * @throws IOException
     *      删除.data文件失败时
     */
    public abstract void deleteFile(String fileId, boolean deleteMetadata) throws IOException;

    /**
     * 根据fileId加载元数据信息
     * 这里的fileId表示.data文件的id，而不是用户存入文件的id
     * @param fileId 文件Id
     * @return @return {@link FileMetadata}
     * @throws IOException
     *         当读取元数据文件失败
     */
    protected abstract FileMetadata loadFileMetadata(String fileId) throws IOException;

    /**
     * 根据给定的文件id检查对应的.data文件是否存在
     * @param fileId 要检查的文件id
     * @return 如果文件存在则返回true，否则返回false
     */
    public abstract boolean fileExists(String fileId);


    /**
     * 获取当前的存储系统生效的版本号，用于更新本地版本
     * @return 当前系统版本
     */
    protected abstract int getLayoutVersionTag();

    /**
     * 检查当前磁盘上的文件版本是否是兼容版本
     * 由于系统的迭代，可能导致系统无法兼容旧的数据文件，但是这个问题在不同的存储系统实现可能会不一样，因此交给子类实现
     * @param version 当前磁盘上的版本
     * @return 如果能兼容则返回true，否则返回false
     */
    protected abstract boolean isCompatibleVersion(int version);

}
