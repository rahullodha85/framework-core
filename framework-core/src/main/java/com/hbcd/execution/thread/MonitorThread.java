package com.hbcd.execution.thread;

import com.hbcd.logging.log.Log;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by ephung on 9/8/16.
 */
public class MonitorThread  implements Runnable {

    private ThreadPoolExecutor executor;
    private int seconds;
    private boolean run=true;

    public MonitorThread(ThreadPoolExecutor executor, int delay)
    {
        this.executor = executor;
        this.seconds=delay;
    }
    public void shutdown(){
        this.run=false;
    }

    @Override
    public void run() {
        while(run){
            Log.Info(
                    String.format("[monitor] [%d/%d] Active Task: %d, Completed Task: %d, Total Task: %d, isShutdown: %s, isTerminated: %s",
                            this.executor.getPoolSize(),
                            this.executor.getCorePoolSize(),
                            this.executor.getActiveCount(),
                            this.executor.getCompletedTaskCount(),
                            this.executor.getTaskCount(),
                            this.executor.isShutdown(),
                            this.executor.isTerminated()));
            try {
                Thread.sleep(seconds*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
