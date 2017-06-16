package com.hbcd.scripting.core;

import com.hbcd.serivce.REST.JSONRequest;
import com.hbcd.service.utility.HTTPUtility;
import com.hbcd.service.utility.RESTUtility;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.testscriptdata.JSONValidation;
import com.hbcd.utility.testscriptdata.KeyValuePair;
import com.hbcd.utility.testscriptdata.ServiceRequestResult;
import com.hbcd.utility.testscriptdata.ValidationDescriptionObject;

public class Service {

    public static boolean isServiceDown(String prodId) throws Exception {
        String srvUrl = ConfigurationLoader.getValue(String.format("%s.SERVICE_URL", ApplicationSetup.get(Setting.SITE)));

        //Testing ONLY
        if ((srvUrl == null) || srvUrl.isEmpty()) {
            srvUrl = "http://qa-s5a-pdp-app.saksdirect.com:8280";
        }
        String jsonReturn = JSONRequest.get(String.format("%s//product//%s", srvUrl, prodId), null);

        if (jsonReturn.length() <= 0) {
            return false;
        }

        String arErrors = RESTUtility.getJSONArrayValue(jsonReturn, "errors");
        if (!arErrors.isEmpty()) {
            String rslt = (String) RESTUtility.getJSONArrayFirstObjectValue(arErrors, "error_code");

            if (!rslt.isEmpty()) {
                int error_code = Integer.parseInt(rslt);

                if (error_code == 0) {
                    //System.out.println("API_SERVICE_IS_DOWNED");
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isAvaliable(String prodId) throws Exception {
        String srvUrl = ConfigurationLoader.getValue(ApplicationSetup.get(Setting.SITE) + ".SERVICE_URL");

        //Testing ONLY
        if ((srvUrl == null) || srvUrl.isEmpty()) {
            srvUrl = "http://qa-s5a-pdp-app.saksdirect.com:8280";
        }
        String jsonReturn = JSONRequest.get(String.format("%s//product//%s" , srvUrl, prodId), null);

        String arSKUS = RESTUtility.getJSONArrayValue(jsonReturn, "skus");
        if (!arSKUS.isEmpty()) {
            System.out.println(arSKUS);
            String hasObject = RESTUtility.findInArray(arSKUS, "status_alias", "available");
            if (!hasObject.isEmpty()) {
                System.out.println(hasObject);
                return true;
            } else {
                System.out.println("NO PRODUCT AVAILABLE");
            }
        }

        return false;
    }

    public static boolean isSoldOut(String prodId) throws Exception {
        String srvUrl = ConfigurationLoader.getValue(String.format("%s.SERVICE_URL", ApplicationSetup.get(Setting.SITE)));

        //Testing ONLY
        if ((srvUrl == null) || srvUrl.isEmpty()) {
            srvUrl = "http://qa-s5a-pdp-app.saksdirect.com:8280";
        }
        String jsonReturn = JSONRequest.get(String.format("%s//product//%s", srvUrl, prodId), null);

        String valueSoldOut = RESTUtility.getJSONValueFromObjectNameAndKey(jsonReturn, "sold_out_message", "enabled");

        if (!valueSoldOut.isEmpty()) {
            System.out.println(valueSoldOut);
            if (valueSoldOut.toUpperCase().equals("TRUE")) {
                System.out.println(String.format("SOLD OUT IS : %s", valueSoldOut));
                return true;
            } else {
                System.out.println("NO PRODUCT AVAILABLE");
                return false;
            }
        }

        return true;
    }


    //REST - GET Value from JSON
    public static String JSONGetValue(String jsonData, String objectName, String keyForValue) {
        String _rtn = "";
        try {
            _rtn = RESTUtility.getJSONValueFromObjectNameAndKey(jsonData, objectName, keyForValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _rtn;
    }

    //REST - GET
    public static ServiceRequestResult JSONValidate(JSONValidation data1) throws Exception {
        boolean isValidate = true;
        boolean isFirstValideChange = true;
        String mappingPath = data1.getResourceMappingPath();
        String parameter = data1.getParameter();
        String errorResponse = "";
        String validationRslt = "";
        String jsonReturn = "";
        String message1 = ""; /* Expected */
        String message2 = ""; /* Actual */

        if ((mappingPath == null) || (mappingPath.isEmpty())) {
            mappingPath = "";
        } else {
            mappingPath = String.format("//%s", data1.getResourceMappingPath());
        }

        String srvUrl = data1.getServiceURL() + mappingPath;

        if (parameter == null || parameter.isEmpty()) {
            jsonReturn = JSONRequest.get(srvUrl, data1.getHeader());
        } else {
            jsonReturn = JSONRequest.post(srvUrl, data1.getHeader(), parameter);
        }

        if ((jsonReturn != null) && !jsonReturn.isEmpty()) {
            errorResponse = RESTUtility.findObject(jsonReturn, "$.errors");
            for (ValidationDescriptionObject vdo : data1.getValidateObject()) {
                boolean isFirst = true;
                String rslt = RESTUtility.findObject(jsonReturn, vdo.getPath());
                if (rslt.length() > 0) {
                    //String json = rslt.get(0);
                    for (KeyValuePair kv : vdo.getValidates()) {
                        boolean localStatus = false;
                        String retrnRslt = RESTUtility.getJSONValue(rslt, kv.getKey());
                        /* Expected */
                        message1 += String.format("%s%s", (isFirst) ? "" : " | ", kv.toString());

						/* Actual */
                        message2 += String.format("%sKey [%s] has Value of %s", (isFirst) ? "" : " | ", kv.getKey(), retrnRslt);

                        if (kv.getValue().equals(retrnRslt)) {
                            message2 += " - PASS";
                            localStatus = true;
                        } else {
                            message2 += " - FAIL";
                            localStatus = false;

                        }
                        isFirst = false;

                        if (isFirstValideChange) {
                            isValidate = localStatus;
                            isFirstValideChange = false;
                        } else {
                            if ((isValidate) && (!localStatus)) {
                                isValidate = false;
                            }
                        }
                    }
                }
            }
        } else {
            isValidate = true;
            message1 += "NO RETURN";
            message2 += "NO RETURN";
        }
        System.out.println(validationRslt);
        return new ServiceRequestResult(errorResponse, isValidate, jsonReturn, message1, message2);
    }

    public static String HTTPRequest(String url) {
        String rtrn = "";
        rtrn = HTTPUtility.get(url);
        return rtrn;
    }

}
