package com.dust.epidemic.data.btree;

import com.dust.epidemic.foundation.buffer.ReusableBuffer;
import com.dust.epidemic.fs.*;
import com.dust.epidemic.fs.entity.WriteResult;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * B树目录中数据的存储节点，用于维护数据本身以及相关描述
 *
 * 数据并不会保存在这个对象中，这个对象持有对本地数据的引用
 * 对接着fs与目录树
 *
 * 就是文件的虚拟对象，同时承担着文件读写入口的工作
 *
 * 每个DataNode只会对应一个文件
 */
public class DataNode {

    //初始化完成，但还无法开始工作。因为还没有载入存储系统
    private static final int INIT       = 0;

    //已经载入存储系统，可以开始工作
    private static final int WORKING    = 1;

    //要被释放，无法继续提供服务
    private static final int DESTROY    = 2;

    /**
     * 虚拟id，这个只是存入文件的id
     * 注意这有两个文件id，一个是对于用户与目录树来说的文件id，这个id用于叶子节点检索
     * 还有一个文件id表示的是.data文件的id
     */
    private String virtualKey;

    /**
     * 文件名称
     * 这个文件名称表示存入这个DataNode的文件名称，而不是fs模块的文件名称
     */
    private String fileName;

    /**
     * 文件虚拟对象的状态
     */
    private int working;

    /**
     * 创建时间
     */
    private int createTime;

    /**
     * 修改时间
     */
    private int updateTime;

    /**
     * 这个文件对应的磁盘文件id与对象id
     */
    private Map<String, List<Long>> fileIdWithObjIdMap;

    /**
     * 存储对象
     */
    private StorageLayout storageLayout;

    /**
     * 用户文件与磁盘信息的映射关系
     */
    private DataInfo dataInfo;


    public DataNode(String key, String fileName) {
        this.fileName = fileName;
        this.virtualKey = key;
        this.working = 0;

        int timestamp = (int) (System.currentTimeMillis() / 1000);
        this.createTime = timestamp;
        this.updateTime = timestamp;
    }

    /**
     * 给存储节点添加存储系统对象
     * @param storageLayout 添加的存储系统对象
     */
    public void init(StorageLayout storageLayout) {
        assert Objects.nonNull(storageLayout);
        this.storageLayout = storageLayout;
        this.fileIdWithObjIdMap = new HashMap<>();
        this.dataInfo = new DataInfo();
        working = 1;
    }

    /**
     * 写入数据
     * 由于文件数据通过RPC传输，因此一个用户文件会分成多次存储，因此会导致一份用户文件被保存在多个.data文件中
     * 因此需要一个数据结构记录某个用户文件所有的写入fileId与objectNo，同时fileId与objectNo是顺序的
     * @return 如果写入失败或者写入完成，则返回null。否则返回本次写入后这个数据对应的objectId
     */
    public String write(ReusableBuffer data) {
        checkStatus();
        WriteResult writeResult;
        try {
            writeResult = storageLayout.writeObject(data, true);
        } catch (IOException e) {
            //TODO 写入文件失败，记录日志
            return null;
        }
        assert Objects.nonNull(writeResult);
        dataInfo.load(writeResult);

        return encode(writeResult);
    }

    /**
     * 通过文件id与对象id获取这个对象的数据
     * 读取的都是最后一个提交的版本
     * @param objectId 对象id，这里的ID不等于存储系统中的对象id，这个id是由fileId和objNo组合而成
     *              为了保证数据的安全性，并不能将fileId直接对外公布，因为fileId不一定都是这个DataNode的数据
     *              所以通过DataNode都只能操作这个DataNode所属的所有object。
     */
    public ObjectInformation read(String objectId) {
        checkStatus();
        FileAndObj fileAndObj = decode(objectId);
        if (Objects.isNull(fileAndObj)) {
            return ObjectInformation.fail("传入的objectId格式错误");
        }

        try {
            FileMetadata md = storageLayout.getFileMetadata(fileAndObj.getFileId());
            if (Objects.isNull(md)) {
                return ObjectInformation.fail("不存在的.data文件");
            }

            long version = md.getLatestObjectVersionByObj(fileAndObj.objNo);
            if (version == -1) {
                return ObjectInformation.fail("不存在的对象版本");
            }

            return storageLayout.readObject(fileAndObj.fileId, md, fileAndObj.objNo, 0, -1, version);
        } catch (IOException e) {
            //TODO 读取失败记录日志
            return ObjectInformation.fail(e.getMessage());
        }
    }

