package com.dust;

import com.dust.core.NodeConfig;

import java.io.*;
import java.util.Properties;

/**
 * 节点启动类
 */
public class EpidemicApplication {

    //配置文件名称
    private static final String CONFIG_NAME = "setting.conf";

    public static void main(String[] args) {
        if (args.length == 1) {
            //TODO 没有配置文件参数
            System.exit(1);
            return;
        }

        String configPath = args[0];
//        String configPath = "/Users/kous/Desktop/setting.conf";

        File confFile = new File(configPath);
        if (!confFile.exists() || !CONFIG_NAME.equals(confFile.getName())) {
            //TODO 配置文件不存在
            System.exit(1);
        }
        try {
            Properties properties = createProperties(confFile);
            EpidemicServer server = EpidemicServer.create(new NodeConfig(properties));
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 根据配置文件信息创建配置通用类
     * @param configFile 配置文件
     * @return 配置对象
     */
    private static Properties createProperties(File configFile) throws IOException {
        Properties properties = new Properties();
        try (var input = new FileReader(configFile)) {
            properties.load(input);
        }
        return properties;
    }

}
