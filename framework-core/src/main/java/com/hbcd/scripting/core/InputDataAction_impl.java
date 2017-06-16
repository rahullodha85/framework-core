package com.hbcd.scripting.core;

import com.hbcd.common.utility.TestDataUtility;
import com.hbcd.logging.log.Log;
import com.hbcd.scripting.core.fluentInterface.InputDataAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ephung on 5/24/2016.
 */
public class InputDataAction_impl implements InputDataAction {
    String _objDataName = "";

    public InputDataAction_impl(String name)
    {
        _objDataName = name;
    }

    private boolean isSelect(String filterPattern, String tagName)
    {
        if ((filterPattern == null) || (filterPattern.isEmpty())) return true;
        //"^(\\d+x\\d+)"
        Pattern p = Pattern.compile(filterPattern);
        Matcher m = p.matcher(tagName);
        return m.matches();
    }

    @Override
    public String select(int idx) throws Exception {
        return TestDataUtility.getTestData(_objDataName, idx);
    }

    @Override
    public List<String> select(String filterPattern) throws Exception {
        List<String> rtrn = new ArrayList<>();
        int maxSize = TestDataUtility.getTestDataSize(_objDataName);

        if ((filterPattern != null) && (!filterPattern.isEmpty())) {
            if (filterPattern.equals("*")) filterPattern = ".*";
        }

        for (int i = 1; i <= maxSize; i++) {
            if (isSelect(filterPattern, Integer.toString(i)))
            {
                rtrn.add(TestDataUtility.getTestData(_objDataName, i));
            }
        }
        return rtrn;
    }

    @Override
    public String selectRandom() throws Exception {
        int maxSize = TestDataUtility.getTestDataSize(_objDataName);
        int min = 1;
        Random random = new Random();
        int randNum = random.nextInt( maxSize - min + 1) + min;
        return TestDataUtility.getTestData(_objDataName, randNum);
    }

    @Override
    public String selectSequential() throws Exception {
        String returnValue = null;
        try {
            returnValue = TestDataUtility.getSequentialTestData(_objDataName);
            if (returnValue == null)
            {
                throw new Exception("Unable to get Sequential Data");
            }
        } catch (Exception ex)
        {
            throw new Exception("");
        }
        return returnValue;
    }

    @Override
    public int size() throws Exception {
        return TestDataUtility.getTestDataSize(_objDataName);
    }

    @Override
    public String selectFirst() throws Exception {
        return TestDataUtility.getTestData(_objDataName, 1);
    }

    @Override
    public String selectLast() throws Exception {
        return TestDataUtility.getTestData(_objDataName, size() );
    }

    @Override
    //Exactly the same as Select First
    public String value() throws Exception {
        return TestDataUtility.getTestData(_objDataName, 1);
    }
}
