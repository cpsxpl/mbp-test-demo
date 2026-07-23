package com.mbp.eng.framework.common.util.ftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class FtpUtil {
    private static final Log logger = LogFactory.getLog(FtpUtil.class);

    /**
     * @param ftpHost     SFTP IP地址
     * @param ftpPort     SFTP端口
     * @param ftpUserName SFTP 用户名
     * @param ftpPassword SFTP用户名密码
     * @param ftpPath     SFTP服务器中文件所在路径 格式： ftptest/aa
     * @param fileName    文件名称
     * @param localPath   下载到本地的位置 格式：H:/download
     * @preserve 从SFTP服务器下载文件
     */
    public static void downloadSftpFile(String ftpHost, int ftpPort, String ftpUserName, String ftpPassword, String ftpPath, String fileName, String localPath) throws JSchException {
        Session session = null;
        Channel channel = null;

        JSch jsch = new JSch();
        session = jsch.getSession(ftpUserName, ftpHost, ftpPort);
        session.setPassword(ftpPassword);
        session.setTimeout(100000);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp chSftp = (ChannelSftp) channel;

        String ftpFilePath = ftpPath + "/" + fileName;
        //String localFilePath = localPath + File.separatorChar + fileName;

        try {
            chSftp.get(ftpFilePath, localPath);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("download error.");
        } finally {
            chSftp.quit();
            channel.disconnect();
            session.disconnect();
        }
    }
    /**
     * This class should preserve.
     *
     * @param hostname:FTP服务器
     * @param port:FTP服务器端口
     * @param username:FTP登录账号
     * @param password:FTP登录密码
     * @param remotePath:FTP服务器上的相对路径
     * @param fileName:要下载的文件名
     * @param localPathFileName:下载后保存到本地的路径
     * @preserve 从Windows系统FTP服务器下载文件
     * @author Chen Pei
     * @Date: 2016-6-14 上午08:54:14
     */
    public static boolean downloadWinftpFileStatus(String hostname, int port, String username, String password, String remotePath, String fileName, String localPathFileName) {
        boolean success = false;
        FTPClient ftpClient = new FTPClient();
        try {
            int reply;
            ftpClient.connect(hostname, port);
            //如果采用默认端口,可以使用ftp.connect(url)的方式直接连接FTP服务器
            //登录
            ftpClient.login(username, password);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return success;
            }
            //转移到FTP服务器目录
            ftpClient.changeWorkingDirectory(remotePath);
            FTPFile[] ftpFileList = ftpClient.listFiles();

            for (FTPFile ftpFile : ftpFileList) {
                // System.out.println("ftpFileList===" + ftpFileList);
                if (ftpFile.getName().equals(fileName)) {
                    // System.out.println("dd");
                    File localFile = new File(localPathFileName + ftpFile.getName());
                    OutputStream outputStream = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(ftpFile.getName(), outputStream);
                    System.out.println("get==" + ftpFile.getName());
                    outputStream.close();
                    // new
                    success = true;
                }
            }
            ftpClient.logout();
            //old
            //success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }

    /**
     * This class should preserve.
     *
     * @param hostname:FTP服务器
     * @param port:FTP服务器端口
     * @param username:FTP登录账号
     * @param password:FTP登录密码
     * @param remotePath:FTP服务器上的相对路径
     * @param fileName:要下载的文件名
     * @param localPathFileName:下载后保存到本地的路径
     * @preserve 从Windows系统FTP服务器下载文件
     * @author Chen Pei
     * @Date: 2016-6-14 上午08:54:14
     */
    public static void downloadWinftpFile(String hostname, int port, String username, String password, String remotePath, String fileName, String localPathFileName) {
        FTPClient ftpClient = new FTPClient();
        try {
            int reply;
            ftpClient.connect(hostname, port);
            // 如果采用默认端口,可以使用ftp.connect(url)的方式直接连接FTP服务器
            // 登录
            ftpClient.login(username, password);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }
            // 转移到FTP服务器目录
            ftpClient.changeWorkingDirectory(remotePath);
            FTPFile[] ftpFileList = ftpClient.listFiles();

            for (FTPFile ftpFile : ftpFileList) {
                // System.out.println("ftpFileList===" + ftpFileList);
                if (ftpFile.getName().equals(fileName)) {
                    // System.out.println("dd");
                    File file = new File(localPathFileName + ftpFile.getName());
                    OutputStream outputStream = new FileOutputStream(file);
                    ftpClient.retrieveFile(ftpFile.getName(), outputStream);
                    System.out.println("get==" + ftpFile.getName());
                    outputStream.close();
                }
            }
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
    }
}
