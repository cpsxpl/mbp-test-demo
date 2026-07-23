package com.mbp.eng.framework.common.util.file;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * file工具类
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    static final String FILESEPARATE = File.separator;

    private static String docBase;

    private FileUtil() {
    }

    /**
     * 返回Web应用程序在文件系统中的根路径
     *
     * @return
     */
    public static String getDocBase() {
        if (docBase != null) {
            return docBase;
        }

        String path = null;
        try {
            path = FileUtil.class.getResource("").getPath();
            if (path.startsWith("file:")) {
                path = path.substring(5);
            }

            if (File.separator.equals("\\") && path.startsWith("/")) {
                path = path.substring(1);
            }

            docBase = path.substring(0, path.indexOf("WEB-INF"));
        } catch (Exception e) {
            throw new RuntimeException("获取应用路径时发生错误！", e);
        }

        return docBase;
    }

    /**
     * 替换文件路径的分隔符，以适应不同的操作系统，window："\"，Linux：“/”
     *
     * @param dir
     * @return
     */
    public static String handleDir(String dir) {
        return handleDir(dir, false);
    }

    /**
     * 替换文件路径的分隔符，以适应不同的操作系统，window："\"，Linux：“/”
     *
     * @param dir
     * @param isFillSep 如果路径的末尾没有路径分隔符，是否补充路径分隔符，比如：补充前为：d:\dir，补充后为d:\dir\
     * @return
     */
    public static String handleDir(String dir, boolean isFillSep) {
        if (dir == null || dir.length() == 0) {
            return dir;
        }

        if (File.separator.equals("\\")) {
            dir = dir.replace("/", "\\");
        } else {
            dir = dir.replace("\\", "/");
        }

        if (isFillSep) {
            if (dir.charAt(dir.length() - 1) != File.separatorChar) {
                dir = dir + File.separator;
            }
        }

        return dir;
    }

    /**
     * 加载properties文件
     *
     * @param path 类路径，比如：xx/xx/xx.properties，或者文件路径，比如：d:/file/xx.properties
     * @return
     */
    public static Properties loadProperties(String path) {
        if (path == null || path.lastIndexOf(".properties") != (path.length() - 11)) {
            throw new IllegalArgumentException("请传入正确的properties文件路径!，path=" + path);
        }

        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = FileUtil.class.getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                inputStream = new FileInputStream(path);
            }

            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }

        return properties;
    }

    public static boolean renameFile(String srcFilePath, String destFilePath) throws IOException {
        File srcFile = new File(srcFilePath);
        File destFile = new File(destFilePath);
        System.out.println("rename to=" + destFile);
        return srcFile.renameTo(destFile);
    }

    /**
     * @param directory
     * @return
     */
    public static String modFilePath(String directory) {
        String newPath = null;
        if (directory == null || "".equals(directory))
            throw new RuntimeException("Directory name cannot be empty.");
        if (directory.lastIndexOf(FILESEPARATE) != directory.length() - 1)
            newPath = directory + FILESEPARATE;
        else
            newPath = directory;
        return newPath;
    }

    public static String getFileNameInStorage(FileInfo fileInfo) {
        return fileInfo.getFileName() + "-" + fileInfo.getOwner() + "-"
                + fileInfo.getFileVersion();
    }

    public static String readFile(File file) throws IOException {
        if (!file.exists())
            throw new IOException("File has not been exist.");
        Reader reader = null;
        reader = new InputStreamReader(new FileInputStream(file));
        StringBuffer stringBuffer = new StringBuffer();
        int tempchar;
        try {
            while ((tempchar = reader.read()) != -1) {
                if (((char) tempchar) != '\r') {
                    stringBuffer.append((char) tempchar);
                }
            }
        } finally {
            reader.close();
        }
        return stringBuffer.toString();
    }

    public static void writeFile(File file, String string) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        bytes = string.getBytes();
        try {
            outputStream.write(bytes);
        } finally {
            outputStream.close();
        }
    }

    /**
     * .txt   text/plain
     * .csv   application/vnd.ms-excel
     */
    public static String getFileType(String path) {
        Path source = Paths.get(path);
        String type = null;
        try {
            type = Files.probeContentType(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return type;
    }

    public static String getFileType(Path path) {
        String type = null;
        try {
            type = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return type;
    }

    public static byte[] inputStreamToByte(InputStream inputStream) {
        try {
            byte[] bytes = ByteStreams.toByteArray(inputStream);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("inputStreamToByte error");
        }
    }
}
