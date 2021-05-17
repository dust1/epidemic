package com.dust;

import com.dust.logs.LogFormat;
import com.dust.logs.Logger;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * 节点启动类
 */
@Slf4j
public class EpidemicApplication {

    //配置文件名称
    private static final String CONFIG_NAME = "setting.conf";

    public static void main(String[] args) {
        if (args.length < 1) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "配置文件参数为空", Arrays.toString(args));
            System.exit(1);
            return;
        }

        String configPath = args[0];
        File confFile = new File(configPath);
        if (!confFile.exists() || !CONFIG_NAME.equals(confFile.getName())) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "配置文件不存在 ", Arrays.toString(args));
            System.exit(1);
        }
        try {
            Properties properties = createProperties(confFile);
            EpidemicServer server = EpidemicServer.create(new NodeConfig(properties));
            server.start();
            server.blockUntilShutdown();
        } catch (Exception e) {
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
