package org.jboss.byteman.charts.swing.config;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public class TextField implements ChartConfigField {
    @Override
    public ChartConfigLabelBuilder labelBuilder() {
        return new DefaultLabelBuilder();
    }

    @Override
    public ChartConfigControlBuilder controlBuilder() {
        return new TextControlBuilder();
    }
}
