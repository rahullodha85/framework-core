package com.hbcd.scripting.core;

import com.hbcd.service.utility.RESTUtility;

public class EmcResponse {


    public static String getEmcEgcResponse(String response, String object, String key) {

        {
            String amount = null;
            try {
                amount = RESTUtility.getJSONValueFromObjectNameAndKey(
                        response, object, key);
            } catch (Exception e) {

                e.printStackTrace();
            }


            return amount;

        }
    }

}
