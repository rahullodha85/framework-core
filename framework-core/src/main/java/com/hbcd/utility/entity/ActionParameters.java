package com.hbcd.utility.entity;

import com.hbcd.core.genericfunctions.HandleUnexpectedEvent;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by ephung on 2/22/2016.
 */
public class ActionParameters implements Cloneable {
    List<ObjectSearchParameters> _listParams = new ArrayList<>();
    HandleUnexpectedEvent _handle;

    public ActionParameters()
    {
        _handle = null;
        try {
            String handleEventClass = ConfigurationLoader.getValue(ApplicationSetup.get(Setting.SITE)+".HANDLE_SPECIAL_EVENT");
            if (com.hbcd.utility.helper.Common.isNotNullAndNotEmpty(handleEventClass))
            {
                Class<?> cn = Class.forName(handleEventClass);
                Callable<Boolean> specialHandler = (Callable<Boolean>) cn.newInstance();
                _handle = () -> {
                    try {
                        return specialHandler.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                };
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            //Supress Log - Ignor if no special handler is implemented
        }
    }

    public ActionParameters(HandleUnexpectedEvent h)
    {
        _handle = h;
    }

    protected Object clone() throws CloneNotSupportedException {

        ActionParameters clone=(ActionParameters)super.clone();

        // make the shallow copy of the object of type Department
        clone._handle=_handle;
        return clone;
    }

    public ObjectSearchParameters Current()
    {
        if (!hasParameter())
        {
            _listParams.add(new ObjectSearchParameters());
        }
        return _listParams.get(_listParams.size() - 1);
    }

    public boolean hasParameter()
    {
        return ((_listParams != null) && !_listParams.isEmpty() && (_listParams.size() > 0));
    }

    public void resetCustomWaitTime(int newWaitTime)
    {
        if (hasParameter())
        {
            _listParams.forEach(objectSearchParameters -> objectSearchParameters.getObjectRepository().setUserDefinedExplicitWaitTime(newWaitTime));
        }
    }

    public List<ObjectSearchParameters> getList()
    {
        return _listParams;
    }

    public void shallowCopy(ActionParameters ap)
    {
        _listParams = ap.getList();
        _handle=ap.getHandle();
    }

    public void deepCopy(ActionParameters ap)
    {
        _listParams = new ArrayList<>(ap.getList());
    }

    public void add(ObjectSearchParameters osp)
    {
        _listParams.add(osp);
    }

    public HandleUnexpectedEvent getHandle()
    {
        return _handle;
    }

    public void setHandle(HandleUnexpectedEvent h)
    {
        _handle = h;
    }

    public Boolean handleSpecialEvent() {
        if (_handle != null) {
            return _handle.execute();
        }
        return false;
    }
}
