package com.hbcd.logging.log;

import org.apache.logging.log4j.Logger;

/**
 * Created by ephung on 8/2/16.
 */
public class LogReport {
    private static String loggerName = "SuiteResultLogger";
    private static Logger reportLogger = null;

    //Initialized the static variable.
    static {
        reportLogger = LogConfiguration.getLogger(loggerName);
    }

    public static void Error(String msg, Throwable e) {

        reportLogger.error(msg, e);
        System.out.println(msg);
    }

    public static void Error(String msg) {
        reportLogger.error(msg);
        System.out.println(msg);
    }

    public static void Info(String msg) {
        reportLogger.info(msg);
        System.out.println(msg);
    }

    public static void Warn(String msg) {
        reportLogger.warn(msg);
        System.out.println(msg);
    }
}
