package com.hbcd.logging.log;

public class SafeLog {

    private static ThreadLocal<LogInfo> _myLog = new ThreadLocal<LogInfo>() {
        public LogInfo initialValue() {
            return new LogInfoImp();

        }
    };

    public static LogInfo Current() {
        return _myLog.get();
    }
}
