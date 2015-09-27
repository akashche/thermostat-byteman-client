package org.jboss.byteman.charts.plot;

/**
 * User: alexkasko
 * Date: 9/26/15
 */
public interface PlotRecord {

    double getValue();

    int getCount();

    String getMarker();

    long getPeriodStart();

    long getPeriodEnd();
}
