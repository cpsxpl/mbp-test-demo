package com.mbp.eng.framework.common.util.os;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve SSH工具类
 * @Date: 2016-05-18
 */
public class ShellUtils {
    private static JSch jsch;
    private static Session session;

    /**
     * 连接到指定的IP
     *
     * @throws JSchException
     */
    public static void connect(String user, String passwd, String host) throws JSchException {
        // 创建JSch对象
        jsch = new JSch();
        // 根据用户名、主机ip、端口号获取一个Session对象
        session = jsch.getSession(user, host, 22);
        // 设置密码
        session.setPassword(passwd);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        // 为Session对象设置properties
        session.setConfig(config);

        // 设置超时
        //session.setTimeout(1500);
        // 通过Session建立连接
        session.connect();
    }

    /**
     * 执行相关的命令
     *
     * @throws JSchException
     */
    public static void execCmd(String command, String user, String passwd, String host) throws JSchException {
        connect(user, passwd, host);

        BufferedReader reader = null;
        Channel channel = null;

        try {
            while (command != null) {
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(command);

                channel.setInputStream(null);
                ((ChannelExec) channel).setErrStream(System.err);

                channel.connect();
                InputStream in = channel.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                String buf = null;
                while ((buf = reader.readLine()) != null) {
                    System.out.println(buf);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            channel.disconnect();
            session.disconnect();
        }
    }
}
