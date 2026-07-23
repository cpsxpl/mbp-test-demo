package com.mbp.eng.framework.jdbc.sql.table;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class SQLStrException extends Exception {
    public SQLStrException() {
    }

    public SQLStrException(String message) {
        super(message);
    }

    public static void main(String[] args) {
        SQLStrException sqlStrException = new SQLStrException();
    }
}
