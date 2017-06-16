package com.hbcd.scripting.core;

import com.hbcd.utility.testscriptdata.JSONValidation;

public class HipChat {

    //Make sure Saks Direct QA Automation has access to the Room
    public static void Notify(long roomNumber, String message) throws Exception {
        if (roomNumber > 0) {
            JSONValidation data = new JSONValidation();
            data.setServiceURL("https://api.hipchat.com/v2/room");
            //data.setResourceMappingPath("1109255/notification?auth_token=sw5L2P9KIk4dthR7ciL0kyguHe2pBP2dbasrQ7e0");
            data.setResourceMappingPath(String.format("%s/notification?auth_token=sw5L2P9KIk4dthR7ciL0kyguHe2pBP2dbasrQ7e0", roomNumber));
            data.setParameter(String.format("{\"color\": \"green\", \"message_format\": \"html\", \"notify\" : \"true\", \"message\": \"%s\" }", message));
            data.getValidateObject().clear();
            Service.JSONValidate(data);
        }
    }
}
