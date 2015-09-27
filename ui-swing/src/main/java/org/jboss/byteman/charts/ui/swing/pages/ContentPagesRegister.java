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

import org.jboss.byteman.charts.plot.Plotter;
import org.jboss.byteman.charts.plot.aggregate.BucketedStackedCountPlotter;
import org.jboss.byteman.charts.plot.plain.PlainStackedPlotter;
import org.jboss.byteman.charts.ui.ConfigurableChart;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.jboss.byteman.charts.ui.swing.pages.ContentPagesRegister.RegisteredChartEntry.chartEntry;
import static org.jboss.byteman.charts.utils.StringUtils.defaultString;

/**
 * User: alexkasko
 * Date: 6/11/15
 */
public class ContentPagesRegister {

    public static final ChartsAppContext APP_CONTEXT = new ChartsAppContextImpl();
//    public static final List<RegisteredChartEntry<?>> PLOTS = Arrays.asList(
//            chartEntry("Plain Stacked", "plain", PlainStackedPlotter.class),
//            chartEntry("Bucketed Stacked", "aggregated", BucketedStackedCountPlotter.class)
//    );

    // todo: make me private
    public static final List<ContentPage> PAGES = Arrays.<ContentPage>asList(
            new DatasetLoadPage(APP_CONTEXT),
            new ChartTypesPage(APP_CONTEXT),
                new PlainChartsPage(APP_CONTEXT),
                new AggregateChartsPage(APP_CONTEXT),
            new SettingsPage(APP_CONTEXT),
            new AboutPage(APP_CONTEXT)
    );

    private static class ChartsAppContextImpl implements ChartsAppContext {

        // todo: properties load, thermostat api access
        private final Map<String, String> props = new ConcurrentHashMap<String, String>();

        private PageManager pm;

        private ChartsAppContextImpl() {
            props.put("byteman_charts.last_chosen_data_file", "/home/alex/projects/redhat/byteman-charts/plot-aggregate/src/test/resources/org/jboss/byteman/charts/plot/aggregate/");
            props.put("byteman_charts.dataset_name_format", "${filename}_${date}");
            props.put("byteman_charts.dataset_name_date_format", "yyyyMMdd_HHmmss");
        }

        @Override
        public String getProp(String propName) {
            return defaultString(props.get(propName));
        }

        @Override
        public void setProp(String propName, String propValue) {
            props.put(propName, propValue);
        }

        @Override
        public void init(PageManager pm) {
            this.pm = pm;
        }

        @Override
        public PageManager getPageManager() {
            return pm;
        }

    }

    static class RegisteredChartEntry<T extends ConfigurableChart & Plotter> {
        final String name;
        final String category;
        final Class<T> chartClass;

        RegisteredChartEntry(String name, String category, Class<T> chartClass) {
            this.name = name;
            this.category = category;
            this.chartClass = chartClass;
        }

        static <T extends ConfigurableChart & Plotter>
        RegisteredChartEntry<T> chartEntry(String name, String category, Class<T> chartClass) {
            return new RegisteredChartEntry<T>(name, category, chartClass);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("RegisteredChartEntry");
            sb.append("{name='").append(name).append('\'');
            sb.append(", category='").append(category).append('\'');
            sb.append(", chartClass=").append(chartClass);
            sb.append('}');
            return sb.toString();
        }
    }
}
