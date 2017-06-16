package com.hbcd.reporting.controllers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hbcd.logging.log.Log;
import com.hbcd.logging.log.LogReport;
import com.hbcd.reporting.impl.CaseRecordImpl;
import com.hbcd.reporting.impl.ModuleRecordImpl;
import com.hbcd.reporting.impl.StepRecordImpl;
import com.hbcd.reporting.objects.Case;
import com.hbcd.reporting.objects.Module;
import com.hbcd.reporting.objects.Step;
import com.hbcd.utility.accessor.Accessor;
import com.hbcd.utility.accessor.impl.MysqlAccessor;
import com.hbcd.utility.common.WebPerformance;
import com.hbcd.utility.helper.Common;

public class MysqlController extends ControllerBase implements Controller {

    private final String insertModule = "INSERT INTO HBCD_Automation.allmodules (module_type, module_scheduler, module_schedule_time, module_complete) VALUES (?, ?, ?, ?)";
    //private final String insertCase = "INSERT INTO HBCD_Automation.allcases (ReqId, CaseName, CaseDescription, CaseStatus) VALUES (?,?,?,?)";
    private final String insertCaseProcedure = "{call InsertCaseWithHTMLReport(?, ?, ?, ?, ?)}";
    private final String insertCaseFunction = "{? = call InsertCaseWithHTMLReport(?, ?, ?, ?, ?, ?, ?)}";
    private final String updateCase = "UPDATE HBCD_Automation.allcases SET CaseStatus = ? WHERE CaseId = ?";
    private final String insertStepProcedure = "{? = call InsertStepWithSnapshot(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private final String insertPerformanceStepProcedure = "{call InsertPerformanceStep(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private PreparedStatement preparedStatement;
    private CallableStatement cStmntInsertStep;
    private PreparedStatement preparedPerformanceStatement;
    private CallableStatement cStmnt;
    Accessor connection = new MysqlAccessor();

    private int key = 0;
    public int storeModule(ModuleRecordImpl moduleInput) throws Exception {
        try {
            iniPreparedStatementWithReturn(insertModule);
            Module moduleRow = new Module(moduleInput.getModuleRow());
            preparedStatement.setString(1, moduleRow.getModuleType());
            preparedStatement.setString(2, moduleRow.getModuleScheduler());
            preparedStatement.setDate(3, moduleRow.getModuleScheduleTime());
            preparedStatement.setBoolean(4, moduleRow.getModuleStatus());
            LogReport.Info(String.format("Inserting Module: %s", moduleRow.toString()));
            preparedStatement.executeUpdate();

            key = getKey(preparedStatement.getGeneratedKeys());
        } catch (SQLException e) {
            LogReport.Error("ERROR WRITE REPORT MODULE", e);
            e.printStackTrace();

        }
        finally
        {

        }

        return key;
    }

    @Override
    public int storeCase(CaseRecordImpl caseInput) throws Exception {
        int caseId = 0;
        try {
            openConnection();
            Case caseRow = new Case(caseInput.getCaseRow());

            //Console and Log first
            LogReport.Info(String.format("%s : %s", _ScenarioName, caseRow.toString()));

            //Ensure Text is truncated prior to insert case
//			iniPreparedStatement(insertCaseProcedure);
//			preparedStatement.setInt(1, caseRow.getModuleId());
//			preparedStatement.setString(2, Common.getMaxSizeValue(caseRow.getCaseName(), 255));
//			preparedStatement.setString(3, Common.getMaxSizeValue(caseRow.getCaseDescription(), 511));
//			preparedStatement.setString(4, Common.getMaxSizeValue(caseRow.getCaseStatus(), 24));
//			preparedStatement.setString(5, caseRow.getCaseHTMLReport());
//			int caseId = preparedStatement.();
//			int caseId = getKey(preparedStatement.executeQuery());

            iniCallableStatement(insertCaseFunction);
            cStmnt.registerOutParameter(1, java.sql.Types.INTEGER);
            cStmnt.setLong(2, caseRow.getRequestId());
            cStmnt.setInt(3, caseRow.getExecutionOrder());
            cStmnt.setString(4, Common.getMaxSizeValue(caseRow.getCaseName(), 255));
            cStmnt.setString(5, Common.getMaxSizeValue(caseRow.getCaseDescription(), 511));
            cStmnt.setString(6, Common.getMaxSizeValue(caseRow.getCaseStatus(), 24));
            cStmnt.setLong(7, caseRow.getTotalExecutionTime());
            cStmnt.setString(8, caseRow.getCaseHTMLReport());
            cStmnt.execute();
            caseId = cStmnt.getInt(1);
        } catch (SQLException e) {
            LogReport.Error("ERROR WRITE REPORT CASE", e);
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
        return caseId;
    }


    @Override
    public void storeStep(StepRecordImpl stepInput) throws Exception {

        long cId = 0;
        int sId = 0;
        Step stepRow = new Step(stepInput.getReportRow());
        cId = stepRow.getCaseRowId();
        sId = stepRow.getStepNumber();
        storeStep(cId, sId, stepInput);
    }

    @Override
    public void storeStep(long cId, int sId, StepRecordImpl stepInput)
            throws Exception {
        try {

            openConnection();

            Step stepRow = new Step(cId, sId, stepInput.getReportRow());

            //Console and Log first
//			logger.info(stepRow.toString());
//			System.out.println(stepRow.toString());
            LogReport.Info(String.format("%s: %s", _ScenarioName, stepRow.toString()));

            //DB Log - Ensure Text is truncated to prior to insert step
            if (stepRow.getSnapShotUrl().length() > 0)
            {
                Log.Info("Has SnapShot");
            }
            else
            {
                Log.Info("No SnapShot");
            }
            iniCallableInsertStepStatement(insertStepProcedure);
            cStmntInsertStep.registerOutParameter(1, java.sql.Types.INTEGER);
            cStmntInsertStep.setLong(2, cId);
            cStmntInsertStep.setInt(3, sId);
            cStmntInsertStep.setInt(4, stepRow.getType());
            cStmntInsertStep.setString(5, Common.getMaxSizeValue(stepRow.getAction(), 255));
            cStmntInsertStep.setString(6, Common.getMaxSizeValue(stepRow.getDesc(), 255));
            cStmntInsertStep.setString(7, Common.getMaxSizeValue(stepRow.getExpected(), 511));
            cStmntInsertStep.setString(8, Common.getMaxSizeValue(stepRow.getActual(), 511));
            cStmntInsertStep.setString(9, Common.getMaxSizeValue(stepRow.getStatus(), 24));
            cStmntInsertStep.setString(10, stepRow.getSnapShotUrl());
            cStmntInsertStep.execute();
            int stepId = cStmntInsertStep.getInt(1);

            if ((stepRow.getPerformancePageName() != null) && (stepRow.getPerformancePageName() != null) && (stepRow.getPerformanceData() != null))
            {
                if ((stepRow.getPerformancePageName().length() > 0) && (stepRow.getXmlPerformanceData().length() > 0) && (stepRow.getPerformanceData().size() > 0))
                {
                    iniPreparedPerformanceStatement(insertPerformanceStepProcedure);
                    preparedPerformanceStatement.setLong(1, stepId);
                    preparedPerformanceStatement.setString(2, stepRow.getPerformancePageName());
                    preparedPerformanceStatement.setString(3, Common.getMaxSizeValue(stepRow.getXmlPerformanceData(), 4095));
                    preparedPerformanceStatement.setLong(4, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.NAVIGATION_START.getvalue())));
                    preparedPerformanceStatement.setLong(5, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.UNLOAD_EVENT_START.getvalue())));
                    preparedPerformanceStatement.setLong(6, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.UNLOAD_EVENT_END.getvalue())));
                    preparedPerformanceStatement.setLong(7, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.REDIRECT_START.getvalue())));
                    preparedPerformanceStatement.setLong(8, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.REDIRECT_END.getvalue())));
                    preparedPerformanceStatement.setLong(9, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.FETCH_START.getvalue())));
                    preparedPerformanceStatement.setLong(10, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.DOMAIN_LOOKUP_START.getvalue())));
                    preparedPerformanceStatement.setLong(11, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.DOMAIN_LOOKUP_END.getvalue())));
                    preparedPerformanceStatement.setLong(12, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.CONNECT_START.getvalue())));
                    preparedPerformanceStatement.setLong(13, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.CONNECT_END.getvalue())));
                    preparedPerformanceStatement.setLong(14, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.REQUEST_START.getvalue())));
                    preparedPerformanceStatement.setLong(15, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.RESPONSE_START.getvalue())));
                    preparedPerformanceStatement.setLong(16, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.RESPONSE_END.getvalue())));
                    preparedPerformanceStatement.setLong(17, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.DOM_LOADING.getvalue())));
                    preparedPerformanceStatement.setLong(18, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.DOM_INTERACTIVE.getvalue())));
                    preparedPerformanceStatement.setLong(19, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.DOM_CONTENT_LOADED_EVENT_START.getvalue())));
                    preparedPerformanceStatement.setLong(20, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.DOM_CONTENT_LOADED_EVENT_END.getvalue())));
                    preparedPerformanceStatement.setLong(21, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.DOM_COMPLETE.getvalue())));
                    preparedPerformanceStatement.setLong(22, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.LOAD_EVENT_START.getvalue())));
                    preparedPerformanceStatement.setLong(23, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.LOAD_EVENT_END.getvalue())));
                    preparedPerformanceStatement.setLong(24, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.PERFORMANCE_START.getvalue())));
                    preparedPerformanceStatement.setLong(25, Common.DefaultLongZero(stepRow.getPerformanceData().get(WebPerformance.PERFORMANCE_END.getvalue())));
                    preparedPerformanceStatement.execute();
                }
            }

        } catch (Exception e) {
            LogReport.Error("Error MYSQL", e);
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }



    private void iniCallableStatement(String input) throws Exception {
        cStmnt = connection.getConnection().prepareCall(input);
        LogReport.Info("Initializing Callable Statement in MysqlController.");
    }

    private void iniPreparedStatementWithReturn(String input) throws Exception {
        preparedStatement = connection.getConnection().prepareStatement(input, Statement.RETURN_GENERATED_KEYS);
        LogReport.Info("Initializing prepared statement in MysqlController.");
    }

    private void iniCallableInsertStepStatement(String input) throws Exception {
        cStmntInsertStep = connection.getConnection().prepareCall(input);
        LogReport.Info("Initializing callable Insert Step statement in MysqlController.");
    }

    private void iniPreparedPerformanceStatement(String input) throws Exception {
        preparedPerformanceStatement = connection.getConnection().prepareStatement(input, Statement.RETURN_GENERATED_KEYS);
        LogReport.Info("Initializing prepared Performance statement in MysqlController.");
    }

    private int getKey(ResultSet rs) throws SQLException{
        while(rs.next())
            return rs.getInt(1);
        return 0;
    }

    @Override
    public void updateCase(long caseId, String Status) throws Exception {
        try
        {
            openConnection();
            LogReport.Info(String.format("CaseId : %s | Status : [%s]", caseId, Status));

            //DB Log
            iniPreparedStatementWithReturn(updateCase);
            preparedStatement.setString(1, Status);
            preparedStatement.setLong(2, caseId);
            preparedStatement.execute();

        } catch (Exception e) {
            LogReport.Error("Error MYSQL", e);
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null)
        {
            connection.closeConnection();
        }
    }

    @Override
    public void openConnection() throws Exception
    {
        connection.getConnection();

    }

    @Override
    public void closeConnection()  throws Exception {
        connection.closeConnection();
    }

