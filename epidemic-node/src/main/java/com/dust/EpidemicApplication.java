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

    public static void main(String[] args) throws FileNotFoundException {
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
            throw new FileNotFoundException(configPath);
        }

        try {
            Properties properties = createProperties(confFile);
            if (!canRun(properties)) {
                //TODO 配置文件参数缺失
                System.exit(1);
                return;
            }

            EpidemicServer server = EpidemicServer.create(new NodeConfig(properties));
            server.start();
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
        Properties properties = createDefaultProperties();
        try (var input = new FileReader(configFile)) {
            properties.load(input);
        }
        return properties;
    }

    /**
     * 创建Properties的默认参数，防止配置文件修改过多导致系统无法正常运行
     * @return 默认配置的Properties
     */
    private static Properties createDefaultProperties() {
        Properties properties = new Properties();
        //TODO 系统的默认配置
        return properties;
    }

    /**
     * 检查配置文件的关键参数，判断能否运行
     * @param properties 用于检查的参数
     * @return 如果必要参数存在，则系统可以运行，返回true；否则返回false
     */
    private static boolean canRun(Properties properties) {
        //TODO 检查必要参数
        return false;
    }

}
