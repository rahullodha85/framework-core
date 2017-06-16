package com.hbcd.common.utility;

import java.io.File;

/**
 * Created by ephung on 9/6/16.
 */
public interface DataServiceLoaderInterface {

    DataServiceLoaderInterface setServiceClass (String sn);

    DataServiceLoaderInterface setEntityClass (String en);

    DataServiceLoaderInterface setService (ServiceName sn);

    DataServiceLoaderInterface setInputFile(File f);

    <T> Object getService() throws Exception;

}
