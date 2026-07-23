package com.mbp.eng.framework.jdbc.sql.manager;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class SQLStatement {
    private StringBuffer stringBuffer = new StringBuffer();

    public SQLStatement() {
    }

    public SQLStatement(String text) {
        if (text != null)
            this.stringBuffer.append(text);
    }

    public void append(String text) {
        if (text != null)
            this.stringBuffer.append(text);
    }

    public void appendNull() {
        this.stringBuffer.append("null");
    }

    public void appendValue(byte[] value) {
        this.stringBuffer.append("0x");
        this.stringBuffer.append(SQLHelper.byteArrayToHex(value));
        this.stringBuffer.append("");
    }

    public void appendValue(double value) {
        this.stringBuffer.append(value);
    }

    public void appendValue(long value) {
        this.stringBuffer.append(value);
    }

    public void appendValue(String value) {
        this.stringBuffer.append(quoteString(value));
    }

    public void appendValue(Date value) {
        if (value == null) {
            appendNull();
        } else {
            this.stringBuffer.append("'");
            this.stringBuffer.append(value.toString());
            this.stringBuffer.append("'");
        }
    }

    public void appendValue(Time value) {
        if (value == null)
            appendNull();
        else
            this.stringBuffer.append(value.toString());
    }

    public void appendValue(Timestamp value) {
        if (value == null)
            appendNull();
        else
            this.stringBuffer.append(value.toString());
    }

    public static String quoteString(String s) {
        if (s == null) {
            return "null";
        }

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("'");
        int i = 0;
        int j;
        while ((j = s.indexOf("'", i)) != -1) {
            stringBuffer.append(s.substring(i, j));
            stringBuffer.append("''");
            i = j + 1;
        }
        stringBuffer.append(s.substring(i));
        stringBuffer.append("'");

        return stringBuffer.toString();
    }

    public String toString() {
        return this.stringBuffer.toString();
    }
}