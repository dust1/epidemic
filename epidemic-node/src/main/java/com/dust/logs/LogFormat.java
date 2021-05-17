package com.dust.logs;

/**
 * 日志格式化的输出参数
 * 所有日志的输出内容都在这里，不要再手动定义输出格式了
 */
public final class LogFormat {

    public static final String SYSTEM_ERROR_FORMAT =
            "errorMsg: {}, " +        //错误信息描述
            "errorDetail: {}";      //错误信息详情

    //路由节点新增节点到桶中
    public static final String LAYOUT_ADD_FORMAT =
            "[Layout][Bucket_Add][0] - " +     //日志标题信息，其中[0]表示这部分日志尚未作为快照文件的一部分持久化到本地。[1]表示已经保存在本地不需要恢复
            "host: {}," +
            "port: {}," +
            "nodeId: {}";

    //路由节点新增节点到缓存中
    public static final String LAYOUT_ADD_CACHE_FORMAT =
            "[Layout][Cache_Add][0] - " +     //日志标题信息，其中[0]表示这部分日志尚未作为快照文件的一部分持久化到本地。[1]表示已经保存在本地不需要恢复
                    "host: {}," +
                    "port: {}," +
                    "nodeId: {}";

    //存储节点新增存储文件
    public static final String STORAGE_SAVE_FORMAT =
            "[Storage][Save] - " +
                "fileId: {}," +
                "fileSize: {}," +
                "blockName: {}," +
                "blockOffset: {}";
}
