package com.dust.epidemic.core;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统配置类
 * 读取配置文件后生成，整个系统配置相关的都要通过它来读取
 */
@Getter
@Setter
public class EpidemicConfig {

    /**
     * 文件保存路径
     */
    private String saveDir;

    /**
     * 是否要开启校验和
     */
    private boolean isUseCheckSum;

}
