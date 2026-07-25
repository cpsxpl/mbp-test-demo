package com.mbp.eng.framework.jdbc.sql.table;

import javax.sql.DataSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class LogFile {
    private String logDirectory;

    private String logFilePrefix;

    private String logFileExtend;

    private String logFileSeqPattern;

    private long logFileMaxSize;

    private DataSource dataSource;

    private File logFile;

    private PrintWriter printWriter;

    private DateFormat dateFormat;

    public LogFile(String logDirectory, String logFilePrefix,
                   String logFileExtend, String logFileSeqPattern,
                   long logFileMaxSize, DataSource dataSource) {
        this.logDirectory = logDirectory;
        this.logFilePrefix = logFilePrefix;
        this.logFileExtend = logFileExtend;
        this.logFileSeqPattern = logFileSeqPattern;
        this.logFileMaxSize = logFileMaxSize;
        this.dataSource = dataSource;

        this.dateFormat = SimpleDateFormat.getDateTimeInstance();
    }

    protected PrintWriter getWriter() {
        if (needReset()) {
            try {
                reset();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return this.printWriter;
    }

    protected boolean needReset() {
        if ((this.logFile == null) || (this.printWriter == null)) {
            return true;
        }

        synchronized (this.logFile) {
            long fileSize = this.logFile.length();
            if (fileSize > this.logFileMaxSize) {
                return true;
            }
        }

        return false;
    }

    protected void reset() throws IOException, SQLException {
        String logSeq = new SimpleDateFormat(this.logFileSeqPattern)
                .format(new Date());

        File logFileDir = new File(this.logDirectory);
        if (!logFileDir.exists()) {
            logFileDir.mkdirs();
        }
        String logFileName = this.logFilePrefix + logSeq + this.logFileExtend;

        File file = new File(logFileDir, logFileName);
        PrintWriter printWriter = new PrintWriter(new FileWriter(file));

        if (this.logFile != null) {
            synchronized (this.logFile) {
                this.logFile = file;
            }
        }
        this.logFile = file;

        if (this.printWriter != null) {
            synchronized (this.printWriter) {
                this.printWriter.flush();
                this.printWriter.close();
                this.printWriter = printWriter;
            }
        }
        this.printWriter = printWriter;

        if (this.dataSource != null)
            synchronized (this.dataSource) {
                this.dataSource.setLogWriter(this.printWriter);
            }
    }

    public synchronized void write(Object obj) {
        PrintWriter printWriter = getWriter();
        if (printWriter != null) {
            printWriter.println(this.dateFormat.format(new Date()) + ": " + obj);
            if ((obj instanceof Throwable)) {
                ((Throwable) obj).printStackTrace(printWriter);
            }

            printWriter.flush();
        }
    }
}