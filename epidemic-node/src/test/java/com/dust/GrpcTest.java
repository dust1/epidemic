package com.dust;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GrpcTest {

    @Test
    public void checkHostnameTest() throws UnknownHostException {
        InetAddress host = InetAddress.getLocalHost();
        System.out.println(host.getHostAddress());
    }

}
