package org.jboss.byteman.charts.plot;

/**
 * User: alexkasko
 * Date: 9/26/15
 */
public interface PlotConfig {

    String getValueAttributeName();

    String getCategoryAttributeName();

    int getMaxRecords();

    boolean isIgnoreAbsentCategory();

    boolean isIgnoreAbsentValue();

    boolean isIgnoreInvalidValue();
}
