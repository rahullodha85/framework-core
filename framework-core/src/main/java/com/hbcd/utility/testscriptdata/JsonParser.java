package com.hbcd.utility.testscriptdata;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonParser {

    public static Object parseJson(String dataType, String dataString) {


        try {
            return new Gson().fromJson(dataString, Class.forName(dataType).newInstance().getClass());
        } catch (JsonSyntaxException | InstantiationException
                | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
