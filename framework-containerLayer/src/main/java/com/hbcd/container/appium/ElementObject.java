package com.hbcd.container.appium;

import com.hbcd.container.common.ElementObjectBase;
import com.hbcd.container.common.ParameterObject;
import com.hbcd.container.web.Utilities;
import com.hbcd.container.web.WebDriverException;
import com.hbcd.containerinterface.ElementSearch;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ElementObject extends com.hbcd.container.web.ElementObject implements ElementSearch {

    public ElementObject(MobileElement e) {
        super(e);
    }

    @Override
    public List<ElementSearch> fw_findElements(Map<String, Object> params) throws Exception {
        ParameterObject parameters = new ParameterObject(params);
        List<ElementSearch> objectList = new ArrayList<>();
        if (element == null)
            throw new Exception("no elements returned");
        objectList.addAll(((MobileElement) element).findElements(
                Utilities.getIdentifier(parameters.getPropertyType(), parameters.getPropertyName())).stream().map(com.hbcd.container.appium.ElementObject::new).collect(Collectors.toList()));
        return objectList;
    }
}
