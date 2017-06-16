package com.hbcd.scripting.core.fluentInterface;

import com.hbcd.scripting.core.ApplyObjectAction_DragDrop_Level1;
import com.hbcd.utility.entity.ObjectProperties;

public interface ObjectAction_CommonAction {
    ObjectAction_CommonAction_Performance click() throws Exception;

    ObjectAction_CommonAction_Performance javascriptClick() throws Exception;

    ObjectAction_CommonAction_Performance submit() throws Exception;

    ObjectAction_CommonAction_Performance enter() throws Exception;

    void clear() throws Exception;

    void input(String data) throws Exception;

    void javascriptInput(String data) throws Exception;

    void inputPresetData() throws Exception;

    void select(int ith) throws Exception;

    void selectPresetValue() throws Exception;

    void selectByValue(String v) throws Exception;

    void select(String v) throws Exception;

    ObjectActionLevel2 getText() throws Exception;

    ObjectActionLevel2 getAttribute(String attrb) throws Exception;

    boolean isDisplayed() throws Exception;

    boolean isEnabled() throws Exception;

    boolean isPresent() throws Exception;

    boolean isPresentDisplayedEnabled() throws Exception;

    boolean isNotPresent() throws Exception;

    boolean validateWithPresetData() throws Exception;

    void hover() throws Exception;

    void dragDrop(String objNm) throws Exception;

    public ApplyObjectAction_DragDrop_Level1 dragFromSourceFrame(String frameNameOrId) throws Exception;

    void doubleClick() throws Exception;

    void mouseClick() throws Exception;

    void javascriptAlterStyle(String data) throws Exception;

    String getDropDownSelectedValue() throws Exception;

    void storeAs(String name) throws Exception;

    ObjectAction_CommonAction hasChildObject(String cObjName) throws Exception;
    ObjectAction_CommonAction hasChildObject(ObjectProperties cObjName) throws Exception;

    MultiObjectsAction hasChildObjects(String cObjName) throws Exception;
    MultiObjectsAction hasChildObjects(ObjectProperties cObjName) throws Exception;


}