package com.hbcd.ContainerUtility;

//import sun.misc.JavaLangAccess;
//import sun.misc.SharedSecrets;

import java.io.File;
import java.net.URL;

/**
 * Created by ephung on 9/15/16.
 */
public class Helper {
    public static File findFile(String fullPathFileName, String fileName) {

        if ((fullPathFileName == null) || (fullPathFileName.trim().isEmpty())) { Log.Error("Unable to load (filename is empty)..."); return null; }
        File inputDataFile =null;
        String fileInfo="";
        try {
            inputDataFile = new File(fullPathFileName);
            if ((inputDataFile == null) || !inputDataFile.exists()) {

                URL dir_url = Helper.class.getClassLoader().getResource(fileName);
                if (dir_url == null) {
                    Log.Error(String.format("Unable to find file %s in Resource.", fileName));
                    throw new Exception(String.format("File %s not found.", fileName));
                }
//                fileInfo = String.format("as Resource %s", dir_url.);
                // Turn the resource into a File object
                inputDataFile = new File(dir_url.toURI());
            }
            else
            {
                fileInfo = String.format("%s", fullPathFileName);
            }

            if ((inputDataFile == null) || !inputDataFile.exists()) {
                Log.Error(String.format("Not found file: %s", fileInfo));
            } else {
                Log.Info(String.format("Found th file: %s", fileInfo));
            }
        }
        catch (Exception ex)
        {
            Log.Error("Unable to Find File", ex);
        }
        return inputDataFile;
    }

//    protected static String[] getClassName() {
//        JavaLangAccess access = SharedSecrets.getJavaLangAccess();
//        Throwable throwable = new Throwable();
//        int depth = access.getStackTraceDepth(throwable);
//
//        boolean lookingForLogger = true;
//        for (int i = 0; i < depth; i++) {
//            // Calling getStackTraceElement directly prevents the VM
//            // from paying the cost of building the entire stack frame.
//            StackTraceElement frame = access.getStackTraceElement(throwable, i);
//            String cname = frame.getClassName();
//            boolean isLoggerImpl = isLoggerImplFrame(cname);
//            if (lookingForLogger) {
//                // Skip all frames until we have found the first logger frame.
//                if (isLoggerImpl) {
//                    lookingForLogger = false;
//                }
//            } else {
//                if (!isLoggerImpl) {
//                    // skip reflection call
//                    if (!cname.startsWith("java.lang.reflect.") && !cname.startsWith("sun.reflect.")) {
//                        // We've found the relevant frame.
//                        return new String[] {cname, frame.getMethodName()};
//                    }
//                }
//            }
//        }
//        return new String[] {};
//        // We haven't found a suitable frame, so just punt.  This is
//        // OK as we are only committed to making a "best effort" here.
//    }
//
//    protected static String[] getClassNameJDK5() {
//        // Get the stack trace.
//        StackTraceElement stack[] = (new Throwable()).getStackTrace();
//        // First, search back to a method in the Logger class.
//        int ix = 0;
//        while (ix < stack.length) {
//            StackTraceElement frame = stack[ix];
//            String cname = frame.getClassName();
//            if (isLoggerImplFrame(cname)) {
//                break;
//            }
//            ix++;
//        }
//        // Now search for the first frame before the "Logger" class.
//        while (ix < stack.length) {
//            StackTraceElement frame = stack[ix];
//            String cname = frame.getClassName();
//            if (isLoggerImplFrame(cname)) {
//                // We've found the relevant frame.
//                return new String[] {cname, frame.getMethodName()};
//            }
//            ix++;
//        }
//        return new String[] {};
//        // We haven't found a suitable frame, so just punt.  This is
//        // OK as we are only committed to making a "best effort" here.
//    }
//
//    private static boolean isLoggerImplFrame(String cname) {
//        // the log record could be created for a platform logger
//        return (
//                cname.equals("my.package.Logger") ||
//                        cname.equals("java.util.logging.Logger") ||
//                        cname.startsWith("java.util.logging.LoggingProxyImpl") ||
//                        cname.startsWith("sun.util.logging."));
//    }
}
