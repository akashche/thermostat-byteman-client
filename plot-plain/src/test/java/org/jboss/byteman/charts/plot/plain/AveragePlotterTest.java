package org.jboss.byteman.charts.plot.plain;

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
                new DataRecord(1, "42", "42", "marker1", "value", 43),
                new DataRecord(2, "42", "42", "marker1", "value", 44)
        );
        PlotConfig conf = TestPlotConfig.builder().build();
        List<PlotRecord> recs = new AveragePlotter().createPlot(conf, data.iterator(), Collections.<ChartFilter>emptyList());
        new TestPlotRenderer().renderToFile(recs);
    }
}
