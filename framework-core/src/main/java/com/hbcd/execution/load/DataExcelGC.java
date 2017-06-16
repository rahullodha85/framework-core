package com.hbcd.execution.load;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import com.hbcd.common.container.DataServiceContainer;
import com.hbcd.common.service.DataService;
import com.hbcd.common.utility.ObjectManager;
import com.hbcd.logging.log.Log;
import com.hbcd.utility.helper.Common;

public class DataExcelGC {


//    public static void LoadEmcEgcCards() {
//
//        String key = "EmcEgcCard";
//        try {
//
//            String filePath = "SaksOFF5th_EmcEgc_Test.xlsx";
//            Class<?> type = Class
//                    .forName("com.hbcd.common.service.EmcEgcService");
//            File xlsxFile = new File("SaksOFF5th_EmcEgc_Test.xlsx");
//
//            if (!xlsxFile.exists()) {
//                String filePath2 = Common.DefaultParameterDirectory + "\\\\"
//                        + filePath;
//                Log.Info("fail with filePath [" + filePath
//                        + "], try to find with DefaultParameterDirectory ["
//                        + filePath2 + "]");
//                xlsxFile = new File(filePath2);
//                if (!xlsxFile.exists()) {
//                    Log.Error("Not found or not a file: " + xlsxFile.getPath());
//                    System.err.println("Not found or not a file: "
//                            + xlsxFile.getPath());
//                    return;
//                }
//                filePath = filePath2; // just for log display
//            }
//
//            Log.Info("Found and Load file: " + filePath);
//            Path p = Paths.get(xlsxFile.getAbsolutePath());
//            BasicFileAttributes attrb = Files.readAttributes(p,
//                    BasicFileAttributes.class);
//            DataService srv = (DataService) DataServiceContainer.getService(key);
//            if ((srv == null)
//                    || ((srv != null) && !srv.isTheSameDataFile(attrb))) {
//
//                try {
//                    if ((srv == null)
//                            || ((srv != null) && !srv.isTheSameDataFile(attrb))) {
//
//                        // getServiceExcelMapping service from EXCEL -> XML -> JAVA Object
//                        Object newService = ObjectManager.getServiceExcelMapping(type, xlsxFile);
//						/*	if (newService != null) {
//							Method m = newService.getClass().getDeclaredMethod(
//									"setFileAttribute",
//									BasicFileAttributes.class);
//							m.invoke(newService, attrb);
//						}*/
//                        // Load DataService into a DataService Container
//
//                        //Object giftCardService=ObjectManager.jaxbXMLToObject(type, sb.toString());
//                        //DataServiceContainer.loadService(key,  giftCardService);
//
//                        DataServiceContainer.loadService(key, newService);
//                        DataServiceContainer.getService(key);
//
//
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw e;
//                } finally {
//
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
