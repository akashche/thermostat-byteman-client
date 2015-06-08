/*
* JBoss, Home of Professional Open Source
* Copyright 2015 Red Hat and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.byteman.charts.plot.aggregate;

import com.google.gson.Gson;
import org.jboss.byteman.charts.data.ChartRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.filter.ChartFilteredIterator;
import org.jboss.byteman.charts.plot.BoundedCategoryDataset;
import org.jboss.byteman.charts.plot.Plotter;
import org.jboss.byteman.charts.ui.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.*;

import static org.jboss.byteman.charts.plot.PlotUtils.toColor;
import static org.jboss.byteman.charts.utils.StringUtils.EMPTY_STRING;
import static org.jboss.byteman.charts.utils.StringUtils.defaultString;
import static org.jboss.byteman.charts.utils.collection.SingleUseIterable.singleUseIterable;

/**
 * User: alexkasko
 * Date: 6/8/15
 */
public class BucketedStackedCountPlotter implements Plotter, ConfigurableChart {

    public static final Gson GSON = new Gson();

    private Config cf;

    @Override
    public JFreeChart build(Iterator<ChartRecord> data, Collection<? extends ChartFilter> filters) {
        BoundedCategoryDataset ds = createDataset(data, filters);
        JFreeChart chart = ChartFactory.createStackedBarChart(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, ds.getDataset(), PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(toColor(cf.backgroundPaint));
        plot.setBackgroundImageAlpha((float) cf.backgroundImageAlpha);
        plot.setDomainGridlinesVisible(cf.domainGridlinesVisible);
        plot.setRangeGridlinePaint(toColor(cf.rangeGridlinePaint));
        plot.getRangeAxis().setRange(new Range(ds.getMin(), ds.getMax() * 1.1));
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.getRangeAxis().setLabel(cf.rangeAxisLabel);
        colorAxis(plot.getRangeAxis());
        colorAxis(plot.getDomainAxis());
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI * cf.domainAxisLabelAngle));
        plot.getDomainAxis().setLowerMargin(cf.domainAxisLowerMargin);
        plot.getDomainAxis().setUpperMargin(cf.domainAxisUpperMargin);
        plot.getDomainAxis().setLabel(cf.domainAxisLabel);
        BarRenderer3D barrenderer = new StackedBarRenderer3D(cf.rendered3dXOffset, cf.rendered3dYOffset);
        // todo: list
        barrenderer.setSeriesPaint(0, toColor(cf.seriesPaint0));
        barrenderer.setSeriesPaint(1, toColor(cf.seriesPaint1));
        barrenderer.setWallPaint(toColor(cf.wallPaint));
        barrenderer.setBaseItemLabelsVisible(cf.baseItemLabelsVisible);
        barrenderer.setShadowVisible(cf.shadowVisible);
        barrenderer.setItemMargin(cf.itemMargin);
        plot.setRenderer(barrenderer);
        plot.setOutlineVisible(cf.outlineVisible);
        return chart;
    }

    private BoundedCategoryDataset createDataset(Iterator<ChartRecord> data, Collection<? extends ChartFilter> filters) {
        Iterator<ChartRecord> filtered = new ChartFilteredIterator(data, filters);
        Map<String, Map<Long, Bucket>> catmap = new LinkedHashMap<String, Map<Long, Bucket>>();
        Map<Long, Counter> comap = new LinkedHashMap<Long, Counter>();
        long minkey = Long.MAX_VALUE, maxkey = Long.MIN_VALUE;
        for (ChartRecord cr : singleUseIterable(filtered)) {
            Long val = ((Number) cr.getData().get(cf.valueAttributeName)).longValue();
            if (null == val) continue;
            long bucketkey = val - (val % cf.step);
            if (bucketkey < minkey) minkey = bucketkey;
            if (bucketkey > maxkey) maxkey = bucketkey;
            Counter co = getCounterDefault(comap, bucketkey);
            co.increment();
            String cat = "" + cr.getData().get(cf.categoryAttributeName);
            Map<Long, Bucket> bucketmap = getCatDefault(catmap, cat);
            Bucket bu = getBucketDefault(bucketmap, bucketkey);
            bu.increment();
        }
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for (Map.Entry<String, Map<Long, Bucket>> encat : catmap.entrySet()) {
            String cat = encat.getKey();
            Map<Long, Bucket> bucketmap = encat.getValue();
            for (long i = minkey; i <= maxkey; i += cf.step) {
                Bucket bu = getBucketDefault(bucketmap, i);
                ds.addValue(bu.count, cat, bu.name());
            }
        }
        int max = Integer.MIN_VALUE;
        for (Counter co : comap.values()) {
            if (co.count > max) max = co.count;
        }
        return new BoundedCategoryDataset(ds, 0, max);
    }

    private void colorAxis(Axis ax) {
        ax.setAxisLinePaint(toColor(cf.axisLinePaint));
        ax.setTickMarkPaint(toColor(cf.tickMarkPaint));
        ax.setTickLabelPaint(toColor(cf.tickLabelPaint));
    }

    @Override
    public Collection<? extends ChartConfigEntry<?>> availableConfig() {
        return cf.toEntries();
    }

    @Override
    public BucketedStackedCountPlotter applyConfig(Map<String, ChartConfigEntry<?>> entries) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for(Map.Entry<String, ChartConfigEntry<?>> en : entries.entrySet()) {
            map.put(en.getKey(), en.getValue().getValue());
        }
        // todo: fixme with proper reflection
        String json = GSON.toJson(map);
        this.cf = GSON.fromJson(json, Config.class);
        return this;
    }

    private Map<Long, Bucket> getCatDefault(Map<String, Map<Long, Bucket>> catmap, String key) {
        Map<Long, Bucket> nullable = catmap.get(key);
        if (null != nullable) return nullable;
        Map<Long, Bucket> en = new LinkedHashMap<Long, Bucket>();
        catmap.put(key, en);
        return en;
    }

    private Bucket getBucketDefault(Map<Long, Bucket> bucketmap, long key) {
        Bucket nullable = bucketmap.get(key);
        if (null != nullable) return nullable;
        Bucket bu = new Bucket(key, key + cf.step);
        bucketmap.put(key, bu);
        return bu;
    }

    private Counter getCounterDefault(Map<Long, Counter> comap, long key) {
        Counter nullable = comap.get(key);
        if (null != nullable) return nullable;
        Counter co = new Counter();
        comap.put(key, co);
        return co;
    }

    private static class Config {
        String rangeAxisLabel = "Number of entries";
        String domainAxisLabel = "Elapsed time intervals";
        boolean domainGridlinesVisible = true;
        double domainAxisLowerMargin = 0.01d;
        double domainAxisUpperMargin = 0.01d;
        double domainAxisLabelAngle = 0.12d;
        double rendered3dXOffset = 16.0d;
        double rendered3dYOffset = 12.0d;
        boolean baseItemLabelsVisible = false;
        boolean shadowVisible = false;
        double itemMargin = 0.0d;
        boolean outlineVisible = false;

        String backgroundPaint = "#FFFFFFFF";
        double backgroundImageAlpha = 0.0d;
        String rangeGridlinePaint = "#FFAAAAAA";
        String seriesPaint0 = "#BB669900";
        String seriesPaint1 = "#BBFF8800";
        String wallPaint = "#FFEEEEEE";
        String axisLinePaint = "#FFAAAAAA";
        String tickMarkPaint = "#FFAAAAAA";
        String tickLabelPaint = "#FF222222";

        String valueAttributeName = "value";
        String categoryAttributeName = "category";
        int step = 1000;

        Collection<? extends ChartConfigEntry<?>> toEntries() {
            return Arrays.asList(
                    // todo: name vs label
                    new StringConfigEntry("valueAttributeName", valueAttributeName),
                    new StringConfigEntry("categoryAttributeName", categoryAttributeName),
                    new IntConfigEntry("step", step, 10, Integer.MAX_VALUE, 1),

                    new StringConfigEntry("rangeAxisLabel", rangeAxisLabel),
                    new StringConfigEntry("domainAxisLabel", domainAxisLabel),
                    new BoolConfigEntry("domainGridlinesVisible", domainGridlinesVisible),
                    new DoubleConfigEntry("domainAxisLowerMargin", domainAxisLowerMargin, 0, 1, 0.1),
                    new DoubleConfigEntry("domainAxisUpperMargin", domainAxisUpperMargin, 0, 1, 0.1),
                    new DoubleConfigEntry("domainAxisLabelAngle", domainAxisLabelAngle, 0, 1, 0.1),
                    new DoubleConfigEntry("rendered3dXOffset", rendered3dXOffset, 0, 100, 0.1),
                    new DoubleConfigEntry("rendered3dYOffset", rendered3dYOffset, 0, 100, 0.1),
                    new BoolConfigEntry("baseItemLabelsVisible", baseItemLabelsVisible),
                    new BoolConfigEntry("shadowVisible", shadowVisible),
                    new DoubleConfigEntry("itemMargin", itemMargin, 0, 100, 0.1),
                    new BoolConfigEntry("outlineVisible", outlineVisible),
                    new StringConfigEntry("backgroundPaint", backgroundPaint),
                    new DoubleConfigEntry("backgroundImageAlpha", backgroundImageAlpha, 0, 1, 0.1),
                    new StringConfigEntry("rangeGridlinePaint", rangeGridlinePaint),
                    new StringConfigEntry("seriesPaint0", seriesPaint0),
                    new StringConfigEntry("seriesPaint1", seriesPaint1),
                    new StringConfigEntry("wallPaint", wallPaint),
                    new StringConfigEntry("axisLinePaint", axisLinePaint),
                    new StringConfigEntry("tickMarkPaint", tickMarkPaint),
                    new StringConfigEntry("tickLabelPaint", tickLabelPaint)
            );
        }
    }

    private static class Bucket {
        final long from;
        final long to;
        int count = 0;

        Bucket(long from, long to) {
            this.from = from;
            this.to = to;
        }

        void increment() {
            count += 1;
        }

        String name() {
            // todo: make tunable
            return from + "-" + to;
        }
    }

    private static class Counter {
        int count = 0;

        void increment() {
            count += 1;
        }
    }
}
