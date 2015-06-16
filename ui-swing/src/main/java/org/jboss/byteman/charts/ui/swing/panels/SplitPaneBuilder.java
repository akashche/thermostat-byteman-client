package org.jboss.byteman.charts.ui.swing.panels;

import org.jboss.byteman.charts.ui.swing.pages.ContentPage;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import java.util.List;

import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
class SplitPaneBuilder {

    public JSplitPane build(List<ContentPage> pages) {

        JSplitPane jp = new JSplitPane();
        jp.setOrientation(HORIZONTAL_SPLIT);
        jp.setUI(new PlainSplitPaneUI());
        jp.setBorder(null);
        jp.setDividerLocation(0.30);
        jp.setDividerSize(5);

        RightPaneBuilder.Result res = new RightPaneBuilder().build(pages);
        jp.setRightComponent(res.getCardbox());
        jp.setLeftComponent(new TreePaneBuilder().build(pages, res.getCardbox(), res.getDeck()));

        return jp;
    }

    private static class PlainSplitPaneUI extends BasicSplitPaneUI {
        @Override
        public BasicSplitPaneDivider createDefaultDivider() {
            return new BasicSplitPaneDivider(this) {
                public void setBorder(Border b) {
                }
            };
        }
    }
}
