package com.hbcd.utility.configurationsetting;

import com.hbcd.common.utility.Common;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ApplicationGenericLoad {
    private static String _fileName = "";
    private static Properties _configProp = new Properties();
    private static Class<?> _class = ApplicationTextLoad.class; //null;
    private static boolean _isloaded = false;
    private static Lock _reEntrantLock = new ReentrantLock();

    private static Properties Current() throws Exception {
        _reEntrantLock.lock();
        try {
            if (!_isloaded) {
                String fullPathFileName = new Common().getFullPathFileName(_fileName);
                if (fullPathFileName != null) {
                    _configProp.load(new FileInputStream(fullPathFileName));
                } else {
                    _configProp.load(_class.getClassLoader().getResourceAsStream(_fileName));
                }
                _isloaded = true;
            }
        } catch (Exception e) {
            //throw e;
            throw new RuntimeException(String.format("Cannot find configuration file (%s): %s\n Cause:%s", _fileName, e.getMessage(), e.getCause()));
        } finally {
            _reEntrantLock.unlock();
        }
        return _configProp;
    }
}
