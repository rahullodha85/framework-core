package com.hbcd.scripting.core;

import com.hbcd.common.utility.ObjectPropertyUtility;
import com.hbcd.core.genericfunctions.GenericFunctions;
import com.hbcd.logging.log.Log;
import com.hbcd.scripting.core.fluentInterface.MultiObjectsAction;
import com.hbcd.scripting.core.fluentInterface.ObjectActionLevel2;
import com.hbcd.scripting.core.fluentInterface.ObjectAction_CommonAction;
import com.hbcd.scripting.core.fluentInterface.ObjectAction_CommonAction_Performance;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.common.Type.Level2Type;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectProperties;
import com.hbcd.utility.entity.ObjectSearchParameters;
import com.hbcd.utility.entity.SelectMultiObjectConfiguration;

import java.util.Date;
import java.util.concurrent.Callable;


public class ApplyObjectAction_Common extends BaseApplyObjectAction implements
        ObjectAction_CommonAction {

    public ApplyObjectAction_Common(ActionParameters ap) {
        super(ap);
    }


    public ObjectAction_CommonAction_Performance submit() throws Exception {
        try {

            String time[] = GenericFunctions.coreSubmit(_parameters);
            String et = Long.toString(new Date().getTime());
            return new ApplyObjectAction_Common_Performance(_parameters, time[0], time[1]);

        } catch (Exception e) {
            throw e;
        }
    }


    public ObjectAction_CommonAction_Performance enter() throws Exception {
        try {
            String time[] = GenericFunctions.coreEnter(_parameters);
            return new ApplyObjectAction_Common_Performance(_parameters, time[0], time[1]);
        } catch (Exception e) {
            throw e;
        }
    }

    public ObjectAction_CommonAction_Performance click() throws Exception {
        try {

//            try {
//                String handleEventClass = ConfigurationLoader.getValue(ApplicationSetup.get(Setting.SITE)+".HANDLE_SPECIAL_EVENT");
//                if (com.hbcd.utility.helper.Common.isNotNullAndNotEmpty(handleEventClass))
//                {
//                    Class<?> cn = Class.forName(handleEventClass);
//                    Callable<Boolean> specialHandler = (Callable<Boolean>) cn.newInstance();
//                    _parameters.setHandle(() -> {
//                        try {
//                            return specialHandler.call();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        return false;
//                    });
//                }
//            }
//            catch (Exception ex)
//            {
//                ex.printStackTrace();
//                //Supress Log - Ignor if no special handler is implemented
//            }
            String time[] = GenericFunctions.coreClick(_parameters);
            String et = Long.toString(new Date().getTime());
            return new ApplyObjectAction_Common_Performance(_parameters, time[0], time[1]);
        } catch (Exception e) {
            throw e;
        }
    }

    public void hover() throws Exception {
        try {
            GenericFunctions.coreHover(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    public void doubleClick() throws Exception {
        try {
            GenericFunctions.coreDoubleClick(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void mouseClick() throws Exception {
        try {
            GenericFunctions.coreMouseClick(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    public void dragDrop(String objNm) throws Exception {
        try {
            ActionParameters destination = new ActionParameters();
            destination.add(new ObjectSearchParameters().setType(1).setObjectRepository(ObjectPropertyUtility.getObjectFromService(objNm)));
            GenericFunctions.coreDragDrop(_parameters, destination);
        } catch (Exception e) {
            throw e;
        }
    }

    public ApplyObjectAction_DragDrop_Level1 dragFromSourceFrame(String frameNameOrId) throws Exception {
        try {
            return new ApplyObjectAction_DragDrop_Level1(_parameters, frameNameOrId);
        } catch (Exception e) {
            throw e;
        }
    }

    public ObjectAction_CommonAction_Performance javascriptClick() throws Exception {
        try {
            String time[] = GenericFunctions.coreJavascriptClick(_parameters);
            String et = Long.toString(new Date().getTime());
            return new ApplyObjectAction_Common_Performance(_parameters, time[0], time[1]);  //Issue Here
        } catch (Exception e) {
            throw e;
        }
    }

    public void clear() throws Exception {
        try {
            GenericFunctions.coreClear(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }


    public void input(String data) throws Exception {
        try {
            _parameters.Current().setActionParamAsString(data);
            GenericFunctions.coreEnterText(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }


    public void inputPresetData() throws Exception {
        try {
            _parameters.Current().setActionParamAsString(_parameters.Current().getObjectRepository().getValue());
            GenericFunctions.coreEnterText(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }


    public void select(int ith) throws Exception {
        try {
            _parameters.Current().setActionParamAsInteger(ith);
            GenericFunctions.coreSelectByIndex(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }


    public void selectPresetValue() throws Exception {
        try {
            _parameters.Current().setActionParamAsInteger(Integer.parseInt(_parameters.Current().getObjectRepository().getValue()));
            GenericFunctions.coreSelectByIndex(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }


    public void selectByValue(String v) throws Exception {
        try {
            if (_parameters.Current().isSearchable()) {
                _parameters.Current().setActionParamAsString(v);
                GenericFunctions.coreSelectByValue(_parameters);
            }
        } catch (Exception e) {
            throw e;
        }
    }


    public ObjectActionLevel2 getText() throws Exception {
        try {
            if (_parameters.Current().isSearchable()) {
                return new ApplyObjectActionLevel2(_parameters, Level2Type.TEXT);
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }


    public ObjectActionLevel2 getAttribute(String attrb) throws Exception {
        try {
            if (_parameters.Current().isSearchable()) {
                return new ApplyObjectActionLevel2(_parameters, Level2Type.ATTRIBUTE, attrb);
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }


    public boolean isDisplayed() throws Exception {
        try {
            //new ObjectSearchParameters().setType(_type).setStoredObjectName(_localObjectSearchName).setObjectRepository(_localObject).setMultiObjectConfiguration(_selectMultiObjectConfig)
            return GenericFunctions.coreIsDisplayed(_parameters);

        } catch (Exception e) {
            throw e;
        }
    }


    public boolean isEnabled() throws Exception {
        try {
            //new ObjectSearchParameters().setType(_type).setStoredObjectName(_localObjectSearchName).setObjectRepository(_localObject).setMultiObjectConfiguration(_selectMultiObjectConfig)
            return GenericFunctions.coreIsEnabled(_parameters);

        } catch (Exception e) {
            throw e;
        }
    }


    public boolean isPresent() throws Exception {
        try {
            return GenericFunctions.coreIsPresent(_parameters);

        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isNotPresent() throws Exception {
        try {
            return GenericFunctions.coreIsNotPresent(_parameters);

        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isPresentDisplayedEnabled() throws Exception {
        try {
            return GenericFunctions.coreIsPresentDisplayedEnabled(_parameters);

        } catch (Exception e) {
            throw e;
        }
    }


    public void select(String text) throws Exception {

        try {

            if (_parameters.Current().isSearchable()) {
                _parameters.Current().setActionParamAsString(text);
                GenericFunctions.coreSelectByVisibleText(_parameters);
            }
        } catch (Exception e) {
            throw e;
        }

    }


    public boolean validateWithPresetData() throws Exception {
        try {
            if (_parameters.Current().isSearchable()) {
                _parameters.Current().setActionParamAsString(_parameters.Current().getObjectRepository().getValue());
                return GenericFunctions.coreValidateText(_parameters);
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }


    public void javascriptInput(String data) throws Exception {
        try {
            _parameters.Current().setActionParamAsString(data);
            GenericFunctions.coreJavascriptEnterText(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }


    public void javascriptAlterStyle(String data) throws Exception {
        try {
            _parameters.Current().setActionParamAsString(data);
            GenericFunctions.coreJavascriptAlter(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String getDropDownSelectedValue() throws Exception {
        try{
            return GenericFunctions.coreGetDropDownSelectedValue(_parameters);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public void storeAs(String name) throws Exception {
        try{
            _parameters.Current().setActionParamAsString(name);
            GenericFunctions.coreStoreCaptureObject(_parameters);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public ObjectAction_CommonAction hasChildObject(String cObjName) throws Exception {
        try {
            ObjectProperties _childObj = ObjectPropertyUtility.getObjectFromService(cObjName);
            if (_childObj == null) {
                throw new Exception(String.format("Object Name: %s - DOES NOT EXIST : Please verify your [EXCEL] Repository.", cObjName));
            }
            _parameters.add(new ObjectSearchParameters().setObjectRepository(_childObj));
            return new ApplyObjectAction(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ObjectAction_CommonAction hasChildObject(ObjectProperties obj) throws Exception {
        try {
            if (obj == null) {
                throw new Exception(String.format("Object Name: %s - DOES NOT EXIST : Please verify your [EXCEL] Repository.", obj.getName()));
            }
            _parameters.add(new ObjectSearchParameters().setObjectRepository(obj));
            return new ApplyObjectAction(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MultiObjectsAction hasChildObjects(String cObjName) throws Exception {
        try {
            ObjectProperties _childObj = ObjectPropertyUtility.getObjectFromService(cObjName);
            if (_childObj == null) {
                throw new Exception(String.format("Object Name: %s - DOES NOT EXIST : Please verify your [EXCEL] Repository.", cObjName));
            }
            _parameters.add(new ObjectSearchParameters().setObjectRepository(_childObj).setMultiObjectConfiguration(new SelectMultiObjectConfiguration()));
            return new ApplyMultiObjectsAction(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MultiObjectsAction hasChildObjects(ObjectProperties obj) throws Exception {

        try {
            if (obj == null) {
                throw new Exception(String.format("Object Name: %s - DOES NOT EXIST : Please verify your [EXCEL] Repository.", obj.getName()));
            }
            _parameters.add(new ObjectSearchParameters().setObjectRepository(obj).setMultiObjectConfiguration(new SelectMultiObjectConfiguration()));
            return new ApplyMultiObjectsAction(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }


}
