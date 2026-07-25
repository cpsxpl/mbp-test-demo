package com.mbp.eng.framework.jdbc.sql.manager;

import com.mbp.eng.framework.jdbc.sql.table.FrameworkException;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve This type was created in VisualAge.
 * @Date: 2016-05-18
 */
public class DAOException extends FrameworkException {
    /**
     * Insert the method's description here. Creation date: (2/19/00 10:27:33
     * PM)
     */
    public DAOException() {
    }

    /**
     * Insert the method's description here. Creation date: (2/19/00 10:27:33
     * PM)
     */
    public DAOException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String errorMessage;

    Exception exception;

    /**
     * Insert the method's description here. Creation date: (2/19/00 10:27:33
     * PM)
     */
    public DAOException(Exception exception) {
        this.exception = exception;
    }
}
