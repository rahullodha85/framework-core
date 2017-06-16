package com.hbcd.scripting.core.fluentInterface;

import com.hbcd.utility.entity.ObjectProperties;

/**
 * Created by ephung on 5/16/2016.
 */
public interface AssertAction {
    AssertActionLevel2 areEqual(String expected, String actual) throws Exception;
    AssertActionLevel2 areEqual(double expected, double actual) throws Exception;
    AssertActionLevel2 areEqual(int expected, int actual) throws Exception;
    AssertActionLevel2 isTrue (boolean condition) throws Exception;
    AssertActionLevel2 isExist(String objName) throws Exception;
    AssertActionLevel2 isNotExist(String objName) throws Exception;
    AssertActionLevel2 isExist(ObjectProperties obj) throws Exception;
    AssertActionLevel2 isNotExist(ObjectProperties obj) throws Exception;
    AssertActionLevel2 isFound(String objName) throws Exception;
    AssertActionLevel2 isFound(ObjectProperties obj) throws Exception;
}
