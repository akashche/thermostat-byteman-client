package org.jboss.byteman.charts.ui.swing.panels;

import net.miginfocom.swing.MigLayout;
import org.jboss.byteman.charts.ui.swing.pages.ContentPage;

import javax.swing.*;
import java.util.List;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
public class MainPaneBuilder {

    public JPanel build(List<ContentPage> pages) {
        JPanel jp = new JPanel(new MigLayout("fill"));
        jp.add(new SplitPaneBuilder().build(pages), "grow");
        // thermostat api point - use its status bar if available
        jp.add(new StatusBarBuilder().build(), "south, h 21, hmax 21");
        return jp;
    }
}
