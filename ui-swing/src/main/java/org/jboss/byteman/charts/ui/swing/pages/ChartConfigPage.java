package org.jboss.byteman.charts.ui.swing.pages;

import net.miginfocom.swing.MigLayout;
import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.filter.ChartFilterUtils;
import org.jboss.byteman.charts.plot.Plotter;
import org.jboss.byteman.charts.plot.swing.JFreeChartBuilder;
import org.jboss.byteman.charts.ui.swing.config.ChartConfigPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

import static org.jboss.byteman.charts.utils.SwingUtils.createFormSectionBorder;

/**
 * User: alexkasko
 * Date: 10/19/15
 */
class ChartConfigPage extends BasePage {
    private final Plotter plotter;

    ChartConfigPage(ChartsAppContext ctx, Plotter plotter) {
        super(ctx, plotter.getName(), plotter.getName(), "mimetype_log_16.png");
        this.plotter = plotter;
    }

    @Override
    public Component createPane() {
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
        top.setBorder(createFormSectionBorder(top.getBackground().darker(), plotter.getName() + " configuration properties" ));
        parent.add(top, "growx, wrap");
        parent.add(createConfigPane());
        return parent;
    }

    private Component createConfigPane() {
        JFreeChartBuilder cb = new JFreeChartBuilder(plotter, Collections.<DataRecord>emptyList(), Collections.<ChartFilter>emptyList());
        JPanel panel = ChartConfigPanel.builder().build(cb.availableConfig()).getPanel();
        return new JScrollPane(panel);
    }

}
