package org.jboss.byteman.charts.plot.plain;

import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.plot.PlotConfig;
import org.jboss.byteman.charts.plot.PlotRecord;
import org.jboss.byteman.charts.plot.Plotter;

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
