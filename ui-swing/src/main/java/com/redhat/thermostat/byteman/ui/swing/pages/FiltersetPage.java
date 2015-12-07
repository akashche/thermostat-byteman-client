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
package com.redhat.thermostat.byteman.ui.swing.pages;

import com.redhat.thermostat.byteman.chart.swing.SwingBarChart;
import com.redhat.thermostat.byteman.ui.swing.util.DataRecordTableModel;
import net.miginfocom.swing.MigLayout;
import com.redhat.thermostat.byteman.data.DataRecord;
import com.redhat.thermostat.byteman.filter.ChartFilter;
import com.redhat.thermostat.byteman.filter.ChartFilterUtils;
import com.redhat.thermostat.byteman.plot.Plotter;
import com.redhat.thermostat.byteman.ui.swing.config.ChartConfigPanel;
import com.redhat.thermostat.byteman.ui.swing.settings.ChartSettings;
import com.redhat.thermostat.byteman.ui.swing.util.ColumnFitTable;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.swing.BorderFactory.*;
import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 * @author akashche
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

    private final ChartSettings chartSettingsOrig;
    private ChartSettings chartSettings;
    private final String parentName;
    private final Plotter plotter;
    private final Iterable<DataRecord> records;
    private final Collection<? extends ChartFilter> filtersOrig;
    private Collection<? extends ChartFilter> filters;
    // not actually required due to ETD, still just in case
    private final AtomicBoolean chartViewActive = new AtomicBoolean(true);
    private final AtomicInteger nameCounter;

    private JPanel cardbox;
    private CardLayout deck;
    private JToggleButton gridButton;
    private JToggleButton chartButton;
    private JButton unzoomButton;
    private JToggleButton filtersButton;
    private JToggleButton configButton;

    SwingBarChart chartBuilder;

    protected FiltersetPage(ChartsAppContext ctx, ChartSettings chartSettings, String name, String label, String parentName,
                            Plotter plotter, Iterable<DataRecord> records, Collection<? extends ChartFilter> filters,
                            AtomicInteger nameCounter) {
        super(ctx, name, label, "mimetype_log_16.png");
        this.chartSettingsOrig = chartSettings.deepCopy();
        this.chartSettings = chartSettings;
        this.parentName = parentName;
        this.records = records;
        this.filtersOrig = deepCopyFilters(filters);
        this.filters = filters;
        this.plotter = plotter;
        this.nameCounter = nameCounter;
    }

    @Override
    public Component createPane() {
        JPanel jp = new JPanel(new MigLayout(
                "fill, insets 0, gapy 0",
                "[fill]",
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
                "[][] [] [][] [][] push []",
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

        unzoomButton = createButton("action_reload_32.png");
        unzoomButton.addActionListener(new UnzoomListener());
//        unzoomButton.setEnabled(false);
        jp.add(unzoomButton, "gap 20lp");

        filtersButton = createToggleButton("app_kappfinder_32.png");
        filtersButton.addActionListener(new ShowFiltersListener());
        jp.add(filtersButton, "gap 20lp");
        configButton = createToggleButton("app_utilities_32.png");
        configButton.addActionListener(new ShowConfigListener());
        jp.add(configButton);

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
        DataRecordTableModel tm = new DataRecordTableModel(records.iterator(), filters, "yyyy-MM-dd HH:mm:ss.SSS");
        JTable table = new ColumnFitTable(tm);
        JScrollPane sp = new JScrollPane(table, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setBorder(createEmptyBorder());
        parent.add(sp, "grow");
        return parent;
    }

    private Component createChart() {
        chartBuilder = new SwingBarChart(plotter, records, filters);
        chartBuilder.applyConfig(chartSettings.configAsMap());
        return chartBuilder.createChartPanel();

    }

    private static Collection<? extends ChartFilter> deepCopyFilters(Collection<? extends ChartFilter> filters) {
        Collection<ChartFilter> res = new ArrayList<ChartFilter>();
        for (ChartFilter fi : filters) {
            ChartFilter cp = fi.copy();
            res.add(cp);
        }
        return res;
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
            String fname = "filtered_" + nameCounter.incrementAndGet();
            String filtername = parentName + "_" + fname;
            ContentPage filterpage = new FiltersetPage(ctx, chartSettings.deepCopy(), filtername,
                    fname, parentName, plotter, records, deepCopyFilters(filters), nameCounter);
            pm.addPage(filterpage, parentName);
            filters = deepCopyFilters(filtersOrig);
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

    private class ShowConfigListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JToggleButton button = (JToggleButton) e.getSource();
            JPanel panel = ChartConfigPanel.builder().build(chartSettings.availableConfig()).getPanel();
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
            cancel.addActionListener(new CancelConfigListener(menu, button));
            bp.add(cancel);
            JButton apply = new JButton("Apply");
            apply.addActionListener(new ApplyConfigListener(menu, button));
            bp.add(apply);
//            bp.setBorder(createEmptyBorder(5, 0, 0, 0));
            bp.setBackground(new Color(0, 0, 0, 0));
            bp.setOpaque(false);
            menu.add(bp, "grow x");
            menu.addPopupMenuListener(new CloseConfigListener());
            menu.show(configButton, 0, configButton.getHeight() - 5);
        }
    }

    private class ApplyConfigListener implements ActionListener {
        private final JPopupMenu menu;
        private final JToggleButton button;

        private ApplyConfigListener(JPopupMenu menu, JToggleButton button) {
            this.menu = menu;
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            PageManager pm = ctx.getPageManager();
            String fname = "filtered_" + nameCounter.incrementAndGet();
            String filtername = parentName + "_" + fname;
            ContentPage filterpage = new FiltersetPage(ctx, chartSettings.deepCopy(), filtername,
                    fname, parentName, plotter, records, deepCopyFilters(filters), nameCounter);
            chartSettings = chartSettingsOrig.deepCopy();
            pm.addPage(filterpage, parentName);
            menu.setVisible(false);
            button.setSelected(false);
        }
    }

    private class CancelConfigListener implements ActionListener {
        private final JPopupMenu menu;
        private final JToggleButton button;

        private CancelConfigListener(JPopupMenu menu, JToggleButton button) {
            this.menu = menu;
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            menu.setVisible(false);
            button.setSelected(false);
        }
    }

    private class CloseConfigListener implements PopupMenuListener {

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            configButton.setSelected(false);
        }
    }

    private class UnzoomListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            chartBuilder.unzoom();
        }
    }
}
