package com.hbcd.utility.entity;

import com.hbcd.utility.common.GenericDataInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ephung on 2/25/2016.
 */
public class TestData extends DataObjectBase  implements Serializable, GenericDataInterface {
    String _name = "";
    List<String> _data = new ArrayList<>();
    int _sequential = -1;
    private final Object lock = new Object();

    public TestData()
    {
        _startColumnName = "DataName";
    }

    public TestData(ArrayList<PropertyIndex> data) {
        init(0, data);
    }

    public TestData(int keyIndx, ArrayList<PropertyIndex> data) {
        _startColumnName = "DataName";
        init(keyIndx, data);
    }

    private void init(int keyIndex, ArrayList<PropertyIndex> data)
    {
        for (PropertyIndex i : data) {
            if (i.getIndex() == keyIndex)
            {
                _name = i.getValueAsString();
            }
            else
            {
                _data.add(i.getValueAsString());
            }
        }
    }

    public TestData(String key, ArrayList<String> values)
    {
        _startColumnName = "DataName";
        _name = key;
        _data = new ArrayList<>(values);
    }

    public int getSize()
    {
        if (_data != null)
        {
            return _data.size();
        }
        else
        {
            return 0;
        }
    }

    public String get() {
        synchronized (lock) {
            if (_data.size() > 0) {
                return _data.get(0);
            }
        }
        return null;
    }

    public String get(int indx) {
        synchronized (lock) {
            //indx 1..Size
            if ((indx > 0) && (_data.size() > 0) && (indx  <= _data.size())) {
                return _data.get(indx-1);
            }
        }
        return null;
    }

    public String getSequential()
    {
        synchronized (lock) {
            if ((_sequential < 0) || (_sequential >= _data.size())) {
                _sequential = 0;  //Initialized first time
            }
            else {
                _sequential++;
                if (_sequential >= _data.size()) {
                    _sequential = 0;
                }
            }
            if (_data.size() > 0) {
                return _data.get(_sequential);
            }
        }
        return null;
    }

    public void add(String v)
    {
        _data.add(v);
    }

    @Override
    public String key() {
        return _name;
    }

    @Override
    public void print() {

    }
}
