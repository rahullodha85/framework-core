package com.hbcd.core.genericfunctions;

/**
 * Created by ephung on 9/29/16.
 */
public interface CorePredicate<T> {
    boolean apply(T t) throws Exception;
}
