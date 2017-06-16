package com.hbcd.utility.schedulerUtility;

import com.hbcd.logging.log.Log;
import com.hbcd.utility.accessor.Accessor;
import com.hbcd.utility.accessor.impl.MysqlAccessor;
import com.hbcd.utility.common.Task;
import com.hbcd.utility.configurationsetting.ConfigurationManager;
import com.hbcd.utility.configurationsetting.ProcessTaskManager;
import com.hbcd.utility.helper.Common;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchedulerManager {

    public static HashMap<String, Object> getTopScheduler(boolean _firstTime, String myIP) {
        try {

            //If first time.  Reset all in progress and start again.
            if (_firstTime) {
                //Pickup from last in progress
                updateDB(String.format("UPDATE HBCD_Automation.executionscheduler AS es LEFT JOIN executionmodule AS m ON (es.ModuleId = m.Id) LEFT JOIN executionMachine as em ON (m.MachineId = em.Id) SET es.StatusId = 0 WHERE em.IP = '%s' AND es.ScheduleTime <= NOW() AND es.StatusId = 1", myIP));
            }

            return selectTopTask(myIP);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public static List<Task> getExecuteTaskList(boolean _firstTime, String myIP) {
        try {

            //If first time.  Reset all in progress and start again.
            if (_firstTime) {
                //Pickup from last in progress
                updateDB(String.format("UPDATE HBCD_Automation.executionscheduler AS es LEFT JOIN executionmodule AS m ON (es.ModuleId = m.Id) LEFT JOIN executionMachine as em ON (m.MachineId = em.Id) SET es.StatusId = 0 WHERE em.IP = '%s' AND es.ScheduleTime <= NOW() AND es.StatusId = 1", myIP));
            }

            return selectTaskQueue(myIP);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    private static List<Task> selectTaskQueue(String myIP) throws Exception {
        Accessor myDAO = new MysqlAccessor();
        Connection _conn = null;
        Statement _selectStmt = null;
        List<Task> rtrn = new ArrayList<Task>();
        try {
            _conn = myDAO.getConnection();
            _selectStmt = _conn.createStatement();
            String strQuery = String.format("SELECT s.ReqID, s.ModuleId, s.ParallelExecution, s.FilterTestCase, em.IP as IP, m.Name as ModuleName, es.Name as SiteName, et.Name as Tool, s.StatusId, st.Description as StatusDesc, eb.Type as BrowserName, ebt.Type as BrowserType, ebt.HubURL as RemoteHub, CONCAT(CONVERT(ers.Width,CHAR(50)), 'x', CONVERT(ers.Height, CHAR(50))) as Resolution, ee.URL as EnvironmentURL, en.hipChatRoomNumber as HipChatNotificationRoom, en.eMail as emailNotification, s.ReRun_ReqId as ReRunReqId  FROM executionscheduler AS s LEFT JOIN executionmodule AS m ON (s.ModuleId = m.Id) LEFT JOIN executionstatus AS st ON (s.StatusId = st.Id) LEFT JOIN executiontool as et ON (s.ToolId = et.Id) LEFT JOIN executionbrowser as eb ON (s.BrowserId = eb.Id) LEFT JOIN executionSite as es ON (m.SiteId = es.Id) LEFT JOIN executionMachine as em ON (m.MachineId = em.Id) LEFT JOIN executionbrowsertype as ebt ON (s.BrowserTypeId = ebt.Id) LEFT JOIN executionresolution as ers ON (s.ResolutionId = ers.Id) LEFT JOIN executionNotification as en ON (m.NotificationId = en.Id) LEFT JOIN executionenvironment as ee ON (ee.Id = s.EnvironmentId) WHERE em.IP = '%s' AND ScheduleTime <= NOW() AND StatusId IN (0,1) ORDER BY ScheduleTime ASC", myIP);
            //logger.entry();
            ResultSet rs = _selectStmt.executeQuery(strQuery);
            return ProcessTaskManager.get(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (_selectStmt != null) {
                _selectStmt.close();
            }
            if (_conn != null) {
                _conn.close();
            }
            myDAO.closeConnection();
        }
        return null;
    }

    public static List<String> selectPreviousFailTask(long reRun_ReqId) throws Exception {
        List<String> _rtrn = new ArrayList<>();
        Accessor myDAO = new MysqlAccessor();
        Connection _conn = null;
        Statement _selectStmt = null;
        try {
            if (reRun_ReqId <= 0) return _rtrn;

            _conn = myDAO.getConnection();
            _selectStmt = _conn.createStatement();

            HashMap<String, Object> rtrn = new HashMap<String, Object>();
            //String strQuery = "SELECT * FROM reporting.executionscheduler WHERE Schedule_Time <= NOW() AND StatusID < 1 ORDER BY Schedule_Time DESC LIMIT 1";
            //String strQuery = "SELECT s.ReqID, s.ModuleId, em.IP as IP, m.Name as ModuleName, es.Name as SiteName, et.Name as Tool, s.StatusId, st.Description as StatusDesc, eb.Type as BrowserType FROM executionscheduler AS s LEFT JOIN executionmodule AS m ON (s.ModuleId = m.Id) LEFT JOIN executionstatus AS st ON (s.StatusId = st.Id) LEFT JOIN executiontool as et ON (s.ToolId = et.Id) LEFT JOIN executionbrowser as eb ON (s.BrowserId = eb.Id) LEFT JOIN executionSite as es ON (m.SiteId = es.Id) LEFT JOIN executionMachine as em ON (m.MachineId = em.Id) WHERE em.IP = '" + myIP + "' AND ScheduleTime <= NOW() AND StatusId = 0 AND NOT EXISTS (SELECT 1 FROM executionscheduler s2 LEFT JOIN executionmodule AS m2 ON (s2.ModuleId = m2.Id) LEFT JOIN executionMachine as em2 ON (m2.MachineId = em2.Id) WHERE em2.IP = '" + myIP + "' AND s2.StatusId = 1) ORDER BY ScheduleTime ASC LIMIT 1";
            //String strQuery = "SELECT s.ReqID, s.ModuleId, em.IP as IP, m.Name as ModuleName, es.Name as SiteName, et.Name as Tool, s.StatusId, st.Description as StatusDesc, eb.Type as BrowserType, ee.URL as EnvironmentURL FROM executionscheduler AS s LEFT JOIN executionmodule AS m ON (s.ModuleId = m.Id) LEFT JOIN executionstatus AS st ON (s.StatusId = st.Id) LEFT JOIN executiontool as et ON (s.ToolId = et.Id) LEFT JOIN executionbrowser as eb ON (s.BrowserId = eb.Id) LEFT JOIN executionSite as es ON (m.SiteId = es.Id) LEFT JOIN executionMachine as em ON (m.MachineId = em.Id)  LEFT JOIN executionenvironment as ee ON (ee.Id = s.EnvironmentId) WHERE em.IP = '" + myIP + "' AND ScheduleTime <= NOW() AND StatusId = 0 AND NOT EXISTS (SELECT 1 FROM executionscheduler s2 LEFT JOIN executionmodule AS m2 ON (s2.ModuleId = m2.Id) LEFT JOIN executionMachine as em2 ON (m2.MachineId = em2.Id) WHERE em2.IP = '" + myIP + "' AND s2.StatusId = 1) ORDER BY ScheduleTime ASC LIMIT 1";
            String strQuery = String.format("SELECT ac.CaseName FROM allcases as ac where ac.reqid = %s and ac.casestatus='FAIL'", reRun_ReqId);
            //logger.entry();
            ResultSet rs = _selectStmt.executeQuery(strQuery);
            while (rs.next()) {

                if ((rs.getString("CaseName") != null) && (rs.getString("CaseName").length() > 0))
                {
                    Pattern pattern = Pattern.compile("[(.*?)]");
                    Matcher matcher = pattern.matcher(rs.getString("CaseName"));
                    String tmp = "";
                    while (matcher.find()) {
                        tmp = matcher.group(1);
                        if (Common.isNotNullAndNotEmpty(tmp))
                        {
                            _rtrn.add(tmp);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (_selectStmt != null) {
                _selectStmt.close();
            }
            if (_conn != null) {
                _conn.close();
            }
            myDAO.closeConnection();
        }
        return _rtrn;
    }

    private static HashMap<String, Object> selectTopTask(String myIP) throws Exception {
        Accessor myDAO = new MysqlAccessor();
        Connection _conn = null;
        Statement _selectStmt = null;
        try {
            _conn = myDAO.getConnection();
            _selectStmt = _conn.createStatement();

            HashMap<String, Object> rtrn = new HashMap<String, Object>();
            //String strQuery = "SELECT * FROM reporting.executionscheduler WHERE Schedule_Time <= NOW() AND StatusID < 1 ORDER BY Schedule_Time DESC LIMIT 1";
            //String strQuery = "SELECT s.ReqID, s.ModuleId, em.IP as IP, m.Name as ModuleName, es.Name as SiteName, et.Name as Tool, s.StatusId, st.Description as StatusDesc, eb.Type as BrowserType FROM executionscheduler AS s LEFT JOIN executionmodule AS m ON (s.ModuleId = m.Id) LEFT JOIN executionstatus AS st ON (s.StatusId = st.Id) LEFT JOIN executiontool as et ON (s.ToolId = et.Id) LEFT JOIN executionbrowser as eb ON (s.BrowserId = eb.Id) LEFT JOIN executionSite as es ON (m.SiteId = es.Id) LEFT JOIN executionMachine as em ON (m.MachineId = em.Id) WHERE em.IP = '" + myIP + "' AND ScheduleTime <= NOW() AND StatusId = 0 AND NOT EXISTS (SELECT 1 FROM executionscheduler s2 LEFT JOIN executionmodule AS m2 ON (s2.ModuleId = m2.Id) LEFT JOIN executionMachine as em2 ON (m2.MachineId = em2.Id) WHERE em2.IP = '" + myIP + "' AND s2.StatusId = 1) ORDER BY ScheduleTime ASC LIMIT 1";
            //String strQuery = "SELECT s.ReqID, s.ModuleId, em.IP as IP, m.Name as ModuleName, es.Name as SiteName, et.Name as Tool, s.StatusId, st.Description as StatusDesc, eb.Type as BrowserType, ee.URL as EnvironmentURL FROM executionscheduler AS s LEFT JOIN executionmodule AS m ON (s.ModuleId = m.Id) LEFT JOIN executionstatus AS st ON (s.StatusId = st.Id) LEFT JOIN executiontool as et ON (s.ToolId = et.Id) LEFT JOIN executionbrowser as eb ON (s.BrowserId = eb.Id) LEFT JOIN executionSite as es ON (m.SiteId = es.Id) LEFT JOIN executionMachine as em ON (m.MachineId = em.Id)  LEFT JOIN executionenvironment as ee ON (ee.Id = s.EnvironmentId) WHERE em.IP = '" + myIP + "' AND ScheduleTime <= NOW() AND StatusId = 0 AND NOT EXISTS (SELECT 1 FROM executionscheduler s2 LEFT JOIN executionmodule AS m2 ON (s2.ModuleId = m2.Id) LEFT JOIN executionMachine as em2 ON (m2.MachineId = em2.Id) WHERE em2.IP = '" + myIP + "' AND s2.StatusId = 1) ORDER BY ScheduleTime ASC LIMIT 1";
            String strQuery = String.format("SELECT s.ReqID, s.ModuleId, em.IP as IP, m.Name as ModuleName, es.Name as SiteName, et.Name as Tool, s.StatusId, st.Description as StatusDesc, eb.Type as BrowserType, ee.URL as EnvironmentURL, en.hipChatRoomNumber as HipChatNotificationRoom, en.eMail as emailNotification, s.ReRun_ReqId as ReRunReqId FROM executionscheduler AS s LEFT JOIN executionmodule AS m ON (s.ModuleId = m.Id) LEFT JOIN executionstatus AS st ON (s.StatusId = st.Id) LEFT JOIN executiontool as et ON (s.ToolId = et.Id) LEFT JOIN executionbrowser as eb ON (s.BrowserId = eb.Id) LEFT JOIN executionSite as es ON (m.SiteId = es.Id) LEFT JOIN executionMachine as em ON (m.MachineId = em.Id) LEFT JOIN executionNotification as en ON (m.NotificationId = en.Id) LEFT JOIN executionenvironment as ee ON (ee.Id = s.EnvironmentId) WHERE em.IP = '%s' AND ScheduleTime <= NOW() AND StatusId = 0 AND NOT EXISTS (SELECT 1 FROM executionscheduler s2 LEFT JOIN executionmodule AS m2 ON (s2.ModuleId = m2.Id) LEFT JOIN executionMachine as em2 ON (m2.MachineId = em2.Id) WHERE em2.IP = '%s' AND s2.StatusId = 1) ORDER BY ScheduleTime ASC LIMIT 1", myIP, myIP);
            ResultSet rs = _selectStmt.executeQuery(strQuery);
            if (rs.next()) {
                //logger.info("Start Execute.");
                rtrn.put("ReqID", rs.getLong("ReqID"));
                rtrn.put("ModuleID", rs.getLong("ModuleId"));
                rtrn.put("ModuleName", rs.getString("ModuleName"));
                rtrn.put("StatusID", rs.getInt("StatusId"));
                rtrn.put("StatusDesc", rs.getString("StatusDesc"));
                rtrn.put("EnvironmentURL", rs.getString("EnvironmentURL"));
                rtrn.put("SiteName", rs.getString("SiteName"));
                rtrn.put("Tool", rs.getString("Tool"));
                rtrn.put("Tool", rs.getString("ReRunReqId"));
                rtrn.put("HipChatNotificationRoom", rs.getInt("HipChatNotificationRoom"));
                rtrn.put("ReRunReqId", rs.getLong("BrowserType"));
                return rtrn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (_selectStmt != null) {
                _selectStmt.close();
            }
            if (_conn != null) {
                _conn.close();
            }
            myDAO.closeConnection();
        }
        return null;
    }

    private static boolean updateDB(String statement) throws Exception {
        Accessor myDAO = new MysqlAccessor();
        Connection _conn = null;
        Statement _updateStmt = null;
        _conn = myDAO.getConnection();
        //_conn = DriverManager.getConnection("jdbc:mysql://10.32.150.107:3306/reporting", "automation", "automation123");
        _updateStmt = _conn.createStatement();

        try {
            _updateStmt.executeUpdate(statement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (_updateStmt != null) {
                _updateStmt.close();
            }
            if (_conn != null) {
                _conn.close();
            }
            myDAO.closeConnection();
        }
        return false;
    }

    private static long InsertDebugScheduler(String statement) {
        Accessor myDAO = new MysqlAccessor();
        Connection _conn = null;
        CallableStatement cStmnt;
        long id = 0L;
        String myIp = Common.getIpAddress();
        String user = System.getProperty("user.name");

        if ((user == null) || user.isEmpty()) {
            user = "TEST DEBUG ";
        } else {
            user += " Test ";
        }
        String info = String.format("%s [%s]", user, myIp);
        try {
            _conn = myDAO.getConnection();
            cStmnt = _conn.prepareCall(statement);
            cStmnt.registerOutParameter(1, java.sql.Types.BIGINT);
            cStmnt.setString(2, info);
            cStmnt.execute();
            id = cStmnt.getLong(1);
        } catch (Exception e) {
            Log.Error("ERROR WRITE REPORT CASE", e);
            e.printStackTrace();
        } finally {
            try {
                if ((_conn != null) && (!_conn.isClosed())) {
                    _conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;

    }

    private static long insertFunctionDB(String statement) throws Exception {
        Accessor myDAO = new MysqlAccessor();
        Connection _conn = null;
        CallableStatement cStmnt;
        long id = 0L;
        try {
            _conn = myDAO.getConnection();
            cStmnt = _conn.prepareCall(statement);
            cStmnt.registerOutParameter(1, java.sql.Types.BIGINT);
            cStmnt.execute();
            id = cStmnt.getLong(1);
        } catch (SQLException e) {
            Log.Error("ERROR WRITE REPORT CASE", e);
            e.printStackTrace();
        } finally {
            if ((_conn != null) && (!_conn.isClosed())) {
                _conn.close();
            }
        }
        return id;
        //_conn = DriverManager.getConnection("jdbc:mysql://10.32.150.107:3306/reporting", "automation", "automation123");
//		_updateStmt = _conn.createStatement();

//		try {
//			_updateStmt.executeUpdate(statement);
//			return true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			if (_updateStmt != null) {_updateStmt.close();}
//			if (_conn != null) { _conn.close();}
//			myDAO.closeConnection();
//		}
//		return false;
    }

    private static String currentDate() {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        return currentTime;
    }

    private static String getUpdatestatement(long _reqId, String _setString) {
        return String.format("UPDATE HBCD_Automation.executionscheduler %s WHERE ReqId = %s", _setString, _reqId);
    }

    public static boolean updateSchedulerPending(long _reqId) throws Exception {
        if (ConfigurationManager.IsRunAsService() || ConfigurationManager.IsReportingToDB()) {
            return updateDB(getUpdatestatement(_reqId, "SET StatusId = 0" /* pending task (new) */));
        }
        return false;
    }

    public static boolean updateSchedulerInProgress(long _reqId) throws Exception {
        if (ConfigurationManager.IsRunAsService() || ConfigurationManager.IsReportingToDB()) {
            return updateDB(getUpdatestatement(_reqId, String.format("SET StatusId = 1, ExecutionStartTime = '%s'", currentDate())/* in progress */));
        }
        return false;
    }

    public static boolean updateSchedulerCompleted(long _reqId) throws Exception {
        if (ConfigurationManager.IsRunAsService() || ConfigurationManager.IsReportingToDB()) {
            return updateDB(getUpdatestatement(_reqId, String.format("SET StatusId = 3, ExecutionEndTime = '%s'", currentDate())/* completed */));
        }
        return false;
    }

    public static long insertDebugScheduler() {
        return InsertDebugScheduler("{? = call InsertDebugScheduler(?)}");
    }

}
