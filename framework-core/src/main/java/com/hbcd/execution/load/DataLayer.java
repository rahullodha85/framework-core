package com.hbcd.execution.load;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import com.hbcd.common.container.DataServiceContainer;
import com.hbcd.common.utility.ObjectManager;
import com.hbcd.utility.accessor.Accessor;
import com.hbcd.utility.accessor.impl.MysqlAccessor;
public class DataLayer {

    private static String giftCardQuery="select distinct CardNumber,Pin from valid_egcs where Balance=25 and status='valid'";
    private static Accessor dao=new MysqlAccessor();
    public static void LoadGiftCards() {

        String key = "GiftCard";
        try {
            //	List<GiftCard> giftcardList= new ArrayList<GiftCard>();
            Statement st=dao.getConnection().createStatement();
            Class<?> type = Class.forName("com.hbcd.common.service.GiftcardService");
            ResultSet rs=st.executeQuery(giftCardQuery);
            ResultSetMetaData rsmd=rs.getMetaData();
            StringBuilder sb=new StringBuilder("<GiftCardList>\r\n");

            while(rs.next()){
                sb.append("<giftcard>\r\n");
                sb.append("<"+rsmd.getColumnName(1)+">");
                sb.append(rs.getString(rsmd.getColumnName(1)));
                sb.append("</"+rsmd.getColumnName(1)+">\r\n");
                sb.append("<"+rsmd.getColumnName(2)+">");
                sb.append(rs.getString(rsmd.getColumnName(2)));
                sb.append("</"+rsmd.getColumnName(2)+">\n");
                sb.append("</giftcard>\r\n");
            }

            sb.append("</GiftCardList>\r\n");

            //Object newService=ObjectManager.getServiceExcelMapping(type, sb);
            Object giftCardService=ObjectManager.jaxbXMLToObject(type, sb.toString());
            DataServiceContainer.loadService(key,  giftCardService);


            //DataServiceContainer.loadService(key, sb);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
