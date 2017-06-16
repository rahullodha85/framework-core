package com.hbcd.scripting.core.fluentInterface;

import com.hbcd.scripting.core.ApplyObjectAction;

import java.util.List;

public interface MultiObjectsAction {

    List<String> getAttributes(String... attribList) throws Exception;

    List<String> getText(String... attribList) throws Exception;

//    void click(int ithIndex) throws Exception;

    int size() throws Exception;

    ObjectAction_CommonAction select(int index) throws Exception;

    ObjectAction_CommonAction selectRandom() throws Exception;

    ObjectAction_CommonAction selectItemContainText(String text) throws Exception;

    int findItemIndexContainText(String text) throws Exception;

}
