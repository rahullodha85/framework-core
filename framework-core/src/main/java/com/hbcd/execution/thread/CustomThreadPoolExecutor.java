package com.hbcd.execution.thread;

import com.hbcd.common.utility.ObjectPropertyUtility;
import com.hbcd.logging.log.Log;
import com.hbcd.scripting.core.BrowserAction;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.configurationsetting.SiteConfigurationManager;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class CustomThreadPoolExecutor extends ThreadPoolExecutor {
    private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();

    public CustomThreadPoolExecutor(int corePoolSize,
                                    int maximumPoolSize, long keepAliveTime,
                                    TimeUnit unit, LinkedBlockingQueue<Runnable> workQueue,
                                    ThreadFactory threadFactory) //BlockingQueue
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime,
                unit, workQueue, threadFactory);
    }

    public CustomThreadPoolExecutor(int corePoolSize,
                                    int maximumPoolSize, long keepAliveTime,
                                    TimeUnit unit, LinkedBlockingQueue<Runnable> workQueue) //BlockingQueue
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime,
                unit, workQueue);
    }

    @Override
    public void beforeExecute(Thread t, Runnable r) {
        if (t == null || r == null) {
            throw new NullPointerException();
        }
        //our code

        try {
            //String browserName, String browserType, String browserVersion, String browserPlatform
            if (ApplicationSetup.getBoolean(Setting.IS_INITIALIZE_DRIVER)) {
                BrowserAction.initializeDriver( ApplicationSetup.get(Setting.BROWSER_TYPE)
                                                , ApplicationSetup.get(Setting.BROWSER_NAME)
                                                , ApplicationSetup.get(Setting.BROWSER_VERSION)
                                                , ApplicationSetup.get(Setting.RESOLUTION)
                                                , ApplicationSetup.get(Setting.REMOTE_HUB)
                                                , ApplicationSetup.get(Setting.BROWSER_PLATFORM)
                                                , ApplicationSetup.get(Setting.PROXY_SETTING)
                );
//                ApplicationSetup.get(Setting.BROWSER_TYPE), ApplicationSetup.get(Setting.BROWSER_TYPE), ApplicationSetup.get(Setting.BROWSER_VERSION), ApplicationSetup.get(Setting.BROWSER_PLATFORM)); //create driver
            }
            super.beforeExecute(t, r);
            //log.fine(String.format("Thread %s: start %s", t, r));
            startTime.set(System.nanoTime());
        } catch (Exception e) {
            e.printStackTrace();
            Log.Error("ERROR beforeExecute Thread", e);
        }

        Log.Info("Done Custom ThreadPool BeforeExecute");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t != null) {
            System.out.println("Perform exception handler logic");
        }

        long endTime = System.nanoTime();
        long taskTime = endTime - startTime.get();
//        numTasks.incrementAndGet();
////        totalTime.addAndGet(taskTime);
//        System.out.println(String.format("Thread %s: end %s, time=%dns",
//                t, r, taskTime));

        System.out.println(String.format("Thread %s: end %s, time=%dns",
                t, r, taskTime));

        try {

            //System.out.println("Perform afterExecute() logic");
            ObjectPropertyUtility.cleanLocalRepositoryObject();  //Clean up Local Repository Memory

            Future<?> retValue = (Future<?>) ((Future<?>) r).get(); // if your Callable returns a String

            // add your code here

        } catch (InterruptedException ie) { // handle it here

        } catch (ExecutionException ee) { // handle it here

        } catch (CancellationException ce) {// handle it here

        } catch (Exception ex) { //handle it here

        }

        try {
            if (ApplicationSetup.getBoolean(Setting.IS_INITIALIZE_DRIVER)) {
                BrowserAction.deleteAllCookies();
                if (SiteConfigurationManager.isCloseBrowserAfterFinish(true)) {
                    BrowserAction.tearDown(); //Close driver
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.Error("ERROR afterExecute Thread", e);
        } finally {
        }
    }
}
