package org.jboss.byteman.charts.ui.swing.panels;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
public class SplitPaneBuilder {

    public JSplitPane build() {
        JSplitPane jp = new JSplitPane();
        jp.setOrientation(HORIZONTAL_SPLIT);
        jp.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {
                    }
                };
            }
        });
        jp.setBorder(null);
        jp.setLeftComponent(new TreePaneBuilder().build());
        jp.setRightComponent(new RightPaneBuilder().build());
        jp.setDividerLocation(0.30);
        jp.setDividerSize(5);
        return jp;
    }
}
