package com.hbcd.utility.toggle;

import com.hbcd.utility.common.SystemToggleType;
import com.hbcd.utility.configurationsetting.ApplicationToggleLoad;

public class PerformanceToggle {

    public static boolean get() {
        try {
            return ApplicationToggleLoad.getModuleToggle(SystemToggleType.Performance.getvalue(), "NOT_SUPPORT");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
