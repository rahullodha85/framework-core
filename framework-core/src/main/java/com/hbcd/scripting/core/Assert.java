package com.hbcd.scripting.core;

import com.hbcd.scripting.core.fluentInterface.AssertAction;

/**
 * Created by ephung on 1/21/2016.
 */
public class Assert {

    public static AssertAction Report(String msg) throws Exception
    {
        return new AssertAction_impl(msg, true);
    }

//    public static AssertAction Fail(String msg) throws Exception
//    {
//        return new AssertAction_impl(msg, false);
//    }
}
