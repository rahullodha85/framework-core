package com.hbcd.utility.helper;

import com.hbcd.logging.log.Log;
import com.hbcd.utility.entity.Performance;
import com.thoughtworks.xstream.XStream;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class Common {

    //	static final Logger log = LogManager.getLogger(LogInfoImp.class);
    //public static String DefaultParameterDirectory = "";
    public static String DefaultParameterDirectory = "";
    public static boolean ServerFirstStart = true;
//	public static <T>void WriteErrorToLog(T obj)
//	{
//		Log.Error("", obj);
//	}

    public static Long DefaultLongZero(Object p) {
        if (p == null) {
            return 0L;
        } else {
            return (Long) p;
        }
    }

    public static String getIpAddress() {
        InetAddress localIP;
        String myIP = "N/A";

        try {
            localIP = InetAddress.getLocalHost();
            myIP = localIP.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return myIP;
    }

    public static String writeErrorToLog(Exception e) {
        String err = String.format("ERROR >> %s", e.toString());
        for (StackTraceElement ste : e.getStackTrace()) {
            err = String.format("%s\n%s", err, ste.toString());
        }
        Log.Error(err);
        return err;
    }

    public static String getExceptionToString(Exception e) {
        String err = String.format("ERROR >> %s", e.toString());
        for (StackTraceElement ste : e.getStackTrace()) {
            err = String.format("%s\n%s", err, ste.toString());
        }
        return err;
    }

    public static String getDefaultDirectory() {
//		java.io.File file = new java.io.File("");   //Dummy file
//		String path = file.getAbsolutePath();
//		return path;
        return System.getProperty("user.dir");
    }

    public static String getSiteStatus(String url) throws IOException {

        String result = "";
        try {
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            if (code == 200) {
                result = "Green";
            }
        } catch (Exception e) {
            result = "->Red<-";
        }
        return result;
    }

    public static String getMaxSizeValue(String value, int maxSize) {
        if (value != null) {
            value = value.substring(0, (value.length() > maxSize) ? maxSize : value.length());
        }
        return value;
    }

    public static int getUniqueNumber() {

        GregorianCalendar date = new GregorianCalendar();

        int jDate = date.get(Calendar.DAY_OF_YEAR);
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int min = date.get(Calendar.MINUTE);
        int sec = date.get(Calendar.SECOND);

        int random = Integer.valueOf(String.valueOf(jDate) + String.valueOf(hour) + String.valueOf(min) + String.valueOf(sec));

        return random;

    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public static boolean isNumeric2(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static boolean isNotNullAndNotEmpty (String str)
    {
        return (str != null && !str.trim().isEmpty());
    }

    public static boolean isNullOrEmpty(String str)
    {
        return (str == null || str.trim().isEmpty());
    }

    public static boolean isNumeric3(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }


    public static <T> String SerializeToXml(String rootName, T obj) {

        XStream xStream = new XStream();
        try {
            xStream.alias(rootName, Performance.class);
            xStream.useAttributeFor(Performance.class, "PageName");
            xStream.useAttributeFor(Performance.class, "ExecutionTime");
            xStream.alias("key", Class.forName("java.lang.String"));
            xStream.alias("value", long.class /* Class.forName("java.lang.Long") */);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        String xml = xStream.toXML(obj);

        //Log.Info(xml);

        return xml;
    }

    public static <T> T deserialize_xml_to_hashmap(String xml) {
        return null;

//	    String xmlAsMap = "<map> <entry>";
//
//	    XStream xStream = new XStream();
//	    xStream.alias("map", java.util.Map.class);
//
//	    @SuppressWarnings("unchecked")
//	    Map<Integer, Restaurant> restaurantConverted = (Map<Integer, Restaurant>) xStream
//	            .fromXML(xmlAsMap);
//
//	    assertThat(resturantConverted, hasKey(new Integer("1")));

    }

    public static boolean isInteger(String str) {
        try {
            int d = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
