package com.dust.epidemic.fs;

/**
 * 写入.data文件的object对象信息
 */
public class ObjectInformation {

    private String message;

    public static ObjectInformation fail(String message) {
        return null;
    }

    public static ObjectInformation empty() {
        //TODO 返回一个空对象
        return null;
    }

}
