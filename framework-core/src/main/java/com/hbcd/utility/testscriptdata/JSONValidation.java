package com.hbcd.utility.testscriptdata;

import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;

import java.util.ArrayList;
import java.util.List;

public class JSONValidation {

    private String ServiceURL = "";
    private String ResourceMappingPath = "";
    private String Parameter = "";
    private List<KeyValuePair> Header = new ArrayList<KeyValuePair>();
    private List<ValidationDescriptionObject> ValidateObject = new ArrayList<ValidationDescriptionObject>();

    public List<KeyValuePair> getHeader() {
        return Header;
    }

    public String getServiceURL() {
        if ((ServiceURL == null) || ServiceURL.isEmpty()) {
            try {
                ServiceURL = ConfigurationLoader.getValue(ApplicationSetup.get(Setting.SITE) + ".URL");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ((ServiceURL == null) || ServiceURL.isEmpty()) {
                try {
                    ServiceURL = ConfigurationLoader.getValue(ApplicationSetup.get(Setting.SITE) + ".SERVICE_URL");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ServiceURL;
    }

    public String getResourceMappingPath() {
        return ResourceMappingPath;
    }


    public String getParameter() {
        return Parameter;
    }

    public List<ValidationDescriptionObject> getValidateObject() {
        return ValidateObject;
    }

    public void setServiceURL(String serviceURL) {
        ServiceURL = serviceURL;
    }

    public void setResourceMappingPath(String resourceMappingPath) {
        ResourceMappingPath = resourceMappingPath;
    }

    public void setParameter(String parameter) {
        Parameter = parameter;
    }

    public void setHeader(List<KeyValuePair> header) {
        Header.addAll(header);
    }

    public void setValidateObject(List<ValidationDescriptionObject> validateObject) {
        ValidateObject.addAll(validateObject);
    }

    public JSONValidation clone() {
        JSONValidation retObj = new JSONValidation();
        retObj.setServiceURL(ServiceURL);
        retObj.setHeader(Header);
        retObj.setParameter(Parameter);
        retObj.setResourceMappingPath(ResourceMappingPath);
        retObj.setValidateObject(ValidateObject);
        return retObj;
    }
}
