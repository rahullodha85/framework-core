package com.hbcd.storage.base;

public interface CommonStorage //<K, T>
{

    <K, T> void save(K key, T value);

    <K, T> T get(K key);

    <K> boolean contain(K key);

    <K, T> void destroy(K key);

    void clear();
}
