package com.hbcd.utility.entity;

import com.hbcd.utility.configurationsetting.ConfigurationLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ephung on 11/10/2015.
 */
public class ObjectSearchParameters {
    int _type = 0;  //0: Default [single Object]   1: Multi Object   2: StorageObject   3: Storage Object with Multi Object
    String _storedObjectName = "";
    ObjectProperties _obj = null;
    SelectMultiObjectConfiguration _multiObjConfig = null;
    String _actionParamAsString = ""; //Multi Purpose String Parameter to pass to the function
    List<String> _listActionParamAsString = new ArrayList<>();
    Map<String, String> _listActionParamAsHashMap = new HashMap<String, String>();
    int _actionParamAsInteger = 0;
    int _blink = 2;
    boolean _isHighlight = true;
    boolean _isScrollToElement = true;
    boolean _isLast = false;

    public ObjectSearchParameters()
    {
        try
        {
            String hightLight = "";
            String scroll = "";
            String numberBlink = "2";
            hightLight = ConfigurationLoader.getConfiguration("HIGHTLIGHT_ELEMENT");
            scroll = ConfigurationLoader.getConfiguration("SCROLL_TO_ELEMENT");
            numberBlink = ConfigurationLoader.getConfiguration("BLINK_ELEMENT");
            _isHighlight = com.hbcd.utility.helper.Common.isNullOrEmpty(hightLight)? true : Boolean.parseBoolean(hightLight) ;
            _isScrollToElement = com.hbcd.utility.helper.Common.isNullOrEmpty(scroll)? true : Boolean.parseBoolean(scroll) ;
            _blink = com.hbcd.utility.helper.Common.isNullOrEmpty(numberBlink)? 2 : Integer.parseInt(numberBlink) ;
        }
        catch (Exception e)
        {
            //
        }
    }

    private ObjectSearchParameters(Builder b) {
        this._type = b.b_type;
        this._storedObjectName = b.b_storedObjectName;
        this._obj = b.b_obj;
        this._multiObjConfig = b.b_multiObjConfig;
    }

    public ObjectSearchParameters(int typ, String strgNm, ObjectProperties obj, SelectMultiObjectConfiguration sel)
    {
        _type = typ;
        _storedObjectName = strgNm;
        _obj = obj;
        _multiObjConfig = sel;
    }

    public ObjectSearchParameters setType(int t)
    {
        _type = t;
        return this;
    }

    public ObjectSearchParameters setStoredObjectName (String name)
    {
        _storedObjectName = name;
        return this;
    }

    public ObjectSearchParameters setMultiObjectConfiguration (SelectMultiObjectConfiguration moc)
    {
        _multiObjConfig = moc;
        return this;
    }

    public ObjectSearchParameters setObjectRepository (ObjectProperties op)
    {
        _obj = op;
        return this;
    }

    public ObjectSearchParameters setHightLight(boolean flag)
    {
        _isHighlight = flag;
        return this;
    }

    public ObjectSearchParameters setScrollToElement(boolean flag)
    {
        _isScrollToElement = flag;
        return this;
    }

    public String getActionParamAsString() {
        return _actionParamAsString;
    }

    public ObjectSearchParameters setListActionParamAsString(List<String> l) {
        _listActionParamAsString = l;
        return this;
    }

    public List<String> getListActionParamAsString() {
        return _listActionParamAsString;
    }

    public ObjectSearchParameters setActionParamAsInteger(int _actionParamAsInteger) {
        this._actionParamAsInteger = _actionParamAsInteger;
        return this;
    }

    public Map<String, String> getListActionParamAsHashMap() {
        return _listActionParamAsHashMap;
    }

    public ObjectSearchParameters setListActionParamAsHashMap(int _actionParamAsHashMap) {
        this._listActionParamAsHashMap = new HashMap<String, String>(_actionParamAsHashMap);
        return this;
    }

    public boolean isFind()
    {
        return ((_type == 0) && (_obj != null));
    }

    public boolean isUseStoredObject()
    {
        return (!_storedObjectName.isEmpty() && (_obj == null) && (_type >=  2));
    }

    public boolean isSaveStoredObject()
    {
        return (!_storedObjectName.isEmpty() && (_obj != null) && (_type >=  2));
    }

    public boolean hasStoredObject()
    {
        return ((_storedObjectName.isEmpty()? false : true) && (_type >= 2));
    }

    public boolean isMultiObject()
    {
        return ((_multiObjConfig != null) && (_type == 1));
    }

    public boolean isHightlight() { return _isHighlight; }

    public boolean isScroll() { return _isScrollToElement; }

    public boolean isLast()
    {
        return _isLast;
    }

    public ObjectProperties getObjectRepository()
    {
        return _obj;
    }

    public String getStoredOjbectName()
    {
        return _storedObjectName;
    }

    public int getActionParamAsInteger() {
        return _actionParamAsInteger;
    }

    public ObjectSearchParameters setActionParamAsString(String sValue) {
        _actionParamAsString = sValue;
        return this;
    }



    public ObjectSearchParameters setIsLast() {
        _isLast = true;
        return this;
    }

    public SelectMultiObjectConfiguration getMultiObjectConfiguration()
    {
        return _multiObjConfig;
    }

    public String getMessage()
    {
        String rtrn = "";
        if (_multiObjConfig != null)
        {
            if (!_multiObjConfig.isMultiObjectFilter()) return rtrn;
            if (_multiObjConfig.isRandom()) {
                rtrn = String.format("  ON RANDOM INDEX [Random Number is '%s']", _multiObjConfig.getIndex());
            } else if (_multiObjConfig.getIndex() > 0)
            {
                rtrn = String.format("  ON INDEX '%s'", _multiObjConfig.getIndex());
            } else if (!_multiObjConfig.getContainText().isEmpty())
            {
                rtrn = String.format("  ON CONTAIN TEXT '%s' WITH INDEX '%s'", _multiObjConfig.getContainText(), _multiObjConfig.getIndex());
            }
        }
        else
        {
            if (_type <= 1)
            {
                rtrn = String.format("  ON Element '%s'", _obj.getName());
            }
            else if (_type > 2)
            {
                rtrn = String.format("  ON Element '%s'", _storedObjectName);
            }
        }
        return rtrn;
    }

    public boolean isSearchable()
    {
        return((_obj != null) || ((_storedObjectName != null) && (!_storedObjectName.isEmpty())));
    }

    public static class Builder {
        private int b_type = 0;  //0: Default [single Object]   1: Multi Object   2: StorageObject   3: Storage Object with Multi Object
        private String b_storedObjectName = "";
        private ObjectProperties b_obj = null;
        private SelectMultiObjectConfiguration b_multiObjConfig = null;
        public Builder type(int t) {
            this.b_type = t;
            return this;
        }

        public Builder storedObjectName(String strObjName) {
            this.b_storedObjectName = strObjName;
            return this;
        }

        public Builder objectRepository(ObjectProperties obj) {
            this.b_obj = obj;
            return this;
        }

        public Builder multiObjConfig(SelectMultiObjectConfiguration smobjCnf) {
            this.b_multiObjConfig = smobjCnf;
            return this;
        }

        public ObjectSearchParameters build() {
            return new ObjectSearchParameters(this);
        }


    }
}

