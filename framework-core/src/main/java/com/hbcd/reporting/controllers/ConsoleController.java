package com.hbcd.reporting.controllers;

import com.hbcd.logging.log.Log;
import com.hbcd.logging.log.LogReport;
import com.hbcd.reporting.impl.CaseRecordImpl;
import com.hbcd.reporting.impl.ModuleRecordImpl;
import com.hbcd.reporting.impl.StepRecordImpl;
import com.hbcd.reporting.objects.Case;
import com.hbcd.reporting.objects.Step;

public class ConsoleController extends ControllerBase implements Controller {

    @Override
    public int storeModule(ModuleRecordImpl moduleInput) throws Exception {
//		Module moduleRow = new Module(moduleInput.getModuleRow());
//		setRowValues(moduleRowCount,
//				this.modules, 
//				String.valueOf(moduleRowCount),
//				moduleRow.getModuleType()+"",
//				moduleRow.getModuleScheduler()+"",
//				moduleRow.getModuleScheduleTime().toString(),
//				moduleRow.getModuleStatus()+"");
//		moduleRowCount++;
        return -1;
    }

    @Override
    public int storeCase(CaseRecordImpl caseInput) throws Exception {
        Case caseRow = new Case(caseInput.getCaseRow());
        //System.out.println(caseRow.toString());
        LogReport.Info(String.format("%s : %s", _ScenarioName, caseRow.toString()));
        return 1;
    }

    @Override
    public void storeStep(StepRecordImpl stepInput) throws Exception {
        try {
            Step stepRow = new Step(stepInput.getReportRow());
            LogReport.Info(String.format("%s: %s",_ScenarioName, stepRow.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeStep(long cId, int sId, StepRecordImpl stepInput) throws Exception {
        try {
            Step stepRow = new Step(cId, sId, stepInput.getReportRow());
            LogReport.Info(String.format("%s: %s", _ScenarioName, stepRow.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//	@Override
//	public TestCaseResult generateReport(int requestId) throws Exception {
//		//System.out.println("#========================   R E P O R T  ===========================#");
//		TestCaseResult tcr = super.generateReport(requestId);
//		//System.out.println("#====================  E N D   R E P O R T  ========================#");
//		return tcr;
//	}

    @Override
    public void updateCase(long caseId, String status) throws Exception {
        LogReport.Info(String.format("Case Id : %s | Status : [%s]", caseId, status));
    }

    @Override
    public void close() throws Exception {
        System.out.println("Report is closed");
    }

}
