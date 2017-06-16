package com.hbcd.common.service;

import com.hbcd.utility.testscriptdata.GiftCard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "GiftCardList")
@XmlAccessorType(XmlAccessType.FIELD)
public class GiftcardService extends DataServiceBase<GiftCard> {

//    @XmlElement(name = "giftcard")
//    public List<GiftCard> getList() {
//        if (_myList == null) {
//            _myList = new ArrayList<GiftCard>();
//        }
//        return _myList;
//    }

    public void setList(List<GiftCard> _l) {
        _myList = _l;
    }

    @Override
    public GiftCard find(String keyID) {
        return null;
    }

    public void setFileAttribute(BasicFileAttributes attrb) {
        _fileAttrib = attrb;
    }

}
