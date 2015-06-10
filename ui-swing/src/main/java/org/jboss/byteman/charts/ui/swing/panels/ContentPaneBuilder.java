package org.jboss.byteman.charts.ui.swing.panels;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
public class ContentPaneBuilder {

    public JPanel build() {
        JPanel jp = new JPanel(new MigLayout("fill"));
        jp.add(new SplitPaneBuilder().build(), "grow");
        jp.add(new StatusBarBuilder().build(), "south, h 21, hmax 21");
        return jp;
    }
}
