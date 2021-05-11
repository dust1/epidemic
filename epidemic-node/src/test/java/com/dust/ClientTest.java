package com.dust;

import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.EpidemicClient;
import io.grpc.ManagedChannelBuilder;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class ClientTest {

    private EpidemicClient client;

    @Before
    public void before() {
        var channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        client = new EpidemicClient(channel);
    }

    @Test
    public void pingTest() {
        for (int i = 0; i < 22; i++) {
            String key = System.currentTimeMillis() + "i=" + i;
            String myNodeId = EpidemicUtils.getSHA1(key.getBytes(StandardCharsets.UTF_8));
            client.ping(myNodeId);
        }
    }

}
