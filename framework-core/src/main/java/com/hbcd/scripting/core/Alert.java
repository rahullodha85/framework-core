package com.hbcd.scripting.core;

import com.hbcd.core.genericfunctions.GenericFunctions;

public class Alert {

    public static void Confirm() {
        GenericFunctions.coreClickConfirm();
    }

    public static void Dismiss() {
        GenericFunctions.coreClickDismiss();
    }
}
