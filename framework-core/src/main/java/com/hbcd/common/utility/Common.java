package com.hbcd.common.utility;

import com.hbcd.logging.log.Log;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 */
public final class Common {

    public static String getDefaultDirectory() {
        return System.getProperty("user.dir");
    }

    /**
     * returns @filename path in the system.
     * @param
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */

    private static String convertSeparator(String filePath)
    {
        if (filePath != null) {
            filePath = filePath.replace("/", System.getProperty("file.separator"));
            filePath = filePath.replace("\\", System.getProperty("file.separator"));
        }
        return filePath;
    }

    public static String getExecutionDirectory() {
        return System.getProperty("user.dir");
    }

    public String getFullPathFileName(String fileName) throws IOException, URISyntaxException {
        String rtnFileName = null;
        java.io.File file = new java.io.File("");   //Dummy file;
        rtnFileName = file.getAbsolutePath() + System.getProperty("file.separator") + fileName;
//        System.out.println(rtnFileName);
        File testFile = new File(rtnFileName);

        if (!testFile.exists()) { //Default Execution Path
            rtnFileName = null;
            //Check Default
            if (com.hbcd.utility.helper.Common.isNotNullAndNotEmpty(com.hbcd.utility.helper.Common.DefaultParameterDirectory)) {
                rtnFileName = convertSeparator(com.hbcd.utility.helper.Common.DefaultParameterDirectory + System.getProperty("file.separator") + fileName);
                File testFile2 = new File(rtnFileName);
                if (!testFile2.exists()) { //Default Execution Path
                    return null;
                }
            }
        }

//        java.io.File file = new java.io.File("");   //Dummy file
//        String path = file.getAbsolutePath();

        return rtnFileName;
//        return this.getClass().getClassLoader().getResource(fileName).getPath();
    }

    public static File findFile(String filePath) throws Exception {
        if ((filePath == null) || (filePath.trim().isEmpty())) { Log.Error("Unable to load (filename is empty)..."); return null; }
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
}
