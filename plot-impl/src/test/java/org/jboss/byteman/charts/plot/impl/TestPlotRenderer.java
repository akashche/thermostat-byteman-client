package org.jboss.byteman.charts.plot.impl;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jboss.byteman.charts.plot.PlotRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import static org.jboss.byteman.charts.plot.PlotUtils.toColor;
import static org.jboss.byteman.charts.utils.IOUtils.closeQuietly;
import static org.jboss.byteman.charts.utils.string.StringUtils.EMPTY_STRING;

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
