package com.dust.logs;

import com.dust.logs.entity.LayoutLog;

/**
 * 日志解析器
 */
public class LogParser {

    public static LogParser parser = new LogParser();

    /**
     * 解析路由日志
     */
    public LayoutLog parseLayout(String log) {
        return LayoutLog.create(log);
    }

}
