package com.hbcd.scripting.core.fluentInterface;

import java.util.List;

/**
 * Created by ephung on 5/24/2016.
 */
public interface InputDataAction {
    String select(int idx) throws Exception;
    List<String> select(String filterPattern) throws Exception;
    String selectRandom() throws Exception;
    String selectSequential() throws Exception;
    int size() throws Exception;
    String selectFirst() throws Exception;
    String selectLast() throws Exception;
    String value() throws Exception;
}
