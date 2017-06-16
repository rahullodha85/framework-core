package com.hbcd.logging.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LogInfoImp implements LogInfo {

//    static final Logger log = LogManager.getLogger(LogInfoImp.class);
    private List<String> _log = new ArrayList<String>();
    private boolean _isWriteLog = true;

    public LogInfoImp()
    {
    }

    public LogInfoImp(boolean isWrite)
    {
        _isWriteLog = isWrite;
    }

    @Override
    public void add(String i) {
        _log.add(i);
    }

    @Override
    public void print() {
        print("");
    }

    public void print(String scenario_name) {
        if (_isWriteLog) {
            String _localLog2 = String.format("[%s] tid.[%s] sn.[%s] : %s", Thread.currentThread().getName(), Thread.currentThread().getId(), scenario_name, toString());
            Log.Info(_localLog2);
        }
    }

    @Override
    public void clear() {
        _log.clear();
    }

    @Override
    public String toString() {
        String rtn = "";
        if (_log.size() > 0) {
            for (String s : _log) {
                rtn += s;
            }
        }
        return rtn;
    }
}
