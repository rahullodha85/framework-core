package com.hbcd.execution.core;

import com.hbcd.common.utility.Data;
import com.hbcd.execution.load.DataLayer;
import com.hbcd.execution.thread.ThreadPool;
import com.hbcd.logging.log.Log;
import com.hbcd.storage.data.SafeStorage;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.ProjectSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.common.Task;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.configurationsetting.ProcessTaskManager;
import com.hbcd.utility.schedulerUtility.SchedulerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class ProcessCallable implements Callable {
    private Task _objSetupConfig = new Task();
    private boolean _isSet = false;
    private boolean _isRunAsThread = false;

    //For DEFAULT CONSOLE - Everything from Configuration.properties
    public ProcessCallable() throws Exception  //Default from configuration.properties
    {
        List<String> listDefaultProcess = new ArrayList<String>();
        String dp = ConfigurationLoader.getSystemStringValue("RUN_DEFAULT_PROCESS_SITE");
        if (dp != null) {
            String newDp = dp.trim();
            if (newDp.length() > 0) {
                listDefaultProcess = new ArrayList<String>(Arrays.asList(dp.split("\\|")));
            }
        }

        if (listDefaultProcess.size() == 1) {
            for (String _siteModule : listDefaultProcess) {
                List<String> siteModule = Arrays.asList(_siteModule.split("\\."));
                if (siteModule.size() != 2) {
                    return;
                }

                //Init Driver set to true
                _objSetupConfig = ProcessTaskManager.get(siteModule); // new Task(_site, _moduleName, _tool, _browser, "", "", true, _hipchatNotificationRoom, _email);
            }

        } else {
            System.out.println("PLEASE SETUP Configuration.Properties with ONE Project ONLY.");
        }

    }

    public ProcessCallable(Task _objS) {
        _objSetupConfig.copy(_objS);
    }

    public ProcessCallable(Task _objS, boolean isRunAsThread) {
        _objSetupConfig.copy(_objS);
        _isSet = true;
        _isRunAsThread = isRunAsThread;
    }

    private void Initialize() throws Exception {

        //Re initialized for InheritableThreadSafe
        ProjectSetup.Setup(_objSetupConfig);
        Data.LoadRepository();
        Data.LoadTestSuite();
        DataLayer.LoadGiftCards();
        String moduleName = ApplicationSetup.get(Setting.MODULE_NAME);
        SafeStorage.Current().save("requestId", _objSetupConfig.getRequestId());

    }

    @Override
    public Object call() throws Exception {
        try {
            //Set Process to 'In Progess' status (Only with DataService or Output to DB
            SchedulerManager.updateSchedulerInProgress(_objSetupConfig.getRequestId());

            Initialize();

            ThreadPool.executeAsThread(_objSetupConfig);
//            if (!_isSet) {
//                _isRunAsThread = ConfigurationManager.IsMultiThread();
//            }
//
//            if (_isRunAsThread) {
//                ThreadPool.executeAsThread(_objSetupConfig, true);
//            } else {
//                ThreadPool.executeAsThread(_objSetupConfig, false);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.Error("ERROR ThreadPool", e);
        } finally {
            try {
                //Set Process to 'Completed' status (Only with DataService or Output to DB
                SchedulerManager.updateSchedulerCompleted(_objSetupConfig.getRequestId());
            } catch (Exception e) {
                Log.Error("Error on Update Scheduler", e);
            }
            System.out.println("FINISH Process Run.  Update Process?");
        }
        return null;
    }
}
