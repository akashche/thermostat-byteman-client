package org.jboss.byteman.charts.ui.swing.panels;

import javax.swing.*;

import static javax.swing.BorderFactory.createMatteBorder;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
public class TreePaneBuilder {


    public JScrollPane build() {


        JScrollPane jp = new JScrollPane();
        jp.setViewportView(tree);
        jp.setBorder(createMatteBorder(0, 0, 0, 1, jp.getBackground().darker()));
        return jp;
    }
}
