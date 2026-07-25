package com.mbp.eng.framework.jdbc.sql.table;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class NullPreStatementException extends Exception {
    public NullPreStatementException() {
    }

    public NullPreStatementException(String message) {
        super(message);
    }

    public static void main(String[] args) {
        NullPreStatementException nullPreStatementException = new NullPreStatementException();
    }
}