    /**
     * 读取这个节点所有数据
     * 返回的数据按顺序存储
     */
    public List<ObjectInformation> readAll() {
        checkStatus();

        var ref = new Object() {
            boolean fileExists = true;
        };

        return dataInfo.toInformation(fileWithObject -> {
            if (!ref.fileExists) {
                return Collections.singletonList(ObjectInformation.fail("文件读取失败,可能不存在这个.data文件"));
            }

            String fileId = fileWithObject.getFileId();
            List<Long> objNos = fileWithObject.getObjectNos();
            try {
                FileMetadata md = storageLayout.getFileMetadata(fileId);
                if (Objects.isNull(md)) {
                    ref.fileExists = false;
                    return Collections.singletonList(ObjectInformation.fail("文件读取失败,可能不存在这个.data文件"));
                }

                return objNos.stream().map(objNo -> {
                    try {
                        return storageLayout.readObject(fileId, md, objNo, 0, -1, md.getLatestObjectVersionByObj(objNo));
                    } catch (IOException e) {
                        //TODO 异常,记录
                        return ObjectInformation.fail(e.getMessage());
                    }
                }).collect(Collectors.toList());
            } catch (IOException e) {
                //TODO 读取异常
                return Collections.singletonList(ObjectInformation.fail(e.getMessage()));
            }
        });
    }

    /**
     * 获取这个节点所有的objectId
     */
    public List<String> getAllObjectId() {
        var that = this;
        return dataInfo.toList(fileWithObject -> {
            String fileId = fileWithObject.getFileId();
            List<Long> objectNos = fileWithObject.getObjectNos();
            return objectNos.stream().map(objNo -> that.encode(fileId, objNo)).collect(Collectors.toList());
        }).stream().flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * 检查当前节点能否正常提供服务
     */
    private void checkStatus() {
        if (working != WORKING) {
            throw new RuntimeException("当前的DataNode状态已经无法提供正常读写服务。当前状态码：" + working);
        }
    }

    /**
     * 释放文件空间
     */
    public boolean destroy() {
        if (working == INIT) {
            //这个节点并没有关联到物理磁盘，直接返回，等待GC将其回收
            return true;
        }
        working = DESTROY;


        //TODO 释放磁盘空间
        return false;
    }

    /**
     *  将给用户的objectId解码为fileId与objNo
     * @param objectId 用户的对象Id
     * @return 解码后的fileId与objNo
     */
    private FileAndObj decode(String objectId) {
        //TODO 将给用户的objectId解码为fileId与objNo
        return null;
    }

    /**
     * 将写入完成的信息编码为objectId
     * @param writeResult 写入完成后的对象
     * @return 将写入完成后的对象编码成objectId，如果写入数据为0，则返回null
     */
    private String encode(WriteResult writeResult) {
        //TODO 将写入完成的信息编码为objectId
        return null;
    }

    /**
     * 根据fileId和objNo将信息编码为objectId
     * @param fileId data文件对应的fileId
     * @param objNo 对象id
     * @return 返回编码成功后的字符串
     */
    private String encode(String fileId, long objNo) {
        //TODO
        return null;
    }

    /**
     * 解码后的对象
     */
    @Getter
    @Setter
    private static class FileAndObj {
        private String fileId;
        private long objNo;
    }

}
