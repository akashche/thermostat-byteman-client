package com.redhat.thermostat.byteman.utils;

/**
 * User: alexkasko
 * Date: 5/25/15
 */
public class UtilsException extends RuntimeException {

    public UtilsException(String message) {
        super(message);
    }

    public UtilsException(String message, Throwable cause) {
        super(message, cause);
    }
}
