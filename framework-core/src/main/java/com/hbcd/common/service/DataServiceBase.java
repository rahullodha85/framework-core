package com.hbcd.common.service;

import com.hbcd.common.utility.ServiceName;
import com.hbcd.logging.log.Log;
import com.hbcd.utility.common.GenericDataInterface;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class DataServiceBase<T> implements DataService<T>, DataServiceMaintenanceInterface {

    protected List<T> _myList = null;
    protected boolean _allowRepetition = false;
    protected BasicFileAttributes _fileAttrib = null;
    protected String _serviceKey = null;
    protected Path _filePath = null;
    protected String _startExcelColumnName = "";
    protected ServiceName _serviceName = null;

//    public DataServiceBase()
//    {
//        _myList = new ArrayList<T>();
//    }

    public int size() {
        return (_myList == null) ? 0 : _myList.size();
    }

    public List<T> getList() {
        if (_myList == null) {
            _myList = new ArrayList<T>();
        }
        return _myList;
    }

    public boolean isTheSameDataFile(BasicFileAttributes attrb) {
        return (_fileAttrib.lastModifiedTime().equals(attrb.lastModifiedTime())
                &&
                (_fileAttrib.size() == attrb.size())
        );
    }

    @Override
    public boolean hasTheDataFileChanged() {
        try {
            BasicFileAttributes attrb = Files.readAttributes(_filePath, BasicFileAttributes.class);
            return !isTheSameDataFile(attrb);  //Not The Same.
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void clearData() {
        if (_myList != null) {
            _myList.clear();
        }
    }

    @Override
    public T find(String key) {
        if (getList() == null) return null;

        List<T> _localL = new ArrayList<T>();

        getList().stream().filter((p) -> (((GenericDataInterface) p).key()).equals(key)).forEach((p) -> {
            _localL.add(p);
        });

        if (_localL.size() == 1) {
            return _localL.get(0);
        }
        return null;
    }

    public void setFileAttribute(BasicFileAttributes attrb) {
        _fileAttrib = attrb;
    }

    public void setFileInfo(ServiceName sn, String key, BasicFileAttributes attrb, Path filePath) {
        _serviceName = sn;
        _serviceKey = key;
        _fileAttrib = attrb;
        _filePath = filePath;
    }

    @Override
    public void add(Object value) {
        if (value == null) return;
        if (_allowRepetition)
        {
            getList().add((T) value);
        }
        else
        {
            String key = ((GenericDataInterface) value).key();
            if (key.length() > 0) {
                if (find(key) == null) {
                    getList().add((T) value);
                }
                else
                {
                    Log.Info(String.format("Key [%s] already exist.", key));
                }
            }
        }
    }

    @Override
    public void print() {
        getList().forEach(n -> ((GenericDataInterface) n).print());
    }

    @Override
    public Class<T> getEntityClass()
    {
        return (Class<T>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public String getEntityFirstColumnName()
    {
        String key = "";
        try {
            Class<T> _entityClassName = getEntityClass();
            key = ((GenericDataInterface) _entityClassName.newInstance()).getStartColumnName();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return key;

    }
}
