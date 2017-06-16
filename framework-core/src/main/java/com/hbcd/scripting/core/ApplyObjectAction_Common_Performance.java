package com.hbcd.scripting.core;

import com.hbcd.core.genericfunctions.GenericFunctions;
import com.hbcd.logging.log.Log;
import com.hbcd.scripting.core.fluentInterface.ObjectAction_CommonAction_Performance;
import com.hbcd.storage.data.SafeInternalStorage;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.Performance;
import com.hbcd.utility.helper.Common;
import com.hbcd.utility.toggle.PerformanceToggle;

import java.util.HashMap;

public class ApplyObjectAction_Common_Performance extends BaseApplyObjectAction implements ObjectAction_CommonAction_Performance {

    private long _startTime = 0L;
    private long _endTime = 0L;

    private String _strStartTime = "";
    private String _strEndTime = "";

//    public ApplyObjectAction_Common_Performance(ActionParameters acp) {
//        super(acp);
//    }
//
//    public ApplyObjectAction_Common_Performance(long startTime, ObjectProperties obj) {
//        super(null, obj);
//        _startTime = startTime;
//        _strStartTime = "" + _startTime;
//    }
//
//    public ApplyObjectAction_Common_Performance(String st, ObjectProperties obj) {
//        super(null, obj);
//        if (Common.isNumeric(st)) {
//            _startTime = Long.parseLong(st);
//        }
//        _strStartTime = st;
//    }


    public ApplyObjectAction_Common_Performance(ActionParameters acp, String st, String et) {
        super(acp);
        if (Common.isNumeric(st)) {
            _startTime = Long.parseLong(st);
        }
        _strStartTime = st;

        if (Common.isNumeric(et)) {
            _endTime = Long.parseLong(et);
        }
        _strEndTime = et;
    }


    public void getPerformance(String action, String webPageName) throws Exception {

        if (!PerformanceToggle.get()) {
            Log.Info("No Performance Measure:  If you want to enable Performance Measure, please check setting in Performance.properties file.");
            return; //Skip if No Performance Testing
        }

        String ObjName = "";
        String desc = action;
        if (_parameters.Current().getObjectRepository() != null) {
            desc += String.format(" on %s ", _parameters.Current().getObjectRepository().getObjectName());
        }

        Performance p = new Performance(webPageName);
        p.setData(new HashMap<String, Long>(GenericFunctions.coreGetPagePerf(_strStartTime, _strEndTime, desc)));
        p.setXml(Common.SerializeToXml("WebPerformance", p));

        Performance pr = p.clone();
        //Validate if the same as Previous Page Load -- AJAX Case
        Performance prev = SafeInternalStorage.Current().get("LAST_PERFORMANCE_MEASUREMENT");
        if (prev != null) {
            if (prev.isSameData(pr.getData())) {
                pr = p.cloneWithResetData();
            }
        }
        SafeInternalStorage.Current().save("LAST_PERFORMANCE_MEASUREMENT", p);

        Report.performance(action, pr.getPageName(), desc, pr.getXml(), pr.getData());
    }
}
