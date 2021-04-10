package com.dust.epidemic.net;

import lombok.*;

/**
 * 节点的对象
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Node {

    private String address;
    private int port;

    public void update(Node node) {
        this.address = node.getAddress();
        this.port = node.getPort();
    }

}
