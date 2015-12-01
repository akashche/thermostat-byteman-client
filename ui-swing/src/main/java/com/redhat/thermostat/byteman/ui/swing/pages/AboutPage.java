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
package com.redhat.thermostat.byteman.ui.swing.pages;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import static com.redhat.thermostat.byteman.utils.SwingUtils.createFormSectionBorder;

/**
 * User: alexkasko
 * Date: 6/15/15
 */
class AboutPage extends BasePage {

    AboutPage(ChartsAppContext ctx) {
        super(ctx, "about", "About", "action_db_status_16.png");
    }

    @Override
    public Component createPane() {
        JPanel jp = new JPanel(new MigLayout(
                "fill",
                "[]",
                "[top]"
        ));
        jp.add(createTopPanel(), "growx");
        return jp;
    }

    private Component createTopPanel() {
        JPanel jp = new JPanel(new MigLayout(
                "fill",
                "",
                ""
        ));
        jp.setBorder(createFormSectionBorder(jp.getBackground().darker(), "About"));
        jp.add(createTextPanel(), "grow");
        return jp;
    }

    private Component createTextPanel() {
        JLabel label = new JLabel("Thermostat-Byteman module, work in progress.");
        JScrollPane sp = new JScrollPane(label, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setBorder(BorderFactory.createEmptyBorder());
        return sp;
    }
}