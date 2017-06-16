package com.hbcd.execution.thread;

import com.hbcd.common.container.DataServiceContainer;
import com.hbcd.common.service.DataService;
import com.hbcd.common.service.ObjectRepositoryDataService;
import com.hbcd.common.service.TestSuiteDataService;
import com.hbcd.common.utility.ServiceName;
import com.hbcd.logging.log.Log;
import com.hbcd.scripting.base.ScenarioParameterSetting;
import com.hbcd.scripting.core.HipChat;
import com.hbcd.scripting.core.TestscriptData;
import com.hbcd.storage.data.SafeInternalStorage;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.common.Task;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.configurationsetting.ProcessTaskManager;
import com.hbcd.utility.configurationsetting.SiteConfigurationManager;
import com.hbcd.utility.entity.ObjectTestScript;
import com.hbcd.utility.schedulerUtility.SchedulerManager;
import com.hbcd.utility.testscriptdata.TestCaseResult;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ThreadPool {

    private static List<ObjectTestScript> getTestSuite()
    {
        String key = String.format("%s.%s.%s", ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME), ServiceName.TEST_SUITE);
        return ((DataService<ObjectTestScript>) DataServiceContainer.getService(key)).getList();
    }

    private static void printObjectRepositoryList()
    {
        String repositoryName = String.format("%s.%s", ApplicationSetup.get(Setting.SITE), ServiceName.OBJECT_REPOSITORY);
        Log.Info("----------------------------------  OBJECT REPOSITORY USAGE REPORT ---------------------------");
        Log.Info(String.format("SUITE: %s", repositoryName));
        Log.Info("Object Name|is Used (Y/N)|Last used");
        ((DataService<ObjectTestScript>) DataServiceContainer.getService(repositoryName)).print();
        Log.Info("----------------------------------------------------------------------------------------------");
    }

    public static ResultRun executeAsThread(Task _objSetupConfig) throws Exception {
        boolean _isMultiThread = ProcessTaskManager.IsMultiThread(_objSetupConfig.getSite(), _objSetupConfig.getModuleName());
        return executeAsThread(_objSetupConfig, _isMultiThread);
    }

    public static ResultRun executeAsThread(Task _objSetupConfig, boolean _isMultiThread) throws Exception {
        List<String> _reRunScript = SchedulerManager.selectPreviousFailTask(_objSetupConfig.getReRunReqId());
        return executeAsThread(_objSetupConfig, ThreadPool.getTestSuite(), _reRunScript, _isMultiThread);
    }

    public static ResultRun executeAsThread(Task _objSetupConfig, List<ObjectTestScript> _scripts) throws Exception {
        boolean _isMultiThread = ProcessTaskManager.IsMultiThread(_objSetupConfig.getSite(), _objSetupConfig.getModuleName());
        if ((_scripts == null) || (_scripts.size() <= 0))
        {
            _scripts = ThreadPool.getTestSuite();
        }

        List<String> _reRunScript = SchedulerManager.selectPreviousFailTask(_objSetupConfig.getReRunReqId());
        return executeAsThread(_objSetupConfig, _scripts, _reRunScript, _isMultiThread);
    }

    public static ResultRun executeAsThread(Task _objSetupConfig, List<ObjectTestScript> _scripts, List<String> _reRunScriptOnly, boolean _isMultiThread) throws Exception {
        AtomicInteger c = new AtomicInteger(0);
        ResultRun result = null;
        ResultRun result2 = null;

        Log.Info("MODULE is executing with Framework PROCESS.");
        //Get Number of Thread if there is Multi-Thread
        if (_objSetupConfig.isParallelExecution()) //Multi-Thread Execution
        {
            Log.Info(String.format("MODULE is executing with Multi-Threading (%s)", _objSetupConfig.getParallelExecution()));
        }

        try {
            if ((_scripts == null) || (_scripts.size() <= 0))
            {
                _scripts = ThreadPool.getTestSuite();
            }
            result = ExecuteThreadPool(_objSetupConfig, _scripts, _reRunScriptOnly);

            if (SiteConfigurationManager.isReRun(false))
            {
                _scripts = result.getFailScript();
                if (_scripts.size() > 0) {
                    result = ExecuteThreadPool(_objSetupConfig, _scripts, null);
                }
            }
            Log.Info("Finished all threads");
        } catch (Exception e) {
            e.printStackTrace();
            Log.Error("Process Error", e);
            throw e;
        } finally {
            printObjectRepositoryList();
        }
        return result;
    }

    public static ResultRun executeAsJUnit (Task _objSetupConfig, List<ObjectTestScript> _scripts) throws Exception {
        //com.hbcd.testscript.s5a.saksUrgentProjectUnregWeb.Scen01UnregWeb
        //waitForEnter = true;
        List<Class> testCases = new ArrayList<>();
        //testCases.addAll(_scripts.stream().filter(t -> !t.getScriptClassName().isEmpty()).map(Class.forName(t.getScriptClassName():new).collect(Collectors.toList()));
        for (ObjectTestScript ots : _scripts) testCases.add(Class.forName(ots.getScriptClassName()));
        Class<?>[] lst = (Class<?>[]) testCases.toArray();

//        JUnitCore.runClasses(lst);

        for (ObjectTestScript ots : _scripts) {
            Result result = JUnitCore.runClasses(Class.forName(ots.getScriptClassName()));
            for (Failure failure : result.getFailures())
            {
                System.out.println(failure.toString());
            }
        }
//        Class<?> [] classesArray = { cn };

//        testCases.add(cn);
//        JUnitCore core = new JUnitCore();
//        Result result = core.run(cn);
//        Result result2 = JUnitCore.runClasses(TestHelloWorld.class);
        return null;
    }

    public static ResultRun executeAsCucumber (Task _objSetupConfig, List<ObjectTestScript> _scripts) throws Exception {
        String _startMessage = "";
        AtomicInteger c = new AtomicInteger(0);
        int numberOfThread = 1; //Default single thread executor
        ResultRun result = null;
        ResultRun result2 = null;

//        if (fullPathFileName != null) {
//            proprs.load(new FileInputStream(fullPathFileName));
//        } else {
//            proprs.load(ConfigurationLoader.class.getClassLoader().getResourceAsStream(configureFileName));
//        }
//        _isloaded = true;

//        //Get Number of Thread if there is Multi-Thread
//        if (_isMultiThread) //Multi-Thread Execution
//        {
//            numberOfThread = ConfigurationManager.NumberOfParallel();
//        }

        try {
            Log.Info("MODULE is executed with CUCUMBER TOOL.");
            result = ExecuteCucumber(_objSetupConfig, numberOfThread, _scripts);

            Log.Info("Finished all threads");

        } catch (Exception e) {
            e.printStackTrace();
            Log.Error("Process Error", e);
            throw e;
        } finally {
//            NotifyEndProcess(result, _startMessage);  /* Notify when done */
            printObjectRepositoryList();
        }
        return result;
    }

    private static ResultRun ExecuteCucumber(Task objSetupConfig, int numberOfThread, List<ObjectTestScript> scripts) {
        Class<?> clazz = ThreadPool.class; //.getClass();
        Runtime runtime;
        RuntimeOptions runtimeOptions;
        RuntimeOptions runtimeOptions2;
        ResourceLoader resourceLoader;
        ClassLoader classLoader = clazz.getClassLoader();

        String fullPathFileName = "";
        String fullPath = com.hbcd.utility.helper.Common.DefaultParameterDirectory;
        System.out.println(fullPath);
//        FeatureResultListener resultListener;

//        String[] params = {".", "--tags", "@mytest", "--no-dry-run", "--glue", ""};  //Minimal
//        String[] params = {".", "--tags", "@mytest,@yourtest", "--no-dry-run", "--glue", "", "--plugin", "html:cucumber-html-reports"};  //Working fine
        //String[] params = {".", "--tags", "@mytest,@yourtest", "--no-dry-run", "--glue", "", "--plugin", "html:cucumber-html-reports"};  //Working fine
        String[] params = {fullPath, "--tags", "@mytest,@yourtest", "--no-dry-run", "--glue", "", "--plugin", "html:cucumber-html-reports"};  //Working fine

        runtimeOptions = new RuntimeOptions(new ArrayList<String>(Arrays.asList(params)));
//        runtimeOptions2 = new RuntimeOptions();
//        runtimeOptions2.
        resourceLoader = new MultiLoader(classLoader);
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);

        try {
//            Glue g = runtime.getGlue();
//            List<HookDefinition> lhd = g.getBeforeHooks();
//            for (HookDefinition hd : lhd) {
//                System.out.println(hd.getLocation(true));
//            }
            //runtime.buildBackendWorlds( new Rep);
            runtime.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isRun(String filterPattern, String tagName)
    {
        if ((filterPattern == null) || (filterPattern.isEmpty())) return true;
        //"^(\\d+x\\d+)"
        Pattern p = Pattern.compile(filterPattern);
        Matcher m = p.matcher(tagName);
        return m.matches();
//        return false;
    }

    private static ResultRun ExecuteThreadPool(Task osu, List<ObjectTestScript> tsl, List<String> reRun) throws Exception {
        int keepAliveTime = 0;
        String _startMessage = "";
        int _executingOrder = 1;
//		Instant _start = Instant.now(); 
//		Instant _end = null;
        long _startTime = 0;
        String filterPattern = osu.getFilterTestCase(); //"test12"; //null; //"test11";
        boolean _isPattern = false;
        final int cpus = java.lang.Runtime.getRuntime().availableProcessors();

//        final ExecutorService es = Executors.newFixedThreadPool( cpus );
//        final Vector< Batch > batches = new Vector< Batch >( cpus )
//        final int batchComputations = count / cpus;
//        for ( int i = 0; i < cpus; i++ ) {
//            batches.add( new Batch( batchComputations ) );
//        }

        if ((filterPattern != null) && (!filterPattern.isEmpty())) {
            _isPattern = true;
            Log.Info(String.format("TEST CASES: FILTER WITH REGULAR EXPRESSION : %s", filterPattern));
            if (filterPattern.equals("*")) filterPattern = ".*";
        } else
        {
            _isPattern = false;
            Log.Info("TEST CASES: Run as setting in EXCEL SetToRun column.");
        }
        ResultRun result = new ResultRun();
        long _req_id = ApplicationSetup.getLong(Setting.REQUEST_ID);
        if (osu.getRequestId() != _req_id) {
            Log.Error("INCONSISTENCY ON REQUEST ID.");
        }

        CustomThreadFactory cThreadFactory = new CustomThreadFactory();
        //Working fine
        ExecutorService executorMultiThread = new CustomThreadPoolExecutor(
                osu.getParallelExecution(), //corePoolSize
                osu.getParallelExecution(), //maxPoolSize
                keepAliveTime,
                TimeUnit.NANOSECONDS,
                new LinkedBlockingQueue<Runnable>() //LinkedBlockingQueue<Runnable>()
        );

        Collection<Future<TestCaseResult>> futures = new LinkedList<Future<TestCaseResult>>();

        //start the monitoring thread
        MonitorThread monitor = new MonitorThread((ThreadPoolExecutor)executorMultiThread, 11 /* Seconds */);
        Thread monitorThread = new Thread(monitor);

        try {
            Log.Info(String.format("TOTAL %s Test Scripts are scheduled for execution.", tsl.size()));
//			_start = Instant.now();
            _startTime = new Date().getTime();
            _startMessage = NotifyStartProcess(_req_id);

            monitorThread.start();

            for (ObjectTestScript ts : tsl) {

                boolean _toRun = false;
                _toRun = (_isPattern) ? isRun(filterPattern, ts.getID()) /* Regular Expression ID column as Tag */ : ts.IsSetToRun() /* regular isRun column */;
                if (!_toRun)
                {
                    continue;
                }

                boolean isExists = false;
                if ((reRun != null) && (reRun.size() > 0)) //has list mean only run within the list.
                {
                    isExists = reRun.stream().anyMatch(t -> t.contains(ts.getScriptClassName()));
                } else  //empty list mean run everything
                {
                    isExists = true;
                }

                if (!isExists) continue;

                if (_toRun) {
                    int size = ts.getToRun();
                    if ((_isPattern) && (size <= 0))
                    {
                            size = 1;
                    }
                    for (int i = 1; i <= size; i++) {
                        //System.out.println("Execute instance of :" + ts.getScriptClassName());
                        Log.Info(String.format("Scheduled instance of : [%s] to ThreadPool for execution", ts.getScriptClassName()));
                        SafeInternalStorage.Current().save("_CURRENT_SCENARIO_NAME_", ts.getScriptClassName());
                        SafeInternalStorage.Current().save("_REQUEST_ID_", osu.getRequestId());
                        SafeInternalStorage.Current().save("_INPUT_DATA_", ts.getDataReferenceID());
                        //String dataString;
                        Class<?> cn = Class.forName(ts.getScriptClassName());

                        Callable<TestCaseResult> testScen = ((Callable<TestCaseResult>) cn.newInstance());

                        try {

                            //Parameter Setup
                            ((ScenarioParameterSetting) testScen).setRequestId(_req_id);
                            ((ScenarioParameterSetting) testScen).setExecutionOrder(_executingOrder);
                            ((ScenarioParameterSetting) testScen).setTestScript(ts.clone());
                            Method ScenMethod = testScen.getClass().getMethod("ini", long.class, String[].class);

                            //Method ScenMethod=testScen.getClass().getMethod("ini", CaseParameter.class, String[].class);
                            if (ScenMethod != null) //Has "ini" method
                            {
                                ScenMethod.invoke(testScen, osu.getRequestId(), new String[]{ts.getDataReferenceID(), ts.getDescription()});
                            }
                        } catch (Exception ex) {
                            Log.Error("ERROR: ", ex);
                        }

                        //Thread myThread = new Thread(testScen, ts.getScriptClassName());

                        //myThread.start();   //For Atomic Counter

                        //executor.execute(myThread);

                        //cThreadFactory.setName(ts.getScriptClassName());

                        //Callable<String> callable = new (testScen);
                        //<T> Callable<T> callable(Runnable task, T result)
                        futures.add(executorMultiThread.submit(testScen));  //WaitFor till End all thread

                        //executorMultiThread.execute(testScen);  //WaitFor till End all thread

                        _executingOrder++; //Count increment

                    }  //End for Loop

                } //end if getToRun

            } //end for

//			_end = Instant.now();

            result = AnalyzeResult(osu, futures);

        } catch (ClassNotFoundException ex1){
            ex1.printStackTrace();
            Log.Error("ERROR: UNABLE TO FIND THE TEST CASE CLASS ", ex1);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.Error("ERROR: ", ex);
        } finally {
            monitor.shutdown();
            result.setTotalExecutionTime((new Date().getTime() - _startTime) / 1000);
            executorMultiThread.shutdown(); /* NO more new thread - still executing submitted threads */
            executorMultiThread = null;
            futures.clear();
            futures = null;
            NotifyEndProcess(result, _startMessage);  /* Notify when done */
        }
        return result;
    }


    private static ResultRun AnalyzeResult(Task osu, Collection<Future<TestCaseResult>> f) throws Exception {
        ResultRun rn = new ResultRun(osu.getRequestId());
        for (Future<TestCaseResult> future : f) {
            //TestCaseResult r = future.get();
            rn.AddToResultAnalysis(future.get());
        }
        return rn;
    }

    private static String NotifyStartProcess(long r_id) {
        String startMsg = "";
        try
        {
            startMsg = String.format("==><br />Started tests execution at %s | Module <b> %s.%s</b>"
                    , ApplicationSetup.get(Setting.ENVIRONMENT_URL), ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME));
            if (r_id > 0) {
                startMsg += String.format(" | Request Id: <b>%s</b>", r_id);
            }
            startMsg += "<br />";

            HipChat.Notify(ApplicationSetup.getLong(Setting.HIPCHAT_NOTIFICATION), startMsg);
        } catch (Exception e) {
            e.printStackTrace();
            Log.Error("ERROR: Unable To Notify With HIPCHAT\n", e);
        }
        return startMsg;
    }

    private static void NotifyEndProcess(ResultRun rr, String strMsg) {
        //int total = rc.getTotal();
        //float percentP = rc.getPercentagePass();
        try
        {
            String result = strMsg + "<br />";
            result = String.format("Test execution finished for %s | Module: <b>%s.%s</b>", ApplicationSetup.get(Setting.ENVIRONMENT_URL),
                    ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME));
            if (rr.getRequestId() > 0) {
                result += String.format(" | Request Id: <b>%s</b><br />", rr.getRequestId());
//            }
                result += String.format("<b>%s</b> out of <b>%s</b> passed.  Took : %s seconds.", rr.getPassCounter(), rr.getTotal(), rr.getTotalExecutionTime());

//            if (rr.getRequestId() > 0) {
                String url = ConfigurationLoader.getDefaultProcessValue("DEFAULT_RESULT_URL");
                if ((url != null) && !url.isEmpty()) {
                    result += String.format(" (<a href='%s'>To learn more about Automation Test Result for request %s</a>)", url + rr.getRequestId(), rr.getRequestId());
                }
            }

            result += "<br /><==<br />";
            System.out.println(result);
            HipChat.Notify(ApplicationSetup.getLong(Setting.HIPCHAT_NOTIFICATION), result);
        } catch (Exception e) {
            e.printStackTrace();
            Log.Error("ERROR: Unable To Notify With HIPCHAT\n", e);
        }
    }
}
