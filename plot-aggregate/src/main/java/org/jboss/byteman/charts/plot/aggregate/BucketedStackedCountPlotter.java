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

/**
 * User: alexkasko
 * Date: 6/8/15
 */
public class BucketedStackedCountPlotter implements Plotter, ConfigurableChart {

    private static final Gson GSON = new Gson();

    private Config cf;

    @Override
    public JFreeChart build(Iterator<ChartRecord> data, List<ChartFilter> filters) {
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

    private BoundedCategoryDataset createDataset(Iterator<ChartRecord> data, List<ChartFilter> filters) {
        ArrayList<ChartRecord> renderTimes1 = new ArrayList<ChartRecord>();
        ArrayList<ChartRecord> renderTimes2 = new ArrayList<ChartRecord>();
        long longest1 = 0;
        long longest2 = 0;
        for (ChartRecord cr : DATA) {
            if ("reportRenderTime".equals(cr.getMarker())) {
                Number valNum = (Number) cr.getData().get("value");
                long val = valNum.longValue();
                if (552275 == ((Number) cr.getData().get("reportId")).intValue()) {
                    renderTimes1.add(cr);
                    if (val > longest1) longest1 = val;
                } else {
                    renderTimes2.add(cr);
                    if (val > longest2) longest2 = val;
                }
            }
        }
        int len = (int) (Math.max(longest1, longest2) / 1000) + 1;
        long[] counted1 = new long[len];
        for (ChartRecord cr : renderTimes1) {
            Number valNum = (Number) cr.getData().get("value");
            long val = valNum.longValue();
            int idx = (int) (val/1000);
            counted1[idx] += 1;
        }
        long[] counted2 = new long[len];
        for (ChartRecord cr : renderTimes2) {
            Number valNum = (Number) cr.getData().get("value");
            long val = valNum.longValue();
            int idx = (int) (val/1000);
            counted2[idx] += 1;
        }
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        long max1 = 1; int i1 = 0;
        for (long val : counted1) {
            if (val > max1) max1 = val;
            ds.addValue(val, "1", Long.toString(i1) + "-" + Long.toString(i1 + 1));
            i1 = i1 + 1;
        }
        long max2 = 1; int i2 = 0;
        for (long val : counted2) {
            if (val > max2) max2 = val;
            ds.addValue(val, "2", Long.toString(i2) + "-" + Long.toString(i2 + 1));
            i2 = i2 + 1;
        }
        long min = 0; long max = max1 + max2;

        return new BoundedCategoryDataset(ds, min, max);
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
    public void applyConfig(Map<String, ChartConfigEntry<?>> entries) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for(Map.Entry<String, ChartConfigEntry<?>> en : entries.entrySet()) {
            map.put(en.getKey(), en.getValue().getValue());
        }
        // todo: fixme with proper reflection
        String json = GSON.toJson(map);
        this.cf = GSON.fromJson(json, Config.class);
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

        Collection<? extends ChartConfigEntry<?>> toEntries() {
            return Arrays.asList(
                    // todo: name vs label
                    new StringConfigEntry("valueAttributeName", valueAttributeName),
                    new StringConfigEntry("categoryAttributeName", categoryAttributeName),
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
}
