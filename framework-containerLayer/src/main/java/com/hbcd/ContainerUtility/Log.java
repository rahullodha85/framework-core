package com.hbcd.ContainerUtility;


import org.apache.logging.log4j.Logger;

/**
 * Created by ephung on 9/15/16.
 */
public class Log {
    private static String containerLoggerName = "ContainerLogger";
    private static final Logger containerLogger = new LogConfiguration().getLogger(containerLoggerName);;

//    static {
//        containerLogger =
//    }

//    //Initialized the static variable.
//    static {
//        logger = _context.getLogger(loggerName);
//    }

    public static void Error(String msg, Throwable e) {
        containerLogger.error(msg, e);
        System.out.println(msg);
    }

    public static void Error(String msg) {
        containerLogger.error(msg);
        System.out.println(msg);
    }

    public static void Info(String msg) {
        containerLogger.info(msg);
        System.out.println(msg);
    }

    public static void Warn(String msg) {
        containerLogger.warn(msg);
        System.out.println(msg);
    }

}
