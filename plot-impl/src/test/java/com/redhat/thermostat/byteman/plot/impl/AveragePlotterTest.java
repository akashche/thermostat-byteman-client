/*
 * Copyright 2012-2015 Red Hat, Inc.
 *
 * This file is part of Thermostat.
 *
 * Thermostat is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2, or (at your
 * option) any later version.
 *
 * Thermostat is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Thermostat; see the file COPYING.  If not see
 * <http://www.gnu.org/licenses/>.
 *
 * Linking this code with other modules is making a combined work
 * based on this code.  Thus, the terms and conditions of the GNU
 * General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this code give
 * you permission to link this code with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also
 * meet, for each linked independent module, the terms and conditions
 * of the license of that module.  An independent module is a module
 * which is not derived from or based on this code.  If you modify
 * this code, you may extend this exception to your version of the
 * library, but you are not obligated to do so.  If you do not wish
 * to do so, delete this exception statement from your version.
 */
package com.redhat.thermostat.byteman.plot.impl;

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
 * @author akashche
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
