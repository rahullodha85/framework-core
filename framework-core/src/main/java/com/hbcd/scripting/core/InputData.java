package com.hbcd.scripting.core;

import com.hbcd.scripting.core.fluentInterface.InputDataAction;

/**
 * Created by ephung on 3/14/2016.
 */
public class InputData {

    public static InputDataAction get(String objectDataName) {
        return new InputDataAction_impl(objectDataName);
    }
}
