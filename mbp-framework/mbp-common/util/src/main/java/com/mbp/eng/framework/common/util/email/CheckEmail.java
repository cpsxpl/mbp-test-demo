package com.mbp.eng.framework.common.util.email;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckEmail {
    public static String getMailByWeb(String mail) {
        String regex = "\\w+@\\w+(\\.\\w+)+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mail);
        return  m.group();
    }
    public static boolean isEmail(String email) {
        //判断是否为空邮箱
        int k = 0;
        if (email == null) {
            return false;
        }

        /*
        * 单引号引的数据 是char类型的
        双引号引的数据 是String类型的
        单引号只能引一个字符
        而双引号可以引0个及其以上*
        */
        //判断是否有仅有一个@且不能在开头或结尾
        if (email.indexOf("@") > 0 && email.indexOf('@') == email.lastIndexOf('@') && email.indexOf('@') < email.length() - 1) {
            k++;
        }
        //判断"@"之后必须有"."且不能紧跟
        if (email.indexOf('.', email.indexOf('@')) > email.indexOf('@') + 1) {
            k++;
        }
        //判断"@"之前或之后不能紧跟"."
        if (email.indexOf('.') < email.indexOf('@') - 1 || email.indexOf('.') > email.indexOf('@') + 1) {
            k++;
        }
        //@之前要有6个字符
        if (email.indexOf('@') > 5) {
            k++;
        }
        if (email.endsWith("com") || email.endsWith("org") || email.endsWith("cn") || email.endsWith("net")) {
            k++;
        }
        if (k == 5) {
            return true;
        }
        return false;
    }
}