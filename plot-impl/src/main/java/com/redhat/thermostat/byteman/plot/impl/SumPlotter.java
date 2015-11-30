package com.redhat.thermostat.byteman.plot.impl;

import com.redhat.thermostat.byteman.data.DataRecord;
import com.redhat.thermostat.byteman.filter.ChartFilter;
import com.redhat.thermostat.byteman.plot.PlotConfig;
import com.redhat.thermostat.byteman.plot.PlotRecord;
import com.redhat.thermostat.byteman.plot.Plotter;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: alexkasko
 * Date: 9/26/15
 */
public class SumPlotter implements Plotter {
    @Override
    public String getName() {
        return "Summation chart";
    }

    @Override
    public String getType() {
        return "PLAIN";
    }

    @Override
    public String getDescription() {
        return "Shows sum of the values";
    }

    @Override
    public Collection<PlotRecord> createPlot(PlotConfig config, long minTimestamp, long maxTimestamp, Iterator<DataRecord> data, Collection<? extends ChartFilter> filters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
