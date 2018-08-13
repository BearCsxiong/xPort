package me.csxiong.library.cache;

import java.util.LinkedHashMap;

/*
 * -----------------------------------------------------------------
 * Copyright by Walten, All rights reserved.
 * -----------------------------------------------------------------
 * desc：对象链表
 * -----------------------------------------------------------------
 * 2018/5/24 : Create LruHashMap.java (Walten);
 * -----------------------------------------------------------------
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