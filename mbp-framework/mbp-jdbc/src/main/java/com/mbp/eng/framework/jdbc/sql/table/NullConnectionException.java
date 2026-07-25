package com.mbp.eng.framework.jdbc.sql.table;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class NullConnectionException extends Exception {
    public NullConnectionException() {
    }

    public NullConnectionException(String message) {
        super(message);
    }

    public static void main(String[] args) {
        NullConnectionException nullConnectionException = new NullConnectionException();
    }
}