package org.jboss.byteman.charts.plot.swing;

import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.plot.plain.AveragePlotter;
import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.ConfigEntryBase;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * User: alexkasko
 * Date: 9/28/15
 */
public class JFreeChartBuilderTest {

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
        JFreeChartBuilder builder = new JFreeChartBuilder(new AveragePlotter(), data, filters);
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
    }

    private static class NamedDateTestConfigEntry extends ConfigEntryBase<Date> {
        NamedDateTestConfigEntry(String name, long timestamp) {
            this.name = name;
            this.value = new Date(timestamp);
        }
    }
}
