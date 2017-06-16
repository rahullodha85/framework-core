package com.hbcd.utility.common;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Task implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String _site = "SAKS_FIFTH";
    private String _driverType = "SELENIUM";
    private String _browserName = "firefox";
    private String _browserType = "LOCAL";
    private String _browserVersion = "";
    private String _browserPlatform = "";
    private String _defaultDir = "";
    private String _environmentURL = "";
    private long _requestId = 0;
    private long _reRun_ReqId = 0;
    private long _moduleId = 0;
    private long _uniqueId = new Date().getTime();
    private int _executionStatus = 2;
    private String _moduleName = "";
    private boolean _isInitializeDriver = true;
    private long _hipchatRoomNumber = 0;
    private String _email = "";
    private String _proxySetting = "";
    private String _executionType = "DEFAULT";  //Framework : DEFAULT,  Cucumber: CUCUMBER
    private Map<String, String> _parameters = new HashMap<String, String>();
    private String _remoteHub="";
    private String _resolution="";
    private String _tool="";
    private String _filterTestCase = "";
    private int _parallelExecution = 1;

    public Task() {
        _defaultDir = System.getProperty("user.dir"); //Common.getDefaultDirectory();
    }

	public Task(ResultSet rs)
	{
        //                //logger.info("Start Execute.");
//                rtrn.put("ReqID", rs.getLong("ReqID"));
//                rtrn.put("ModuleID", rs.getLong("ModuleId"));
//                rtrn.put("ModuleName", rs.getString("ModuleName"));
//                rtrn.put("StatusID", rs.getInt("StatusId"));
//                rtrn.put("StatusDesc", rs.getString("StatusDesc"));
//                rtrn.put("EnvironmentURL", rs.getString("EnvironmentURL"));
//                rtrn.put("SiteName", rs.getString("SiteName"));
//                rtrn.put("Tool", rs.getString("Tool"));
//                rtrn.put("BrowserType", rs.getString("BrowserType"));
//                rtrn.put("HipChatNotificationRoom", rs.getInt("HipChatNotificationRoom"));
        try {
            _requestId = rs.getLong("ReqID");
            _site = rs.getString("ReqID");
            _moduleName = rs.getString("ModuleName");
//            _driverType = rs.getLong("DriverType");
            _browserType = rs.getString("BrowserType");
            _browserVersion = rs.getString("BrowserVersion");
            _browserPlatform = rs.getString("BrowserPlatform");

            _hipchatRoomNumber = rs.getLong("HipChatNotificationRoom");
        } catch (Exception ex)
        {

        }
	}
