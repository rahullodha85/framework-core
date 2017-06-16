package com.hbcd.utility.entity;

import com.google.gson.annotations.SerializedName;
import com.hbcd.logging.log.Log;
import com.hbcd.utility.common.GenericDataInterface;
import com.hbcd.utility.common.ObjectPropertyGetter;
import com.hbcd.utility.common.ObjectPropertySetter;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ObjectProperties extends DataObjectBase implements Serializable, ObjectPropertyGetter, ObjectPropertySetter {

    /**
     *
     */
    private final int DEFAULT_EXPLICIT_WAIT_TIME = 1;
    private static final long serialVersionUID = 1L;
    @SerializedName("ObjectName")
    private String _objName = "";
    @SerializedName("ID")
    private String _id = "";
    @SerializedName("Name")
    private String _name = "";
    @SerializedName("ClassName")
    private String _className = "";
    @SerializedName("CssSelector")
    private String _cssSelector = "";
    @SerializedName("LinkText")
    private String _linkText = "";
    @SerializedName("PartialLinkText")
    private String _partialLinkText = "";
    @SerializedName("TagName")
    private String _tagName = "";
    @SerializedName("XPath")
    private String _xPath = "";
    @SerializedName("IsParent")
    private int _isParent = 0;
    @SerializedName("ithInMultiple")
    private int _ith = 0;
    @SerializedName("ContainsText")
    private String _containsText = "";
    @SerializedName("AttributeKey1")
    private String _attributeKey1 = "";
    @SerializedName("AttributeValue1")
    private String _attributeValue1 = "";
    @SerializedName("AttributeKey2")
    private String _attributeKey2 = "";
    @SerializedName("AttributeValue2")
    private String _attributeValue2 = "";
    private String child_text = "";
    private String _attribute = "";
    @SerializedName("Value")
    private String _value = "";
    @SerializedName("SQL")
    private String _sql = "";
    @SerializedName("ReferenceDataId")
    private String _referenceDataId = "";
    @SerializedName("UserDefinedMaxWaitTime")
    private int _userDefinedExplicitWaitTime = DEFAULT_EXPLICIT_WAIT_TIME;
    @SerializedName("UserDefineThreadWaitTime")
    private int _userDefinedThreaddWaitTime = 0;
    @SerializedName("IsSelectedWithNotVisible")
    private int _isSelectedWithNotVisible = 0; //always display
    @SerializedName("IsSelectedWithDisable")
    private int _isSelectedWithDisable = 0; //always enable

    //Internally Use ONLY
    private String _originalStyle = "";
    private boolean _isUsed = false;
    private Date _lastUsed = new Date();

//    protected
    public ObjectProperties() {
        _startColumnName = "ObjectName";
    }

    public ObjectProperties(String objName) {

        _startColumnName = "ObjectName";
        _objName = objName;
    }

    public ObjectProperties(String id, String nm, String cc) {
        _startColumnName = "ObjectName";
        _id = id;
        _name = nm;
        _className = cc;
    }

    //CONSTRUCTOR
    //Build ObjectRepository from Excel base on List of PropertyIndex Key/Value/Type read from each cell
    public ObjectProperties(int keyIndex, ArrayList<PropertyIndex> data) {
        _startColumnName = "ObjectName";
        ObjectProperties child = null;
        for (PropertyIndex i : data) {
            switch (i.getIndex()) {
                case 0:
                    this.setObjectName(i.getValueAsString());
                    break;
                case 1:
                    this.setID(i.getValueAsString());
                    break;
                case 2:
                    this.setName(i.getValueAsString());
                    break;
                case 3:
                    this.setClassName(i.getValueAsString());
                    break;
                case 4:
                    this.setCssSelector(i.getValueAsString());
                    break;
                case 5:  //CssSelector2
                    break;
                case 6:  //CssSelector3
                    break;
                case 7:
                    this.setLinkText(i.getValueAsString());
                    break;
                case 8:
                    this.setPartialLinkText(i.getValueAsString());
                    break;
                case 9:
                    this.setTagName(i.getValueAsString());
                    break;
                case 10:
                    this.setXPath(i.getValueAsString());
                    break;
                case 11:
                    this.setIth(i.getValueAsInt());
                    break;
                case 12:
                    this.setContainsText(i.getValueAsString());
                    break;
                case 13:
                    this.setAttributeKey1(i.getValueAsString());
                    break;
                case 14:
                    this.setAttributeValue1(i.getValueAsString());
                    break;
                case 15:
                    this.setAttributeKey2(i.getValueAsString());
                    break;
                case 16:
                    this.setAttributeValue2(i.getValueAsString());
                    break;
                case 17:
                    this.setIsParent(i.getValueAsInt());
                    break;
                case 18:
                    this.setValue(i.getValueAsString());
                    break;
                case 19:
                    this.setSQL(i.getValueAsString());
                    break;
                case 20:
                    this.setUserDefinedExplicitWaitTime(i.getValueAsInt());
                    break;
                case 21:
                    this.setUserDefinedThreadWaitTime(i.getValueAsInt());
                    break;
                case 22:
                    this.setReferenceDataId(i.getValueAsString());
                    break;
                case 23: //isSelectedWithNotVisible
                    this.setIsSelectedWithNotVisible(i.getValueAsInt());
                    break;
                case 24: //isSelectedWithNotEnable
                    this.setIsSelectedWithDisable(i.getValueAsInt());
                    break;
                case 25:
                    break;
            }

            if (this.getIsParent() == 1) {
                if (child == null) {
                    child = new ObjectProperties();
                }

                switch (i.getIndex()) {
                    case 26:
                        child.setID(i.getValueAsString());
                        break;
                    case 27:
                        child.setName(i.getValueAsString());
                        break;
                    case 28:
                        child.setClassName(i.getValueAsString());
                        break;
                    case 29:
                        child.setCssSelector(i.getValueAsString());
                        break;
                    case 30:  //CssSelector2
                        break;
                    case 31:  //CssSelector3
                        break;
                    case 32:
                        child.setLinkText(i.getValueAsString());
                        break;
                    case 33:
                        child.setPartialLinkText(i.getValueAsString());
                        break;
                    case 34:
                        child.setTagName(i.getValueAsString());
                        break;
                    case 35:
                        child.setXPath(i.getValueAsString());
                        break;
                    case 36:
                        child.setIth(i.getValueAsInt());
                        break;
                    case 37:
                        child.setContainsText(i.getValueAsString());
                        break;
                    case 38:
                        child.setAttributeKey1(i.getValueAsString());
                        break;
                    case 39:
                        child.setAttributeValue1(i.getValueAsString());
                        break;
                    case 40:
                        child.setAttributeKey2(i.getValueAsString());
                        break;
                    case 41:
                        child.setAttributeValue2(i.getValueAsString());
                        break;
                    case 42:
                        child.setValue(i.getValueAsString());
                        break;
                    case 43:
                        child.setSQL(i.getValueAsString());
                        break;
                    case 44:
                        child.setUserDefinedExplicitWaitTime(i.getValueAsInt());
                        break;
                    case 45:
                        child.setUserDefinedThreadWaitTime(i.getValueAsInt());
                        break;
                    case 46:
                        child.setReferenceDataId(i.getValueAsString());
                        break;
                    case 47: //isSelectedWithNotVisible
                        child.setIsSelectedWithNotVisible(i.getValueAsInt());
                        break;
                    case 48: //isSelectedWithDisable
                        child.setIsSelectedWithDisable(i.getValueAsInt());
                        break;
                    case 49:
                        break;
                }
            }
        }

        if ((this != null) && (child != null)) {
            this.setChild(child);
        }
    }

    public void PrintObject() {
        Log.Info(String.format("ObjectName: %s\nID: %s\nName: %s\nClassName: %s\nCssSelector: %s\nLinkText: %s\nPartialLinkText: %s\nTagName: %s\nXPath: %s\nIsParent: %s\nValue: %s\nSQL: %s\n"
                , this.getName()
                , this.getID()
                , this.getName()
                , this.getClassName()
                , this.getCssSelector()
                , this.getLinkText()
                , this.getPartialLinkText()
                , this.getTagName()
                , this.getXPath()
                , this.getIsParent()
                , this.getValue()
                , this.getSQL()));
    }

    public ObjectProperties cloneObject() {
        ObjectProperties rtrnObj = new ObjectProperties();

        rtrnObj.setObjectName(this.getObjectName());
        rtrnObj.setID(this.getID());
        rtrnObj.setName(this.getName());
        rtrnObj.setClassName(this.getClassName());
        rtrnObj.setCssSelector(this.getCssSelector());
        rtrnObj.setLinkText(this.getLinkText());
        rtrnObj.setPartialLinkText(this.getPartialLinkText());
        rtrnObj.setTagName(this.getTagName());
        rtrnObj.setXPath(this.getXPath());
        rtrnObj.setIth(this.getIth());
        rtrnObj.setContainsText(this.getContainsText());
        rtrnObj.setAttributeKey1(this.getAttributeKey1());
        rtrnObj.setAttributeValue1(this.getAttributeValue1());
        rtrnObj.setAttributeKey2(this.getAttributeKey2());
        rtrnObj.setAttributeValue2(this.getAttributeValue2());
        rtrnObj.setIsParent(this.getIsParent());
        rtrnObj.setValue(this.getValue());
        rtrnObj.setSQL(this.getSQL());
        rtrnObj.setUserDefinedExplicitWaitTime(this.getUserDefinedExplicitWaitTime());
        rtrnObj.setUserDefinedThreadWaitTime(this.getUserDefinedThreadWaitTime());
        rtrnObj.setReferenceDataId(this.getReferenceDataId());
        rtrnObj.setIsSelectedWithNotVisible(this.getIsSelectedWithNotVisible());
        rtrnObj.setIsSelectedWithDisable(this.getIsSelectedWithDisable());

        if ((this.child != null) && (this.getIsParent() == 1)) {
            rtrnObj.child = this.child.cloneObject();  //recursive
        }
        return rtrnObj;
    }

    private void reset()
    {
        _objName = "";
        _id = "";
        _name = "";
        _className = "";
        _cssSelector = "";
        _linkText = "";
        _partialLinkText = "";
        _tagName = "";
        _xPath = "";
        _isParent = 0;
        _ith = 0;
        _containsText = "";
        _attributeKey1 = "";
        _attributeValue1 = "";
        _attributeKey2 = "";
        _attributeValue2 = "";
        child_text = "";
        _attribute = "";
        _value = "";
        _sql = "";
        _originalStyle = "";
        _isUsed = false;
        _lastUsed = new Date();
        _userDefinedExplicitWaitTime=DEFAULT_EXPLICIT_WAIT_TIME;
        _userDefinedThreaddWaitTime=0;
        _referenceDataId="";
        _isSelectedWithNotVisible=0;
        _isSelectedWithDisable=0;
    }

    public String getOriginalStyle() {
        return _originalStyle;
    }

    public ObjectProperties setOriginalStyle(String os) {
        _originalStyle = os;
        return this;
    }

    @XmlElement(name = "ObjectName")
    public String getObjectName() {
        return _objName;
    }

    public ObjectProperties setObjectName(String on) {
        _objName = on;
        return this;
    }

    @XmlElement(name = "ID")
    public String getID() {
        return _id;
    }

    public ObjectProperties setID(String id) {
        _id = id;
        return this;
    }

    @XmlElement(name = "Name")
    public String getName() {
        return _name;
    }

    public ObjectProperties setName(String name) {
        _name = name;
        return this;
    }

    @XmlElement(name = "ClassName")
    public String getClassName() {
        return _className;
    }

    public ObjectProperties setClassName(String cn) {
        _className = cn;
        return this;
    }

    @XmlElement(name = "CssSelector")
    public String getCssSelector() {
        return _cssSelector;
    }

    public ObjectProperties setCssSelector(String cs) {
        _cssSelector = cs;
        return this;
    }

    @XmlElement(name = "LinkText")
    public String getLinkText() {
        return _linkText;
    }

    public ObjectProperties setLinkText(String lt) {
        _linkText = lt;
        return this;
    }

    @XmlElement(name = "PartialLinkText")
    public String getPartialLinkText() {
        return _partialLinkText;
    }

    public ObjectProperties setPartialLinkText(String pl) {
        _partialLinkText = pl;
        return this;
    }

    @XmlElement(name = "TagName")
    public String getTagName() {
        return _tagName;
    }

    public ObjectProperties setTagName(String t) {
        _tagName = t;
        return this;
    }

    @XmlElement(name = "XPath")
    public String getXPath() {
        return _xPath;
    }

    public ObjectProperties setXPath(String x) {
        _xPath = x;
        return this;
    }

    @XmlElement(name = "ithInMultiple", nillable = true)
    public int getIth() {
        return _ith;
    }

    public ObjectProperties setIth(int i) {
        _ith = i;
        return this;
    }


    @XmlElement(name = "IsParent")
    public int getIsParent() {
        return _isParent;
    }

    public ObjectProperties setIsParent(int i) {
        _isParent = i;
        return this;
    }

//	public boolean IsParent ()
//	{
//		return (_isParent != 0);
//	}

    @XmlElement(name = "ContainsText")
    public String getContainsText() {
        return _containsText;
    }

    public ObjectProperties setContainsText(String ct) {
        _containsText = ct;
        return this;
    }

    @XmlElement(name = "AttributeKey1")
    public String getAttributeKey1() {
        return _attributeKey1;
    }

    public ObjectProperties setAttributeKey1(String ak1) {
        _attributeKey1 = ak1;
        return this;
    }

    @XmlElement(name = "AttributeValue1")
    public String getAttributeValue1() {
        return _attributeValue1;
    }

    public ObjectProperties setAttributeValue1(String av1) {
        _attributeValue1 = av1;
        return this;
    }

    @XmlElement(name = "AttributeKey2")
    public String getAttributeKey2() {
        return _attributeKey2;
    }

    public ObjectProperties setAttributeKey2(String ak2) {
        _attributeKey2 = ak2;
        return this;
    }

    @XmlElement(name = "AttributeValue2")
    public String getAttributeValue2() {
        return _attributeValue2;
    }

    public ObjectProperties setAttributeValue2(String av2) {
        _attributeValue2 = av2;
        return this;
    }

    @XmlElement(name = "Value")
    public String getValue() {
        return _value;
    }

    public ObjectProperties setValue(String v) {
        _value = v;
        return this;
    }

    @XmlElement(name = "SQL")
    public String getSQL() {
        return _sql;
    }

     public ObjectProperties setSQL(String s) {
        _sql = s;
         return this;
    }

    @XmlElement(name = "ReferenceDataId")
    public String getReferenceDataId() {
        return _referenceDataId;
    }

    public ObjectProperties setReferenceDataId(String dt) {
        _referenceDataId = dt;
        return this;
    }

    public int getIsSelectedWithNotVisible() {
        return _isSelectedWithNotVisible;
    }

    public ObjectProperties setIsSelectedWithNotVisible(int isSet) {
        _isSelectedWithNotVisible = isSet;
        return this;
    }

    public int getIsSelectedWithDisable() {
        return _isSelectedWithDisable;
    }

    public ObjectProperties setIsSelectedWithDisable(int isSet) {
        _isSelectedWithDisable = isSet;
        return this;
    }

    public ObjectProperties set (int i) {
        _isParent = i;
        return this;
    }

    public String getProperty(String subPro) {
        return null;
    }

    @XmlElement(name = "Child", nillable = true)
    @SerializedName("Child")
    public ObjectProperties child;

    @Override
    public ObjectProperties setChild(Object o) {
        child = (ObjectProperties) o;
        return child;
    }

    @XmlElement(name = "UserDefinedMaxWaitTime")
    public int getUserDefinedExplicitWaitTime() { return _userDefinedExplicitWaitTime; }

    @Override
    public ObjectProperties setUserDefinedExplicitWaitTime(int mt) {
        _userDefinedExplicitWaitTime = mt;
        return this;
    }

    @XmlElement(name = "UserDefineThreadWaitTime")
    public int getUserDefinedThreadWaitTime() { return _userDefinedThreaddWaitTime; }

    @Override
    public ObjectProperties setUserDefinedThreadWaitTime(int tt) {
        _userDefinedThreaddWaitTime = tt;
        return this;
    }

    @Override
    public String key() {
        return _objName;
    }

    @Override
    public void print() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Log.Info(String.format("OBJECT: \"%s\"|%s|%s", _objName, (_isUsed ? "YES" : "NO"), df.format(_lastUsed)));
    }

    public void setIsUsed()
    {
        _lastUsed = new Date();
        _isUsed = true;
    }

    public boolean isUsed()
    {
        return _isUsed;
    }

    public void clear()
    {
        reset();
    }


    //BUILDER PATTERN
    //ObjectProperties Private Constructor
    private ObjectProperties(Builder builder) {
        
    }

    public static class Builder{

        public Builder() {

        }

        public Builder ID(String id) {
            return this;
        }

        public ObjectProperties build() {
            return new ObjectProperties(this);
        }
    }

    //END BUILDER PATTERN

}
