package com.hbcd.common.utility;

import com.hbcd.common.container.DataServiceContainer;
import com.hbcd.common.service.DataService;
import com.hbcd.logging.log.Log;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.entity.TestData;

/**
 * Created by ephung on 3/14/2016.
 */
public class TestDataUtility {
    private static TestData getTestDataService(String objectDataName) {
        String repositoryName = String.format("%s.%s", ApplicationSetup.get(Setting.SITE), ServiceName.TEST_DATA);
        TestData objInMemory = null;


        if (DataServiceContainer.existKey(repositoryName)) {
            objInMemory = ((DataService<TestData>) DataServiceContainer.getService(repositoryName)).find(objectDataName);
        }
        if (objInMemory == null) //Found in GLOBAL Repository
        {
            System.err.println(String.format("Test Data Name: %s - DOES NOT EXIST : Please verify your TEST DATA EXCEL.", objectDataName));
            return null;
        }

        return objInMemory;  //ensure no change in memory object (GLOBAL);
    }

    public static String getTestData(String objectDataName) {
        TestData td = getTestDataService(objectDataName);
        if (td != null)
        {
            return td.get();
        }
        else
        {
            Log.Error("Error Data does not exist.");
        }
        return "";
    }

    public static String getSequentialTestData(String objectDataName) {
        TestData td = getTestDataService(objectDataName);
        if (td != null)
        {
            return td.getSequential();
        }
        else
        {
            Log.Error("Error Data does not exist.");
        }
        return "";
    }

    public static String getTestData(String objectDataName, int indx) {
        TestData td = getTestDataService(objectDataName);
        if (td != null)
        {
            return td.get(indx);
        }
        else
        {
            Log.Error("Error Data does not exist.");
        }
        return "";
    }

    public static int getTestDataSize(String objectDataName) {
        String repositoryName = String.format("%s.%s", ApplicationSetup.get(Setting.SITE), ServiceName.TEST_DATA);
        if (DataServiceContainer.existKey(repositoryName)) {
            if (((DataService<TestData>) DataServiceContainer.getService(repositoryName)).size() == 0) {
                return 0;
            }
            TestData td = getTestDataService(objectDataName);
            if (td == null) return 0;
            return  ((DataService<TestData>)DataServiceContainer.getService(repositoryName)).find(objectDataName).getSize();
        }
        return 0;
    }
}
