package com.hbcd.scripting.core;

import com.hbcd.common.container.LocalDataServiceContainer;
import com.hbcd.common.service.DataService;
import com.hbcd.common.service.DataServiceMaintenanceInterface;
import com.hbcd.common.service.ObjectRepositoryDataService;
import com.hbcd.common.utility.ObjectPropertyUtility;
import com.hbcd.common.utility.ServiceName;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.entity.ObjectProperties;

import java.util.ArrayList;

public class Update {
    private static ObjectProperties _localObject = null;

    public static void changePresetData(String objNm, String value) {
        try {
            String repositoryName = String.format("%s.%s", ApplicationSetup.get(Setting.SITE), ServiceName.OBJECT_REPOSITORY);
            _localObject = ObjectPropertyUtility.getObjectFromService(objNm);
            _localObject.setValue(value);
            ObjectProperties objToMemory = null;
            if (!LocalDataServiceContainer.existKey(repositoryName)) {
//                ObjectRepositoryDataService drs = new ObjectRepositoryDataService();
                //drs.setList(new ArrayList<ObjectProperties>());
                LocalDataServiceContainer.loadService(repositoryName, new ObjectRepositoryDataService());
            }

            ((DataServiceMaintenanceInterface) LocalDataServiceContainer
                    .getService(repositoryName)).add(_localObject.cloneObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
