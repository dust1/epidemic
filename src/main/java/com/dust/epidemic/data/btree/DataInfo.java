package com.dust.epidemic.data.btree;

import com.dust.epidemic.fs.ObjectInformation;
import com.dust.epidemic.fs.entity.WriteResult;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户保存记录
 * 这是保存用户文件到磁盘文件的映射关系的记录对象
 * 其中fileId与objectNo都是顺序存储
 */
public class DataInfo {


    private List<FileWithObject> files;

    public DataInfo() {
        this.files = new ArrayList<>();
    }

    /**
     * 迭代计算并返回整个文件的所有ObjectInformation信息
     * @param fnc 计算方法
     */
    public List<ObjectInformation> toInformation(Function<FileWithObject, List<ObjectInformation>> fnc) {
        return files.stream().map(fnc).flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * 将映射对象转化为集合
     * @param fnc 转化函数
     * @param <T> 返回对象类型
     */
    public <T> List<T> toList(Function<FileWithObject, T> fnc) {
        return files.stream().map(fnc).collect(Collectors.toList());
    }

    /**
     * 加载WriteResult对象，将写入信息记录到DataInfo中呢
     * @param writeResult 完成写入并返回的DataInfo对象
     */
    public void load(WriteResult writeResult) {
        if (writeResult.getLength() > 0) {
            addObject(writeResult.getFileId(), writeResult.getObjNo());
        }
    }

    private void addObject(String fileId, long objectNo) {
        for (FileWithObject fileWithObject : files) {
            if (fileWithObject.fileId.equals(fileId)) {
                fileWithObject.addObject(objectNo);
                return;
            }
        }

        FileWithObject fileWithObject = new FileWithObject(fileId, objectNo);
        files.add(fileWithObject);
    }


    /**
     * 单个.data文件与object的映射关系
     */
    @Getter
    static class FileWithObject {

        private String fileId;

        private List<Long> objectNos;

        public FileWithObject(String fileId, long objectNo) {
            this.fileId = fileId;
            this.objectNos = new ArrayList<>();

            objectNos.add(objectNo);
        }

        public void addObject(long objectNo) {
            if (!objectNos.contains(objectNo)) {
                objectNos.add(objectNo);
            }
        }


    }

}
