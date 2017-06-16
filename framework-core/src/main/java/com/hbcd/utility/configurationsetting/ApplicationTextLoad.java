package com.hbcd.utility.configurationsetting;

import com.hbcd.common.utility.Common;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ApplicationTextLoad {

    private static String textFileName = "Text.properties";
    private static Properties _msgProp = new Properties();
    private static boolean _isloaded = false;
    private static Lock _lockme = new ReentrantLock();

    private static Properties Current() throws Exception {
        _lockme.lock();
        try {
            if (!_isloaded) {

                String fullPathFileName = new Common().getFullPathFileName(textFileName);
                if (com.hbcd.utility.helper.Common.isNotNullAndNotEmpty(fullPathFileName))
                {
                    _msgProp.load(new FileInputStream(fullPathFileName));
                }
                else
                {
                    _msgProp.load(ApplicationTextLoad.class.getClassLoader().getResourceAsStream(textFileName));
                }
//                File testFile = new File(fileName);
//
//                if (!testFile.exists()) {
//                    fileName = Common.DefaultParameterDirectory + "\\" + fileName;
//                }

//                _msgProp.load(new FileInputStream(fileName));
                _isloaded = true;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            _lockme.unlock();
        }
        return _msgProp;
    }

    public static String getValue(String s) throws Exception {
        return Current().getProperty(s);
    }

    public static String getSiteText(String messageId) throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }
        return Current().getProperty(String.format("%s.TEXT.%s", ApplicationSetup.get(Setting.SITE), messageId));
    }

    public static String getSiteText(int messageId) throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }
        return Current().getProperty(String.format("%s.TEXT.%s", ApplicationSetup.get(Setting.SITE), Integer.toString(messageId)));
    }

    public static String getSystemText(String s) throws Exception {
        return Current().getProperty(String.format("SYSTEM.%s", s));
    }
}
