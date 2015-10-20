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
import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.swing.config.ChartConfigPanel;
import org.jboss.byteman.charts.ui.swing.settings.Settings;
import org.jboss.byteman.charts.ui.swing.settings.SettingsManager;
import org.jboss.byteman.charts.ui.swing.settings.SystemSettings;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

import static org.jboss.byteman.charts.utils.SwingUtils.createFormSectionBorder;

/**
 * User: alexkasko
 * Date: 6/15/15
 */
class SettingsPage extends BasePage {

    private final Settings settings = new SettingsManager().loadSettings();

    SettingsPage(ChartsAppContext ctx) {
        super(ctx, "settings", "Settings", "action_configure_16.png");
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
        top.setBorder(createFormSectionBorder(top.getBackground().darker(), "System settings"));
        parent.add(top, "growx, wrap");
        parent.add(createConfigPanel());
        return parent;
    }

    public Component createConfigPanel() {
        JPanel panel = ChartConfigPanel.builder().build(settings.getSystem().availableConfig()).getPanel();
        return new JScrollPane(panel);
    }
}
