package com.hbcd.reporting.objects;

import com.hbcd.reporting.constants.ModuleConstant;

import java.sql.Date;
import java.util.HashMap;

public class Module {
    private int moduleId;
    private String moduleType;
    private String moduleScheduler;
    private Date moduleScheduleTime;
    private boolean moduleStatus;

    public Module(HashMap<ModuleConstant, Object> input) {
        this.moduleType = (String) input.get(ModuleConstant.moduleType);
        this.moduleScheduler = (String) input.get(ModuleConstant.moduleScheduler);
        this.moduleScheduleTime = (Date) input.get(ModuleConstant.moduleScheduleTime);
        this.moduleStatus = (boolean) input.get(ModuleConstant.moduleStatus);
    }

    public String toString() {
        return String.format("ModuleType= %s, Scheduler= %s, Time=%s, Status=%s."
                , this.moduleType
                , this.moduleScheduler
                , this.moduleScheduleTime
                , this.moduleStatus);
    }

    public int getModuleId() {
        return moduleId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public String getModuleScheduler() {
        return moduleScheduler;
    }

    public Date getModuleScheduleTime() {
        return moduleScheduleTime;
    }

    public boolean getModuleStatus() {
        return moduleStatus;
    }
}
