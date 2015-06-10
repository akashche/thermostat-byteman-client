package org.jboss.byteman.charts.ui.swing.panels;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

import static javax.swing.BorderFactory.createMatteBorder;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
public class StatusBarBuilder {

    public JPanel build() {
        JPanel jp = new JPanel();
        jp.setLayout(new MigLayout("fill"));
        jp.setBorder(createMatteBorder(1, 0, 0, 0, jp.getBackground().darker()));
        JLabel label = new JLabel("status wll be here");
        jp.add(label, "west, gapbottom 2, gapleft 5");
        return jp;
    }
}
