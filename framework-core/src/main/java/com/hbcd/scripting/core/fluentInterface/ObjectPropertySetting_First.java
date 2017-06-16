package com.hbcd.scripting.core.fluentInterface;

import com.hbcd.utility.common.Type.PropertyIntType;
import com.hbcd.utility.common.Type.PropertyType;

public interface ObjectPropertySetting_First {
    ObjectPropertySetting setProperty(PropertyType t, String value);

    ObjectPropertySetting setProperty(PropertyIntType t, int value);

    ObjectPropertySetting setChildProperty(PropertyType t, String value);

    ObjectPropertySetting setChildProperty(PropertyIntType t, int value);
}
