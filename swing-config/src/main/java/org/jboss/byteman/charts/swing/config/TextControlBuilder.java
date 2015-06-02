package org.jboss.byteman.charts.swing.config;

import javax.swing.*;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public class TextControlBuilder implements ChartConfigControlBuilder {

    @Override
    public JComponent build() {
        return new JTextField();
    }

    @Override
    public String layoutOptions() {
        return "";
    }
}
