package com.dust.epidemic.data.btree;

/**
 * 树节点接口
 */
public interface TreeNode {

    /**
     * 添加节点
     * 这里只是根据要添加的文件关键字预先占位，并不会进行数据的写入
     * 后续根据这个关键字获取数据节点对象，并直接写入对象中
     * @return 返回对象主要是保证在插入后返回root节点
     */
    TreeNode insert(String k, String fileName);

    /**
     * 删除节点
     * @return 理由同上
     */
    TreeNode delete(String k);

    /**
     * 查询节点
     */
    DataNode find(String k);

}
