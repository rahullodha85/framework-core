package com.hbcd.execution.thread;

import java.util.concurrent.ThreadFactory;

public class CustomThreadFactory implements ThreadFactory {

    private String name = "";

    public void setName(String threadName) {
        this.name = threadName;
    }

    public Thread newThread(Runnable r) {
        return new Thread(r, name);
    }
}
