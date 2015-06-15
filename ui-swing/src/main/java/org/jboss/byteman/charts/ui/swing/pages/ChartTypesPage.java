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

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static java.util.Arrays.asList;
import static org.jboss.byteman.charts.utils.SwingUtils.createFormSectionBorder;

/**
 * User: alexkasko
 * Date: 6/15/15
 */
class ChartTypesPage implements ContentPage {

    @Override
    public String getName() {
        return "chart_types";
    }

    @Override
    public String getLabel() {
        return "Chart Types";
    }

    @Override
    public String getIcon() {
        return "app_Volume_Manager_16.png";
    }

    @Override
    public List<String> getChildren() {
        return asList(PlainChartsPage.NAME, AggregateChartsPage.NAME);
    }

    @Override
    public Component createPane() {
        JPanel jp = new JPanel();
        jp.setBorder(createFormSectionBorder(jp.getBackground().darker(), "[TODO] List of the supported charts"));
        return jp;
    }
}
