package com.hbcd.logging.log;

import org.apache.logging.log4j.Logger;

public class Log {
    private static String loggerName = "FrameworkLogger";
    private static Logger logger = null;

    //Initialized the static variable.
    static {
        logger = LogConfiguration.getLogger(loggerName);
    }

    public static void Error(String msg, Throwable e) {
        logger.error(msg, e);
        System.out.println(msg);
    }

    public static void Error(String msg) {
        logger.error(msg);
        System.out.println(msg);
    }

    public static void Info(String msg) {
        logger.info(msg);
        System.out.println(msg);
    }

    public static void Warn(String msg) {
        logger.warn(msg);
        System.out.println(msg);
    }
}
