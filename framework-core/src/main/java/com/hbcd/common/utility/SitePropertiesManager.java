package com.hbcd.common.utility;


import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class SitePropertiesManager {


    private static final ThreadLocal<Properties> _ObjProp = new ThreadLocal<Properties>() {
        @Override
        protected Properties initialValue() {
//				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSS");
//				Date date = new Date();
//				System.out.println(dateFormat.format(date));
            return new Properties();
        }
    };

    private static ThreadLocal<Boolean> _isLoaded = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            _isLoaded.set(Boolean.FALSE);
            return _isLoaded.get();
        }
    };

    private final static ThreadLocal<Lock> _lock = new ThreadLocal<Lock>() {
        @Override
        protected Lock initialValue() {
            return new ReentrantLock();
        }
    };


    public synchronized static Properties Current() throws Exception {
        _lock.get().lock();
        try {
            if (_isLoaded.get() == false) {
                try {
                    _ObjProp.get().load(new FileInputStream(ConfigurationLoader.getSiteRepository(ApplicationSetup.get(Setting.SITE))));
                    _isLoaded.set(true);
                } catch (Exception e) {
                    throw e;
                }
            }
        } finally {
            _lock.get().unlock();
        }

        return _ObjProp.get();
    }
}
