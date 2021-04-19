package com.dust.epidemic.logging;

import java.util.Objects;

/**
 * 自己实现的日志类
 */
public class Logging {

    /**
     * 日志实体类
     */
    private static Logging instance;

    /**
     * 日志类别
     */
    public enum Category {
        all
    }


    private Logging() {

    }

    public static void logMessage(int level, Category category, Object msg, String formatPattern, Object... args) {
        checkLoggingStarted();
        //TODO 写入日志操作
    }

    public static void logMessage(int level, Object me, String formatPattern, Object... args) {
        logMessage(level, Category.all, me, formatPattern, args);
    }

    /**
     * 检查日志是否启动
     */
    private static void checkLoggingStarted() {
        if (Objects.isNull(instance)) {
            throw new RuntimeException("日志尚未启动完成，或者日志启动失败");
        }
    }

    public synchronized static void start(int level, Category... categories) {
        if (Objects.isNull(instance)) {
            //TODO 初始化日志文件

            instance = new Logging();
        }
    }

}
