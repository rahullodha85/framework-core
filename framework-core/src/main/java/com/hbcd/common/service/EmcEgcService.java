package com.hbcd.common.service;

import com.hbcd.utility.testscriptdata.GiftCard;
import com.hbcd.utility.testscriptdata.ObjectEmcEgcScript;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "CreditCardList")
@XmlAccessorType(XmlAccessType.FIELD)

public class EmcEgcService extends DataServiceBase<ObjectEmcEgcScript> {

//    @XmlElement(name = "GiftCard")
//    public List<ObjectEmcEgcScript> getList() {
//        if (_myList == null) {
//            _myList = new ArrayList<ObjectEmcEgcScript>();
//        }
//        return _myList;
//    }

    public void setList(List<ObjectEmcEgcScript> _l) {
        _myList = _l;
    }

//    @Override
//    public GiftCard find(String keyID) {
//        return null;
//    }

    public void setFileAttribute(BasicFileAttributes attrb) {
        _fileAttrib = attrb;
    }

}
