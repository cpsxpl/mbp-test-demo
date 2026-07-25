package com.mbp.eng.framework.common.util.ftp;

import com.jcraft.jsch.JSchException;

import java.io.File;

public class FtpUtilTest {
    public static void main(String[] args) throws NumberFormatException, JSchException {
/*String ftpHost = "10.71.3.21";
String ftpPort = "22";
String ftpUserName = "schedule";
String ftpPassword = "sd*8=Cheule";
String privateKey = "";
String passphrase = "";
String ftpPath = "/home/schedule/wyz_test";
String localPathFileName = "e:/workspace/eclipse-jee-mars-1-win32/asiainfo/mysqlJdbc/config/dataFile";
String ftpFileName = "1472400000.json";*/

/*String ftpHost = "10.71.13.90";
String ftpPort = "21";
String ftpUserName = "asiainfo";
String ftpPassword = "abc@123";
String privateKey = "";
String passphrase = "";
String ftpPath = "/";
String localPathFileName = "D:\\90_";
String ftpFileName = "launch_u_ex17022504.log";*/

        String ftpHost = "10.71.3.29";
        String ftpPort = "21";
        String ftpUserName = "dcweb";
        String ftpPassword = "dw@Cntv*2016";
        String privateKey = "";
        String passphrase = "";
        String ftpPath = "/home/dcweb/data";
        String localPathFileName = "D:\\29_";
        String ftpFileName = "2017-02-27-13";

        File file = new File("");
        if (!file.exists()) {
            localPathFileName = file.getAbsolutePath() + "\\" + "29_";
        }

//FtpUtil.downloadSftpFile(ftpHost, Integer.valueOf(ftpPort),ftpUserName, ftpPassword,  ftpPath, ftpFileName,localPathFileName);

        if (!FtpUtil.downloadWinftpFileStatus(ftpHost, Integer.valueOf(ftpPort), ftpUserName, ftpPassword, ftpPath, ftpFileName, localPathFileName)) {
            System.out.println("文件不存在!");
            System.exit(0);
        }

        System.out.println(FtpUtil.downloadWinftpFileStatus(ftpHost, Integer.valueOf(ftpPort), ftpUserName, ftpPassword, ftpPath, ftpFileName, localPathFileName));
//FtpUtil.downloadWinftpFile(ftpHost, Integer.valueOf(ftpPort),ftpUserName, ftpPassword,  ftpPath, ftpFileName,localPath);
    }
}
