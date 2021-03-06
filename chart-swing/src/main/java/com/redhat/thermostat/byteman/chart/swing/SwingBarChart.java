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
package com.redhat.thermostat.byteman.chart.swing;

import com.redhat.thermostat.byteman.chart.*;
import net.miginfocom.swing.MigLayout;
import com.redhat.thermostat.byteman.data.DataRecord;
import com.redhat.thermostat.byteman.filter.ChartFilter;
import com.redhat.thermostat.byteman.plot.PlotConfig;
import com.redhat.thermostat.byteman.plot.PlotException;
import com.redhat.thermostat.byteman.plot.PlotRecord;
import com.redhat.thermostat.byteman.plot.Plotter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static com.redhat.thermostat.byteman.utils.ColorUtils.toColor;

/**
 * Generic implementation of the bar chart using JFreeCharts library
 *
 * @author akashche
 * Date: 9/27/15
 */
public class SwingBarChart implements Configurable {
    private final Plotter plotter;
    private final Iterable<DataRecord> data;
    private Collection<? extends ChartFilter> filters;
    private long periodStart = 0;
    private long periodEnd = 1;

    private final Config cf = new Config();
    private final Manager zm = new Manager();

    /**
     * Constructor
     *
     * @param plotter plotter instance that will be used to aggregate data
     * @param data raw data iterator
     * @param filters set of filters to apply to raw data
     */
    public SwingBarChart(Plotter plotter, Iterable<DataRecord> data, Collection<? extends ChartFilter> filters) {
        this.plotter = plotter;
        this.data = data;
        this.filters = filters;
        extractPeriod(filters);
    }

    /**
     * Creates swing panel containing rendered chart
     *
     * @return swing panel containing rendered chart
     */
    public JPanel createChartPanel() {
        return zm.createPanel();
    }

    /**
     * Applies filters to rendered chart
     *
     * @param filters set of filters to apply
     * @return this chart instance
     */
    public synchronized SwingBarChart applyFilters(Collection<? extends ChartFilter> filters) {
        extractPeriod(filters);
        this.filters = filters;
        zm.reset();
        return this;
    }

    /**
     * Resets zoom level to default
     */
    public synchronized void unzoom() {
        zm.reset();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<? extends ChartConfigEntry<?>> availableConfig() {
        return cf.toEntries();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Configurable applyConfig(Map<String, ChartConfigEntry<?>> entries) {
        for (Map.Entry<String, ChartConfigEntry<?>> en : entries.entrySet()) {
            setFieldValue(en.getKey(), en.getValue().getValue());
        }
        return this;
    }

    // todo: think about making 'magic' filters explicit
    private void extractPeriod(Collection<? extends ChartFilter> filters) {
        for (ChartFilter fi : filters) {
            ChartConfigEntry<?> cce = fi.configEntry();
            if ("timestampFrom".equals(cce.getName())) {
                periodStart = ((Date) cce.getValue()).getTime();
            } else if ("timestampTo".equals(cce.getName())) {
                periodEnd = ((Date) cce.getValue()).getTime();
            }
        }
    }

    private JFreeChart createChart(Collection<PlotRecord> records) {
        // data
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for (PlotRecord re : records) {
            Long label = re.getPeriodStart() + ((re.getPeriodEnd() - re.getPeriodStart())/2);
            ds.addValue(re.getValue(), re.getCategory(), label);
        }
        // chart
        BarRenderer3D br = new StackedBarRenderer3D(cf.rendered3dXOffset, cf.rendered3dYOffset);
        ZoomablePlot plot = new ZoomablePlot(zm, ds, new CategoryAxis(), new NumberAxis(), br);
        plot.setOrientation(PlotOrientation.VERTICAL);
        JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        ChartFactory.getChartTheme().apply(chart);
        // renderer
        br.setSeriesPaint(0, toColor(cf.seriesPaint0));
        br.setSeriesPaint(1, toColor(cf.seriesPaint1));
        br.setSeriesPaint(2, toColor(cf.seriesPaint2));
        br.setSeriesPaint(3, toColor(cf.seriesPaint3));
        br.setSeriesPaint(4, toColor(cf.seriesPaint4));
        br.setSeriesPaint(5, toColor(cf.seriesPaint5));
        br.setWallPaint(toColor(cf.wallPaint));
        br.setBaseItemLabelsVisible(cf.baseItemLabelsVisible);
        br.setShadowVisible(cf.shadowVisible);
        br.setItemMargin(cf.itemMargin);
        // plot
        plot.setBackgroundPaint(toColor(cf.backgroundPaint));
        plot.setBackgroundImageAlpha((float) cf.backgroundImageAlpha);
        plot.setDomainGridlinesVisible(cf.domainGridlinesVisible);
        plot.setRangeGridlinePaint(toColor(cf.rangeGridlinePaint));
//        plot.getRangeAxis().setRange(new Range(ds.getMin(), ds.getMax() * 1.1));
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.getRangeAxis().setLabel(cf.rangeAxisLabel);
        colorAxis(plot.getRangeAxis());
        colorAxis(plot.getDomainAxis());
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI * cf.domainAxisLabelAngle));
        plot.getDomainAxis().setLowerMargin(cf.domainAxisLowerMargin);
        plot.getDomainAxis().setUpperMargin(cf.domainAxisUpperMargin);
        plot.getDomainAxis().setLabel(cf.domainAxisLabel);
        plot.setOutlineVisible(cf.outlineVisible);

        return chart;
    }

