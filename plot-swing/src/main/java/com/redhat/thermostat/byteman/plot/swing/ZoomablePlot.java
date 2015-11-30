package com.redhat.thermostat.byteman.plot.swing;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;

import java.awt.geom.Point2D;

/**
 * User: alexkasko
 * Date: 9/28/15
 */
public class ZoomablePlot extends CategoryPlot {
    private final ZoomManager zm;

    public ZoomablePlot(ZoomManager zm, CategoryDataset dataset, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryItemRenderer renderer) {
        super(dataset, domainAxis, rangeAxis, renderer);
        this.zm = zm;
    }

    @Override
    public boolean isDomainZoomable() {
        return true;
    }

    @Override
    public void zoomDomainAxes(double lowerPercent, double upperPercent, PlotRenderingInfo state, Point2D source) {
        zm.zoom((float) lowerPercent, (float) upperPercent);
    }

    @Override
    public void zoomRangeAxes(double factor, PlotRenderingInfo state, Point2D source) {
        // noop
    }

}
