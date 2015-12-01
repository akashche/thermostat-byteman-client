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
package com.redhat.thermostat.byteman.ui.swing.pages;

import net.miginfocom.swing.MigLayout;
import com.redhat.thermostat.byteman.plot.Plotter;
import com.redhat.thermostat.byteman.ui.swing.util.ColumnFitTable;
import com.redhat.thermostat.byteman.ui.swing.util.PlotterTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import static com.redhat.thermostat.byteman.utils.SwingUtils.createFormSectionBorder;

/**
 * User: alexkasko
 * Date: 6/15/15
 */
class AggregateChartsPage extends BasePage {
    static final String NAME = "aggregate_charts";

    private JTable plotsTable = new JTable();

    AggregateChartsPage(ChartsAppContext ctx) {
        super(ctx, NAME, "Aggregate Charts", "mimetype_colorscm_16.png");
    }

    @Override
    public Component createPane() {
        JPanel jp = new JPanel(new MigLayout(
                "fill",
                "[]",
                "[top]"
        ));
        jp.add(createTopPanel(), "growx, wrap");
        return jp;
    }

    private Component createTopPanel() {
        JPanel jp = new JPanel(new MigLayout(
                "fill",
                "",
                ""
        ));
        jp.setBorder(createFormSectionBorder(jp.getBackground().darker(), "List of aggregate chart types"));
        jp.add(createPlotsTable(), "height ::256lp, growx");
        return jp;
    }

    private Component createPlotsTable() {
        JPanel jp = new JPanel(new MigLayout(
                "fill, insets 0",
                "[]",
                "[top]"
        ));
        PlotterTableModel tm = new PlotterTableModel(Collections.<Plotter>emptyList());
        plotsTable = new ColumnFitTable(tm);
        plotsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        JScrollPane sp = new JScrollPane(plotsTable, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setBorder(createMatteBorder(1, 1, 1, 1, plotsTable.getBackground().darker()));
        jp.add(sp, "grow");
        return jp;
    }
}