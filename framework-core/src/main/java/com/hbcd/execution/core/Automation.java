package com.hbcd.execution.core;

import com.hbcd.execution.thread.ResultRun;
import com.hbcd.logging.log.Log;
import com.hbcd.utility.common.Task;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.configurationsetting.ConfigurationManager;
import com.hbcd.utility.configurationsetting.ProcessTaskManager;
import com.hbcd.utility.helper.Common;
import com.hbcd.utility.schedulerUtility.SchedulerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

public class Automation {

    static boolean isAsService = false;
    static String myIP = "N/A";
    static List<String> applicaitonArguments = null;

    private static void StartAsService() {
        try {
            while (true) {
                ExecuteTask();
                Thread.sleep(30000); //30 Seconds
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void ExecuteTask() {
        myIP = Common.getIpAddress();
//        HashMap<String, Object> taskData = SchedulerManager.getTopScheduler(Common.ServerFirstStart, myIP);
//        if (taskData != null) {
//            //Start Execute - SHOULD always be thread
//            try {
//
//                Task tsk = ProcessTaskManager.get(taskData);
//                //new Thread((Runnable) new Process(sc)).start();
//                new Process(tsk).start();
//                Common.ServerFirstStart = false;
//
//            } catch (Exception ex) {
//                Log.Error("Error", ex);
//            }
//        } else {
//            Log.Info("NO JOB FOUND!!!");
//        }

        List<Task> tskList = SchedulerManager.getExecuteTaskList(Common.ServerFirstStart, myIP);

        if (tskList == null) return;
        if (tskList.size() == 0) {
            Log.Info("<<<NO JOB FOUND!!!>>>");
        }
        else if (tskList.size() > 0)
        {
            //Start Execute - SHOULD always be thread
            try {
                List<Task> taskInProgress = tskList.stream().filter(task -> task.getExecutionStatus()==1).collect(Collectors.toList());
                List<Task> taskInQueue = tskList.stream().filter(task -> task.getExecutionStatus()==0).collect(Collectors.toList());

                if (taskInProgress.size() > 0)
                {
                    int _pC = taskInProgress.size();
                    int _qC = taskInQueue.size();
                    String _m = "";
                    String _m2 = "";
                    _m2 = _qC > 1 ? " other tasks are in Queue" : " other task is in Queue.";
                    _m = (_qC > 0 ?  " :: " + _qC + _m2 : "");

                    Log.Info(String.format("<<<JOB EXECUTION IN PROGRESS!!!>>> - Running module: %s.%s", taskInProgress.get(0).getSite(), taskInProgress.get(0).getModuleName() + _m));
                }
                else
                {
                    //Start New Process
                    Log.Info(String.format("<<<START EXECUTION MODULE>>> %s.%s", tskList.get(0).getSite(), tskList.get(0).getModuleName()));
                    new Process(tskList.get(0)).start();
                }

                //new Thread((Runnable) new Process(sc)).start();
                //new Process(tsk).start();
                Common.ServerFirstStart = false;

            } catch (Exception ex) {
                Log.Error("Error", ex);
            }
        } else {
            Log.Info("NO JOB FOUND!!!");
        }
    }

    private static int StartAsApplication() {
        int _status = 1; //0: pass 1 : fail
        int countFail = 0;
        List<String> listDefaultProcess = getDefaultProcessSite();
        if (listDefaultProcess.size() <= 0)
        {
            Log.Info("There is nothing set to run.   Please set module execution in: SYSTEM.RUN_DEFAULT_PROCESS_SITE");
            return 1; //fail
        }
        for (String _siteModule : listDefaultProcess) {
            List<String> siteModule = Arrays.asList(_siteModule.split("\\."));
            if (siteModule.size() != 2) {
                Log.Info("Error: Please set value:  #SITE#.#MODULE#");
                return 1; //fail
            }
            //new Thread((Runnable) new Process(ProcessTaskManager.get(siteModule))).start();
            //new Process(ProcessTaskManager.get(siteModule)).start();
            try {
                countFail = 0;
                ResultRun _temp_status = new Process(ProcessTaskManager.get(siteModule)).execute();
                if (_temp_status.getProcessResultStatus() == 1) //There are fail
                {
                    countFail++;
                }

            } catch (Exception e) {
                countFail = -1;
                e.printStackTrace();
            }
        }
        if (countFail == 0) { _status = 0; }
        return _status;
    }

    public static int start(String[] argv)
    {
        if ((argv != null) && argv.length > 0)
        {
            applicaitonArguments = Arrays.asList(argv);
        }
        return start();
    }
    public static int start() {
        int _status = 1;
        try {
            myIP = Common.getIpAddress();
            setDefaultDirectory();

            Log.Info("*************************************************************************");
            Log.Info(String.format("**            Starting test execution on server : %s         **", myIP));
            Log.Info(String.format("** Default Directory: %s         **", Common.DefaultParameterDirectory));
            Log.Info(String.format("** User Directory: %s             **", System.clearProperty("usr.dir")));
            Log.Info("*************************************************************************");
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.Info(e2.getLocalizedMessage());
        }

        try {
            //Is Run as DataService?
            if (ConfigurationManager.IsRunAsService()) {
                //RUN AS SERVICE (infinite loop) - using Scheudler
                _status = 0; //always pass since not execute as command
                Log.Info("APPLICATION is started as SERVICE");
                StartAsService();
            } else {
                //COMMAND LINE - NOT A SERVICE - Running as a console
                //return (StartAsApplication() == 0) ? System.exit(0) : System.exit(1);
                Log.Info("APPLICATION is started as COMMAND");
                _status = StartAsApplication();
                if (_status == 0) { //pass
                    Log.Info("Exit with Pass Status: 0");
                }
                else {
                    Log.Info(String.format("Exit with Fail Status: %s", + _status));
                }
                System.exit(_status);
            }
        } catch (Exception e) {
            Log.Error("Unable to run as service", e);
        }

        return _status;
    }

    private static void setDefaultDirectory()
    {
        try {

            String sourcePath = getSourcePath();
            sourcePath =  removeFirstSlash(sourcePath);
            String projectPath =  removeFromString(sourcePath, "/target/classes"); //sourcePath.substring(0, position);

            if (Common.isNotNullAndNotEmpty(projectPath)
                    && Common.isNullOrEmpty(com.hbcd.utility.helper.Common.DefaultParameterDirectory))
            {
                com.hbcd.utility.helper.Common.DefaultParameterDirectory = projectPath;
            }
        } catch (Exception e) {
            Log.Error("Unable to run as service", e);
        }
    }

    private static String getSourcePath()
    {
        try {
//            System.out.println(file.getAbsolutePath());

//            URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
//            for (URL url: urls) {
//                System.out.println(url.getFile());
//            }
            StackTraceElement[] stkElements =Thread.currentThread().getStackTrace();
            String mainClassName = null;
            if (stkElements.length > 0)
            {
                for (StackTraceElement stkTrcElement : stkElements )
                {
                    if (stkTrcElement.getMethodName().equals("main"))
                    {
                        mainClassName = stkTrcElement.getClassName();
                        break;
                    }
                }
                if (mainClassName != null)
                {
                    Class<?> clsName = Class.forName(mainClassName);
                    String nativeDir = clsName.getProtectionDomain().getCodeSource().getLocation().getPath();
                    if(nativeDir.endsWith(".jar")) {
                        String separator = System.getProperty("file.separator");
                        if (nativeDir.startsWith("/"))
                        {
                            separator = "/";
                        } else if (nativeDir.startsWith("\\"))
                        {
                            separator = "\\";
                        }
//                        System.out.println("YES WITH JAR IN THE END");
                        nativeDir = nativeDir.substring(0, nativeDir.lastIndexOf(separator));
//                        System.out.println("AFTER REMOVED JAR : " + nativeDir);
                    }
                    return nativeDir;

                }
            }
        } catch (Exception e) {
            Log.Error("Unable to run as service", e);
        }
        return null;
    }

    private static String removeFromString(String sourceString, String subString)
    {
        if (Common.isNullOrEmpty(sourceString)) return null;
        int position = sourceString.indexOf(subString);
        return (position > 0)? sourceString.substring(0, position) : sourceString;
    }

    private static String removeFirstSlash(String sourceString) {
        if (Common.isNullOrEmpty(sourceString)) return null;
//        return ((sourceString.startsWith("/")) || (sourceString.startsWith("\\"))) ? sourceString.substring(1) : sourceString;
        return (sourceString.startsWith("\\")) ? sourceString.substring(1) : sourceString;
    }

    public static Object startCallable() {
        ArrayList<Future<Object>> results = new ArrayList<Future<Object>>();
        try {
            Callable<Object> p = new ProcessCallable();
            FutureTask<Object> task =
                    new FutureTask<Object>(p);
            results.add(task);
            //Create a thread object using the task object created
            Thread t = new Thread(task);
            //Start the thread as usual
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.Error("ERROR ThreadPool", e);
        } finally {
            System.out.println("FINISH Process Run.  Update Process?");
        }
        return null;
    }

    private static List<String> getDefaultProcessSite() {
        String dp = ConfigurationLoader.getSystemStringValue("RUN_DEFAULT_PROCESS_SITE");
        if (dp != null) {
            if (dp.trim().length() > 0) {
                return Arrays.asList(dp.trim().split("\\|"));
            }
        }
        return new ArrayList<>();
    }
}
