package com.hbcd.common.utility;

import com.hbcd.logging.log.Log;
import com.hbcd.utility.entity.PropertyIndex;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.extractor.XSSFExportToXml;
import org.apache.poi.xssf.usermodel.XSSFMap;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class ExcelDataLoader extends DataServiceLoader<ExcelDataLoader> implements DataServiceLoaderInterface {

    protected int[] getStartColumnIndex(Row r, String expectedFirstColumnName) {
        int totalCol = r.getLastCellNum();
        int colIdx = -1;
        for (int i = 0; i < totalCol; i++) {
            if (r.getCell(i) == null) {
                continue;
            }
            if (r.getCell(i).toString().trim().equalsIgnoreCase(expectedFirstColumnName)) {
                colIdx = i;
                break;
            }
        }
        return new int[] {colIdx, totalCol};
    }

    @Override
    protected Object load() {
        OPCPackage pkg = null;
        String key="";
        XSSFSheet sheet = null;
        try {
            pkg = OPCPackage.open(_inputDataFile.getPath(), PackageAccess.READ);

            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            sheet = wb.getSheetAt(0);

            Row row1 = sheet.getRow(0);

            int[] result = getStartColumnIndex(row1, _expectFirstColumnName);
            int firstColIndx = result[0];
            int totalCol = result[1];
//			System.out.println("TOTAL COLUMNS: " + totalCol);
//			System.out.println("FIRST COLUMNS INDEX: " + firstColIndx);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    continue;
                }
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                ArrayList<PropertyIndex> data = new ArrayList<PropertyIndex>();
                while (cellIterator.hasNext())  //Only when Cell is not Empty
                {
                    Cell cell = cellIterator.next();
                    PropertyIndex pi = readData(cell, cell.getColumnIndex(),firstColIndx);
                    if (pi != null)
                    {
                        data.add(pi);
                    }

                    //Cell cell = cellIterator.next();
                    //Check the cell type and format accordingly
                }
                _serviceObject.add(buildObject(firstColIndx, data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pkg != null) {
                try {
                    pkg.close();
                } catch (IOException e) {
                    Log.Error("Unable to Close Package", e);
                }
            }
        }
        return null;
    }

    @Override
    protected PropertyIndex readData(Object obj, int colIndex, int fIndex)
    {
        Cell cell = null;
        if (obj == null) return null;
        cell = (Cell)obj;
        colIndex = colIndex - fIndex;
        if (colIndex >= 0) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
//                  System.out.print(cell.getNumericCellValue() + "|");
                    return new PropertyIndex(colIndex, cell.getNumericCellValue());
                case Cell.CELL_TYPE_STRING:
//                  System.out.print(cell.getStringCellValue() + "|");
                    return new PropertyIndex(colIndex, cell.getStringCellValue());
            }
        }
        return null;
    }
}
