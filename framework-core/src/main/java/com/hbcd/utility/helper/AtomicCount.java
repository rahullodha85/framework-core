package com.hbcd.utility.helper;

public class AtomicCount {
    private static int count = 0;

    public static void incrementCount() {
        synchronized (AtomicCount.class) {
            count++;
        }
    }

    public static void decrementCount() {
        synchronized (AtomicCount.class) {
            count--;
        }
    }

    public static int getCount() {
        synchronized (AtomicCount.class) {
            return count;
        }
    }

    public static void setCount(int i) {
        synchronized (AtomicCount.class) {
            count = i;
        }
    }
}
