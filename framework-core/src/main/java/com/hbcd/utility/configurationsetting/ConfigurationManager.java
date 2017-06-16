package com.hbcd.utility.configurationsetting;

public class ConfigurationManager {

    public static boolean IsRecordVideo() {
        try {
            return ConfigurationLoader.getSystemStringValue("RECORD_VIDEO").toUpperCase().equals("TRUE") ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    public static boolean IsMultiThread() {
//        try {
//
//            return ConfigurationLoader.getSystemStringValue("MULTI_THREAD").toUpperCase().equals("TRUE") ? true : false;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    public static int NumberOfParallel() //throws NumberFormatException, Exception
    {
        try {
            return Integer.parseInt(ConfigurationLoader.getSystemStringValue("NUMBER_OF_THREAD"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static String ReportOutput() {
        try {
            return ConfigurationLoader.getSystemStringValue("REPORT_OUTPUT").toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "CONSOLE";
    }

    public static boolean IsRunAsService() {
        try {
            return ConfigurationLoader.getSystemStringValue("RUN_AS").toUpperCase().equals("SERVICE") ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean IsReportingToDB() {
        return ReportOutput().toUpperCase().equals("DB");
    }
}
