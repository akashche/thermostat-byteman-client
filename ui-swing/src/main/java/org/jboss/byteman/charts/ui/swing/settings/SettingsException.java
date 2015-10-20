package org.jboss.byteman.charts.ui.swing.settings;

/**
 * User: alexkasko
 * Date: 10/20/15
 */
public class SettingsException extends RuntimeException {
    public SettingsException(String message) {
        super(message);
    }

    public SettingsException(String message, Throwable cause) {
        super(message, cause);
    }
}
