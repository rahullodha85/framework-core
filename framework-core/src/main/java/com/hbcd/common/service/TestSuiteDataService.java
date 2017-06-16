package com.hbcd.common.service;

import com.hbcd.utility.entity.ObjectTestScript;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "TestScripts")    //work
@XmlAccessorType(XmlAccessType.FIELD)
public class TestSuiteDataService extends DataServiceBase<ObjectTestScript> {

    public TestSuiteDataService()
    {
        _allowRepetition = true; //allow repeated test script
        _startExcelColumnName = "ID";
    }

//    public void setList(List<ObjectTestScript> _l) {
//        _myList = _l;
//    }

}
