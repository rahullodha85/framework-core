package com.hbcd.common.service;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public interface DataService<T> {

    T find(String key);

    List<T> getList();

    boolean isTheSameDataFile(BasicFileAttributes attrb);

    boolean hasTheDataFileChanged();

    int size();

    void clearData();

    void print();
}
