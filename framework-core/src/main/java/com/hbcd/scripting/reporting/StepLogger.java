package com.hbcd.scripting.reporting;

import com.hbcd.reporting.controllers.ExcelController;
import com.hbcd.reporting.controllers.SafeReport;
import com.hbcd.reporting.impl.StepRecordImpl;
import com.hbcd.scripting.core.BrowserAction;
import com.hbcd.scripting.core.Find;
import com.hbcd.scripting.core.Update;
import com.hbcd.scripting.core.fluentInterface.ObjectAction;
import com.hbcd.scripting.enums.impl.Check;
import com.hbcd.storage.report.SafeReportStorage;
import com.hbcd.utility.helper.Common;

import java.lang.reflect.Method;

public class StepLogger {

    //Maximum 10 arguments
    //Require:
    //    First Argument (Args[0])  : ObjectName
    //    Second Argument (Args[1]) : PresetData -- null mean not change
    public static void validate(Check checkEnum, String... args) {
        try {
            if (args.length >= 2) {
                if (args[1] != null) {
                    Update.changePresetData(args[0], args[1]);
                }
            }

            if (args.length == 1) //Validation Object (not)Present and Text (not)Contain with preset Data on Object Repo
            {
                Method m = StepLogger.class.getDeclaredMethod(checkEnum.getvalue(), ObjectAction.class);
                m.invoke(StepLogger.class.newInstance(), Find.Object(args[0]));
            } else if (args.length == 2) //Text (not)Contain with Passing in Data
            {
                Method m = StepLogger.class.getDeclaredMethod(checkEnum.getvalue(), ObjectAction.class, String.class);
                m.invoke(StepLogger.class.newInstance(), Find.Object(args[0]), args[1]);
            } else if (args.length == 3) {
                Method m = StepLogger.class.getDeclaredMethod(checkEnum.getvalue(), ObjectAction.class, String.class);
                m.invoke(StepLogger.class.newInstance(), Find.Object(args[0]), args[2]);
            } else if (args.length == 4) {
                Method m = StepLogger.class.getDeclaredMethod(checkEnum.getvalue(), ObjectAction.class, String.class, String.class);
                m.invoke(StepLogger.class.newInstance(), Find.Object(args[0]), args[2], args[3]);
            } else if (args.length == 5) {
                Method m = StepLogger.class.getDeclaredMethod(checkEnum.getvalue(), ObjectAction.class, String.class, String.class, String.class);
                m.invoke(StepLogger.class.newInstance(), Find.Object(args[0]), args[2], args[3], args[4]);
            } else if (args.length == 6) {
                Method m = StepLogger.class.getDeclaredMethod(checkEnum.getvalue(), ObjectAction.class, String.class, String.class, String.class, String.class);
                m.invoke(StepLogger.class.newInstance(), Find.Object(args[0]), args[2], args[3], args[4], args[5]);
            } else if (args.length == 7) {
                Method m = StepLogger.class.getDeclaredMethod(checkEnum.getvalue(), ObjectAction.class, String.class, String.class, String.class, String.class, String.class);
                m.invoke(StepLogger.class.newInstance(), Find.Object(args[0]), args[2], args[3], args[4], args[5], args[6]);
            } else if (args.length == 8) {
                Method m = StepLogger.class.getDeclaredMethod(checkEnum.getvalue(), ObjectAction.class, String.class, String.class, String.class, String.class, String.class, String.class);
                m.invoke(StepLogger.class.newInstance(), Find.Object(args[0]), args[2], args[3], args[4], args[5], args[6], args[7]);
            } else if (args.length == 9) {
                Method m = StepLogger.class.getDeclaredMethod(checkEnum.getvalue(), ObjectAction.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class);
                m.invoke(StepLogger.class.newInstance(), Find.Object(args[0]), args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
            } else if (args.length == 10) {
                Method m = StepLogger.class.getDeclaredMethod(checkEnum.getvalue(), ObjectAction.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class);
                m.invoke(StepLogger.class.newInstance(), Find.Object(args[0]), args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = Common.getExceptionToString(e);
            if (msg == null) {
                msg = e.toString();
            }
            try {
                SafeReport.Current().createStep(msg, "fail_error", "");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    //check with preset data
    private static void TextPresent(ObjectAction object) throws Exception {
        ExcelController ec = new ExcelController();
        StepRecordImpl step = newStep();
        step.setActual(object.getText().value());
        step.setStatus(object.validateWithPresetData() ? "pass" : "fail");
        //((List<StepRecordImpl>) SafeInternalStorage.Current().get("threadScenarioStepList")).add(step);
        SafeReportStorage.Store(step);
    }


    //Check with preset data
    private static void TextNotPresent(ObjectAction object) throws Exception {
        StepRecordImpl step = newStep();
        step.setActual(object.getText().value());
        step.setStatus(!(object.validateWithPresetData()) ? "pass" : "fail");
        //((List<StepRecordImpl>) SafeInternalStorage.Current().get("threadScenarioStepList")).add(step);
        SafeReportStorage.Store(step);
    }

    //check with passing data
    private static void TextPresent(ObjectAction object, String passData) throws Exception {
        ExcelController ec = new ExcelController();
        StepRecordImpl step = newStep();
        String value = object.getText().value();
        step.setActual(value);
        step.setStatus(value.contains(passData) ? "pass" : "fail");
        //((List<StepRecordImpl>) SafeInternalStorage.Current().get("threadScenarioStepList")).add(step);
        SafeReportStorage.Store(step);
    }

    //check with passing data
    private static void TextNotPresent(ObjectAction object, String passData) throws Exception {
        StepRecordImpl step = newStep();
        String value = object.getText().value();
        step.setActual(value);
        step.setStatus(!value.contains(passData) ? "pass" : "fail");
        //((List<StepRecordImpl>) SafeInternalStorage.Current().get("threadScenarioStepList")).add(step);
        SafeReportStorage.Store(step);
    }

    private static void ObjectDisplayed(ObjectAction object) throws Exception {
        StepRecordImpl step = newStep();
        boolean isPresent = object.isPresent();
        step.setActual(isPresent ? "object present" : "object not present");
        step.setStatus(isPresent ? "pass" : "fail");
        //((List<StepRecordImpl>) SafeInternalStorage.Current().get("threadScenarioStepList")).add(step);
        SafeReportStorage.Store(step);
    }

    public static void ObjectNotDisplayed(ObjectAction object) throws Exception {
        StepRecordImpl step = newStep();
        boolean isNotPresent = object.isNotPresent();
        step.setActual(isNotPresent ? "object not present" : "object present");
        step.setStatus(isNotPresent ? "pass" : "fail");
        //((List<StepRecordImpl>) SafeInternalStorage.Current().get("threadScenarioStepList")).add(step);
        SafeReportStorage.Store(step);
    }

    private static StepRecordImpl newStep() {
        return newStep(true);
    }

    private static StepRecordImpl newStep(boolean isRequireSnapShot) {
        StepRecordImpl step = new StepRecordImpl(isRequireSnapShot ? BrowserAction.screenshot() : "");
        String action = "";
        boolean methodFound = false;
        for (int i = 0; i < Thread.currentThread().getStackTrace().length; i++) {
            if (Thread.currentThread().getStackTrace()[i].getMethodName().equals("executeScript")) {
                action = Thread.currentThread().getStackTrace()[i - 1].getMethodName();
                methodFound = true;
                break;
            }
        }
        if (!methodFound)
            action = "Method NOT found";
        System.out.println(action);
        step.setAction(action);
        step.setDesc("");
        //step.setSnapShotUrl(BrowserAction.screenshot());
        return step;
    }
}
