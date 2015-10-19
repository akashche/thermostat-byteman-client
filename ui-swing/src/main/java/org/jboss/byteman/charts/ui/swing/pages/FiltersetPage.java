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
import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.filter.ChartFilterUtils;
import org.jboss.byteman.charts.plot.Plotter;
import org.jboss.byteman.charts.plot.plain.PlainStackedPlotter;
import org.jboss.byteman.charts.plot.swing.JFreeChartBuilder;
import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.StringConfigEntry;
import org.jboss.byteman.charts.ui.swing.config.ChartConfigPanel;
import org.jboss.byteman.charts.ui.swing.util.ChartRecordTableModel;
import org.jboss.byteman.charts.ui.swing.util.ColumnFitTable;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static javax.swing.BorderFactory.*;
import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 * User: alexkasko
 * Date: 6/23/15
 */
class FiltersetPage extends BasePage {

// data filters:
//    timestamp from
//    timestamp to

//    string category regex
//    double value from
//    double value to
//    string attribute name:regex format

    private static final String GRID_VIEW_NAME = "grid";
    private static final String CHART_VIEW_NAME = "chart";
    static final String ALL_RECORDS_LABEL = "All Records";

    private final String parentName;
    private final Plotter plotter;
    private final Iterable<DataRecord> records;
    private final Collection<? extends ChartFilter> filters;
    // not actually required due to ETD, still just in case
    private final AtomicBoolean chartViewActive = new AtomicBoolean(true);

    private JPanel cardbox;
    private CardLayout deck;
    private JToggleButton gridButton;
    private JToggleButton chartButton;
    private JToggleButton filtersButton;

    protected FiltersetPage(ChartsAppContext ctx, String name, String label, String parentName,
                            Plotter plotter, Iterable<DataRecord> records, Collection<? extends ChartFilter> filters) {
        super(ctx, name, label, "mimetype_log_16.png");
        this.parentName = parentName;
        this.records = records;
        this.filters = filters;
        this.plotter = plotter;
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

        filtersButton = createToggleButton("app_kappfinder_32.png");
        filtersButton.addActionListener(new ShowFiltersListener());
        jp.add(filtersButton, "gap 20lp");
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
                "fill, insets 0",
                "[]",
                "[top]"
        ));
        ChartRecordTableModel tm = new ChartRecordTableModel(records.iterator(), filters, "yyyy-MM-dd HH:mm:ss.SSS");
        JTable table = new ColumnFitTable(tm);
        JScrollPane sp = new JScrollPane(table, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setBorder(createEmptyBorder());
        parent.add(sp, "grow");
        return parent;
    }

    private Component createChart() {
        return new JFreeChartBuilder(plotter, records, filters).createChartPanel();

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
                chartButton.setSelected(true);
                if (!chartViewActive.compareAndSet(false, true)) return;
                gridButton.setSelected(false);
                deck.show(cardbox, CHART_VIEW_NAME);
            } else {
                gridButton.setSelected(true);
                if (!chartViewActive.compareAndSet(true, false)) return;
                chartButton.setSelected(false);
                deck.show(cardbox, GRID_VIEW_NAME);
            }
        }
    }

    private class ShowFiltersListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JToggleButton button = (JToggleButton) e.getSource();
            JPanel panel = ChartConfigPanel.builder().build(ChartFilterUtils.toEntries(filters)).getPanel();
            panel.setBackground(new Color(0, 0, 0, 0));
            panel.setOpaque(false);
            // menu won't work with comboboxes http://stackoverflow.com/a/11246209/314015
            JPopupMenu menu = new JPopupMenu();
            menu.setLayout(new MigLayout("insets 0, gap 0"));
            menu.add(panel, "wrap");
            JPanel bp = new JPanel(new MigLayout("align right"));
            JButton clear = new JButton("Clear");
            clear.setEnabled(false);
            bp.add(clear);
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new CancelFiltersListener(menu, button));
            bp.add(cancel);
            JButton apply = new JButton("Apply");
            apply.addActionListener(new ApplyFiltersListener(menu, button));
            bp.add(apply);
//            bp.setBorder(createEmptyBorder(5, 0, 0, 0));
            bp.setBackground(new Color(0, 0, 0, 0));
            bp.setOpaque(false);
            menu.add(bp, "grow x");
            menu.addPopupMenuListener(new CloseFiltersListener());
            menu.show(filtersButton, 0, filtersButton.getHeight() - 5);
        }
    }

    private class ApplyFiltersListener implements ActionListener {
        private final JPopupMenu menu;
        private final JToggleButton button;

        private ApplyFiltersListener(JPopupMenu menu, JToggleButton button) {
            this.menu = menu;
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            PageManager pm = ctx.getPageManager();
            String fname = "tmp_filtered" + System.currentTimeMillis();
            String filtername = parentName + "_" + fname;
            // todo: deep clone filters
            ContentPage filterpage = new FiltersetPage(ctx, filtername, fname, parentName, plotter, records, Collections.<ChartFilter>emptyList());
            pm.addPageAsync(filterpage, parentName);
            menu.setVisible(false);
            button.setSelected(false);
        }
    }

    private class CancelFiltersListener implements ActionListener {
        private final JPopupMenu menu;
        private final JToggleButton button;

        private CancelFiltersListener(JPopupMenu menu, JToggleButton button) {
            this.menu = menu;
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            menu.setVisible(false);
            button.setSelected(false);
        }
    }

    private class CloseFiltersListener implements PopupMenuListener {

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            filtersButton.setSelected(false);
        }
    }
}
