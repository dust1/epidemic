package com.dust.logs;

import org.slf4j.LoggerFactory;

/**
 * 日志对象整合
 */
public final class Logger {

    /**
     * 存储日志对象
     */
    public static final org.slf4j.Logger storageLog = LoggerFactory.getLogger("StorageOperation");

    /**
     * 路由节点日志对象
     */
    public static final org.slf4j.Logger layoutLog = LoggerFactory.getLogger("LayoutOperation");

    /**
     * 系统日志对象
     */
    public static final org.slf4j.Logger systemLog = LoggerFactory.getLogger("SystemOperation");

}
