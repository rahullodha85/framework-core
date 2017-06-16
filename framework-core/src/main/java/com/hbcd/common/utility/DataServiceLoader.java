package com.hbcd.common.utility;

import com.hbcd.common.service.DataServiceMaintenanceInterface;
import com.hbcd.common.service.DataService;
import com.hbcd.logging.log.Log;
import com.hbcd.utility.common.GenericDataInterface;
import com.hbcd.utility.entity.PropertyIndex;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by ephung on 9/1/16.
 */
public class DataServiceLoader<T> implements DataServiceLoaderInterface {


    Class<?> _serviceClassName = null; //
    Class<?> _entityClassName = null; //Class.forName(sn.getEntityClassName());
    File _inputDataFile = null;
    DataServiceMaintenanceInterface _serviceObject = null;
    String _expectFirstColumnName = ""; //assume first column by default.

    @Override
    public DataServiceLoaderInterface setServiceClass (String sn)
    {
        try {
            _serviceClassName = Class.forName(sn);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public DataServiceLoaderInterface setEntityClass (String en)
    {
        try {
            _entityClassName = Class.forName(en);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public DataServiceLoaderInterface setService(ServiceName sn) {
        try {
            _serviceClassName = Class.forName(sn.getServiceClassName());
            _entityClassName = Class.forName(sn.getEntityClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public DataServiceLoaderInterface setInputFile(File f)
    {
        _inputDataFile = f;
        return this;
    }

    @Override
    public <T> Object getService() throws Exception {
        _serviceObject = (DataServiceMaintenanceInterface) _serviceClassName.newInstance();
        _expectFirstColumnName = _serviceObject.getEntityFirstColumnName(); // ((GenericDataInterface) _entityClassName.newInstance()).getStartColumnName();
        _entityClassName = (Class<T>)_serviceObject.getEntityClass();
        load();
        Log.Info(String.format("Loaded: %d record(s).", ((DataService)_serviceObject).size()));
        return _serviceObject;
    }

    protected Object load() throws IOException {
        DataService s = null;
        return s;
    }

    protected T buildObject(ArrayList<PropertyIndex> data) throws Exception {
        //get constructor that takes a String as argument
        //Each Entity require key will be define in constructor.
        Constructor<?> constructor = _entityClassName.getConstructor(data.getClass());
        Object myObject = constructor.newInstance(data);
        return (T)myObject;
    }

    protected T buildObject(int keyIndex, ArrayList<PropertyIndex> data) throws Exception {
        //get constructor that takes a String as argument
        //Each Entity require key will be define in constructor.
        Constructor<?> constructor = _entityClassName.getConstructor(int.class, data.getClass());
        Object myObject = constructor.newInstance(keyIndex, data);
        return (T)myObject;
    }

    protected PropertyIndex readData(Object obj, int colIndex, int fIndex)
    {
        return null;
    }
}
