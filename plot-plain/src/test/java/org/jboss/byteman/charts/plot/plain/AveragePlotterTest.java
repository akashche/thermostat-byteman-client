package org.jboss.byteman.charts.plot.plain;

import com.google.gson.GsonBuilder;
import junit.framework.Assert;
import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.plot.PlotConfig;
import org.jboss.byteman.charts.plot.PlotRecord;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: alexkasko
 * Date: 9/27/15
 */
public class AveragePlotterTest {

    @Test
    public void testSimple() throws Exception {
        List<DataRecord> data = Arrays.asList(
                new DataRecord(0, "42", "42", "marker1", "value", 42),
                new DataRecord(1000, "42", "42", "marker1", "value", 43),
                new DataRecord(2000, "42", "42", "marker1", "value", 44)
        );
        PlotConfig conf = TestPlotConfig.builder()
                .withMaxRecords(3)
                .withMinTimestamp(0)
                .withMaxTimestamp(3000)
                .build();
        List<PlotRecord> recs = new AveragePlotter().createPlot(conf, data.iterator(), Collections.<ChartFilter>emptyList());
//        System.out.println(recs);
//        new TestPlotRenderer().renderToFile(recs, "testSimple");
    }

    @Test
    public void testSparse() throws Exception {
        List<DataRecord> data = Arrays.asList(
                new DataRecord(400, "42", "42", "marker1", "value", 42),
                new DataRecord(1400, "42", "42", "marker1", "value", 43),
                new DataRecord(5400, "42", "42", "marker1", "value", 44),
                new DataRecord(5600, "42", "42", "marker1", "value", 46)
        );
        PlotConfig conf = TestPlotConfig.builder()
                .withMaxRecords(6)
                .withMinTimestamp(0)
                .withMaxTimestamp(6000)
                .build();
        List<PlotRecord> recs = new AveragePlotter().createPlot(conf, data.iterator(), Collections.<ChartFilter>emptyList());
//        System.out.println(recs);
//        new TestPlotRenderer().renderToFile(recs, "testSparse");

    }

    // corner cases:
    // - min >= max
    // - (last) step rounding
    // - step is less than millisecond

}
