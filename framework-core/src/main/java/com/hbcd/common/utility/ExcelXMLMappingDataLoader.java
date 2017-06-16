package com.hbcd.common.utility;

import com.hbcd.logging.log.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.extractor.XSSFExportToXml;
import org.apache.poi.xssf.usermodel.XSSFMap;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by ephung on 9/6/16.
 */
public class ExcelXMLMappingDataLoader extends DataServiceLoader<ExcelXMLMappingDataLoader> implements DataServiceLoaderInterface {

    @Override
    protected Object load() {
        String myXML = "";
        try {
            myXML = getXML();
            if (myXML == null) {
                myXML = "";
            }
            return jaxbXMLToObject(_serviceClassName, myXML);
        } catch (Exception e) {
//            throw e;
            Log.Error("Mapping Error", e);
        }
        return null;
    }

    private static <T> T jaxbXMLToObject(Class<T> type, String xml) throws JAXBException {
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
    private String getXML()
    {
        String xml = "";
        OPCPackage pkg = null;
        XSSFSheet sheet = null;
        try {
            pkg = OPCPackage.open(_inputDataFile.getPath(), PackageAccess.READ);

            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            sheet = wb.getSheetAt(0);
            Row row1 = sheet.getRow(0);
            int totalCol = row1.getLastCellNum();

            for (XSSFMap map : wb.getCustomXMLMappings()) {
                XSSFExportToXml exporter = new XSSFExportToXml(map);

                ByteArrayOutputStream os = new ByteArrayOutputStream();

                try {
                    exporter.exportToXML(os, true);
                    xml = os.toString("UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.Error("Error", e);
                }
            }

        } catch (InvalidFormatException | IOException e) {
//            throw e;
            Log.Error("Open File Error", e);
        } finally {
            if (pkg != null) {
                try {
                    pkg.close();
                } catch (IOException e) {
//                    throw e;
                    Log.Error("Unable to close Package Error", e);
                }
            }
        }
        return xml;
    }
}
