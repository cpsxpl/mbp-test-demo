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
public class StoredProcCall {
    private SQLStatement sqlStatement = new SQLStatement();

    private int paramCount = 0;

    private String execTag = "call ";

    public StoredProcCall(String procName) {
        this.execTag = (this.execTag + procName + "(");
    }

    public void addNullParameter() {
        appendComma();
        this.sqlStatement.appendNull();
    }

    public void addParameter(byte[] value) {
        appendComma();
        this.sqlStatement.appendValue(value);
    }

    public void addParameter(double value) {
        appendComma();
        this.sqlStatement.appendValue(value);
    }

    public void addParameter(long value) {
        appendComma();
        this.sqlStatement.appendValue(value);
    }

    public void addParameter(String value) {
        if ((value != null) && (!value.trim().equals(""))) {
            appendComma();
            this.sqlStatement.appendValue(value);
        } else {
            this.sqlStatement.append(",null");
        }
    }

    public void addParameter(Date value) {
        appendComma();
        this.sqlStatement.appendValue(value);
    }

    public void addParameter(Time value) {
        appendComma();
        this.sqlStatement.appendValue(value);
    }

    public void addParameter(Timestamp value) {
        appendComma();
        this.sqlStatement.appendValue(value);
    }

    private void appendComma() {
        if (this.paramCount > 0) {
            this.sqlStatement.append(",");
        }
        this.paramCount += 1;
    }

    public String toString() {
        String oriStr = this.sqlStatement.toString();

        String endStr = "{" + this.execTag + oriStr + ")}";

        return endStr;
    }
}