package com.hbcd.scripting.core;

import com.hbcd.common.container.DataServiceContainer;
import com.hbcd.common.service.DataService;
import com.hbcd.common.service.EmcEgcService;
import com.hbcd.common.service.GiftcardService;
import com.hbcd.utility.testscriptdata.ObjectEmcEgcScript;

public class TestscriptData {

    public static void saveGiftCardList() {
        //for(GiftCard gs: ((GiftcardService)DataServiceContainer.getServiceExcelReadCell("GiftCard")).getList())
        {
            Storage.save("giftCardsList", ((DataService<ObjectEmcEgcScript>) DataServiceContainer.getService("GiftCard")).getList());
        }
    }

    public static void saveEmcGiftCardList() {

        {
            Storage.save("EmcEgcCardsList", ((DataService<ObjectEmcEgcScript>) DataServiceContainer.getService("EmcEgcCard")).getList());
        }
    }
}
