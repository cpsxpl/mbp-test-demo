package com.mbp.eng.framework.jdbc.sql.table;
/**
 * ===========================================================================
 * Copyright AlwaysCome Ltd. All Rights Reserved
 * ===========================================================================
 **/

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @version 1.02, 01/01/00
 * @preserve <code>EcomFrameworkException</code> class is the base class for exceptions
 * thrown by the EcomFramework.Subclasses of this class generally represent
 * configuration or other environmental error conditions.
 */
public class FrameworkException extends Exception {
    /**
     * Constructs a EcomFrameworkException object.
     */
    public FrameworkException() {
    }

    /**
     * Constructs a EcomFrameworkException object with a descriptive message.
     *
     * @param message A message describing the exception.
     */
    public FrameworkException(String message) {
        super(message);
    }
}