//	@Override
//	public TestCaseResult generateReport(int requestId) throws Exception {
////		System.out.println("#========================   R E P O R T  ===========================#");
////		Log.Info("#========================   R E P O R T  ===========================#");
//		TestCaseResult tcr = super.generateReport(requestId);
////		System.out.println("#====================  E N D   R E P O R T  ========================#");
////		Log.Info("#====================  E N D   R E P O R T  ========================#");
//		return tcr;
//	}

    //
    // private final static String caseQuery =
    // "SELECT * FROM cases WHERE module_id = ? AND case_row_id = ?";
    // private final static String stepQuery =
    // "SELECT * FROM steps WHERE step_row_id = ? AND step_number = ?";
    // private final static String caseStepsQuery =
    // "SELECT * FROM steps WHERE case_row_id = ? ORDER BY step_number ASC";
    // private final static String moduleCaseQuery =
    // "SELECT * FROM cases WHERE module_id = ? ORDER BY case_id ASC";
    // public static ResultSet getStep(int caseId, int stepNumber) throws
    // SQLException{
    // iniPreparedStatement(stepQuery);
    // preparedStatement.setInt(1, caseId);
    // preparedStatement.setInt(1, stepNumber);
    // return preparedStatement.executeQuery();
    // }
    // public static ResultSet getCase(int moduleId, int caseId) throws
    // SQLException{
    // iniPreparedStatement(caseQuery);
    // preparedStatement.setInt(1, moduleId);
    // preparedStatement.setInt(1, caseId);
    // return preparedStatement.executeQuery();
    // }
    // public static ResultSet getAllStepsForCase(int caseId) throws
    // SQLException{
    // iniPreparedStatement(caseStepsQuery);
    // preparedStatement.setInt(1, caseId);
    // return preparedStatement.executeQuery();
    // }
    // public static ResultSet getAllCasesForModule(int moduleId, int
    // scheduleid) throws SQLException{
    // iniPreparedStatement(moduleCaseQuery);
    // preparedStatement.setInt(1, moduleId);
    // return preparedStatement.executeQuery();
    // }
}
