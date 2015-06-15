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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: alexkasko
 * Date: 6/11/15
 */
public class ContentPagesRegister {

    // todo: properties load, thermostat api point
    private static final Map<String, String> PROPS = new ConcurrentHashMap<String, String>();

    static {
        PROPS.put("byteman_charts.last_chosen_data_file", "/home/alex/projects/redhat/byteman-charts/plot-aggregate/src/test/resources/org/jboss/byteman/charts/plot/aggregate/");
        PROPS.put("byteman_charts.dataset_name_format", "${filename}_${date}");
        PROPS.put("byteman_charts.dataset_name_date_format", "yyyy-MM-dd_HH:mm:ss");
    }

    public static final List<ContentPage> PAGES = Arrays.asList(
            new DataRootPage(PROPS),
            new ChartTypesPage(),
                new PlainChartsPage(),
                new AggregateChartsPage(),
            new SettingsPage(),
            new AboutPage()
    );
}
