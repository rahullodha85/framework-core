package com.hbcd.scripting.core;

import com.hbcd.common.service.DynamicData;
import com.hbcd.scripting.enums.impl.ItemType;
import com.hbcd.utility.accessor.impl.MongoAccessor;
import com.hbcd.utility.dao.impl.MongoProductImpl;
import com.hbcd.utility.testscriptdata.CheckoutDataRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScriptData {
    private static DynamicData data = new DynamicData();
    private Map<ItemType, Integer> itemCounter = new HashMap<>();
    private CheckoutDataRow dataObject;
    private int index = 0;

    public ScriptData() {
        for (ItemType t : ItemType.values())
            itemCounter.put(t, 0);
    }

    public ScriptData(CheckoutDataRow dataObject){
        for(ItemType t : ItemType.values())
            itemCounter.put(t,0);
        this.dataObject = dataObject;
    }

    public String get(ItemType t) {
        int i = itemCounter.get(t);
        itemCounter.put(t, i + 1);

        try {
            return data.get(t.toString()).get(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        index++;
        return dataObject.getSkuListInfo().get(index-1);
    }

    public ArrayList<String> itemsOfType(ItemType t) {
        return data.get(t.toString());
    }
}
