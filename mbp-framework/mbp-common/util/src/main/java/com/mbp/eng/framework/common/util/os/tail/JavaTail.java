package com.mbp.eng.framework.common.util.os.tail;

import com.mbp.eng.framework.common.util.date.DateUtil;
import com.mbp.eng.framework.common.util.num.NumUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.text.ParseException;

public class JavaTail {
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        if (args.length < 1) {
            System.out.println("请输入参数!");
            System.exit(0);
        }
        if (args.length > 1) {
            System.out.println("请输入正确的参数个数!");
            System.exit(0);
        }
        if (!NumUtil.isNumeric2(args[0])) {
            System.out.println("参数必须为数字!");
            System.exit(0);
        }
        String typeFile = args[0];
        String periodID = DateUtil.getHourId();
        String filename = typeFile + "_" + periodID;
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("找不到 " + file.getAbsolutePath() + " 文件!");
            System.exit(0);
        } else {
            JavaTail(filename);
        }
    }

    public static void JavaTail(String filename) throws IOException, InterruptedException {
        InetAddress address = InetAddress.getLocalHost();
        String periodID = filename.substring(filename.lastIndexOf('_') + 1, filename.length());
        InputStream inputStream = null;
        Reader reader = null;
        BufferedReader bufferedReader = null;
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println(filename + "文件不存在!");
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            fileNOExists(filename);
        } else {
            inputStream = new FileInputStream(filename);
            reader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(reader);
        }
        String singleLine = "";
        while (true) {
            String newPeriodID = DateUtil.getHourId();
            singleLine = bufferedReader.readLine();
            if (singleLine != null) {
                System.out.println("address=" + address + "***" + singleLine);
                continue;
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            if (!periodID.equals(newPeriodID)) {
                filename = filename.replace(periodID, newPeriodID);
                JavaTailNewRun(filename);
            }
        }
        bufferedReader.close();
    }

    public static void JavaTailNewRun(String filename) throws IOException, InterruptedException {
        JavaTail(filename);
    }

    public static void fileNOExists(String filename) throws IOException, InterruptedException {
        JavaTail(filename);
    }
}