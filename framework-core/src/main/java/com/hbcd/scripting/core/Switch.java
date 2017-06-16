package com.hbcd.scripting.core;

import com.hbcd.common.utility.ObjectPropertyUtility;
import com.hbcd.core.genericfunctions.GenericFunctions;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectProperties;
import com.hbcd.utility.entity.ObjectSearchParameters;

public class Switch {

    public static void toFrame(String IDorName) throws Exception {
        try {
            GenericFunctions.coreSwitchToFrame(IDorName);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void toFrame(int index) throws Exception {
        try {
            GenericFunctions.coreSwitchToFrame(index);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void toFrameObject(String objNm) throws Exception {
        try {
            ObjectProperties _localObject = ObjectPropertyUtility.getObjectFromService(objNm);
            if (_localObject == null) {
                throw new Exception(String.format("Object Name: %s - DOES NOT EXIST : Please verify your [EXCEL] Repository.", objNm));
            }
            ActionParameters acp = new ActionParameters();
            acp.add(new ObjectSearchParameters().setObjectRepository(_localObject));
            GenericFunctions.coreSwitchToFrame(acp);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void toFrameObject(ObjectProperties obj) throws Exception {
        try {
            if (obj == null) {
                throw new Exception(String.format("Object Name: %s - DOES NOT EXIST.", obj.getName()));
            }
            ActionParameters acp = new ActionParameters();
            acp.add(new ObjectSearchParameters().setObjectRepository(obj));
            GenericFunctions.coreSwitchToFrame(acp);
        } catch (Exception e) {
            throw e;
        }
    }



    public static void toFrameCapturedObject(String capturedObjNm) throws Exception {
        try {
            ActionParameters acp = new ActionParameters();
            acp.add(new ObjectSearchParameters().setType(2).setStoredObjectName(capturedObjNm));
            GenericFunctions.coreSwitchToFrame(acp);
        } catch (Exception e) {
            throw e;
        }
    }

    //SWITCH to Frame by name or ID attribute
    public static void toDefaultContent() throws Exception {
        try {
            GenericFunctions.coreSwitchToDefaultContent();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void toNewWindow(String windowTitle) throws Exception {

        try {
            GenericFunctions.coreSwitchToNewWindow(windowTitle);
        } catch (Exception e) {
            throw e;
        }
    }
}
