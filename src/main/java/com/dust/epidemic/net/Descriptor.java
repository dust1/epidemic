package com.dust.epidemic.net;

import lombok.Getter;
import lombok.Setter;

/**
 * 描述符
 */
@Getter
@Setter
public class Descriptor {

    private String address;
    private int port;
    private int hotCount;

    public void increase() {
        this.hotCount += 1;
    }

}
