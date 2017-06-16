package com.hbcd.reporting.controllers;

import com.hbcd.utility.configurationsetting.ConfigurationManager;

public class SafeReport {

    private static ThreadLocal<Controller> _myReport = new ThreadLocal<Controller>() {
        public Controller initialValue() {
            try {
                return getController();
            } catch (Exception e) {
            }
            return null;
        }
    };

    public synchronized static Controller Current() {
        return _myReport.get();
    }

    public synchronized static void init(String browserName, String browserType, String browserVersion, String browserPlatform) throws Exception {
        if (_myReport == null) {
            _myReport = new ThreadLocal<Controller>() {
                public Controller initialValue() {
                    try {
                        return getController();
                    } catch (Exception e) {
                    }
                    return null;
                }
            };
        }

        _myReport.get().openConnection();
    }

    private static Controller getController() throws Exception {
        switch (ConfigurationManager.ReportOutput()) {
            case "DB":
                return new MysqlController();
            case "CONSOLE":
                return new ConsoleController();
            case "EXCEL":
                ExcelController ec = new ExcelController();
                //ec.init();  //Should Put init in Constructor.
                return ec;

            default:
                return null;
        }
    }
}
