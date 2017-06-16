package com.hbcd.core.genericfunctions;

import com.hbcd.container.common.ElementObjectBase;
import com.hbcd.container.common.SafeDriver;
import com.hbcd.container.web.WebDriverException;
import com.hbcd.containerinterface.ElementSearch;
import com.hbcd.logging.log.LogInfo;
import com.hbcd.logging.log.LogInfoImp;
import com.hbcd.scripting.enums.impl.Keys;
import com.hbcd.storage.data.SafeStorage;
import com.hbcd.storage.object.SafeObjectContentStorage;
import com.hbcd.utility.common.WebPerformance;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectSearchParameters;
import com.hbcd.utility.entity.SelectMultiObjectConfiguration;
import com.hbcd.utility.helper.Common;
import org.apache.commons.lang3.StringUtils;

import java.util.*;


public class GenericFunctions {

    private static String _methodNameError = "ThrowError";
    //Utility Function Find methodName and className in Stack Trace
    private static boolean isCalleddMethodNameInStack(String className, String methodName)
    {
        boolean status = false;
        StackTraceElement[] stkElements =Thread.currentThread().getStackTrace();
        String mainClassName = null;
        if (stkElements.length > 0) {
            for (StackTraceElement stkTrcElement : stkElements) {
                if ((stkTrcElement.getClassName().equals(className)) &&
                        (stkTrcElement.getMethodName().equals(methodName))){
                    status = true;
                    break;
                }
            }
        }
        return status;
    }

