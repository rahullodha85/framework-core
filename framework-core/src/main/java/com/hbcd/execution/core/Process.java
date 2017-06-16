package com.hbcd.execution.core;

import com.hbcd.common.utility.Data;
import com.hbcd.execution.load.DataLayer;
import com.hbcd.execution.thread.ResultRun;
import com.hbcd.execution.thread.ThreadPool;
import com.hbcd.logging.log.Log;
import com.hbcd.storage.data.SafeStorage;
import com.hbcd.utility.common.ProjectSetup;
import com.hbcd.utility.common.Task;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.entity.ObjectTestScript;
import com.hbcd.utility.schedulerUtility.SchedulerManager;

import java.util.ArrayList;
import java.util.List;

public class Process extends Thread {
    private Task _task = new Task();
//    private boolean _isRunAsThread = ConfigurationManager.IsMultiThread();
    private List<ObjectTestScript> _scriptList = new ArrayList<ObjectTestScript>();
    private boolean _isRMI = false;

    //For DEFAULT CONSOLE - Everything from Configuration.properties
    public Process() throws Exception  //Default from configuration.properties
    {
        _task = ProjectSetup.GetDefaultSetup();
    }

    public Process(Task _objS) {
        _task.copy(_objS);
        //Re initialized for InheritableThreadSafe
    }

    public Process(Task _objS, List<ObjectTestScript> _sl) {
        try {
            if (_sl == null) {
                System.out.println("ERROR: _sL IS NULL.");
                throw new Exception("Script List Empty");
            } else {
                System.out.println(String.format("SIZE : %s", _sl.size()));
            }
            _task.copy(_objS);
            //if (_scriptList == null) { _scriptList = new ArrayList<ObjectTestScript>();}
            _sl.forEach(myObject -> _scriptList.add(myObject.clone()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void Initialize() throws Exception {
        //Re initialized for InheritableThreadSafe
        ProjectSetup.Setup(_task);
//		RepoController.mapObjects();
        Data.LoadRepository();
        Data.LoadTestSuite();
        Data.LoadTestData();
//        DataExcelGC.LoadEmcEgcCards();
        DataLayer.LoadGiftCards();  //Move to Scripting  Should be in a Scripting Scenario and not in Framework
        SafeStorage.Current().save("requestId", _task.getRequestId());
    }

    //CURRENTLY being used when run as COMMAND
    public ResultRun execute() throws Exception {
        try {
            //Set Process to 'In Progess' status (Only with DataService or Output to DB
            SchedulerManager.updateSchedulerInProgress(_task.getRequestId());

            Initialize();
            if ((_task.getExecutionType().isEmpty()) || (_task.getExecutionType().equalsIgnoreCase("DEFAULT"))) {
                if ((_scriptList == null) || (_scriptList.size() <= 0)) {
                    return ThreadPool.executeAsThread(_task);
                } else {
                    return ThreadPool.executeAsThread(_task, _scriptList);
                }
            } else if (_task.getExecutionType().equalsIgnoreCase("CUCUMBER")) {
                return ThreadPool.executeAsCucumber(_task, _scriptList);
            } else if (_task.getExecutionType().equalsIgnoreCase("JUNIT")) {
                return ThreadPool.executeAsJUnit(_task, _scriptList);
            } else {
                throw new Exception("Unable to determin PROJECT TYPE (\"DEFAULT\"/\"CUCUMBER\")");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.Error("ERROR ThreadPool", e);
            throw e;
        } finally {
            try {
                //Set Process to 'Completed' status (Only with DataService or Output to DB
                SchedulerManager.updateSchedulerCompleted(_task.getRequestId());
            } catch (Exception e) {
                Log.Error("Error on Update Scheduler", e);
            }
            System.out.println("FINISH Process Execution.  Update Process?");
        }
    }

    //CURRENTLY being used when RUN AS SERVICE
    @Override
    public void run() {
        try {
            //Set Process to 'In Progess' status (Only with DataService or Output to DB
            SchedulerManager.updateSchedulerInProgress(_task.getRequestId());

            Initialize();

            if (ConfigurationLoader.getSystemBooleanValue("RMI_ENABLE")) {

            } else {
                if (_task.getExecutionType().equalsIgnoreCase("DEFAULT")) {
                    if ((_scriptList == null) || (_scriptList.size() <= 0)) {
                        //ThreadPool.executeAsThread(_task, _isRunAsThread);
                        ThreadPool.executeAsThread(_task);
                    } else {
                        //ThreadPool.executeAsThread(_task, _scriptList, _isRunAsThread);
                        ThreadPool.executeAsThread(_task, _scriptList);
                    }
                }
                else if (_task.getExecutionType().equalsIgnoreCase("CUCUMBER"))
                {
                    ThreadPool.executeAsCucumber(_task, _scriptList);
                }
                else if (_task.getExecutionType().equalsIgnoreCase("JUNIT"))
                {
                    ThreadPool.executeAsJUnit(_task, _scriptList);
                }
                else
                {
                    throw new Exception("Unable to determin PROJECT TYPE (\"DEFAULT\"/\"CUCUMBER\")");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.Error("ERROR ThreadPool", e);
        } finally {
            try {
                //Set Process to 'Completed' status (Only with DataService or Output to DB
                SchedulerManager.updateSchedulerCompleted(_task.getRequestId());
            } catch (Exception e) {
                Log.Error("Error on Update Scheduler", e);
            }
            System.out.println("FINISH Process Run.  Update Process?");
        }

    }
}
