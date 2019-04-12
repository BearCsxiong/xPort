package me.csxiong.library.integration.cache;

import java.util.LinkedHashMap;

/**-------------------------------------------------------------------------------
*| 
*| desc : 自定义链表
*| 
*|--------------------------------------------------------------------------------
*| on 2018/8/20 created by csxiong 
*|--------------------------------------------------------------------------------
*/
final class LruHashMap<K, V> extends LinkedHashMap<K, V> {

    private final int capacity;

    public LruHashMap(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Entry entry) {
        return size() > capacity;
    }

}