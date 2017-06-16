package com.hbcd.scripting.core;

import com.hbcd.scripting.core.fluentInterface.ObjectPropertySetting;
import com.hbcd.utility.common.Type;
import com.hbcd.utility.entity.ActionParameters;

public class ApplyObjectPropertySetting extends ApplyObjectAction_AttributeParent implements ObjectPropertySetting {

    public ApplyObjectPropertySetting(ActionParameters acp) {
        super(acp);
    }


    public ObjectPropertySetting setProperty(Type.PropertyType t, String value) {
        return new BaseObjectPropertySetting(_parameters).setProperty(t, value);
    }


    public ObjectPropertySetting setProperty(Type.PropertyIntType t, int value) {
        return new BaseObjectPropertySetting(_parameters).setProperty(t, value);
    }


    public ObjectPropertySetting setChildProperty(Type.PropertyType t, String value) {
        return new BaseObjectPropertySetting(_parameters).setChildProperty(t, value);
    }


    public ObjectPropertySetting setChildProperty(Type.PropertyIntType t, int value) {
        return new BaseObjectPropertySetting(_parameters).setChildProperty(t, value);
    }
}
