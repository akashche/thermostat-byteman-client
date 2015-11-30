package com.redhat.thermostat.byteman.plot.swing;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;

import javax.swing.*;

import static com.redhat.thermostat.byteman.utils.ColorUtils.toColor;

/**
 * User: alexkasko
 * Date: 9/28/15
 */
public class ZoomableChartPanel extends ChartPanel {
    public ZoomableChartPanel(JFreeChart chart) {
        super(chart, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_MINIMUM_DRAW_WIDTH, DEFAULT_MINIMUM_DRAW_HEIGHT,
                DEFAULT_MAXIMUM_DRAW_WIDTH, DEFAULT_MAXIMUM_DRAW_HEIGHT, DEFAULT_BUFFER_USED,
                false, false, false, false, false, false);
        this.setDomainZoomable(true);
        this.setRangeZoomable(false);
//        this.setZoomOutlinePaint(Color.blue);
        this.setZoomFillPaint(toColor("#55ff9800"));
        this.addChartMouseListener(new RightClickListener());
    }

    private class RightClickListener implements ChartMouseListener {
        @Override
        public void chartMouseClicked(ChartMouseEvent event) {
            ChartEntity entity = event.getEntity();
            if (entity instanceof CategoryItemEntity) {
                CategoryItemEntity en = (CategoryItemEntity) entity;
//                System.out.println(en);
                // todo
                JPopupMenu menu = new JPopupMenu();
                menu.add(new JLabel("Value: " + en.getRowKey().toString()));

                menu.show(ZoomableChartPanel.this, event.getTrigger().getX(), event.getTrigger().getY());

            }
        }

        @Override
        public void chartMouseMoved(ChartMouseEvent event) {
        }
    }
}
