package com.hbcd.common.service;

import com.hbcd.utility.entity.ObjectProperties;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Repository")    //work
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectRepositoryDataService extends DataServiceBase<ObjectProperties> {

    public ObjectRepositoryDataService() {
        _myList = new ArrayList<ObjectProperties>();
        _allowRepetition = false;
        _startExcelColumnName = "ObjectName";
    }

//    @XmlElementWrapper(name = "Objects")    //work
//    @XmlElement(name = "Object")
//    public List<ObjectProperties> getList() {
//        if (_myList == null) {
//            _myList = new ArrayList<ObjectProperties>();
//        }
//        return _myList;
//    }

//    public void setList(List<ObjectProperties> _l) {
//        _myList = _l;
//    }

    @Override
    public void print() {
        getList().stream().forEach(o -> {if (o.isUsed()) { o.print(); } });
    }

}
