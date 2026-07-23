package com.mbp.eng.framework.common.util.os;

import com.jcraft.jsch.JSchException;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve SSH工具类
 * @Date: 2016-05-18
 */
public class CommondHelperTest {
    public static void main(String[] args) throws JSchException {
        String host = "10.71.5.3";
        String user = "ocdp";
        String passwd = "ocdp950@WJR";
        String command = "hadoop fs -chmod 777 /user/pirate/testSpark2hdfs";
        CommondHelper.execCmd(host, user, passwd, command);
    }
}
