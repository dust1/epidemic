package com.dust.epidemic.net;

import com.dust.epidemic.data.NodeView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 在网络兼传输的消息包装对象
 */
@Getter
@Setter
@ToString
public class NetMessage {

    /**
     * 传输关键字
     */
    private int[] keys;

    /**
     * 关键字对应的值
     */
    private String[] value;

    /**
     * 这个请求的发起方
     */
    private Descriptor address;

    /**
     * 这个信息最初的发源地
     */
    private Descriptor sourceAddress;

    /**
     * 消息校验
     */
    private String timestamp;

}
