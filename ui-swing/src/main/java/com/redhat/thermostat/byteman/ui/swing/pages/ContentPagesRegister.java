/*
 * Copyright 2012-2015 Red Hat, Inc.
 *
 * This file is part of Thermostat.
 *
 * Thermostat is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2, or (at your
 * option) any later version.
 *
 * Thermostat is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Thermostat; see the file COPYING.  If not see
 * <http://www.gnu.org/licenses/>.
 *
 * Linking this code with other modules is making a combined work
 * based on this code.  Thus, the terms and conditions of the GNU
 * General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this code give
 * you permission to link this code with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also
 * meet, for each linked independent module, the terms and conditions
 * of the license of that module.  An independent module is a module
 * which is not derived from or based on this code.  If you modify
 * this code, you may extend this exception to your version of the
 * library, but you are not obligated to do so.  If you do not wish
 * to do so, delete this exception statement from your version.
 */
package com.redhat.thermostat.byteman.ui.swing.pages;

import com.redhat.thermostat.byteman.plot.Plotter;
import com.redhat.thermostat.byteman.plot.impl.AveragePlotter;
import com.redhat.thermostat.byteman.plot.impl.SumPlotter;
import com.redhat.thermostat.byteman.ui.swing.settings.Settings;
import com.redhat.thermostat.byteman.ui.swing.settings.SettingsManager;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.redhat.thermostat.byteman.utils.string.StringUtils.defaultString;

/**
 * @author akashche
 * Date: 6/11/15
 */
public class ContentPagesRegister {

    public static final ChartsAppContext APP_CONTEXT = new ChartsAppContextImpl();
    public static final List<? extends Plotter> PLOTS = Arrays.asList(
            new AveragePlotter(),
            new SumPlotter()
    );
    public static final JLabel STATUS = new JLabel();

    // todo: make me private
    public static final List<ContentPage> PAGES = Arrays.<ContentPage>asList(
            new DatasetLoadPage(APP_CONTEXT),
            new ChartTypesPage(APP_CONTEXT),
                new PlainChartsPage(APP_CONTEXT),
                    new ChartConfigPage(APP_CONTEXT, PLOTS.get(0)),
                    new ChartConfigPage(APP_CONTEXT, PLOTS.get(1)),
                new AggregateChartsPage(APP_CONTEXT),
            new SettingsPage(APP_CONTEXT),
            new AboutPage(APP_CONTEXT)
    );

    private static class ChartsAppContextImpl implements ChartsAppContext {

        // todo: properties load, thermostat api access
        private final Map<String, String> props = new ConcurrentHashMap<String, String>();

        private final SettingsManager sm = new SettingsManager();
        private PageManager pm;

        private ChartsAppContextImpl() {
            // todo: removeme
            props.put("byteman_charts.last_chosen_data_file", "/home/alex/projects/redhat/byteman-charts/plot-impl/src/test/resources/");
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

        @Override
        public void setStatus(String text) {
            STATUS.setText(text);
        }

        @Override
        public Settings loadSettings() {
            return sm.loadSettings();
        }

        @Override
        public void saveSettings(Settings settings) {
            sm.saveSettings(settings);
        }
    }
}
