package com.hbcd.common.utility;

import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ephung on 1/19/2016.
 */
public class PropertiesHelper {
    public static Properties matchingKeySubset(Properties properties, String regularExpression) {
        Properties result = new Properties();

        // sanity check
        if (regularExpression == null || regularExpression.length() == 0) {
            return result;
        }

        Pattern pattern = Pattern.compile(regularExpression);

        for (Enumeration e = properties.propertyNames(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);

            // unparse value ${prop.key} inside braces
            Matcher matcher = pattern.matcher(key);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                result.setProperty(key, value);
            }
        }
        // final
        return result;
    }

    public static Properties matchingSubset(Properties properties, String prefix,
                                            boolean keepPrefix) {
        Properties result = new Properties();

        // sanity check
        if (prefix == null || prefix.length() == 0) {
            return result;
        }

        String prefixMatch; // match prefix strings with this
        String prefixSelf; // match self with this
        if (prefix.charAt(prefix.length() - 1) != '.') {
            // prefix does not end in a dot
            prefixSelf = prefix;
            prefixMatch = prefix + '.';
        } else {
            // prefix does end in one dot, remove for exact matches
            prefixSelf = prefix.substring(0, prefix.length() - 1);
            prefixMatch = prefix;
        }
        // POSTCONDITION: prefixMatch and prefixSelf are initialized!

        // now add all matches into the resulting properties.
        // Remark 1: #propertyNames() will contain the System properties!
        // Remark 2: We need to give priority to System properties. This is done
        // automatically by calling this class's getProperty method.
        String key;
        for (Enumeration e = properties.propertyNames(); e.hasMoreElements(); ) {
            key = (String) e.nextElement();

            if (keepPrefix) {
                // keep full prefix in result, also copy direct matches
                if (key.startsWith(prefixMatch) || key.equals(prefixSelf)) {
                    result.setProperty(key,
                            properties.getProperty(key));
                }
            } else {
                // remove full prefix in result, dont copy direct matches
                if (key.startsWith(prefixMatch)) {
                    result.setProperty(key.substring(prefixMatch.length()),
                            properties.getProperty(key));
                }
            }
        }

        // done
        return result;
    }
}
