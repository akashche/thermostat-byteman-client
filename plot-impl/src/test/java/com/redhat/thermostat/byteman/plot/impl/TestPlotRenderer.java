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

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import com.redhat.thermostat.byteman.plot.PlotRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;

import static com.redhat.thermostat.byteman.utils.ColorUtils.toColor;
import static com.redhat.thermostat.byteman.utils.IOUtils.closeQuietly;
import static com.redhat.thermostat.byteman.utils.string.StringUtils.EMPTY_STRING;

/**
 * User: alexkasko
 * Date: 9/27/15
 */
class TestPlotRenderer {

    void renderToFile(Collection<PlotRecord> records, String filename) throws Exception {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for (PlotRecord re : records) {
            Long label = re.getPeriodStart() + ((re.getPeriodEnd() - re.getPeriodStart())/2);
            ds.addValue(re.getValue(), re.getCategory(), label);
        }
        JFreeChart chart = ChartFactory.createStackedBarChart(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, ds, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(toColor("#FFFFFFFF"));
        plot.setBackgroundImageAlpha((float) 0.0d);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(toColor("#FFAAAAAA"));
//        plot.getRangeAxis().setRange(new Range(ds.getMin(), ds.getMax() * 1.1));
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//        plot.getRangeAxis().setLabel(cf.rangeAxisLabel);
//        colorAxis(plot.getRangeAxis());
//        colorAxis(plot.getDomainAxis());
//        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI * 0.12d));
//        plot.getDomainAxis().setLowerMargin(cf.domainAxisLowerMargin);
//        plot.getDomainAxis().setUpperMargin(cf.domainAxisUpperMargin);
//        plot.getDomainAxis().setLabel(cf.domainAxisLabel);
        BarRenderer3D barrenderer = new StackedBarRenderer3D(16.0d, 12.0d);
        barrenderer.setSeriesPaint(0, toColor("#00FFFFFF"));
        barrenderer.setSeriesPaint(1, toColor("#BB669900"));
        barrenderer.setSeriesPaint(2, toColor("#BBFF8800"));
        barrenderer.setWallPaint(toColor("#FFEEEEEE"));
//        barrenderer.setBaseItemLabelsVisible(cf.baseItemLabelsVisible);
        barrenderer.setShadowVisible(false);
        barrenderer.setItemMargin(0.0d);
        plot.setRenderer(barrenderer);
        plot.setOutlineVisible(false);

        chartToSvg(chart, 1024, 600, filename);
    }

    private static void chartToSvg(JFreeChart chart, int width, int height, String filename) throws Exception {
        Writer writer = null;
        try {
            DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
            Document document = domImpl.createDocument(null, "svg", null);
            SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
            svgGenerator.setSVGCanvasSize(new Dimension(width, height));
            chart.draw(svgGenerator, new Rectangle(width, height));
            OutputStream os = new FileOutputStream(filename + ".svg");
            writer = new BufferedWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
            svgGenerator.stream(writer);
        } finally {
            closeQuietly(writer);
        }
    }
}
