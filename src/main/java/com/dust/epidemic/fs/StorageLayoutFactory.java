package com.dust.epidemic.fs;

import com.dust.epidemic.core.EpidemicConfig;

import java.io.IOException;
import java.util.Objects;

/**
 * 存储对象生成工厂
 */
public class StorageLayoutFactory {

    private static FileStorageLayout fileStorageLayout;

    public static StorageLayout createFile(EpidemicConfig config, MetadataCache cache) throws IOException {
        if (Objects.isNull(fileStorageLayout)) {
            synchronized (StorageLayoutFactory.class) {
                if (Objects.isNull(fileStorageLayout)) {
                    fileStorageLayout = new FileStorageLayout(config, cache);
                }
            }
        }
        return fileStorageLayout;
    }

}
