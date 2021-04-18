package com.dust.epidemic.foundation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU缓存
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int maxSize;

    public LRUCache(int maxSize) {
        this.maxSize = maxSize;
        checkSize();
    }

    private void checkSize() {
        if (maxSize < 1) {
            throw new IllegalArgumentException("maxSize must be >= 1");
        }
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
        return size() < maxSize;
    }

}
