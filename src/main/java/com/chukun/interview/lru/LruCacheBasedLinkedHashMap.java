package com.chukun.interview.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chukun
 *  使用 linkedHashMap实现LRU算法
 *
 * @see LinkedHashMap
 */
public class LruCacheBasedLinkedHashMap<K,V> extends LinkedHashMap<K,V> {

    private int capacity;

    public LruCacheBasedLinkedHashMap(int capacity) {
        /**
         * 第三个参数为true，表示与访问顺序有关
         * 假设size为3，
         * 例如
         *    put(1,1)
         *    put(2,2)
         *    put(3,3)
         *    put(4,4) 触发淘汰机制，此时key 4,3,2
         *    这时如果继续 put(3,3)，那么key 3,4,2
         * 而为false时，继续 put(3,3) 此时key 4,3,2
         */
        super(capacity, 0.75F, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return super.size() > this.capacity;
    }
}
