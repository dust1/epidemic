package com.dust.epidemic.net;

import com.dust.epidemic.core.NodeView;
import lombok.Getter;
import lombok.Setter;

/**
 * 在网络兼传输的消息包装对象
 */
@Getter
@Setter
public class NetMessage {

    /**
     * 信息主体
     */
    private NodeView view;

    /**
     * 这个请求的发起方
     */
    private Descriptor address;

    /**
     * 这个信息最初的发源地
     */
    private Descriptor sourceAddress;

    public static NetMessage merge(NodeView nodeView, Descriptor descriptor, Descriptor sourceAddress) {
        descriptor.increase();
        NetMessage message = new NetMessage();
        message.setAddress(descriptor);
        message.setView(nodeView);
        message.setSourceAddress(sourceAddress);
        return message;
    }

    public String logInfo() {
        return "message view:" + view.toString() + ";" +
                "from:" + address.toString() + ";" +
                "source from:" + sourceAddress.toString() + ";";
    }

}
