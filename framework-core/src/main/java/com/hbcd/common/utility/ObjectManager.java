package com.hbcd.common.utility;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;

public class ObjectManager {

    public static <T> T jaxbXMLToObject(Class<T> type, String xml) throws JAXBException {
        try {

            if (xml.length() == 0) return null;
            JAXBContext context = JAXBContext.newInstance(type);
            Unmarshaller un = context.createUnmarshaller();
            StringBuffer xmlStr = new StringBuffer(xml /* BuildObjectDescription.getXMLString() */);
            Object o = un.unmarshal(new StreamSource(new StringReader(xmlStr.toString())));
            return (T) o;
        } catch (JAXBException e) {
            throw e;
        }
    }

//    public static <T> T getServiceExcelMapping(Class<T> type, File file) throws Exception {
//        String myXML = "";
//        try {
//            myXML = ExcelDataLoader.CustomXMLMapping(file);
//            if (myXML == null) {
//                myXML = "";
//            }
//            return jaxbXMLToObject(type, myXML);
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    public static <T> T getServiceExcelReadCell(ServiceName sn, File file) throws Exception {
        String myXML = "";
        try {
            //return (T) ExcelDataLoader.toServiceObject(sn, file);
            return (T)new ExcelDataLoader().setServiceClass(sn.getServiceClassName()).setEntityClass(sn.getEntityClassName()).setInputFile(file).getService();
        } catch (Exception e) {
            throw e;
        }
    }

    public static <T> T getServiceCSVRead(ServiceName sn, File file) throws Exception {
        //return (T) CSVDataLoader.toServiceObject(sn, file);
        return (T)new CSVDataLoader().setServiceClass(sn.getServiceClassName()).setEntityClass(sn.getEntityClassName()).setInputFile(file).getService();
    }
}
