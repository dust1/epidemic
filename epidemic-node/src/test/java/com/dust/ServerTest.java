package com.dust;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {

    private String configPath;

    @Before
    public void before() {
        configPath = "/Users/kous/Desktop/setting.conf";
    }

    @Test
    public void serverStartTest() {
        String[] args = {configPath};
        EpidemicApplication.main(args);
    }

}
