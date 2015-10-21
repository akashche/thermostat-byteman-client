/*
* JBoss, Home of Professional Open Source
* Copyright 2015 Red Hat and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.byteman.charts.ui.swing.pages;

import net.miginfocom.swing.MigLayout;
import org.jboss.byteman.charts.ui.swing.config.ChartConfigPanel;
import org.jboss.byteman.charts.ui.swing.settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import static org.jboss.byteman.charts.utils.SwingUtils.createFormSectionBorder;
import static org.jboss.byteman.charts.utils.SwingUtils.sleepNonFlicker;

/**
 * User: alexkasko
 * Date: 6/15/15
 */
class SettingsPage extends BasePage {

    Settings settings;
    private JPanel parentPanel = new JPanel();
    private ChartConfigPanel config;

    SettingsPage(ChartsAppContext ctx) {
        super(ctx, "settings", "Settings", "action_configure_16.png");
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
                "fill",
                "",
                ""
        ));
        jp.setBorder(createFormSectionBorder(jp.getBackground().darker(), "System settings"));
        jp.add(createConfigPanel(), "grow");
        return jp;
    }

    public Component createConfigPanel() {
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
            ctx.getPageManager().showPageSplash(SettingsPage.this.getName());
        }

        @Override
        protected Void doInBackground() throws Exception {
            long start = System.currentTimeMillis();
            settings = ctx.loadSettings();
            config = ChartConfigPanel.builder().build(settings.getSystem().availableConfig());
            sleepNonFlicker(start);
            return null;
        }

        @Override
        protected void done() {
            JPanel panel = config.getPanel();
            parentPanel.remove(0);
            parentPanel.add(panel, "growx");
            ctx.setStatus("Settings loaded");
            ctx.getPageManager().hidePageSplash(SettingsPage.this.getName());
        }
    }

    private class SaveWorker extends SwingWorker<Void, Void> {

        SaveWorker() {
            ctx.getPageManager().showPageSplash(SettingsPage.this.getName());
        }

        @Override
        protected Void doInBackground() throws Exception {
            long start = System.currentTimeMillis();
            settings.getSystem().applyConfig(config.getComponents());
            ctx.saveSettings(settings);
            sleepNonFlicker(start);
            return null;
        }
        @Override
        protected void done() {
            ctx.setStatus("Settings saved");
            ctx.getPageManager().hidePageSplash(SettingsPage.this.getName());
        }
    }

}
