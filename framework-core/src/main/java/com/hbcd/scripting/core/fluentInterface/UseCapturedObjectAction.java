package com.hbcd.scripting.core.fluentInterface;


import com.hbcd.utility.entity.ObjectProperties;

/**
 * Created by ephung on 10/30/2015.
 */
public interface UseCapturedObjectAction {
    ObjectAction FindObject(String objNm) throws Exception;
    MultiObjectsAction FindMultipleObjects(String objNm) throws Exception;
    ObjectAction FindObject(ObjectProperties obj) throws Exception;
    MultiObjectsAction FindMultipleObjects(ObjectProperties obj) throws Exception;
    ObjectAction AsIs() throws Exception;
}
