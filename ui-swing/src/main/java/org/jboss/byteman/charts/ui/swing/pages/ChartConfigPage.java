package org.jboss.byteman.charts.ui.swing.pages;

import net.miginfocom.swing.MigLayout;
import org.jboss.byteman.charts.plot.Plotter;

import javax.swing.*;
import java.awt.*;

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
        top.setBorder(createFormSectionBorder(top.getBackground().darker(), "[TODO] Chart config form [" + plotter.getName() + "]"));
        parent.add(top, "growx");
        return parent;
    }
}
