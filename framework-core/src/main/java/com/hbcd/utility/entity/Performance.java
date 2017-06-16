package com.hbcd.utility.entity;

import com.hbcd.utility.common.WebPerformance;
import com.hbcd.utility.helper.Common;

import java.util.HashMap;

public class Performance {

    private String PageName = "";
    private long ExecutionTime = System.currentTimeMillis();
    private HashMap<String, Long> Data = new HashMap<String, Long>();
    private String Xml = "";

    public Performance() {

    }

    public Performance(String wpn) {
        PageName = wpn;
        ExecutionTime = System.currentTimeMillis();  //UTC Time-point
    }

    private Performance(String wpn, long exect) {
        PageName = wpn;
        ExecutionTime = exect;  //UTC Time-point
    }

    /**
     * @return the executionTime
     */
    public long getExecutionTime() {
        return ExecutionTime;
    }

    /**
     * @return the pageName
     */
    public String getPageName() {
        return PageName;
    }

    /**
     * @param pageName the pageName to set
     */
    public void setPageName(String pageName) {
        PageName = pageName;
    }
    /**
     * @return the executionTime
     */

    /**
     * @return the data
     */
    public HashMap<String, Long> getData() {
        return Data;
    }

    /**
     * @param data the data to set
     */
    public void setData(HashMap<String, Long> data) {
        Data = data;
    }

    /**
     * @return the xml
     */
    public String getXml() {
        return Xml;
    }

    /**
     * @param xml the xml to set
     */
    public void setXml(String xml) {
        Xml = xml;
    }

    public boolean isSameXml(String xml) {
        return Xml.equals(xml);

    }

    public boolean isSameData(HashMap<String, Long> data) {
        boolean rtrn = false;
        for (WebPerformance wp : WebPerformance.values()) {
            if ((wp != WebPerformance.PERFORMANCE_START) && (wp != WebPerformance.PERFORMANCE_END)) {
                if (Common.DefaultLongZero(Data.get(wp.getvalue())).longValue() == Common.DefaultLongZero(data.get(wp.getvalue())).longValue()) {
                    rtrn = true;
                } else {
                    rtrn = false;
                    break;
                }
            }
        }
        return rtrn;
    }

    public void resetData() {
        long start = Common.DefaultLongZero(Data.get(WebPerformance.PERFORMANCE_START.getvalue()));
        long end = Common.DefaultLongZero(Data.get(WebPerformance.PERFORMANCE_END.getvalue()));
        Data.clear();
        Data.put(WebPerformance.PERFORMANCE_START.getvalue(), start);
        Data.put(WebPerformance.PERFORMANCE_END.getvalue(), end);
        Xml = Common.SerializeToXml("WebPerformance", this);
    }

    public Performance cloneWithResetData() {
        Performance p = new Performance(PageName, ExecutionTime);
        HashMap<String, Long> data = new HashMap<String, Long>();
        data.clear();
        data.put(WebPerformance.PERFORMANCE_START.getvalue(), Common.DefaultLongZero(Data.get(WebPerformance.PERFORMANCE_START.getvalue())));
        data.put(WebPerformance.PERFORMANCE_END.getvalue(), Common.DefaultLongZero(Data.get(WebPerformance.PERFORMANCE_END.getvalue())));
        p.setData(data);
        p.setXml(Common.SerializeToXml("WebPerformance", p));
        return p;

    }

    public Performance clone() {
        Performance p = new Performance(PageName, ExecutionTime);
        HashMap<String, Long> data = new HashMap<String, Long>();
        for (String key : Data.keySet()) {
            data.put(key, Data.get(key));
        }
        p.setData(data);
        p.setXml(Common.SerializeToXml("WebPerformance", p));
        return p;
    }
}
