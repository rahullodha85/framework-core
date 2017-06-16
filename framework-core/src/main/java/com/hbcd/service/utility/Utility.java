package com.hbcd.service.utility;

import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Iterator;

public class Utility {

    private static JSONObject findJSONObjectByObjectName(String jsonString, String findKey) throws JSONException {
        Object json = new JSONTokener(jsonString).nextValue();
        if (json instanceof JSONObject) {
            JSONObject jobj = (JSONObject) json;
            Iterator<String> keys = jobj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String val = null;
                try {
                    if (jobj.get(key) instanceof JSONObject) {
//	            		System.out.println("BEGIN OBJECT.");
//	            		System.out.println("key : "+ key + " IS OBJECT.");

                        if (key.equals(findKey)) {
                            return (JSONObject) jobj.get(key);
                        }
                        JSONObject j = findJSONObjectByObjectName(jobj.get(key).toString(), findKey);
                        if (j != null) {
                            return j;
                        }
//	            		System.out.println("END OBJECT.");
                    } else if (jobj.get(key) instanceof JSONArray) {
//	            		System.out.println("BEGIN ARRAY.");
//	            		System.out.println("key : "+ key + " IS ARRAY");
                        JSONArray ja = (JSONArray) jobj.get(key);
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject j = findJSONObjectByObjectName(ja.getJSONObject(i).toString(), findKey);
                            if (j != null) {
                                return j;
                            }
                        }
//	            		System.out.println("END ARRAY.");
                    } else if (jobj.get(key) instanceof String) {
//	            		System.out.println("key : "+ key + " IS STRING.");
                    } else if (jobj.get(key).getClass().isAssignableFrom(Integer.class)) {
//	            		System.out.println("key : "+ key + " IS INT.");
                    } else if (jobj.get(key).getClass().isAssignableFrom(Boolean.class)) {
//	            		System.out.println("key : "+ key + " IS BOOLEAN");
                    }

                } catch (Exception e) {
                    try {
                        val = jobj.getString(key);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } else if (json instanceof JSONArray) {
//	    	System.out.println("Is Array");
        }
        return null;
    }

    private static JSONArray findJSONArrayObjectName(String jsonString, String findKey) throws JSONException {
        Object json = new JSONTokener(jsonString).nextValue();
        if (json instanceof JSONObject) {
            JSONObject jobj = (JSONObject) json;
            Iterator<String> keys = jobj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String val = null;
                try {
                    if (jobj.get(key) instanceof JSONObject) {
//	            		System.out.println("BEGIN OBJECT.");
//	            		System.out.println("key : "+ key + " IS OBJECT.");
//	            		System.out.println("END OBJECT.");
                        JSONArray ja = findJSONArrayObjectName(jobj.get(key).toString(), findKey);
                        if (ja != null) {
                            return ja;
                        }
                    } else if (jobj.get(key) instanceof JSONArray) {

//	            		System.out.println("BEGIN ARRAY.");
//	            		System.out.println("key : "+ key + " IS ARRAY");;
                        if (key.equals(findKey)) {
                            return (JSONArray) jobj.get(key);
                        } else {
                            JSONArray ja = (JSONArray) jobj.get(key);
                            for (int i = 0; i < ja.length(); i++) {
                                JSONArray j = findJSONArrayObjectName(ja.getJSONObject(i).toString(), findKey);
                                if (j != null) {
                                    return j;
                                }
                            }
                        }
//	            		System.out.println("END ARRAY.");
                    } else if (jobj.get(key) instanceof String) {
//	            		System.out.println("key : "+ key + " IS STRING.");
                    } else if (jobj.get(key).getClass().isAssignableFrom(Integer.class)) {
//	            		System.out.println("key : "+ key + " IS INT.");
                    } else if (jobj.get(key).getClass().isAssignableFrom(Boolean.class)) {
//	            		System.out.println("key : "+ key + " IS BOOLEAN");
                    }

                } catch (Exception e) {
                    try {
                        val = jobj.getString(key);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } else if (json instanceof JSONArray) {
//	    	System.out.println("Is Array");
        }
        return null;
    }

    private static JSONObject findJSONObjectByKeyWithExactText(String jsonString, String findKey, String text) throws JSONException {
        return findJSONObjectByKeyWithText(jsonString, findKey, text, true, false);
    }

    private static JSONObject findJSONObjectByKeyWithText(String jsonString, String findKey, String text) throws JSONException {
        return findJSONObjectByKeyWithText(jsonString, findKey, text, false, false);
    }

    private static JSONObject findJSONObjectByKeyWithText(String jsonString, String findKey, String text, boolean isExact, boolean isCaseSensitive) throws JSONException {
        Object json = new JSONTokener(jsonString).nextValue();
        if (json instanceof JSONObject) {
            JSONObject jobj = (JSONObject) json;
            Iterator<String> keys = jobj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String val = null;
                try {
                    if (jobj.get(key) instanceof JSONObject) {
//	            		System.out.println("BEGIN OBJECT.");
//	            		System.out.println("key : "+ key + " IS OBJECT.");
                        JSONObject j = findJSONObjectByKeyWithText(jobj.get(key).toString(), findKey, text, isExact, isCaseSensitive);
                        if (j != null) {
                            return j;
                        }
//	            		System.out.println("END OBJECT.");
                    } else if (jobj.get(key) instanceof JSONArray) {
//	            		System.out.println("BEGIN ARRAY.");
//	            		System.out.println("key : "+ key + " IS ARRAY");
                        JSONArray ja = (JSONArray) jobj.get(key);
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject j = findJSONObjectByKeyWithText(ja.getJSONObject(i).toString(), findKey, text, isExact, isCaseSensitive);
                            if (j != null) {
                                return j;
                            }
                        }
//	            		System.out.println("END ARRAY.");
                    } else if (jobj.get(key) instanceof String) {
//	            		System.out.println("key : "+ key + " IS STRING.");
                        if (key.equals(findKey)) {
                            try {
                                if (jobj.get(key) instanceof String) {
                                    String value = jobj.getString(findKey);
                                    if (isExact) {
                                        if (isCaseSensitive) {
                                            if (value.equals(text)) {
                                                return jobj;
                                            }
                                        } else  //NOT case sensitive
                                        {
                                            if (value.toUpperCase().equals(text.toUpperCase())) {
                                                return jobj;
                                            }
                                        }
                                    } else  //Contain Text
                                    {
                                        if (isCaseSensitive) {
                                            if (value.contains(text)) {
                                                return jobj;
                                            }
                                        } else  //NOT case sensitive
                                        {
                                            if (value.toUpperCase().contains(text.toUpperCase())) {
                                                return jobj;
                                            }
                                        }
                                    }
//	        							,  boolean isCaseSensitive
//	        						if (.contains(containText))
//	        						{
//	        							return jobj;
//	        						}
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (jobj.get(key).getClass().isAssignableFrom(Integer.class)) {
//	            		System.out.println("key : "+ key + " IS INT.");
                    } else if (jobj.get(key).getClass().isAssignableFrom(Boolean.class)) {
//	            		System.out.println("key : "+ key + " IS BOOLEAN");
                    }

                } catch (Exception e) {
                    try {
                        val = jobj.getString(key);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } else if (json instanceof JSONArray) {
//	    	System.out.println("Is Array");
        }
        return null;
    }

//	private static String getValue(JSONObject obj, String key) throws JSONException
//	{
//		if (obj.get(key) instanceof String )
//    	{
////    		System.out.println("key : "+ key + " IS STRING.");
//			return obj.getString(key).toString();
//    	}
//    	else if (obj.get(key).getClass().isAssignableFrom(Integer.class))
//    	{
////    		System.out.println("key : "+ key + " IS INT.");
//    	}
//    	else if (obj.get(key).getClass().isAssignableFrom(Boolean.class))
//    	{
//
//    	}
//		
//		return "";
//		
//	}

    public static String getJSONObjectString(String jsonString, String targetObjectKey, String targetObjectContainValue) throws Exception {
        JSONObject o = findJSONObjectByKeyWithText(jsonString, targetObjectKey, targetObjectContainValue);
        if (o == null) {
            return "";
        } else {
            return o.toString();
        }
    }


    public static String getJSONArrayValue(String j, String k) throws Exception {
        JSONArray ja = findJSONArrayObjectName(j, k);
        if (ja != null) {
            return ja.toString();
        }
        return "";
    }


    public static Object getJSONArrayFirstObjectValue(String j, String key) throws Exception {
        JSONArray ja = new JSONArray(j);
        if (ja != null) {
            if (ja.length() > 0) {

                if (ja.getJSONObject(0).get(key) instanceof String) /* Is String */ {
                    return ja.getJSONObject(0).getString(key);
                } else if (ja.getJSONObject(0).get(key).getClass().isAssignableFrom(Integer.class)) {
                    return ja.getJSONObject(0).getInt(key);
                } else if (ja.getJSONObject(0).get(key).getClass().isAssignableFrom(Boolean.class)) {
                    return ja.getJSONObject(0).getBoolean(key);
                }
            }
        }
        return "";
    }


    public static String findInArray(String jsn, String findKey, String containText) throws Exception {
        Object json = new JSONTokener(jsn).nextValue();
        if (json instanceof JSONArray) {
            JSONArray ja = (JSONArray) json;
            for (int i = 0; i < ja.length(); i++) {
                JSONObject j = findJSONObjectByKeyWithExactText(ja.getJSONObject(i).toString(), findKey, containText);
                if (j != null) {
                    return j.toString();
                }
            }
        }
        return "";
    }


    public static String getJSONValue(String obj, String key) throws Exception {
        JSONObject jsonObj = new JSONObject(obj);
        if (jsonObj != null) {
            if (jsonObj.get(key) instanceof String) {
                return jsonObj.getString(key).toString();
            } else if (jsonObj.get(key).getClass().isAssignableFrom(Integer.class)) {
                return Integer.toString(jsonObj.getInt(key));
            } else if (jsonObj.get(key).getClass().isAssignableFrom(Boolean.class)) {
                return Boolean.toString(jsonObj.getBoolean(key));
            }
        }
        return "";
    }

    public static String getJSONValueFromObjectNameAndKey(String objJSONRoot, String objName, String key) throws Exception {
        JSONObject jsonObj = findJSONObjectByObjectName(objJSONRoot, objName);
        if (jsonObj != null) {
            if (jsonObj.get(key) instanceof String) {
                return jsonObj.getString(key).toString();
            } else if (jsonObj.get(key).getClass().isAssignableFrom(Integer.class)) {
                return Integer.toString(jsonObj.getInt(key));
            } else if (jsonObj.get(key).getClass().isAssignableFrom(Boolean.class)) {
                return Boolean.toString(jsonObj.getBoolean(key));
            }
        }
        return "";
    }

    public static String findObject(String json, String xpath) {
        //xpath "$.store.book[*].author"
        String returnJSON = "";
        net.minidev.json.JSONObject jsonObj = null;
        try {

            Object obj = JsonPath.read(json, xpath);
            if (obj != null) {
                if (obj instanceof net.minidev.json.JSONObject) {
                    if (((net.minidev.json.JSONObject) obj).isEmpty()) {
                        returnJSON = "";
                    } else {
                        returnJSON = obj.toString();
                    }
                } else if (obj instanceof net.minidev.json.JSONArray) {
                    //if ((net.minidev.json.JSONArray) obj).size() > 0)
                    if (((net.minidev.json.JSONArray) obj).isEmpty()) {
                        returnJSON = "";
                    } else {
                        returnJSON = obj.toString();
                    }
                }
                return returnJSON;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

}
