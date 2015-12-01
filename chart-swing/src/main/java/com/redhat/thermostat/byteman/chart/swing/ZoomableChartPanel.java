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

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;

import javax.swing.*;

import static com.redhat.thermostat.byteman.utils.ColorUtils.toColor;

/**
 * Extension for JFreeCharts' chart panel that supports
 * chart zooming using horizontal mouse drag
 *
 * @author akashche
 * Date: 9/28/15
 */
public class ZoomableChartPanel extends ChartPanel {
    /**
     * Constructor
     *
     * @param chart chart object
     */
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
