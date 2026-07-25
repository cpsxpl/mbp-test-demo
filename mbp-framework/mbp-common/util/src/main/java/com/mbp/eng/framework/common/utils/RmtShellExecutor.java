package com.mbp.eng.framework.common.utils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.tika.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Date;

public class RmtShellExecutor {
    private static final Logger logger = LoggerFactory.getLogger(RmtShellExecutor.class);

    private Connection connection;
    /**
     * 远程机器IP
     */
    private String ip;
    /**
     * 用户名
     */
    private String usr;
    /**
     * 密码
     */
    private String psword;
    private String charset = Charset.defaultCharset().toString();

    private static final int TIME_OUT = 1000 * 60 * 5;

    /**
     * 构造函数
     * @param param 传入参数Bean 一些属性的getter setter 实现略
     */
   /* public RmtShellExecutor(ShellParam param) {
        this.ip = param.getIp();
        this.usr = param.getUsername();
        this.psword = param.getPassword();
    }*/

    /**
     * 构造函数
     *
     * @param ip
     * @param usr
     * @param ps
     */
    public RmtShellExecutor(String ip, String usr, String ps) {
        this.ip = ip;
        this.usr = usr;
        this.psword = ps;
    }

    /**
     * 登录
     *
     * @return
     * @throws IOException
     */
    private boolean login() throws IOException {
        connection = new Connection(ip);
        connection.connect();
        return connection.authenticateWithPassword(usr, psword);
    }

    private boolean login(String ip, String user, String password) throws IOException {
        connection = new Connection(ip, 22);
        connection.connect();
        return connection.authenticateWithPassword(user, password);
    }

    /**
     * 执行脚本
     *
     * @param cmds
     * @return
     * @throws Exception
     */
    public int exec(String cmds, boolean waitFlag) {
        int retry = 1;
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        int ret = -1;
        try {
            if (waitFlag) {
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < retry; i++) {
                try {
                    logger.info("---start try---" + i + "---date---" + new Date());
                    if (login()) {
                        logger.info("---login try---" + i + "---date---" + new Date());
                        // Open a new {@link Session} on this connection
                        Session session = connection.openSession();
                        // Execute a command on the remote machine.
                        session.execCommand(cmds);
                        stdOut = new StreamGobbler(session.getStdout());
                        outStr = processStream(stdOut, charset);
                        stdErr = new StreamGobbler(session.getStderr());
                        outErr = processStream(stdErr, charset);
                        int waitForCondition = session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
                        logger.info("@exec: waitForCondition is {}", waitForCondition);
                        logger.info("@exec: outStr is {}", outStr);
                        logger.info("@exec: outErr is {}", outErr);
                        try {
                            ret = session.getExitStatus();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        session.close();
//                        String exitSignal = session.getExitSignal();
//                        logger.info("@exec: exitSignal is {}", exitSignal);
                        logger.info("---end try---" + i + "---date---" + new Date());
                        break;
                    } else {
                        logger.error("登录远程机器失败" + ip);
                        Thread.sleep(1000 * 10 * 1);
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(1000 * 5 * 1);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    /**
     * @param in
     * @param charset
     * @return
     * @throws IOException
     */
    private String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }

    /**
     *
     */
    public void scpPut() {
        try {
            if (login()) {
                SCPClient scpClient = new SCPClient(connection);
                String localFile1 = "/export/App/file/failover.sh";
                String localFile2 = "/export/App/file/rm-failover.sh";
                String[] localFiles = new String[]{localFile1, localFile2};
                String remoteDir = "/opt/jmr/.jmr-prepare-conf/";
                scpClient.put(localFiles, remoteDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public boolean uploadFilesToRemote(String[] localFiles, String remoteTargetDir) {
        try {
            SCPClient scpClient = connection.createSCPClient();
            for (String localFile : localFiles) {
                File item = new File(localFile);
                if (item.isFile()) {
                    logger.info("upload file:" + localFile);
                    scpClient.put(URLDecoder.decode(localFile, "UTF-8"), remoteTargetDir, "0777");
                } else {
                    logger.error("File:" + localFile + " is not file,can't be upload.");
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.close();
            }
            connection = null;
        }
    }

    public boolean downloadFileFromRemote(String[] remoteFiles, String localTargetDirectory) {
        for (String remoteFile : remoteFiles) {
            logger.info("Download file:" + remoteFile);
        }
        try {
            SCPClient scpClient = new SCPClient(connection);
            scpClient.get(remoteFiles, localTargetDirectory);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
