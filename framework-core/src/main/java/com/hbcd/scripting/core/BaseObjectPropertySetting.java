package com.hbcd.scripting.core;

import com.hbcd.common.utility.ObjectPropertyUtility;
import com.hbcd.scripting.core.fluentInterface.ObjectPropertySetting;
import com.hbcd.utility.common.Type.PropertyIntType;
import com.hbcd.utility.common.Type.PropertyType;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectProperties;

public class BaseObjectPropertySetting extends BaseApplyObjectAction {

    public BaseObjectPropertySetting(ActionParameters acp) {
        super(acp);
    }


    public ObjectPropertySetting setProperty(PropertyType t, String value) {
        ObjectPropertyUtility.setObject(_parameters.Current().getObjectRepository(), t, value);
        return new ApplyObjectPropertySetting(_parameters);
    }


    public ObjectPropertySetting setProperty(PropertyIntType t, int value) {
        ObjectPropertyUtility.setObject(_parameters.Current().getObjectRepository(), t, value);
        return new ApplyObjectPropertySetting(_parameters);
    }


    public ObjectPropertySetting setChildProperty(PropertyType t, String value) {
        if (_parameters.Current().getObjectRepository().child == null) {
            _parameters.Current().getObjectRepository().setChild(new ObjectProperties());
        }
        ObjectPropertyUtility.setObject(_parameters.Current().getObjectRepository().child, t, value);
        return new ApplyObjectPropertySetting(_parameters);
    }


    public ObjectPropertySetting setChildProperty(PropertyIntType t, int value) {
        if (_parameters.Current().getObjectRepository().child == null) {
            _parameters.Current().getObjectRepository().setChild(new ObjectProperties());
        }
        ObjectPropertyUtility.setObject(_parameters.Current().getObjectRepository().child, t, value);
        return new ApplyObjectPropertySetting(_parameters);
    }
}
