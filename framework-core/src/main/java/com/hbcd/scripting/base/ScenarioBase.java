package com.hbcd.scripting.base;
import com.hbcd.logging.log.Log;
import com.hbcd.scripting.core.BrowserAction;
import com.hbcd.scripting.core.HomePage;
import com.hbcd.scripting.core.Report;
import com.hbcd.scripting.core.Storage;
import com.hbcd.scripting.exception.DataException;
import com.hbcd.scripting.exception.ScriptErrorException;
import com.hbcd.scripting.exception.ScriptFailException;
import com.hbcd.scripting.exception.SystemException;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.configurationsetting.ConfigurationManager;
import com.hbcd.utility.configurationsetting.SiteConfigurationManager;
import com.hbcd.utility.entity.CaseParameter;
import com.hbcd.utility.entity.ObjectTestScript;
import com.hbcd.utility.helper.Common;
import com.hbcd.utility.screenrecorder.VideoRecorder;
import com.hbcd.utility.testscriptdata.TestCaseResult;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.rmi.Remote;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
public class ScenarioBase implements Serializable, Remote, Callable<Object>, ScenarioParameterSetting { // Runnable {

    private static final long serialVersionUID = 1L;
    protected long _caseId = 0;
    protected String _rawInputData = "";
    protected long _requestId = 0;
    protected int _executionOrder = 0;
    protected String _testScriptDescription = "";
    private Instant _start;
    private Instant _end;
    protected long _duration_MilliSecond = 0;
    protected ObjectTestScript _testScriptObject = null;
    private VideoRecorder v1;
    private TestCaseResult _tcr = new TestCaseResult(_caseId, "FAIL");
    private boolean _requiredFinalScreenAnalysis = false;
    private Exception _exceptionCaught = null;

    public ScenarioBase(){
    }

    private void initScript() throws Exception{
        _start = Instant.now();
        Log.Info("run thread");
        Log.Info("beforeExecutScript");
        //Setting up Logging
        Storage.save("CURRENT_SCENARIO_NAME", this.getClass().getName());
        Storage.save("CURRENT_SCENARIO_DESC", _testScriptDescription);
        Report.init();
        v1 = new VideoRecorder(ConfigurationLoader.getSystemStringValue("DEFAULT_VIDEO_OUTPUT"), ConfigurationManager.IsRecordVideo());
        v1.startRecording((Storage.get("CURRENT_SCENARIO_NAME")==null)?"":Storage.get("CURRENT_SCENARIO_NAME").toString());
    }

    public void beforeExecuteScript() throws Exception{
        try{
            HomePage.Go().getPerformance(String.format("HOME PAGE: %s",ConfigurationLoader.getWebSiteURL(ApplicationSetup.get(Setting.SITE))), "HomePage");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void executeScript() throws Exception{
    }

    public void afterExecuteScript() throws Exception{
        Report.end_script("SCRIPT IS COMPLETED - NO Screenshot", "");
    }

    public void finalizedExecution() throws Exception{
        try{
            _end = Instant.now();
            _duration_MilliSecond = Duration.between(_start, _end).toMillis();
            Report.log("SCENARIO DURATION", String.format("Total execution Time for [%s] is %s seconds.", (Storage.get("CURRENT_SCENARIO_NAME")==null)?"":Storage.get("CURRENT_SCENARIO_NAME").toString(), (_duration_MilliSecond / 1000)), "pass", "");
            _tcr = Report.generate(_requestId, _executionOrder, _duration_MilliSecond);
            _tcr.setTestScriptObject(_testScriptObject);
        }catch (Exception ex) {
            ex.printStackTrace();
        }finally{

            Report.close();  //DB Close
        }
    }

    public void ini(CaseParameter param, String... args) throws Exception{
        _requestId = param.getRequestId();
        _executionOrder = param.getCaseExecutionOrder();
    }

    public void ini(long requestId, String... args) throws Exception{
        _requestId = requestId;
    }

    public void run()throws Exception{
        call();
    }

    public Object call()throws Exception{
        try{
            initScript();
            beforeExecuteScript();
            executeScript();
            afterExecuteScript();
        }catch(Exception ex){
            ex.printStackTrace();
            _exceptionCaught = ex;
            _requiredFinalScreenAnalysis = true;   //Final Analysis is required -- use in Final try catch
            try{
                String msg = Common.getExceptionToString(ex);
                if(msg == null){
                    msg = ex.toString();
                }
                if(!((ex instanceof DataException)
                        || (ex instanceof SystemException)
                        || (ex instanceof ScriptErrorException)
                        || (ex instanceof ScriptFailException)
                )){
                    Report.error(msg, BrowserAction.screenshot());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }finally{
            boolean _foundCause = false;
            try{
                if (ApplicationSetup.getBoolean(Setting.IS_INITIALIZE_DRIVER)) {
                    if(SiteConfigurationManager.isCloseBrowserAfterFinish(true)) {
                        _foundCause = Report.errorAnalysis(_requiredFinalScreenAnalysis, ApplicationSetup.get(Setting.SITE) + ".");
                        BrowserAction.tearDown(); //Close driver
                    }
                }
                if(!_foundCause){
                    Report.errorAnalysis(_exceptionCaught);
                }
                v1.stopRecording();
                if(_tcr.getStatus().equalsIgnoreCase("pass") && ConfigurationManager.IsRecordVideo()){
                    boolean deleteStatus = new File(String.format("%s\\%s", ConfigurationLoader.getSystemStringValue("DEFAULT_VIDEO_OUTPUT"), v1.getRecordedFileName())).delete();
                    Report.log("File has be be deleted on pass", String.format("File %s :%s\\%s", (deleteStatus ? "Deleted" : "Delete failure"), ConfigurationLoader.getSystemStringValue("DEFAULT_VIDEO_OUTPUT"), v1.getRecordedFileName()), "Delete Successful", "");
                }

            }catch(Exception e){
                Common.writeErrorToLog(e);
                e.printStackTrace();
            }
            finalizedExecution();
        }
        Log.Info("***********  C O M P L E T E D ************");
        return _tcr;
    }

    public void setRequestId(long rId){
        _requestId = rId;
    }

    public void setExecutionOrder(int ith){
        _executionOrder = ith;
    }

    public void setTestScript(Object obj){
        _testScriptObject = (ObjectTestScript) obj;
    }

}
