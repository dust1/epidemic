package com.dust.epidemic.data;

import com.dust.epidemic.net.Descriptor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 节点中对所有存储信息的管理表
 */
@Getter
@Setter
public class DataManager {

    private NodeView<String, Integer> view;

    private Set<String> cache;

    public DataManager() {
        this.view = new NodeView<>(5);
        this.cache = new HashSet<>();
    }

    public boolean check(String sign) {
        if (cache.contains(sign)) {
            return false;
        }
        cache.add(sign);
        return true;
    }

    /**
     * 将另一个节点的数据跟自身数据合并
     */
    public void merge(int[] keys, String[] value) {
        if (keys.length != value.length) {
            System.out.println("参数错误,关键字与值为对应");
            return;
        }
        int len = keys.length;
        for (int i = 0; i < len; i++) {
            view.insert(keys[i], value[i]);
        }
    }


}
