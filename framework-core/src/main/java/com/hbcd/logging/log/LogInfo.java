package com.hbcd.logging.log;

public interface LogInfo {

    void add(String i);

    void print();

    void print(String scenario_name);

    void clear();

    String toString();
}
