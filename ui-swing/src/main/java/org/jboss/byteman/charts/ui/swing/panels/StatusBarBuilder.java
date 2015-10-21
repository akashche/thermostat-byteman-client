package org.jboss.byteman.charts.ui.swing.panels;

import net.miginfocom.swing.MigLayout;
import org.jboss.byteman.charts.ui.swing.pages.ContentPagesRegister;

import javax.swing.*;

import static javax.swing.BorderFactory.createMatteBorder;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
class StatusBarBuilder {

    public JPanel build() {
        JPanel jp = new JPanel(new MigLayout("fill, insets 10lp"));
        jp.setBorder(createMatteBorder(1, 0, 0, 0, jp.getBackground().darker()));
        jp.add(ContentPagesRegister.STATUS, "west, gapleft 5lp");
        return jp;
    }
}
