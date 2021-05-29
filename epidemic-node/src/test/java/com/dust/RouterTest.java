package com.dust;

import com.dust.fundation.EpidemicUtils;
import com.dust.router.RouterLayout;
import com.dust.router.kademlia.KademliaRouterLayout;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RouterTest {

    private String configPath = "/Users/kous/Desktop/setting.conf";

    private RouterLayout routerLayout;

    @Before
    public void before() throws IOException {
        Properties properties = new Properties();
        try (var input = new FileReader(configPath)) {
            properties.load(input);
        }
        var config = new NodeConfig(properties);

        routerLayout = KademliaRouterLayout.create(config);
        routerLayout.before();
    }

    @Test
    public void routerTest() {
        List<String> ids = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            var nodeId = EpidemicUtils.randomNodeId("random");
            ids.add(nodeId);
            routerLayout.haveNewNode(nodeId, "127.0.0." + i, i);
        }
        for (var id : ids) {
            var findNodes = routerLayout.findNode(id);
            System.out.println(findNodes);
        }
    }

    @Test
    public void readTest() {
        for (int i = 0; i < 100; i++) {
            var nodeId = EpidemicUtils.randomNodeId("random");
            var list = routerLayout.findNode(nodeId);
            System.out.println(list);
        }
    }

}
