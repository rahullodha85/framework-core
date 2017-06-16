package com.hbcd.scripting.core;
import com.hbcd.scripting.core.fluentInterface.CaptureObjectAction;
import com.hbcd.scripting.core.fluentInterface.UseCapturedObjectAction;
import com.hbcd.utility.entity.ObjectProperties;

/**
 * Created by ephung on 10/30/2015.
 */
public class Use {

    private static String _ObjectName="";

    public static UseCapturedObjectAction CapturedObject(String objNm) throws Exception {
        try {
//            _localObject = RepoController.getObject(objNm);
//            _ObjectName = objNm;
//            if (_ObjectName == null) {
//                throw new Exception("Capture Object Name: " + objNm + " - DOES NOT EXIST : Please verify your [EXCEL] Repository.");
//            }
            return new ApplyUseCapturedObjectAction(objNm);
        } catch (Exception e) {
            throw e;
        }
    }
}