    public static Collection<? extends ChartConfigEntry<?>> defaultConfig() {
        return new Config().toEntries();
    }

    private void setFieldValue(String fieldName, Object fieldValue) {
        try {
            Field fi = Config.class.getDeclaredField(fieldName);
            fi.setAccessible(true);
            fi.set(this.cf, fieldValue);
        } catch (NoSuchFieldException e) {
            throw new PlotException("Config apply error, name: [" + fieldName + "], value: ["  + fieldValue + "]", e);
        } catch (IllegalAccessException e) {
            throw new PlotException("Config apply error, name: [" + fieldName + "], value: ["  + fieldValue + "]", e);
        }
    }

    private void colorAxis(Axis ax) {
        ax.setAxisLinePaint(toColor(cf.axisLinePaint));
        ax.setTickMarkPaint(toColor(cf.tickMarkPaint));
        ax.setTickLabelPaint(toColor(cf.tickLabelPaint));
    }

    private class Manager implements ZoomManager {
        private final JPanel panel = new JPanel(new MigLayout(
                "fill, insets 0",
                "[fill]",
                "[fill]"
        ));
        private float prevLower = 0f;
        private float prevUpper = 1f;


        @Override
        // todo: use SwingWorker
        public synchronized void zoom(float lower, float upper) {
            float prevInterval = prevUpper - prevLower;
            float curLower = prevLower + prevInterval * lower;
            float curUpper = prevUpper - prevInterval * (1 - upper);
            prevLower = curLower;
            prevUpper = curUpper;
            rescale(curLower, curUpper);
        }

        @Override
        // todo: use SwingWorker
        public synchronized void reset() {
            prevLower = 0f;
            prevUpper = 1f;
            rescale(0f, 1f);
        }

        private void rescale(float lower, float upper) {
            ChartPanel cp = buildChartPanel(lower, upper);
            // data corner case, should be fixed in aggregator
            if (null != cp) {
                panel.removeAll();
                panel.revalidate();
                panel.add(cp);
                panel.repaint();
            }
        }

        private ChartPanel buildChartPanel(float lower, float upper) {
            long delta = periodEnd - periodStart;
            long from = periodStart + (long)(delta * lower);
            long to = periodEnd - (long)(delta * (1 - upper));
            Collection<PlotRecord> records = plotter.createPlot(cf, from, to, data.iterator(), filters);
            JFreeChart chart = createChart(records);
            return new ZoomableChartPanel(chart);
        }

        public JPanel createPanel() {
            reset();
            return panel;
        }
    }

    private static class Config implements PlotConfig {
        String valueAttributeName = "value";
        String categoryAttributeName = "category";

        private double maxRecords = 24;
        private boolean ignoreAbsentCategory = true;
        private boolean ignoreAbsentValue = true;
        private boolean ignoreInvalidValue = true;

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
        String seriesPaint0 = "#00FFFFFF";
        String seriesPaint1 = "#BB669900";
        String seriesPaint2 = "#BBFF8800";
        String seriesPaint3 = "#BBCC0000";
        String seriesPaint4 = "#BB0099CC";
        String seriesPaint5 = "#BB9933CC";
        String wallPaint = "#FFEEEEEE";
        String axisLinePaint = "#FFAAAAAA";
        String tickMarkPaint = "#FFAAAAAA";
        String tickLabelPaint = "#FF222222";


        @Override
        public String getValueAttributeName() {
            return valueAttributeName;
        }

        @Override
        public String getCategoryAttributeName() {
            return categoryAttributeName;
        }

        @Override
        public int getMaxRecords() {
            return (int) maxRecords;
        }

        @Override
        public boolean isIgnoreAbsentCategory() {
            return ignoreAbsentCategory;
        }

        @Override
        public boolean isIgnoreAbsentValue() {
            return ignoreAbsentValue;
        }

        @Override
        public boolean isIgnoreInvalidValue() {
            return ignoreInvalidValue;
        }

        Collection<? extends ChartConfigEntry<?>> toEntries() {
            return Arrays.asList(
                    // todo: name vs label
                    new StringConfigEntry("valueAttributeName", valueAttributeName),
                    new StringConfigEntry("categoryAttributeName", categoryAttributeName),

                    new DoubleConfigEntry("maxRecords", maxRecords, 1, 64, 1),
                    new BoolConfigEntry("ignoreAbsentCategory", ignoreAbsentCategory),
                    new BoolConfigEntry("ignoreAbsentValue", ignoreAbsentValue),
                    new BoolConfigEntry("ignoreInvalidValue", ignoreInvalidValue),

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
//                    new StringConfigEntry("backgroundPaint", backgroundPaint),
                    new DoubleConfigEntry("backgroundImageAlpha", backgroundImageAlpha, 0, 1, 0.1)
//                    new StringConfigEntry("rangeGridlinePaint", rangeGridlinePaint),

                    //todo: color chooser
//                    new StringConfigEntry("seriesPaint1", seriesPaint1),
//                    new StringConfigEntry("seriesPaint2", seriesPaint2),
//                    new StringConfigEntry("seriesPaint3", seriesPaint3),
//                    new StringConfigEntry("seriesPaint4", seriesPaint4),
//                    new StringConfigEntry("seriesPaint5", seriesPaint5),
//
//                    new StringConfigEntry("wallPaint", wallPaint),
//                    new StringConfigEntry("axisLinePaint", axisLinePaint),
//                    new StringConfigEntry("tickMarkPaint", tickMarkPaint),
//                    new StringConfigEntry("tickLabelPaint", tickLabelPaint)
            );
        }
    }
}
