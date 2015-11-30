package com.redhat.thermostat.byteman.plot;

import com.redhat.thermostat.byteman.data.DataRecord;

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
