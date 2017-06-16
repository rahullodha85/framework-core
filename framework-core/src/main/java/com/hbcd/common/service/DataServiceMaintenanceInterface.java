package com.hbcd.common.service;

import java.io.File;
import java.lang.reflect.Type;

/**
 * Created by ephung on 9/1/16.
 */
public interface DataServiceMaintenanceInterface {

    void add(Object value);

    String getEntityFirstColumnName();

    Type getEntityClass();

//    void setList(Array)

}
