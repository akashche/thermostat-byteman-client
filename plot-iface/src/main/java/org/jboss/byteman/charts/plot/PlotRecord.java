package org.jboss.byteman.charts.plot;

import org.jboss.byteman.charts.data.DataRecord;

import java.util.List;

/**
 * User: alexkasko
 * Date: 9/26/15
 */
public interface PlotRecord {

    double getValue();

    String getCategory();

    long getPeriodStart();

    long getPeriodEnd();

    List<DataRecord> getDataRecords();
}
