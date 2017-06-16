package com.hbcd.common.utility;

import com.hbcd.logging.log.Log;
import com.hbcd.utility.entity.PropertyIndex;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 461967 on 3/31/2016.
 */
public class CSVDataLoader extends DataServiceLoader<CSVDataLoader>  implements DataServiceLoaderInterface {

     private CellProcessor[] getProcessors(int headerCount) {
        final CellProcessor[] processors;
        switch (headerCount) {
//            case 50: {
//                processors = new CellProcessor[]{
//                        new NotNull(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(new ParseInt()),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(new ParseInt()),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(new ParseInt()),
//                        new Optional(new ParseInt()),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(new ParseInt()),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(new ParseInt()),
//                        new Optional(new ParseInt()),
//                        new Optional(),
//                        new Optional(),
//                        new Optional(),
//                        new Optional()
//                };
//                break;
//            }
//            case 5: {
//                processors = new CellProcessor[]{
//                        new NotNull(new ParseInt()),
//                        new NotNull(new ParseInt()),
//                        new NotNull(),
//                        new Optional(),
//                        new Optional()
//                };
//                break;
//            }
            default: {
                ArrayList<CellProcessor> p = new ArrayList<>();
                for (int i = 0; i < headerCount; i++) {
                    p.add(new Optional());
                }
                processors = p.toArray(new CellProcessor[]{});
                break;
            }
        }
        return processors;
    }

    @Override
    public Object load() throws IOException {
        ICsvListReader listReader = null;
        try{
            FileInputStream fis = new FileInputStream(_inputDataFile);
            listReader = new CsvListReader(new InputStreamReader(fis),
                    CsvPreference.STANDARD_PREFERENCE);

            final String[] header = listReader.getHeader(true);
            int[] result = getStartColumnIndex(header, _expectFirstColumnName);
            int firstColIndx = result[0];
            int totalCol = result[1];

            final CellProcessor[] processors = getProcessors(header.length);

            List<Object> localObject;
            while( (localObject = listReader.read(processors)) != null ) {
//                System.out.println(String.format("lineNo=%s, rowNo=%s, localObject=%s", listReader.getLineNumber(),
//                        listReader.getRowNumber(), localObject));
                ArrayList<PropertyIndex> data = new ArrayList<>();
                for(int i=0; i<localObject.size(); i++){
//                    if(localObject.get(i)!=null){
//                        data.add(new PropertyIndex(i, localObject.get(i)));
//                    }
                    PropertyIndex pi = readData(localObject.get(i), i, firstColIndx);
                    if (pi != null)
                    {
                        data.add(pi);
                    }
                }

                _serviceObject.add(buildObject(firstColIndx, data));
            }
            return _serviceObject;
        } catch (Exception e) {
            Log.Error(e.getMessage());
            e.printStackTrace();
        } finally {
            listReader.close();
        }
        return _serviceObject;
    }

    protected int[] getStartColumnIndex(String[] header, String expectedFirstColumnName) {
        int totalCol = 0;
        int colIdx = -1;

        for(int i=0; i<header.length; i++){
            totalCol++;
            if ((header[i] != null) && (header[i].equalsIgnoreCase(expectedFirstColumnName))){
                colIdx = i;
            }
        }
        return new int[]{colIdx, totalCol};
    }

    @Override
    protected PropertyIndex readData(Object data, int colIndex, int fIndex)
    {
        if (data == null) return null;
        colIndex = colIndex - fIndex;
        if (colIndex >= 0) {
            return new PropertyIndex(colIndex, data);
        }
        return null;
    }
}