//	
//	public Task(int rqID, String site, int mdlID, String mdlName, String url, String dtyp, String btyp, String bvr, String bpltf, boolean idrv, int hcroomNum, String eml)
//	{
//		_site = site;
//		_environmentURL = url;
//		_driverType = dtyp;
//		_browserType = btyp;
//		_browserVersion = bvr;
//		_browserPlatform = bpltf;
//		_defaultDir = System.getProperty("user.dir"); //Common.getDefaultDirectory();
//		_requestId = rqID;
//		_moduleId = mdlID;
//		_moduleName = mdlName;
//		_isInitializeDriver = idrv;
//		_hipchatRoomNumber = hcroomNum;
//		_email = eml;
//	}


    public void copy(Task from) {
        if (from == null) return;
        _site = from.getSite();
        _environmentURL = from.getEnvironmentURL();
        _driverType = from.getDriver();
        _browserType = from.getBrowserType();
        _browserName = from.getBrowserName();
        _browserVersion = from.getBrowserVersion();
        _browserPlatform = from.getBrowserPlatform();
        _defaultDir = from.getDefaultDir(); //Common.getDefaultDirectory();
        _requestId = from.getRequestId(); //Common.getDefaultDirectory();
        _moduleId = from.getModuleId();
        _moduleName = from.getModuleName();
        _executionStatus = from.getExecutionStatus();
        _isInitializeDriver = from.getIsInitializeDriver();
        _hipchatRoomNumber = from.getHipChatRoomNumber();
        _email = from.getEmail();
        _proxySetting = from.getProxySetting();
        _parameters = from.cloneParams();
        _executionType = from.getExecutionType();
        _reRun_ReqId = from.getReRunReqId();
        _resolution = from.getResolution();
        _remoteHub = from.getRemoteHub();
        _tool = from.getTool();
        _parallelExecution = from.getParallelExecution();
        _filterTestCase = from.getFilterTestCase();
    }

    public int getExecutionStatus() {
        return _executionStatus;
    }

    public long getModuleId() {
        return _moduleId;
    }

    public String getModuleName() {
        return _moduleName;
    }

    public long getRequestId() {
        return _requestId;
    }

    public long getHipChatRoomNumber() {
        return _hipchatRoomNumber;
    }

    public String getSite() {
        return _site;
    }

    public String getEmail() {
        return _email;
    }

    public String getEnvironmentURL() {
        return _environmentURL;
    }

    public String getDriver() {
        return _driverType;
    }

    public String getBrowserType() {
        return _browserType;
    }

    public String getBrowserName()
    {
        return _browserName;
    }

    public String getBrowserVersion() {
        return _browserVersion;
    }

    public String getBrowserPlatform() {
        return _browserPlatform;
    }

    public String getDefaultDir() {
        return _defaultDir;
    }

    public boolean getIsInitializeDriver() {
        return _isInitializeDriver;
    }

    public String getProxySetting() {
        return _proxySetting;
    }

    public String getRemoteHub() { return _remoteHub; }

    public String getResolution() { return _resolution; }

    public String getExecutionType() { return _executionType; }

    public long getUniqueId() { return _uniqueId; }

    public long getReRunReqId() { return _reRun_ReqId; }

    public String getTool() { return _tool; }

    public boolean isParallelExecution()
    {
        return (_parallelExecution <= 1) ? false : true;
    }

    public int getParallelExecution()
    {
        return _parallelExecution;
    }

    public String getFilterTestCase()
    {
        return _filterTestCase;
    }

    public Map<String, String> getParams() { return _parameters; }

    /*********************************************************************/

    public Task setExecutionStatus(int _executionStatus) {
        this._executionStatus = _executionStatus;
        return this;
    }
    /**
     * @param _site the _site to set
     */
    public Task setSite(String _site) {
        this._site = _site;
        return this;
    }

    /**
     * @param _driverType the _driverType to set
     */
    public Task setDriverType(String _driverType) {
        this._driverType = _driverType;
        return this;
    }

    /**
     * @param _browserType the _browserType to set
     */
    public Task setBrowserType(String _browserType) {
        this._browserType = _browserType;
        return this;
    }

    public Task setBrowserName(String _browserName) {
        this._browserName = _browserName;
        return this;
    }

    /**
     * @param _browserVersion the _browserVersion to set
     */
    public Task setBrowserVersion(String _browserVersion) {
        this._browserVersion = _browserVersion;
        return this;
    }

    /**
     * @param _browserPlatform the _browserPlatform to set
     */
    public Task setBrowserPlatform(String _browserPlatform) {
        this._browserPlatform = _browserPlatform;
        return this;
    }

    /**
     * @param _defaultDir the _defaultDir to set
     */
    public Task setDefaultDir(String _defaultDir) {
        this._defaultDir = _defaultDir;
        return this;
    }

    /**
     * @param _environmentURL the _environmentURL to set
     */
    public void setEnvironmentURL(String _environmentURL) {
        this._environmentURL = _environmentURL;
    }

    /**
     * @param _requestId the _requestId to set
     */
    public Task setRequestId(long _requestId) {
        this._requestId = _requestId;
        return this;
    }

    /**
     * @param _moduleId the _moduleId to set
     */
    public Task setModuleId(long _moduleId) {
        this._moduleId = _moduleId;
        return this;
    }

    /**
     * @param _moduleName the _moduleName to set
     */
    public Task setModuleName(String _moduleName) {
        this._moduleName = _moduleName;
        return this;
    }

    /**
     * @param _isInitializeDriver the _isInitializeDriver to set
     */
    public Task setIsInitializeDriver(boolean _isInitializeDriver) {
        this._isInitializeDriver = _isInitializeDriver;
        return this;
    }

    /**
     * @param _hipchatRoomNumber the _hipchatRoomNumber to set
     */
    public Task setHipchatRoomNumber(long _hipchatRoomNumber) {
        this._hipchatRoomNumber = _hipchatRoomNumber;
        return this;
    }

    /**
     * @param _email the _email to set
     */
    public Task setEmail(String _email) {
        this._email = _email;
        return this;
    }

    /**
     * @param _proxySetting the _proxySetting to set
     */
    public Task setProxySetting(String _proxySetting) {
        this._proxySetting = _proxySetting;
        return this;
    }


    public Task setExecutionType(String _prjType) {
        this._executionType = _prjType;
        return this;
    }

    public Task setReRunRequestId(long rrid)
    {
        this._reRun_ReqId = rrid;
        return this;
    }

    public Task setRemoteHub(String rhub)
    {
        this._remoteHub = rhub;
        return this;
    }

    public Task setResolution(String rsl)
    {
        this._resolution = rsl;
        return this;
    }

    public Task setTool(String t)
    {
        this._tool = t;
        return this;
    }

    public Task setParallelExecution(int p)
    {
        if (p <= 0) p = 1;
        this._parallelExecution = p;
        return this;
    }

    public Task setFilterTestCase(String regEx)
    {
        this._filterTestCase = regEx;
        return this;
    }

    private HashMap<String, String> cloneParams()
    {
        return new HashMap<String, String>(_parameters);
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("Site: ").append(_site)
                .append("| Environment: ").append(_environmentURL)
                .append("| Driver : ").append(_driverType)
                .append("| Module Name: ").append(_moduleName);
        System.out.println(sb.toString());
    }
}
