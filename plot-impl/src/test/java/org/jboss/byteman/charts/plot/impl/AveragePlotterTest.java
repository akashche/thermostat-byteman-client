package com.redhat.thermostat.byteman.plot.impl;

import com.redhat.thermostat.byteman.plot.impl.AveragePlotter;
import junit.framework.Assert;
import com.redhat.thermostat.byteman.data.DataRecord;
import com.redhat.thermostat.byteman.filter.ChartFilter;
import com.redhat.thermostat.byteman.plot.PlotConfig;
import com.redhat.thermostat.byteman.plot.PlotRecord;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
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
                new DataRecord(0, "42", "42", "marker1", "value", 42, "category", "cat1"),
                new DataRecord(1000, "42", "42", "marker1", "value", 43, "category", "cat1"),
                new DataRecord(2000, "42", "42", "marker1", "value", 44, "category", "cat1")
        );
        PlotConfig conf = TestPlotConfig.builder()
                .withMaxRecords(3)
                .build();
        Collection<PlotRecord> recs = new AveragePlotter().createPlot(conf, 0, 3000,
                data.iterator(), Collections.<ChartFilter>emptyList());
//        System.out.println(recs);
//        new TestPlotRenderer().renderToFile(recs, "testSimple");
    }

    @Test
    public void testSparse() throws Exception {
        List<DataRecord> data = Arrays.asList(
                new DataRecord(400, "42", "42", "marker1", "value", 42, "category", "cat1"),
                new DataRecord(1400, "42", "42", "marker1", "value", 43, "category", "cat1"),
                new DataRecord(5400, "42", "42", "marker1", "value", 44, "category", "cat1"),
                new DataRecord(5600, "42", "42", "marker1", "value", 46, "category", "cat1")
        );
        PlotConfig conf = TestPlotConfig.builder()
                .withMaxRecords(6)
                .build();
        Collection<PlotRecord> recs = new AveragePlotter().createPlot(conf, 0, 6000,
                data.iterator(), Collections.<ChartFilter>emptyList());
//        System.out.println(recs);
//        new TestPlotRenderer().renderToFile(recs, "testSparse");
    }

    @Test
    public void testNegativePeriod() {
        List<DataRecord> data = Arrays.asList(new DataRecord(42, "42", "42", "marker1", "value", 42, "category", "cat1"));
        PlotConfig conf = TestPlotConfig.builder().build();
        Collection<PlotRecord> recs = new AveragePlotter().createPlot(conf, 2000, 1000,
                data.iterator(), Collections.<ChartFilter>emptyList());
        Assert.assertEquals(0, recs.size());
    }

    @Test
    public void testStepRounding() {
        List<DataRecord> data = Arrays.asList(new DataRecord(1, "42", "42", "marker1", "value", 42, "category", "cat1"));
        PlotConfig conf = TestPlotConfig.builder()
                .withMaxRecords(3)
                .build();
        Collection<PlotRecord> recs = new AveragePlotter().createPlot(conf, 0, 11,
                data.iterator(), Collections.<ChartFilter>emptyList());
        PlotRecord re = recs.iterator().next();
        Assert.assertEquals(0, re.getPeriodStart());
        Assert.assertEquals(3, re.getPeriodEnd());
    }

    @Test
    public void testNarrow() throws Exception {
        List<DataRecord> data = Arrays.asList(
                new DataRecord(0, "42", "42", "marker1", "value", 42, "category", "cat1"),
                new DataRecord(1, "42", "42", "marker1", "value", 42, "category", "cat1"),
                new DataRecord(2, "42", "42", "marker1", "value", 42, "category", "cat1")
        );
        PlotConfig conf = TestPlotConfig.builder()
                .withMaxRecords(4)
                .build();
        Collection<PlotRecord> recs = new AveragePlotter().createPlot(conf, 0, 3,
                data.iterator(), Collections.<ChartFilter>emptyList());
        PlotRecord re = recs.iterator().next();
        Assert.assertEquals(0, re.getPeriodStart());
        Assert.assertEquals(1, re.getPeriodEnd());
        new TestPlotRenderer().renderToFile(recs, "testNarrow");
    }

}
