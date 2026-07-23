package com.mbp.eng.framework.common.util.os;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestCommond {
    public static void main(String[] args) {
        String hostname = "10.71.5.64";
        String username = "ocdp";
        String password = "ocdp950@WJR";
        //指明连接主机的IP地址
        Connection connection = new Connection(hostname);
        Session session = null;
        try {
            //连接到主机
            connection.connect();
            //使用用户名和密码校验
            boolean isconn = connection.authenticateWithPassword(username, password);
            if (!isconn) {
                System.out.println("用户名称或者是密码不正确");
            } else {
                System.out.println("已经连接OK");
                session = connection.openSession();
                //使用多个命令用分号隔开
                //ssh.execCommand("pwd;cd /tmp;mkdir hb;ls;ps -ef|grep weblogic");
                session.execCommand("cd /home/ocdp/chenp; ");
                //只允许使用一行命令，即ssh对象只能使用一次execCommand这个方法，多次使用则会出现异常
                //ssh.execCommand("mkdir hb");
                //将屏幕上的文字全部打印出来
                InputStream inputStream = new StreamGobbler(session.getStdout());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
            }
            //连接的Session和Connection对象都需要关闭
            session.close();
            connection.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
