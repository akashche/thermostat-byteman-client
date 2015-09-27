package org.jboss.byteman.charts.plot;

/**
 * User: alexkasko
 * Date: 9/26/15
 */
public interface PlotConfig {

    String getValueAttributeName();

    int getMaxRecords();

    long getMinTimestamp();

    long getMaxTimestamp();

    boolean isIgnoreAbsentMarker();

    boolean isIgnoreAbsentValue();

    boolean isIgnoreInvalidValue();
}
