package com.hbcd.reporting.impl;

import com.hbcd.reporting.Record;
import com.hbcd.reporting.constants.ModuleConstant;

import java.sql.Date;
import java.util.HashMap;

public class ModuleRecordImpl implements Record {
    private HashMap<ModuleConstant, Object> moduleMap = new HashMap<ModuleConstant, Object>();

    public void setModuleType(String moduleType) {
        moduleMap.put(ModuleConstant.moduleType, moduleType);
    }

    public void setModuleScheduler(String name) {
        moduleMap.put(ModuleConstant.moduleScheduler, name);
    }

    public void setModuleScheduleTime(Date time) {
        moduleMap.put(ModuleConstant.moduleScheduleTime, time);
    }

    public void setModuleStatus(boolean status) {
        moduleMap.put(ModuleConstant.moduleStatus, status);
    }

    public HashMap<ModuleConstant, Object> getModuleRow() {
        return moduleMap;
    }
}