    //Utility Function
    private static void ThrowError(LogInfo _l, ActionParameters acp, ReRunFunction reRun) throws Exception
    {
        _l.add(", START HANDLE SPECIAL EVENT");
        if (acp.handleSpecialEvent()) {
            _l.add(", HANDLE SPECIAL EVENT SUCCESSFUL, RE-EXECUTE THE STEP");
            reRun.execute();
        }
        else {
            _l.add(", END HANDEL SPECIAL EVENT (EVENT FAILED)");
            throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _l.toString()));
        }
    }

    private static boolean isFoundElement(ElementSearch es)
    {
        //return (es != null) ? (es.fw_isNotFound() ? false : true) : false;
        return ((es != null) && (!es.fw_isNotFound()));
    }

    private static ElementSearch findInList(ElementSearch object, SelectMultiObjectConfiguration selMOConfig) throws Exception
    {
        ElementSearch rtrn = null;
        if (selMOConfig.isMultiObjectFilter()) {
            if (selMOConfig.isRandom()) {
                rtrn = object.fw_getElementRandomInList();
            } else if (selMOConfig.getIndex() > 0)
            {
                rtrn = object.fw_getElementInList(selMOConfig.getIndex());
                if ((rtrn != null) && (rtrn.fw_isFound())){
                    rtrn.fw_scrollToElement();
                    rtrn.fw_elementBlink(2);
                    rtrn.fw_elementHighlightOnly();
                }

            } else if (!selMOConfig.getContainText().isEmpty())
            {
                rtrn = object.fw_getElementInList(selMOConfig.getContainText());
                if ((rtrn != null) && (rtrn.fw_isFound())) {
                    rtrn.fw_scrollToElement();
                    rtrn.fw_elementBlink(2);
                    rtrn.fw_elementHighlightOnly();
                }
            }
            selMOConfig.setIndex(object.fw_getSelectedElementIndexFromMultipleObject()); //NOT FOUND
        }
        //Make sure NOT RETURN NULL.
        if (rtrn == null) rtrn = new ElementObjectBase(1); //NOT FOUND
        return rtrn;
    }

    private static ElementSearch findElement(ObjectSearchParameters param, LogInfo _log) throws Exception {
        return findElement(null, param, _log);
    }

    private static ElementSearch findElement(ElementSearch content, ObjectSearchParameters param, LogInfo _log) throws Exception {
        try {

            if (param.isUseStoredObject())
            {
                return SafeObjectContentStorage.Current().get(param.getStoredOjbectName());
            }
            else
            {
                ElementSearch object = Utility.coreFindElement(param.hasStoredObject() ? SafeObjectContentStorage.Current().get(param.getStoredOjbectName()) : content, param.getObjectRepository(), true, _log, param.isHightlight());
                if (object.fw_isFound()) {
                    return object;
                } else if (object.fw_isMulitpleFound() && (param.getMultiObjectConfiguration() != null))
                {
                    if (object.fw_isMulitpleFound() && param.getMultiObjectConfiguration().isMultiObjectFilter()) {
                        return findInList(object, param.getMultiObjectConfiguration());
                    }
                    else {
                        return object;
                    }
                } else if (object.fw_isMulitpleFound() && (param.getMultiObjectConfiguration() == null))
                {
                    return object;
                } else {
                    return new ElementObjectBase(1); //NOT FOUND
                }
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        }
    }

    private static ElementSearch findElement(ActionParameters actParams, LogInfo _log) throws Exception {
        ElementSearch _main = null;
        int _level = 0;
        try {
            final Iterator<ObjectSearchParameters> itr = actParams.getList().iterator();

            while (itr.hasNext()) {
                ObjectSearchParameters p = itr.next();
                _level++;
                String _name = p.isUseStoredObject() ? p.getStoredOjbectName() : ((p.getObjectRepository() != null) ? p.getObjectRepository().getObjectName() : "NO NAME");
                _log.add(String.format(", [<Level-%s> %s]", _level, _name));
                _main = findElement(_main, p, _log);

                if (!isFoundElement(_main) && itr.hasNext())  //still has element (not last) this is ERROR.
                {
                    _log.add(", INCOMPLETE - has more level");
                    break;
                }
            }
        }
        catch (Exception ex)
        {
            throw (ex);
        }
        return _main;
    }

   // Clear Object Value
    public static void coreClear(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
                ElementSearch es = findElement(actParams, _log);
                if (isFoundElement(es))
                {
                    if (es.fw_isMulitpleFound())
                        throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                    _log.add(", Action: CLEAR ");
                    es.fw_clear();
                }
                else {
                    throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
                }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // SELECT (Drop down list box - select by index)
    public static void coreSelectByIndex(ActionParameters actParams)
            throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                int index = actParams.Current().getActionParamAsInteger();  //Select by Index
                _log.add(String.format(", Action: SELECT DROPDOWN INDEX '%s' %s", index, actParams.Current().getMessage()));
                es.fw_selectByIndex(index);
            }
            else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // SELECT (Drop down list box - select by visible Text (Display Text) )
    public static void coreSelectByVisibleText(ActionParameters actParams )
            throws Exception {

        LogInfo _log = new LogInfoImp();
        try {

            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                String text = actParams.Current().getActionParamAsString();  //Select by Visible Text
                if (es.fw_isDisplayed()) {
                    _log.add(String.format(", Action: SELECT VISIBLE TEXT '%s' %s", text, actParams.Current().getMessage()));
                    es.fw_selectByVisibleText(text);
                }
            }
            else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // SELECT (Drop down list box - select by value)
    public static void coreSelectByValue(ActionParameters actParams)
            throws Exception {
        LogInfo _log = new LogInfoImp();

        try {

            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                String value = actParams.Current().getActionParamAsString(); //Select by Value
                _log.add(String.format(", Action: SELECT DROPDOWN BY VALUE '%s' %s", value, actParams.Current().getMessage()));
                es.fw_selectByValue(value);
            }
            else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }

    }

    // Get Object Text
    public static String coreGetText(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException( String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                _log.add(String.format(", Action: GET TEXT  %s", actParams.Current().getMessage()));
                return es.fw_getText();
            }
            else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // Get HTML Page Source
    public static String coreGetHTMLSource() {
        return SafeDriver.Current().fw_getPageSource();
    }

    // Get Attribute value
    public static String coreGetAttribute(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {

            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                String attrbName = actParams.Current().getActionParamAsString();
                _log.add(String.format(", Action: GET ATTRIBUTE '%s' %s", attrbName, actParams.Current().getMessage()));
                return es.fw_getAttribute(attrbName);
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // INPUT - Input Text
    public static void coreEnterText(ActionParameters actParams)
            throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                String text = actParams.Current().getActionParamAsString();
                _log.add(String.format(", Action: ENTER TEXT '%s' %s", text, actParams.Current().getMessage()));
                es.fw_sendKeys(text);
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // INPUT - Input Text
    public static void coreJavascriptEnterText(ActionParameters actParams)
            throws Exception {
        LogInfo _log = new LogInfoImp();
        try {

            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                String text = actParams.Current().getActionParamAsString();  //JavaScript
                if (es.fw_isDisplayed()) {
                    _log.add(String.format(", Action: JAVASCRIPT ENTER TEXT : '%s' %s", text, actParams.Current().getMessage()));
                    es.fw_executeJavaScript(String.format("arguments[0].setAttribute('value', '%s')", text));
                }
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    public static Object coreJavascriptExecuteScript( String text)
            throws Exception {
        LogInfo _log = new LogInfoImp();
        try {

                  return  SafeDriver.Current().fw_executeJavaScript(text);

        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }


    public static void coreJavascriptAlter(ActionParameters actParams)
            throws Exception {
        LogInfo _log = new LogInfoImp();
        try {

            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                String text = actParams.Current().getActionParamAsString();
                _log.add(String.format(", Action: JAVASCRIPT Edit STYLE Attribute  %s", actParams.Current().getMessage()));
                es.fw_executeJavaScript(String.format("arguments[0].setAttribute('style', '%s')", text));

            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // Validate Text
    public static boolean coreValidateText(ActionParameters actParams) throws Exception {
        boolean status = false;
        LogInfo _log = new LogInfoImp();
        try {

            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                String text = actParams.Current().getActionParamAsString(); //Text to be validate
                if (es.fw_isDisplayed()) {
                    _log.add(String.format(", Action: VALIDATE TEXT '%s' %s", text, actParams.Current().getMessage()));
                    status = es.fw_getText().contains(text);
                }
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return status;
    }

    // Click
    public static String[] coreClick(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        String strStartTime = "";
        String strEndTime = "";
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s : EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                _log.add(String.format(", Action: Click %s", actParams.Current().getMessage()));
                strStartTime = Long.toString(new Date().getTime());
                es.fw_click();
                strEndTime = Long.toString(new Date().getTime());
            } else {
                //Make Sure the 'ThrowError' will be called only ONCE in this statement
                //Look into Thread Stack Trace and if it does ('ThrowError') not exist then execute
                //otherwise do not execute to prevent infinite call.
                if(!isCalleddMethodNameInStack(GenericFunctions.class.getName(), _methodNameError)) {
                    ThrowError(_log, actParams, ()->{
                        try {
                            coreClick(actParams);
                            _log.add(", RE-EXECUTE SUCCESSFULLY!");
                        } catch (Exception e) {
                            e.printStackTrace();
                            _log.add(", RE-EXECUTE FAILED!");
                            throw e;
                        }
                    });
                }
                else
                {
                    throw new CoreException(String.format("%s : ELEMENT NOT FOUND!", _log.toString()));
                }
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return new String[]{strStartTime, strEndTime};
    }

    public static String[] coreJavascriptClick(ActionParameters actParams)
            throws Exception {
        LogInfo _log = new LogInfoImp();
        String strStartTime = "";
        String strEndTime = "";
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s : EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                if (es.fw_isDisplayed()) {
                    _log.add(String.format(", Action: JAVASCRIPT CLICK %s", actParams.Current().getMessage()));
                    strStartTime = Long.toString(new Date().getTime());
                    es.fw_javascriptClick();
                    strEndTime = Long.toString(new Date().getTime());
                }
            } else {
                throw new CoreException(String.format("%s ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return new String[]{strStartTime, strEndTime};
    }

    // SUBMIT - form
    public static String[] coreSubmit(ActionParameters actParams)
            throws Exception {
        LogInfo _log = new LogInfoImp();
        String strStartTime = "";
        String strEndTime = "";
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                if (es.fw_isDisplayed()) {
                    _log.add(String.format(", Action: SUBMIT %s", actParams.Current().getMessage()));
                    strStartTime = Long.toString(new Date().getTime());
                    es.fw_submit();
                    strEndTime = Long.toString(new Date().getTime());
                }
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return new String[]{strStartTime, strEndTime};
    }

    public static String[] coreEnter(ActionParameters actParams)
            throws Exception {
        LogInfo _log = new LogInfoImp();
        String strStartTime = "";
        String strEndTime = "";
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                _log.add(String.format(", Action: ENTER %s", actParams.Current().getMessage()));
                strStartTime = Long.toString(new Date().getTime());
                es.fw_enter();
                strEndTime = Long.toString(new Date().getTime());
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return new String[]{strStartTime, strEndTime};
    }

    // Navigate URL
    public static String[] coreGo(String url) throws Exception {
        LogInfo _log = new LogInfoImp();
        boolean status = false;
        String strStartTime = "";
        String strEndTime = "";
        try {
            strStartTime = Long.toString(new Date().getTime());
            status = SafeDriver.Current().fw_get(url);
            strEndTime = Long.toString(new Date().getTime());
            if (status) {
                _log.add(String.format("NAVIGATE TO: %s", url));
            } else {
                throw new CoreException(String.format("ERROR UNABLE TO GET TO %s", url));
            }
        } catch (Exception ex) {
            _log.add(String.format("ERROR UNABLE TO GET TO %s", url));
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return new String[]{strStartTime, strEndTime};
    }

    // Navigate URL
    public static String[] coreBack() throws Exception {
        LogInfo _log = new LogInfoImp();
        boolean status = false;
        String strStartTime = "";
        String strEndTime = "";
        try {
            strStartTime = Long.toString(new Date().getTime());
            SafeDriver.Current().fw_back();
            strEndTime = Long.toString(new Date().getTime());

        } catch (Exception ex) {
            _log.add("ERROR UNABLE TO GO BACK");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return new String[]{strStartTime, strEndTime};
    }

    // Element in HTML
    public static boolean coreIsPresent(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        String _additionalMessage = "";
        try {
                ElementSearch es = findElement(actParams, _log);
                if (actParams.Current().isMultiObject())
                {
                    _additionalMessage = String.format(" ON INDEX '%s'", actParams.Current().getMultiObjectConfiguration().getIndex());
                }
                if (isFoundElement(es))
                {
                    if (es.fw_isMulitpleFound())
                        throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                    _log.add(", Action: ELEMENT PRESENT IS TRUE.");
                    return true;
                }
                else
                {
                    _log.add(", Action: ELEMENT PRESENT IS FALSE");
                    return false;
                }

        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    public static boolean coreIsNotPresent(ActionParameters actParams) {
        LogInfo _log = new LogInfoImp();
        String _additionalMessage = "";
        try {
                ElementSearch es = findElement(actParams, _log);
                if (actParams.Current().isMultiObject())
                {
                    _additionalMessage = String.format(" ON INDEX '%s'", actParams.Current().getMultiObjectConfiguration().getIndex());
                }
                if (isFoundElement(es))
                {
                    if (es.fw_isMulitpleFound())
                        throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                    _log.add(String.format(", Check: ELEMENT NOT PRESENT ON INDEX IS FALSE %s", _additionalMessage));
                    return false;
                } else
                {
                    _log.add(String.format(", Check: ELEMENT NOT PRESENT IS TRUE %s", _additionalMessage));
                    return true;
                }
        } catch (Exception ex) {
            _log.add(", Check: ELEMENT PRESENT IS TRUE");
            return true;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // Element is Enable
    public static boolean coreIsEnabled(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        String _additionalMessage = "";
        try {

            ElementSearch es = findElement(actParams, _log);
            if (actParams.Current().isMultiObject())
            {
                _additionalMessage = String.format(" ON INDEX '%s'", actParams.Current().getMultiObjectConfiguration().getIndex());
            }
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException( String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                if (es.fw_isEnabled()) {
                    _log.add(String.format(", Check: ELEMENT ENABLED IS TRUE  %s", _additionalMessage));
                    return true;
                } else {
                    _log.add(String.format(", Check: ELEMENT ENABLED IS FALSE  %s", _additionalMessage));
                    return false;
                }
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }

        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // Element is Visible
    public static boolean coreIsDisplayed(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        String _additionalMessage = "";
        try {

            ElementSearch es = findElement(actParams, _log);
            if (actParams.Current().isMultiObject())
            {
                _additionalMessage = String.format(" ON INDEX '%s'", actParams.Current().getMultiObjectConfiguration().getIndex());
            }
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format( "%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                if (es.fw_isDisplayed()) {
                    _log.add(String.format(", Check: ELEMENT DISPLAYED IS TRUE %s", _additionalMessage));
                    return true;
                } else {
                    _log.add(String.format(", Check: ELEMENT DISPLAYED IS FALSE %s", _additionalMessage));
                    return false;
                }
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // Element is Visible and Enable
    public static boolean coreIsPresentDisplayedEnabled(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        String _additionalMessage = "";
        try {
            ElementSearch es = findElement(actParams, _log);
            if (actParams.Current().isMultiObject())
            {
                _additionalMessage = String.format(" ON INDEX '%s'", actParams.Current().getMultiObjectConfiguration().getIndex());
            }
            if (isFoundElement(es)) {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                if (es.fw_isDisplayed() && es.fw_isEnabled())
                {
                    _log.add(String.format(", Check: ELEMENT 'PRESENT DISPLAYED ENABLED' IS TRUE %s", _additionalMessage));
                    return true;
                } else {
                    _log.add(String.format(", Check: ELEMENT 'PRESENT DISPLAYED ENABLED' IS FALSE %s", _additionalMessage));
                    return false;
                }
            }
            else
            {
                _log.add(String.format(", Check: ELEMENT 'PRESENT DISPLAYED ENABLED' IS FALSE %s", _additionalMessage));
                return false;
            }

        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // Element is Visible and Enable
    public static boolean coreIsMultipleElemenstReturn (ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound()) {
                    _log.add(", Check: MULTIPLE ELEMENTS RETURN IS TRUE");
                    return true;
                }
                else
                {
                    _log.add(", Check: MULTIPLE ELEMENTS RETURN IS FALSE");
                    return false;
                }
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // SWITCH to Frame by FrameObject Description
    public static void coreSwitchToFrame (ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound()) {
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                }
                _log.add(String.format(", Action: SWITCH TO FRAME OBJECT %s", actParams.Current().getMessage()));
                SafeDriver.Current().fw_switchToFrame(es);
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // SWITCH to Frame as Object
    public static void coreSwitchToFrame(String IDorName) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            if (!IDorName.isEmpty()) {
                _log.add(String.format("Action: SWITH TO FRAME '%s'", IDorName));
                SafeDriver.Current().fw_switchToFrame(IDorName);
            } else {
                throw new CoreException("Switch to Frame");
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // SWITCH to Frame by Index
    public static void coreSwitchToFrame(int index) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            if (index != -1) {
                _log.add(String.format("Action: SWITH TO FRAME '%s'", index));
                SafeDriver.Current().fw_switchToFrame(index);
            } else {
                throw new CoreException("Switch to Frame");
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // SWITCH to Frame by Frame_Name or Frame_ID attribute
    public static void coreSwitchToDefaultContent() {
        LogInfo _log = new LogInfoImp();
        try {
            _log.add("Action: SWITCH TO DEFAULT CONTENT");
            SafeDriver.Current().fw_switchToDefaultContent();
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // SWITCH to New Window
    public static void coreSwitchToNewWindow(String windowTitle) {
        LogInfo _log = new LogInfoImp();
        try {
            _log.add(String.format("Action: SWITCH TO WINDOW '%s'", windowTitle));
            SafeDriver.Current().fw_switchNewWindow(windowTitle);
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    public static String coreGetCurrentUrl() {
        LogInfo _log = new LogInfoImp();
        try {
            _log.add("Action: GET CURRENT URL");
            return SafeDriver.Current().fw_getCurrentUrl();
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // SWITCH to Frame by name or ID attribute
    public static String coreGetCurrentWindowHandle() {
        LogInfo _log = new LogInfoImp();
        try {
            _log.add("Action: GET CURRENT WINDOWS HANDLE");
            return SafeDriver.Current().fw_currentWindowHandle();
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // Click on Confirmation Box
    public static void coreClickConfirm() {
        LogInfo _log = new LogInfoImp();
        try {
            _log.add("Action: CLICK CONFIRM");
            SafeDriver.Current().fw_clickConfirmAlert();
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    public static void coreClickDismiss() {
        LogInfo _log = new LogInfoImp();
        try {
            _log.add("Action: CLICK DISMISS");
            SafeDriver.Current().fw_clickDismissAlert();
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    // Mouse OVER
    public static void coreMouseOver() throws WebDriverException {

        // Actions builder = new Actions(SeleniumDriver.currentDriver());
        // ElementSearch tagElement = element.fw_findElement("", "");
        // builder.moveToElement((WebElement) tagElement).build().perform();
    }

    public static void coreHover(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {

            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                _log.add(String.format(", Action: HOVER %s", actParams.Current().getMessage()));
                if (es.fw_isDisplayed()) {
                    SafeDriver.Current().fw_hover(es);
                }
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    public static void coreDoubleClick(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                _log.add(String.format(", Action: DOUBLE CLICK %s", actParams.Current().getMessage()));
                SafeDriver.Current().fw_doubleClick(es);
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    public static void coreMouseClick(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                _log.add(String.format(", Action: MOUSE CLICK %s", actParams.Current().getMessage()));
                SafeDriver.Current().fw_mouseClick(es);
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    private static int polling(CorePredicate p, ActionParameters acp, String actionMsg, String failActionMsg, LogInfo actionLog) throws Exception
    {
        //Status: 0:true   1:false     2:timeout
        int rtrnStatus = 1; //false as default

        long accumulatePollingTime = 0;
        long pollingIntervalTime = 500;

        long pollingPerSecond = 1000/pollingIntervalTime;
        //if MaxWaitTime is less than 30 seconds then set it as 30 seconds.
        int maxTime = acp.Current().getObjectRepository().getUserDefinedExplicitWaitTime();
        long totalWaitTimeInSec = ( maxTime <30 ) ? maxTime : 30;
        long totalPoolingTimeBeforeExipred = totalWaitTimeInSec * 1000; // * pollingPerSecond;
        acp.resetCustomWaitTime(3);
        while(true)
        {
            if (p.apply(acp))
            {
                rtrnStatus = 0;
                break;
            } else
            {
                accumulatePollingTime += pollingIntervalTime;
                if (accumulatePollingTime >= totalPoolingTimeBeforeExipred)  //Max 30 seconds.
                {
                    System.out.println(String.format("Exit Wait %s (Expired max wait time %s ms).", failActionMsg, totalPoolingTimeBeforeExipred));
                    //throw new CoreException(String.format("%s: %s!", actionLog.toString(), failActionMsg));
                    rtrnStatus = 2;
                    break;
                }
                Thread.sleep(pollingIntervalTime); //0.5 a second wait and test again.
                actionLog.add(String.format("Continue Waiting (%s ms)...", pollingIntervalTime));
                System.out.println(String.format("Continue Waiting for %s  - has been waited for %s ms, expected max end wait %s ms", actionMsg, accumulatePollingTime, totalPoolingTimeBeforeExipred));
            }
        } //end while
        return rtrnStatus;
    }

    public static int coreWaitTillElementDisappear(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        int rtrnStatus = 1;
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                //Polling Check
                rtrnStatus = polling(acp -> {
                                ElementSearch es2 = null;
                                LogInfo _log2 = new LogInfoImp();
                                try {
                                    es2 = findElement((ActionParameters)acp, _log2);
                                    if (isFoundElement(es2)) {
                                        _log.add(", OBJECT IS DISAPPEARED.");
                                        return true;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    throw e;
                                }
                                return false;
                            },
                        actParams,
                        "Object to be Disappeared",
                        "Object is NOT Disappeared",
                        _log );

//                polling(new CorePredicate() {
//                            @Override
//                            public boolean apply(Object acp) {
//                                ElementSearch es2 = null;
//                                try {
//                                    es2 = findElement((ActionParameters)acp, _log2);
//                                    if (es2==null) {
//                                        _log.add("ELEMENT DISAPPEAR.");
//                                        return true;
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                return false;
//                            }
//                        },
//                        actParams,
//                        _log2);
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return rtrnStatus;
    }

    public static int coreWaitTillTextChange(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        int rtrnStatus = 1;
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                String _originalTextToBeCompared = actParams.Current().getActionParamAsString();
                if (!es.fw_getText().equalsIgnoreCase(_originalTextToBeCompared))
                    throw new CoreException(String.format("%s: Initial Text is not the same as %s!", _log.toString(), _originalTextToBeCompared));
                //Polling Check
                rtrnStatus = polling(acp -> {
                            ElementSearch es2 = null;
                            LogInfo _log2 = new LogInfoImp();
                            try {
                                es2 = findElement((ActionParameters)acp, _log2);
                                if (isFoundElement(es2)) {
                                    if (!es2.fw_getText().equalsIgnoreCase(_originalTextToBeCompared)) {
                                        _log.add(", TEXT HAS CHANGED.");
                                        return true;
                                    }
                                }
                                else
                                {
                                    _log.add(String.format(",%s: ELEMENT IS NO LONGER EXIST!", _log2.toString()));
                                    //throw new CoreException("ELEMENT IS NO LONGER EXIST!!");
                                    return false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //throw e;
                            }
                            return false;
                        },
                        actParams,
                        String.format("TEXT DIFFERENT than [%s]", _originalTextToBeCompared),
                        "TEXT has NOT CHANGED.",
                        _log );
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return rtrnStatus;
    }

    public static int coreWaitTillAttribueValueChangeContainText(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        int rtrnStatus = 1;
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                Map.Entry<String, String > next = (actParams.Current().getListActionParamAsHashMap().size() == 1) ? actParams.Current().getListActionParamAsHashMap().entrySet().iterator().next() : null;
                if (next == null) return 3; //Problem
                String _key = next.getKey();
                String _value = next.getValue();
                rtrnStatus = polling(acp -> {
                            ElementSearch es2 = null;
                            LogInfo _log2 = new LogInfoImp();
                            try {
                                es2 = findElement((ActionParameters) acp, _log2);
                                if (isFoundElement(es2)) {
                                    if (es2.fw_getAttribute(_key).contains(_value)) {
                                        _log.add(String.format(", ATTRIBUTE HAS CHANGED TO TEXT CONTAIN %s", _value));
                                        return true;
                                    }
                                } else {
                                    _log.add(String.format(",%s: ELEMENT IS NO LONGER EXIST!", _log2.toString()));
                                    //throw new CoreException("ELEMENT IS NO LONGER EXIST!!");
                                    return false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //throw e;
                            }
                            return false;
                        },
                        actParams,
                        String.format("ATTRIBUTE HAS NOT CHANGED to [%s]", _value), //Error Message
                        String.format("ATTRIBUTE HAS CHANGED to [%s]", _value), //Log Message
                        _log);
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return rtrnStatus;
    }

    public static int coreWaitTillAttribueNotContainText(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        int rtrnStatus = 1;
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                Map.Entry<String, String > next = (actParams.Current().getListActionParamAsHashMap().size() == 1) ? actParams.Current().getListActionParamAsHashMap().entrySet().iterator().next() : null;
                if (next == null) return 3;
                String _key = next.getKey();
                String _value = next.getValue();
                rtrnStatus = polling(acp -> {
                            ElementSearch es2 = null;
                            LogInfo _log2 = new LogInfoImp();
                            try {
                                es2 = findElement((ActionParameters) acp, _log2);
                                if (isFoundElement(es2)) {
                                    if (!es2.fw_getAttribute(_key).contains(_value)) {
                                        _log.add(String.format(", ATTRIBUTE CHANGE TO NOT CONTAIN TO TEXT CONTAIN %s", _value));
                                        return true;
                                    }
                                } else {
                                    _log.add(String.format(",%s: ELEMENT IS NO LONGER EXIST!", _log2.toString()));
                                    //throw new CoreException("ELEMENT IS NO LONGER EXIST!!");
                                    return false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //throw e;
                            }
                            return false;
                        },
                        actParams,
                        String.format("ATTRIBUTE STILL HAS TEXT [%s]", _value), //Error Message
                        String.format("ATTRIBUTE HAS CHANGED NOT CONTAIN TEXT [%s]", _value), //Log Message
                        _log);
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return rtrnStatus;
    }

    public static int coreWaitTillHasText(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        int rtrnStatus = 1;
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                String _originalTextToBeCheck = actParams.Current().getActionParamAsString();
                if (!es.fw_getText().contains(_originalTextToBeCheck))
                    throw new CoreException(String.format("%s: Initial Text is not the same as %s!", _log.toString(), _originalTextToBeCheck));
                //Polling Check
                rtrnStatus = polling(acp -> {
                            ElementSearch es2 = null;
                            LogInfo _log2 = new LogInfoImp();
                            try {
                                es2 = findElement((ActionParameters)acp, _log2);
                                if (isFoundElement(es2)) {
                                    if (!es2.fw_getText().contains(_originalTextToBeCheck)) {
                                        _log.add(String.format(", TEXT CONTAINS [%s].", _originalTextToBeCheck));
                                        return true;
                                    }
                                }
                                else
                                {
                                    _log.add(String.format(",%s: ELEMENT IS NO LONGER EXIST!", _log2.toString()));
                                    //throw new CoreException("ELEMENT IS NO LONGER EXIST!!");
                                    return false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //throw e;
                            }
                            return false;
                        },
                        actParams,
                        String.format("TEXT DIFFERENT than [%s]", _originalTextToBeCheck),
                        "TEXT has NOT CHANGED.",
                        _log );
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return rtrnStatus;
    }

    public static void coreDragDrop(ActionParameters srcParams, ActionParameters desParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            ElementSearch source = findElement(srcParams, _log);
            ElementSearch target = findElement(desParams, _log);
            if (source.fw_isFound()) {
                _log.add(", Action: Drag Drop");
                if (source.fw_isDisplayed()) {
                    SafeDriver.Current().fw_dragDrop(source, target);
                }
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    public static void coreDragDrop2(String sourceFrame, ActionParameters srcParams, String targetFrame, ActionParameters desParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            if (Common.isNullOrEmpty(sourceFrame))
            { SafeDriver.Current().fw_switchToDefaultContent(); }
            else
            { SafeDriver.Current().fw_switchToFrame(sourceFrame); }
            ElementSearch source = findElement(srcParams, _log);
            if (Common.isNullOrEmpty(targetFrame))
            { SafeDriver.Current().fw_switchToDefaultContent(); }
            else
            { SafeDriver.Current().fw_switchToFrame(targetFrame); }
            ElementSearch target = findElement(desParams, _log);
            if (Common.isNullOrEmpty(sourceFrame))
            { SafeDriver.Current().fw_switchToDefaultContent(); }
            else
            { SafeDriver.Current().fw_switchToFrame(sourceFrame); }
            if (source.fw_isFound()) {
                _log.add(", Action: Drag Drop");
                if (source.fw_isDisplayed()) {
                    SafeDriver.Current().fw_clickAndHoldAndMoveTo(sourceFrame, source, targetFrame, target);
                }
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    public static void coreInitializeDriver(String browserType, String browserName, String browserVersion, String resolution, String remoteHub, String browserPlatform, String proxy) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            _log.add("Action: Initialize Web Driver");
            SafeDriver.init(browserType, browserName, browserVersion, resolution, remoteHub, browserPlatform, proxy, Common.DefaultParameterDirectory);
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    public static void coreTearDown() {
        LogInfo _log = new LogInfoImp();
        try {
            _log.add("Action: QUIT BROWSER");
            SafeDriver.teardown();
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    public static void coreClose() {
        LogInfo _log = new LogInfoImp();
        try {
            _log.add("Action: CLOSE BROWSER");
            SafeDriver.Current().fw_close();
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    /**
     * Takes the screenshot of the browser
     **/

    public static String coreScreenshot() {
        LogInfo _log = new LogInfoImp();
        try {
            _log.add("Action: TAKE SCREENSHOT");
            return SafeDriver.Current().screenshot();
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    public static void coreDeleteCookies() {
        LogInfo _log = new LogInfoImp();
        try {
            _log.add("Action: DELETE COOKIES");
            SafeDriver.Current().fw_deleteAllCookies();
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    /* FOR MULTIPLE ELEMENTS RETURN ONLY */
    public static int coreGetReturnSize(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            ElementSearch object = findElement(actParams, _log);
            if (object.fw_isFound()) {
                return 1;
            }
            else if (object.fw_isMulitpleFound()) {
                _log.add("Action: GET SIZE OF MULTIPLE ELEMENTS FOUND.");
                return object.fw_getReturnSize();
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
    }

    /* FOR MULTIPLE ELEMENTS RETURN ONLY */
    public static List<String> coreGetAttributes(ActionParameters actParams) throws Exception {
        List<String> rtrn = new ArrayList();
        LogInfo _log = new LogInfoImp();
        try {
            SafeDriver.Current().fw_overlayMessage("Finding Multiple Elements...", "green");
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es)) {
                List<String> attribList = actParams.Current().getListActionParamAsString();
                if (es.fw_isMulitpleFound()) {
                    int _size = es.fw_getFoundList().size();
                    String _listAttrib = StringUtils.join(attribList, "|");
                    for (ElementSearch e : es.fw_getFoundList()) {
                        String temp = "";
                        boolean isFirst = true;
                        for (String attrib : attribList) {
                            temp += (isFirst ? "" : "|") + e.fw_getAttribute(attrib);
                            if (isFirst) isFirst = false;
                        }
                        rtrn.add(temp);
                        //e.fw_elementBlink(2);
                        //e.fw_elementHighlightOnly();
                        SafeDriver.Current().fw_overlayMessage(String.format("Attributes [%s]: [%s]!", _listAttrib, temp), "green");
                        SafeDriver.Current().fw_overlayMessageBoxRePosition("green");
                    }
                    _log.add("Action: GET ATTRIBUTES OF MULTIPLE ELEMENTS.");
                    SafeDriver.Current().fw_overlayMessage(String.format("Total: [%s] elements found!", _size), "green");
                    SafeDriver.Current().fw_overlayMessageBoxRePosition("green");
                }
                else //Single Element
                {
                    boolean isFirst = true;
                    String temp = "";
                    for (String attrib : attribList) {
                        temp += (isFirst ? "" : "|") + es.fw_getAttribute(attrib);
                        if (isFirst) isFirst = false;
                    }
                    rtrn.add(temp);
                    _log.add("Action: GET ATTRIBUTES OF THE ELEMENT.");
                }

            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return rtrn;
    }

    /**
     * returns text property of multiple elements
     * @param actParams
     * @return
     * @throws Exception
     */
    public static List<String> coreGetTexts(ActionParameters actParams) throws Exception {
        List<String> rtrn = new ArrayList();
        LogInfo _log = new LogInfoImp();
        try {

            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                _log.add(String.format(", Action: GET TEXT %s", actParams.Current().getMessage()));
                if (!es.fw_isMulitpleFound()) {
                    rtrn.add(es.fw_getText());
                }
                else
                {
                    _log.add(", Action: GET TEXTS FOR ALL ELEMENTS");
                    for (ElementSearch e : es.fw_getFoundList()) {
                        e.fw_elementHighlightOnly();
                        rtrn.add(e.fw_getText());
                    }
                }

            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return rtrn;
    }

    /* FOR MULTIPLE ELEMENTS RETURN ONLY */
    public static void coreClickOnIth(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {

            ElementSearch es = findElement(actParams, _log);
            int index = actParams.Current().getActionParamAsInteger();
            if (es.fw_isFound()) {
                if (index != 1)
                {
                    es.fw_click();
                }
                else
                {
                    throw new CoreException(String.format("%s: OUT OF RANGE INDEX!", _log.toString()));
                }
            } else if (es.fw_isMulitpleFound()) {
                int i = 1;
                for (ElementSearch e : es.fw_getFoundList()) {
                    if (i == index) {
                        _log.add(String.format("Action: CLICK ON THE %sth ELEMENT OF MULTIPLE ELEMENTS.", index));
                        es.fw_elementBlink(2);
                        es.fw_elementHighlightOnly();
                        e.fw_click();
                        break;
                    }
                }
            } else {
                throw new CoreException(String.format("\"%s\": ELEMENT NOT FOUND!\"", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }

    }

    /* FOR MULTIPLE ELEMENTS RETURN ONLY */
    public static void coreSelectIthElement(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        try {
            ElementSearch es = findElement(actParams, _log);
            int index = actParams.Current().getActionParamAsInteger(); //Index Selected
            if (es.fw_isFound()) {

                if (index != 1)
                {
                    es.fw_click();
                }
                else
                {
                    throw new CoreException(String.format("%s: OUT OF RANGE INDEX!", _log.toString()));
                }
            } else if (es.fw_isMulitpleFound()) {
                int i = 1;
                for (ElementSearch e : es.fw_getFoundList()) {
                    if (i == index) {
                        _log.add(String.format("Action: CLICK ON THE %sth ELEMENT OF MULTIPLE ELEMENTS.", index));
                        e.fw_elementBlink(2);
                        e.fw_elementHighlightOnly();
                        e.fw_click();
                        break;
                    }
                }
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }

    }

    public static HashMap<String, Long> coreGetPagePerf() throws Exception {
        return coreGetPagePerf("");
    }


    public static String coreTimeNow() throws Exception {
        String strRslt = "";

        try {
            String scrpt = String.format("var start_time = new Date().getTime(); return start_time;");
            Object obj = SafeDriver.Current().fw_executeJavaScript(scrpt);

            strRslt = obj.toString();

        } catch (Exception ex) {

        }

        return strRslt;
    }

    public static HashMap<String, Long> coreGetPagePerfWithResource(String startTime, String endTime, String info) throws Exception {
        HashMap<String, Long> result = new HashMap<String, Long>();
        String _localEndTime = Long.toString(new Date().getTime());
        try {
            if ((info != null) && !info.isEmpty()) {
                //System.out.println(info + " >> ");
            }
            String scrpt = ""
                    + "var timings = null;"
                    + "var resources = null;"
                    + "$(document).ready(function() {"
                    + "          var performance = window.performance || window.webkitPerformance || window.mozPerformance || window.msPerformance || {};"
                    + "          timings = performance.timing || {};"
                    + "          resources = performance.getEntries();"  // performance.getEntries()

                    + "});"
                    + "return timings;";
            Object obj = SafeDriver.Current().fw_executeJavaScript(scrpt);

            String strRslt = obj.toString();
            strRslt = strRslt.replace("{", "");
            strRslt = strRslt.replace("}", "");
            strRslt = strRslt.replace("[", "");
            strRslt = strRslt.replace("]", "");
            String[] allKV = strRslt.split(",");

            if (!Common.isNumeric(startTime)) {
                startTime = "0";
            }
            result.put(WebPerformance.PERFORMANCE_START.getvalue(), Long.parseLong(startTime));
            if (!Common.isNumeric(_localEndTime)) {
                _localEndTime = "0";
            }
            result.put(WebPerformance.PERFORMANCE_END.getvalue(), Long.parseLong(_localEndTime));

            for (String pair : allKV) {
                String[] parts = pair.split("=");
                String key = parts[0].trim();
                String value = parts[1].trim();

                if (Common.isNumeric(value)) {
                    result.put(key, Long.parseLong(value));
                }
            }
        } catch (Exception ex) {

        }

        return result;
    }

    public static HashMap<String, Long> coreGetPagePerf(String info) throws Exception {
        return coreGetPagePerf("0L", info);
    }

    public static HashMap<String, Long> coreGetPagePerf(String startTime, String info) throws Exception {
        String endTime = Long.toString(new Date().getTime());
        return coreGetPagePerf(startTime, endTime, info);
    }


    public static HashMap<String, Long> coreGetPagePerf(String startTime, String endTime, String info) throws Exception {
        HashMap<String, Long> result = new HashMap<String, Long>();
        String _localEndTime = Long.toString(new Date().getTime());
        try {
            if ((info != null) && !info.isEmpty()) {
            }
            String scrpt = ""
                    + "var timings = null;"
                    + "var t_end = null;"
                    + "var end_time = new Date().getTime();"
                    + String.format("var endTime = '%s =' + end_time;", WebPerformance.PERFORMANCE_END2.getvalue())
                    + "$(document).ready(function() {"
                    + "          var performance = window.performance || window.webkitPerformance || window.mozPerformance || window.msPerformance || {};"
                    + "          timings = performance.timing || {};"
                    + "          t_end = performance.now();"
                    + "          resources = performance.getEntries();"  // performance.getEntries()
                    + "});"
                    + "return timings;";

            Map<String, Object> webTiming = (Map<String, Object>)SafeDriver.Current().fw_executeJavaScript(scrpt);

            if (webTiming.containsKey("toJSON"))
            {
                webTiming.remove("toJSON");
            }

            if (!Common.isNumeric(startTime)) {
                startTime = "0";
            }

            //PREPARE THE RETURN RESULT
            result.put(WebPerformance.PERFORMANCE_START.getvalue(), Long.parseLong(startTime));
            if (!Common.isNumeric(_localEndTime)) {
                _localEndTime = "0";
            }
            result.put(WebPerformance.PERFORMANCE_END.getvalue(), Long.parseLong(_localEndTime));
            webTiming.keySet().stream().filter(key -> Common.isNumeric(webTiming.get(key).toString())).forEach(key -> {
                result.put(key, Long.parseLong(webTiming.get(key).toString()));
            });
        } catch (Exception ex) {
        }
        return result;
    }

    // Click
    public static String coreGetDropDownSelectedValue(ActionParameters actParams) throws Exception {
        LogInfo _log = new LogInfoImp();
        String strStartTime = "";
        String strEndTime = "";
        String returnValue="";
        try {
            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format( "%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                _log.add(String.format(", Action: get selected value %s", actParams.Current().getMessage()));
                strStartTime = Long.toString(new Date().getTime());
                returnValue=es.fw_getDropDownSelectedText();
                strEndTime = Long.toString(new Date().getTime());
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return returnValue;
    }

    public static void coreStoreCaptureObject(ActionParameters actParams) throws Exception
    {
        LogInfo _log = new LogInfoImp();
        try {

            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                if (es.fw_isMulitpleFound())
                    throw new CoreException(String.format("%s: EXPECTED ONE ELEMENT BUT FOUND MULTIPLE ELEMENTS!", _log.toString()));
                String objCaptured = actParams.Current().getActionParamAsString(); //Captured Object's Name
                if (es.fw_isFound()) {
                    es.fw_elementBlink(2);
                    SafeObjectContentStorage.Current().save(objCaptured, es);
                }
                _log.add(String.format(", Action: Save Object %s as %s", actParams.Current().getObjectRepository().getName(), objCaptured));
            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }

    }

    public static boolean coreManualConfirmation(String message) throws Exception {

        boolean bRslt = false;

        try {
            String scrpt = String.format("var r = confirm('%s'); return r;", message);
            Object obj = SafeDriver.Current().executeAsyncScript(scrpt, null);

            bRslt = Boolean.parseBoolean(obj.toString());

        } catch (Exception ex) {

        }

        return bRslt;
    }

    public static void coreSwitchToFrameObject(ActionParameters acp) {
    }

    public static void coreSendKey(Keys k) {
        SafeDriver.Current().fw_sendKeys(k.getUnicodeValue());
    }

    public static void coreGetAllElementsObject()
    {
        List<ElementSearch> lst = SafeDriver.Current().fw_findAllDOMElementsObject();
    }

    public static int getContainTextIndex(ActionParameters actParams) throws Exception{
        int index=0;
        LogInfo _log = new LogInfoImp();
        try {

            ElementSearch es = findElement(actParams, _log);
            if (isFoundElement(es))
            {
                _log.add(String.format(", Action: GET INDEX WITH CONTAIN TEXT %s", actParams.Current().getMessage()));
                if (!es.fw_isMulitpleFound()) { //Single Element
                    index=actParams.Current().getMultiObjectConfiguration().getIndex();
                }
                else //Multiple Elements
                {
                    throw new CoreException(String.format("%s: MULTIPLE ELEMENTS FOUND!", _log.toString()));
//                    _log.add(", Action: FIND CONTENT TEXT INDEX");
//                    int count = 0;
//                    for (ElementSearch e : es.fw_getFoundList()) {
//                        count++;
//                        if (e.fw_getText().contains(actParams.Current().getMultiObjectConfiguration().getContainText())) {
//                            index = count;
//                            break;
//                        }
//                    }
                }

            } else {
                throw new CoreException(String.format("%s: ELEMENT NOT FOUND!", _log.toString()));
            }
        } catch (Exception ex) {
            _log.add(", OUTPUT ERROR!");
            throw ex;
        } finally {
            _log.print(SafeStorage.Current().get("CURRENT_SCENARIO_NAME"));
        }
        return index;
    }
}
