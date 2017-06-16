package com.hbcd.utility.entity;

/**
 * Created by ephung on 10/21/2015.
 */
public class SelectMultiObjectConfiguration {

    private boolean _isMultiObjectFilter = false;
    private boolean _isRandom = false;
    private int _multiElementSelectWithIndex = 0;
    private String _multiElementSelectWithContainText = "";

//    public int getMultiElementSelectWithContainTextIndex() {
//        return _multiElementSelectWithContainTextIndex;
//    }
//
//    public void setMultiElementSelectWithContainTextIndex(int _mIndex) {
//        this._multiElementSelectWithContainTextIndex = _mIndex;
//    }

//    private int _multiElementSelectWithContainTextIndex = 0;

    public SelectMultiObjectConfiguration() {
    }

    public SelectMultiObjectConfiguration(int index) {
        _multiElementSelectWithIndex = index;
    }

    public SelectMultiObjectConfiguration(String containText) {
        _multiElementSelectWithContainText = containText;
    }

    public SelectMultiObjectConfiguration(int index, String containText) {
        _multiElementSelectWithIndex = index;
        _multiElementSelectWithContainText = containText;
    }

    public SelectMultiObjectConfiguration setIsMultiObjectFilter(boolean isMulti) {
        this._isMultiObjectFilter = isMulti;
        return this;
    }

    public SelectMultiObjectConfiguration setRandom(boolean isRandom) {
        this._isRandom = isRandom;
        return this;
    }

    public int getIndex() {
        return _multiElementSelectWithIndex;
    }

    public SelectMultiObjectConfiguration setIndex(int _multiElementIndex) {
        this._multiElementSelectWithIndex = _multiElementIndex;
        return this;
    }

    public String getContainText() {
        return _multiElementSelectWithContainText;
    }
    public SelectMultiObjectConfiguration setContainText(String _multiElementContainText) {
        this._multiElementSelectWithContainText = _multiElementContainText;
        return this;
    }

    public boolean isMultiObjectFilter()
    {
        return _isMultiObjectFilter;
    }

    public boolean isMultiObjectIndexed()
    {
        return (_multiElementSelectWithIndex > 0) ? true : false;
    }

    public boolean isRandom()
    {
        return _isRandom;
    }

//    public String getMessage()
//    {
//        String rtrn = "";
//        if (!_isMultiObjectFilter) return rtrn;
//        if (_isRandom) {
//            rtrn = "  ON RANDOM INDEX [Random Number is '" + _multiElementSelectWithIndex + "']";
//        } else if (_multiElementSelectWithIndex > 0)
//        {
//            rtrn = "  ON INDEX '" + _multiElementSelectWithIndex + "'";
//        } else if (!_multiElementSelectWithContainText.isEmpty())
//        {
//            rtrn = "  ON CONTAIN TEXT '" + _multiElementSelectWithContainText + "' WITH INDEX '" + _multiElementSelectWithIndex + "'";
//        }
//        return rtrn;
//    }

    public SelectMultiObjectConfiguration cloneObject()
    {
        SelectMultiObjectConfiguration rtrnObj = new SelectMultiObjectConfiguration();
        rtrnObj.setIndex(this._multiElementSelectWithIndex);
        rtrnObj.setContainText(this._multiElementSelectWithContainText);
        rtrnObj.setRandom(this._isRandom);;
        rtrnObj.setIsMultiObjectFilter(this._isMultiObjectFilter);
        return rtrnObj;
    }
}
