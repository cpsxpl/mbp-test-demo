package com.mbp.eng.framework.jdbc.sql.table;

import org.apache.commons.beanutils.PropertyUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class HTMLHelper {
    public static String encode(String inString) {
        if (inString == null) {
            return "";
        }

        //字符串中只要有双引号就替换为&QUOT;
        inString = StringHelper.stringReplace(inString, "\"", "&QUOT;");
        //字符串中只要有 < 号就替换为 &LT;
        inString = StringHelper.stringReplace(inString, "<", "&LT;");
        //字符串中只要有 > 号就替换为 &GT;
        inString = StringHelper.stringReplace(inString, ">", "&GT;");
        //字符串中只要有 null 就替换为  空;
        inString = StringHelper.stringReplace(inString, "null", "");

        return inString;
    }

    public static void encode(Object obj) throws Exception {
        Map map = PropertyUtils.describe(obj);
        for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            Object object = map.get(name);
            String type = PropertyUtils.getPropertyType(obj, name).getName();
            if (type.equals("java.lang.Class")) {
                continue;
            }

            if (type.equals("java.lang.String")) {
                object = encode((String) object);
                PropertyUtils.setProperty(obj, name, object);
            } else {
                encode(object);
            }
        }
    }
}
