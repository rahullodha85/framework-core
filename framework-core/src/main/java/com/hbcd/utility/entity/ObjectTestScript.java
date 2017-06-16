package com.hbcd.utility.entity;

import com.hbcd.utility.common.GenericDataInterface;
import com.google.gson.annotations.SerializedName;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;

public class ObjectTestScript extends DataObjectBase implements Serializable, GenericDataInterface  {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SerializedName("ID")
    String _id = "";
    @SerializedName("SetToRun")
    int _isSetToRun = 0;
    @SerializedName("ScriptClassName")
    String _scriptClassName = "";
    @SerializedName("Description")
    String _description = "";
    @SerializedName("DataReferenceID")
    String _dataReferenceID = "";


    public ObjectTestScript() {
        _startColumnName = "ID";
    }

    public ObjectTestScript(String id, int isRun, String classname, String desc, String data) {
        _startColumnName = "ID";
        _id = id;
        _isSetToRun = isRun;
        _scriptClassName = classname;
        _description = desc;
        _dataReferenceID = data;
    }

    //CONSTRUCTOR
    //Build ObjectTestScript from Excel base on List of PropertyIndex Key/Value/Type read from each cell
    public ObjectTestScript(int keyIndex, ArrayList<PropertyIndex> data) {
        _startColumnName = "ID";
        for (PropertyIndex i : data) {
            switch (i.getIndex()) {
                case 0:
                    this.setID(i.getValueAsString());
                    break;
                case 1:
                    this.setIsSetToRun(i.getValueAsInt());
                    break;
                case 2:
                    this.setScriptClassName(i.getValueAsString()); //data.get(2).toString()
                    break;
                case 3:
                    this.setDescription(i.getValueAsString());
                    break;
                case 4:
                    this.setDataReferenceID(i.getValueAsString());
                    break;
            }
        }
    }

    @XmlElement(name = "ID")
    public String getID() {
        return _id;
    }

    public void setID(String id) {
        _id = id;
    }


    @XmlElement(name = "SetToRun")
    public int getIsSetToRun() {
        return _isSetToRun;
    }

    public void setIsSetToRun(int s) {
        _isSetToRun = s;
    }

    public boolean IsSetToRun() {
        return (_isSetToRun > 0);
    }

    public int getToRun() {
        return _isSetToRun;
    }

    @XmlElement(name = "ScriptClassName")
    public String getScriptClassName() {
        return _scriptClassName;
    }

    public void setScriptClassName(String scn) {
        _scriptClassName = scn;
    }

    @XmlElement(name = "Description")
    public String getDescription() {
        return _description;
    }

    public void setDescription(String d) {
        _description = d;
    }

    @XmlElement(name = "DataReferenceID")
    public String getDataReferenceID() {
        return _dataReferenceID;
    }

    public void setDataReferenceID(String d) {
        _dataReferenceID = d;
    }

    public ObjectTestScript clone() {
        return new ObjectTestScript(_id, _isSetToRun, _scriptClassName, _description, _dataReferenceID);
    }

    @Override
    public String key() {
        return _scriptClassName;
    }

    public void print() {
        System.out.println(String.format("ID : %s ISRUN: %s CLASSNAME: %s DESC: %s", _id, _isSetToRun, _scriptClassName, _description));
    }
}
