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
package org.jboss.byteman.charts.ui.swing.pages;

import net.miginfocom.swing.MigLayout;
import org.jboss.byteman.charts.data.ChartRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.plot.aggregate.BucketedStackedCountPlotter;
import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.StringConfigEntry;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static javax.swing.BorderFactory.createMatteBorder;
import static org.jboss.byteman.charts.utils.SwingUtils.createFormSectionBorder;

/**
 * User: alexkasko
 * Date: 6/23/15
 */
class FiltersetPage extends BasePage {

    private static final String GRID_VIEW_NAME = "grid";
    private static final String CHART_VIEW_NAME = "chart";
    static final String ALL_RECORDS_LABEL = "All Records";

    private final String parentName;
    private final Iterable<ChartRecord> records;
    private final Collection<? extends ChartFilter> filters;
    // not actually required due to ETD, still just in case
    private final AtomicBoolean chartViewActive = new AtomicBoolean(true);

    private JPanel cardbox;
    private CardLayout deck;
    private JToggleButton gridButton;
    private JToggleButton chartButton;

    protected FiltersetPage(ChartsAppContext ctx, String name, String label, String parentName, Iterable<ChartRecord> records,
                            Collection<? extends ChartFilter> filters) {
        super(ctx, name, label, "mimetype_log_16.png");
        this.parentName = parentName;
        this.records = records;
        this.filters = filters;
    }

    @Override
    public Component createPane() {
        JPanel jp = new JPanel(new MigLayout(
                "fill, insets 0, gapy 0",
                "[]",
                "[top][fill]"
        ));
        jp.add(createToolbar(), "width 100%, height pref!, wrap");
        jp.add(createContent(), "pushy");
        return jp;
    }

    @Override
    public boolean isUserPage() {
        return !name.endsWith(ALL_RECORDS_LABEL);
    }

    private JPanel createToolbar() {
        JPanel jp = new JPanel(new MigLayout(
                "insets 0, gapx 0",
                "[][] [][] [][] push []",
                "[]"
        ));
        jp.setBorder(createMatteBorder(0, 0, 1, 0, jp.getBackground().darker()));

        gridButton = createToggleButton("app_database_32.png");
        gridButton.setName(GRID_VIEW_NAME);
        gridButton.setSelected(false);
        gridButton.addActionListener(new ToggleListener());
        jp.add(gridButton);
        chartButton = createToggleButton("app_Volume_Manager_32.png");
        chartButton.setName(CHART_VIEW_NAME);
        chartButton.setSelected(true);
        chartButton.addActionListener(new ToggleListener());
        jp.add(chartButton);

        JButton filters = createButton("app_kappfinder_32.png");
        filters.setEnabled(false);
        jp.add(filters, "gap 20lp");
        JButton config = createButton("app_utilities_32.png");
        config.setEnabled(false);
        jp.add(config);

        JButton export = createButton("action_db_update_32.png");
        export.setEnabled(false);
        jp.add(export, "gap 20lp");
        JButton print = createButton("action_fileprint_32.png");
        print.setEnabled(false);
        jp.add(print);

        JButton close = createButton("action_button_cancel_32.png");
        close.addActionListener(new ClosePageListener());
        close.setEnabled(!name.endsWith(ALL_RECORDS_LABEL));
        jp.add(close);

        return jp;
    }

    private JToggleButton createToggleButton(String icon) {
        URL imageURL = FiltersetPage.class.getResource("icons/" + icon);
        JToggleButton tb = new JToggleButton();
        tb.setIcon(new ImageIcon(imageURL));
        return tb;
    }

    private JButton createButton(String icon) {
        URL imageURL = FiltersetPage.class.getResource("icons/" + icon);
        JButton jb = new JButton();
        jb.setIcon(new ImageIcon(imageURL));
        return jb;
    }

    private JPanel createContent() {
        deck = new CardLayout();
        cardbox = new JPanel(deck);
        cardbox.add(createChart(), CHART_VIEW_NAME);
        cardbox.add(createGrid(), GRID_VIEW_NAME);
        return cardbox;
    }

    private JPanel createGrid() {
        JPanel parent = new JPanel(new MigLayout(
                "fill",
                "[]",
                "[top]"
        ));
        JPanel top = new JPanel(new MigLayout(
                "",
                "",
                ""
        ));
        top.setBorder(createFormSectionBorder(top.getBackground().darker(), "[TODO] Grid view"));
        parent.add(top, "growx");
        return parent;
    }

    private ChartPanel createChart() {
        // todo: temporary harcoded
        Map<String, ChartConfigEntry<?>> conf = new HashMap<String, ChartConfigEntry<?>>();
        conf.put("categoryAttributeName", new StringConfigEntry("categoryAttributeName", "reportId"));
        conf.put("domainAxisLabel", new StringConfigEntry("domainAxisLabel", "[TODO] incomplete charts panel, should support filters"));
        JFreeChart chart = new BucketedStackedCountPlotter()
                .applyConfig(conf)
                .build(records.iterator(), filters);
        return new ChartPanel(chart);
    }

    private class ClosePageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ctx.getPageManager().switchPage(parentName);
            ctx.getPageManager().removePage(name);
        }
    }

    private class ToggleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!(e.getSource() instanceof JToggleButton)) return;
            JToggleButton tb = (JToggleButton) e.getSource();
            if (CHART_VIEW_NAME.equals(tb.getName())) {
                if (!chartViewActive.compareAndSet(false, true)) return;
                gridButton.setSelected(false);
                chartButton.setSelected(true);
                deck.show(cardbox, CHART_VIEW_NAME);
            } else {
                if (!chartViewActive.compareAndSet(true, false)) return;
                chartButton.setSelected(false);
                gridButton.setSelected(true);
                deck.show(cardbox, GRID_VIEW_NAME);
            }
        }
    }
}
