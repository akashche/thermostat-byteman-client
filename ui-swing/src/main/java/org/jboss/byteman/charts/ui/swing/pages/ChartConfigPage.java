package org.jboss.byteman.charts.ui.swing.pages;

import net.miginfocom.swing.MigLayout;
import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.filter.ChartFilterUtils;
import org.jboss.byteman.charts.plot.Plotter;
import org.jboss.byteman.charts.plot.swing.JFreeChartBuilder;
import org.jboss.byteman.charts.ui.swing.config.ChartConfigPanel;
import org.jboss.byteman.charts.ui.swing.settings.ChartSettings;
import org.jboss.byteman.charts.ui.swing.settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import static org.jboss.byteman.charts.utils.SwingUtils.createFormSectionBorder;
import static org.jboss.byteman.charts.utils.SwingUtils.sleepNonFlicker;

/**
 * User: alexkasko
 * Date: 10/19/15
 */
class ChartConfigPage extends BasePage {
    private final Plotter plotter;

    Settings settings;
    ChartSettings chartSettings;
    private JPanel parentPanel = new JPanel();
    private ChartConfigPanel config;

    ChartConfigPage(ChartsAppContext ctx, Plotter plotter) {
        super(ctx, plotter.getName(), plotter.getName(), "mimetype_log_16.png");
        this.plotter = plotter;
    }

    @Override
    public Component createPane() {
        JPanel jp = new JPanel(new MigLayout(
                "fill",
                "[]",
                "[top][bottom]"
        ));
        jp.add(createTopPanel(), "growx, wrap");
        jp.add(createButtonsPanel(), "growx");
        return jp;
    }

    @Override
    public void onShow() {
        new SettingsLoadWorker().execute();
    }

    public Component createTopPanel() {
        JPanel jp = new JPanel(new MigLayout(
                "fill, insets n 0 0 0",
                "",
                ""
        ));
        String label = "Configuration properties for chart: [" + plotter.getName() + "]";
        jp.setBorder(createFormSectionBorder(jp.getBackground().darker(), label));
        jp.add(createConfigPane(), "grow");
        return jp;
    }

    private Component createConfigPane() {
        parentPanel = new JPanel(new MigLayout(
                "fill, insets n 0 0 0",
                "",
                ""
        ));
        parentPanel.add(new JPanel());
        JScrollPane sp = new JScrollPane(parentPanel, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    private JPanel createButtonsPanel() {
        JPanel jp = new JPanel(new MigLayout(
                "fillx, insets n 0 0 0",
                "push[right][right]",
                "[]"
        ));
        JButton reloadButton = new JButton("Reload");
        reloadButton.addActionListener(new ReloadListener());
        jp.add(reloadButton);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveListener());
        jp.add(saveButton);
        jp.setBorder(createFormSectionBorder(jp.getBackground().darker(), ""));
        return jp;
    }

    private class ReloadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new SettingsLoadWorker().execute();
        }
    }

    private class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new SaveWorker().execute();
        }
    }

    private class SettingsLoadWorker extends SwingWorker<Void, Void> {

        SettingsLoadWorker() {
            ctx.getPageManager().showPageSplash(ChartConfigPage.this.getName());
        }

        @Override
        protected Void doInBackground() throws Exception {
            long start = System.currentTimeMillis();
            settings = ctx.loadSettings();
            // todo: check
            chartSettings = settings.getCharts().get(ChartConfigPage.this.getName());
            config = ChartConfigPanel.builder().build(chartSettings.availableConfig());
            sleepNonFlicker(start);
            return null;
        }

        @Override
        protected void done() {
            JPanel panel = config.getPanel();
            parentPanel.remove(0);
            parentPanel.add(panel, "growx");
            ctx.setStatus("Settings loaded");
            ctx.getPageManager().hidePageSplash(ChartConfigPage.this.getName());
        }
    }

    private class SaveWorker extends SwingWorker<Void, Void> {

        SaveWorker() {
            ctx.getPageManager().showPageSplash(ChartConfigPage.this.getName());
        }

        @Override
        protected Void doInBackground() throws Exception {
            long start = System.currentTimeMillis();
            chartSettings.applyConfig(config.getComponents());
            ctx.saveSettings(settings);
            sleepNonFlicker(start);
            return null;
        }
        @Override
        protected void done() {
            ctx.setStatus("Settings saved");
            ctx.getPageManager().hidePageSplash(ChartConfigPage.this.getName());
        }
    }
}
