package com.hbcd.common.utility;

import com.hbcd.utility.common.FileType;

/**
 * Created by ephung on 9/6/16.
 */
public class DataServiceLoaderFactory<T> {

    public static DataServiceLoaderInterface create(FileType ft)
    {
        switch (ft)
        {
            case CSV:
                return new CSVDataLoader();
            case EXCEL:
                return new ExcelDataLoader();
            case EXCELXMLMAPPING:
                return new ExcelXMLMappingDataLoader();
            case XML:
                return new XMLDataLoader();
            default:
                return new CSVDataLoader();
        }
    }
}
