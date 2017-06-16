package com.hbcd.common.utility;

import com.hbcd.common.container.DataServiceContainer;
import com.hbcd.common.service.DataService;
import com.hbcd.logging.log.Log;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.FileType;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//import com.hbcd.utility.helper.Common;

public class Data {

    private static Lock _lockmeRepository = new ReentrantLock();
    private static Lock _lockmeTestSuite = new ReentrantLock();

    public static <T> void Load(ServiceName sn, String key, String filePath) throws Exception {
        try {
            File _inputDataFile = findFile(filePath);
            if ((_inputDataFile == null) || !_inputDataFile.exists()) {
                String s = String.format("Not found or not a file: %s", filePath);
                Log.Error(s);
//                System.err.println("Not found or not a file: " + filePath);
                return;
            }

            //Log.Info("Found and Load file: " + filePathInfo);
            Path p = Paths.get(_inputDataFile.getAbsolutePath());
            BasicFileAttributes attrb = Files.readAttributes(p, BasicFileAttributes.class);
            DataService srv = DataServiceContainer.getService(key, true);
            if ((srv == null) || ((srv != null) && !srv.isTheSameDataFile(attrb))) {
                _lockmeTestSuite.lock();
                try {
                    Object newService = null;
                    if ((srv == null) || ((srv != null) && !srv.isTheSameDataFile(attrb))) {
                        if (srv != null)
                        {
                            srv.clearData();
                        }
                        //Get Service
                        newService = DataServiceLoaderFactory.create(getFileType(_inputDataFile)).setService(sn).setInputFile(_inputDataFile).getService();
                        if (newService != null) {
                            Method m = newService.getClass().getSuperclass().getDeclaredMethod("setFileInfo", ServiceName.class, String.class, BasicFileAttributes.class, Path.class);
                            m.invoke(newService, sn, key, attrb, p);
                        }
                        //Load DataService into a DataService Container
                        DataServiceContainer.loadService(key, newService);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    _lockmeTestSuite.unlock();
                }
            }

        } catch (Exception e1) {
            throw e1;
        }
    }

    private static File findFile(String filePath) throws Exception {
        if ((filePath == null) || (filePath.trim().isEmpty())) { Log.Error("Unable to load (filename is empty)...");return null; }
        File inputDataFile =null;
        String filePathInfo="";
        String fullPathFileName = new com.hbcd.common.utility.Common().getFullPathFileName(filePath);
        if ((fullPathFileName != null) &&(!fullPathFileName.isEmpty())) {
            inputDataFile = new File(fullPathFileName);
            filePathInfo=String.format(" from directory: %s", fullPathFileName);
        }
        else
        {
            URL dir_url = Data.class.getClassLoader().getResource(filePath);
            if (dir_url == null)
            {
                Log.Error(String.format("Unable to find file %s in Resource.", filePath));
                throw new Exception(String.format("File %s not found.", filePath));
            }
            // Turn the resource into a File object
            inputDataFile = new File(dir_url.toURI());
            filePathInfo= String.format(" from Resource: %s", dir_url);
        }

        if ((inputDataFile == null) || !inputDataFile.exists()) {
            Log.Error(String.format("Not found or not a file: %s", filePath));
//            System.err.println("Not found or not a file: " + filePath);
        }
        else {
            Log.Info(String.format("Found and Load file: %s", filePathInfo));
        }
        return inputDataFile;
    }

    private static FileType getFileType(File f)
    {
        //Default File Type;
        FileType fileType = FileType.CSV;

        if (ConfigurationLoader.getSystemStringValue("EXCEL_DATA_LOADER").equalsIgnoreCase("CELLBYCELL") ||
                ConfigurationLoader.getSystemStringValue("DATA_LOADER").equalsIgnoreCase("CELLBYCELL") ) {
            if(f.getName().endsWith(".csv")){
                fileType = FileType.CSV;
                Log.Info("Reading csv data");
            } else if(f.getName().endsWith(".xlsx")) {
                //Read Cell by Cell and getServiceExcelMapping JAVA Object
                fileType = FileType.EXCEL;
                Log.Info("Load Excel Data by Read Cell-by-Cell.");
            }
        } else if (ConfigurationLoader.getSystemStringValue("EXCEL_DATA_LOADER").equalsIgnoreCase("MAPXML") ||
                ConfigurationLoader.getSystemStringValue("DATA_LOADER").equalsIgnoreCase("MAPXML")) {
            //getServiceExcelMapping service from EXCEL -> XML -> JAVA Object
            fileType = FileType.EXCELXMLMAPPING;
            Log.Info("Load Excel Data with XML Mapping.");
        } else if (ConfigurationLoader.getSystemStringValue("EXCEL_DATA_LOADER").equalsIgnoreCase("XML") ||
                ConfigurationLoader.getSystemStringValue("DATA_LOADER").equalsIgnoreCase("XML")) {
            fileType = FileType.XML;
        } else if (ConfigurationLoader.getSystemStringValue("DATA_LOADER").equalsIgnoreCase("CSV")) {
            Log.Info("Reading csv data");
            fileType = FileType.CSV;
        }
        return fileType;
    }

    public static void ReLoadService(String key) throws Exception {
        if (key.equalsIgnoreCase(String.format("%s.%s", ApplicationSetup.get(Setting.SITE), ServiceName.OBJECT_REPOSITORY)))
        {
            LoadRepository();
        }
        else if (key.equalsIgnoreCase(String.format("%s.%s.%s", ApplicationSetup.get(Setting.SITE),ApplicationSetup.get(Setting.MODULE_NAME),ServiceName.TEST_SUITE)))
        {
            LoadTestSuite();
        }
        else if (key.equalsIgnoreCase(String.format("%s.%s", ApplicationSetup.get(Setting.SITE),ServiceName.TEST_DATA)))
        {
            LoadTestData();
        }
//        else if (key.equalsIgnoreCase())
//        {
//
//        }
    }

    public static void LoadRepository() throws Exception {
        if (!ApplicationSetup.isSetup()) {
            throw new Exception("Need to setup Process First.");
        }
        try {

            String key = String.format("%s.%s", ApplicationSetup.get(Setting.SITE), ServiceName.OBJECT_REPOSITORY);
            String filePath = ConfigurationLoader.getSiteExcelRepository(ApplicationSetup.get(Setting.SITE));
            Log.Info(String.format("Loading Object Repository [%s].... file [%s]", key, filePath));
            Load(ServiceName.OBJECT_REPOSITORY, key, filePath);

        } catch (Exception e) {

            throw e;
        }

    }

    public static void LoadTestSuite() throws Exception {
        try {
            String key = String.format("%s.%s.%s", ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME), ServiceName.TEST_SUITE);
            String filePath = ConfigurationLoader.getExcelTestSuite(String.format("%s.%s", ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME)));
            Log.Info(String.format("Loading Test Suite [%s].... file [%s]", key, filePath));
            Load(ServiceName.TEST_SUITE, key, filePath);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void LoadTestData() throws Exception {
        try {
            String key = String.format("%s.%s", ApplicationSetup.get(Setting.SITE),ServiceName.TEST_DATA);
            String filePath = ConfigurationLoader.getExcelTestData(ApplicationSetup.get(Setting.SITE));
            Log.Info(String.format("Loading Test Data [%s].... file [%s]", key, filePath));
            Load(ServiceName.TEST_DATA, key, filePath);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void LoadGiftCards() throws Exception {
        String key = String.format("%s.%s.%s", ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME), ServiceName.GIFT_CARD);
        //Database.executeQuery(query);

    }
}
