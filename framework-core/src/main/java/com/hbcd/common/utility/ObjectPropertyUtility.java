package com.hbcd.common.utility;

import com.hbcd.common.container.DataServiceContainer;
import com.hbcd.common.container.LocalDataServiceContainer;
import com.hbcd.common.service.DataService;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.common.Type;
import com.hbcd.utility.entity.ObjectProperties;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectPropertyUtility {


    public static void setObject(ObjectProperties obj, Type.PropertyType t, String value) {
        try {
            Method m = obj.getClass().getDeclaredMethod(t.getvalue(), String.class);
            m.invoke(obj, value);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void setObject(ObjectProperties obj, Type.PropertyIntType t, int value) {
        try {
            Method m = obj.getClass().getDeclaredMethod(t.getvalue(), int.class);
            m.invoke(obj, value);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static ObjectProperties getObjectFromService(String objectName) {
        String repositoryName = String.format("%s.%s", ApplicationSetup.get(Setting.SITE), ServiceName.OBJECT_REPOSITORY);
        ObjectProperties objInMemory = null;
        if (LocalDataServiceContainer.existKey(repositoryName)) {
            objInMemory = ((DataService<ObjectProperties>) LocalDataServiceContainer
                    .getService(repositoryName)).find(objectName);
        }

        if (objInMemory != null)  //Found in LOCAL Repository
        {
            return objInMemory;
        }

        if (DataServiceContainer.existKey(repositoryName)) {
            objInMemory = ((DataService<ObjectProperties>) DataServiceContainer.getService(repositoryName)).find(objectName);
        }
        if (objInMemory == null) //Found in GLOBAL Repository
        {
            System.err.println(String.format("Object Name: %s - DOES NOT EXIST : Please verify your EXCEL Repository.", objectName));
            return null;
        }

        objInMemory.setIsUsed(); //Mark Global Repository ONLY
        return objInMemory.cloneObject();  //ensure no change in memory object (GLOBAL);
    }

    public static void cleanLocalRepositoryObject() {

        String repositoryName = String.format("%s.%s", ApplicationSetup.get(Setting.SITE), ServiceName.OBJECT_REPOSITORY);
        DataService srv = (DataService) LocalDataServiceContainer.getService(repositoryName);
        if (srv != null) {
            srv.clearData();
        }
    }

    public static ObjectProperties determineAndGetObjectProperties(Object obj) throws Exception
    {
        ObjectProperties _localObject = null;
        if (obj == null)
        {
            throw new Exception("Object Parameter is NULL.");
        }
        if (obj instanceof ObjectProperties)
        {
            _localObject = (ObjectProperties)obj;
        }
        else if (obj instanceof String)
        {
            _localObject = ObjectPropertyUtility.getObjectFromService(obj.toString());
            if (_localObject == null) {
                throw new Exception(String.format("Object Name: %s - DOES NOT EXIST : Please verify your [EXCEL] Repository.",  obj.toString()));
            }
        }
        else
        {
            throw new Exception(String.format("Object Name: %s - IS NOT ObjectProperties or  NOT Exist IN OBJECT REPOSITORY.", obj.toString()));
        }
        return _localObject;
    }
}
