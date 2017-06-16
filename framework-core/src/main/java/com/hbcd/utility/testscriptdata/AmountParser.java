package com.hbcd.utility.testscriptdata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AmountParser {

    public static double toPrice(String amountStr) {
        String amountRegEx = "[0-9]+([,.][0-9]{1,2})?";
        return Double.parseDouble(extractHelpler(amountRegEx, amountStr));
    }

	/*public static String keyExtract(String card) {
        String cardRegEx = "-?\\d+";
		return extractHelpler(cardRegEx, card);
	}*/


    private static String extractHelpler(String processingExpression,
                                         String data) {
        StringBuffer sBuffer = new StringBuffer();
        Pattern p = Pattern.compile(processingExpression);
        Matcher m = p.matcher(data);
        while (m.find()) {
            sBuffer.append(m.group());
        }
        System.out.println(sBuffer.toString());
        return sBuffer.toString();
    }
}
