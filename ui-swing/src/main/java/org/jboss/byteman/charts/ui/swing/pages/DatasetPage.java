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

import org.jboss.byteman.charts.data.ChartRecord;

import javax.swing.*;
import java.awt.*;

import static org.jboss.byteman.charts.utils.SwingUtils.createFormSectionBorder;

/**
 * User: alexkasko
 * Date: 6/10/15
 */

// subnodes for each applied filter set
class DatasetPage extends BasePage {
    private final String name;
    private final Iterable<ChartRecord> records;

    public DatasetPage(ChartsAppContext ctx, String name, Iterable<ChartRecord> records) {
        super(ctx, name, name, "filesystem_folder_blue_16.png");
        this.name = name;
        this.records = records;
    }

    @Override
    public boolean isUserPage() {
        return true;
    }

    @Override
    public Component createPane() {
        JPanel jp = new JPanel();
        jp.setBorder(createFormSectionBorder(jp.getBackground().darker(), name));
        return jp;
    }
}
