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
package com.redhat.thermostat.byteman.chart.swing;

import com.redhat.thermostat.byteman.data.DataRecord;
import com.redhat.thermostat.byteman.filter.ChartFilter;
import com.redhat.thermostat.byteman.plot.impl.AveragePlotter;
import com.redhat.thermostat.byteman.chart.ChartConfigEntry;
import com.redhat.thermostat.byteman.chart.ConfigEntryBase;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * @author akashche
 * Date: 9/28/15
 */
public class SwingBarChartTest {

    @Test
    public void test() {
        Iterable<DataRecord> data = Arrays.asList(
                new DataRecord(0, "42", "42", "marker1", "value", 42, "category", "cat1"),
                new DataRecord(7000, "42", "42", "marker1", "value", 43, "category", "cat1"),
                new DataRecord(9000, "42", "42", "marker1", "value", 44, "category", "cat1")
        );
        Collection<NamedDateTestFilter> filters = Arrays.asList(
                new NamedDateTestFilter("timestampFrom", 0),
                new NamedDateTestFilter("timestampTo", 10000)
        );
        SwingBarChart builder = new SwingBarChart(new AveragePlotter(), data, filters);
//        TestSwingUtils.showAndWait(builder.createChartPanel());
    }

    private static class NamedDateTestFilter implements ChartFilter {
        private final NamedDateTestConfigEntry entry;

        private NamedDateTestFilter(String name, long date) {
            this.entry = new NamedDateTestConfigEntry(name, date);
        }

        @Override
        public boolean apply(DataRecord record) {
            return true;
        }

        @Override
        public ChartConfigEntry<?> configEntry() {
            return entry;
        }

        @Override
        public <T extends ChartFilter> T copy() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private static class NamedDateTestConfigEntry extends ConfigEntryBase<Date> {
        NamedDateTestConfigEntry(String name, long timestamp) {
            this.name = name;
            this.value = new Date(timestamp);
        }

        @Override
        public <T1 extends ChartConfigEntry<Date>> T1 copy() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
