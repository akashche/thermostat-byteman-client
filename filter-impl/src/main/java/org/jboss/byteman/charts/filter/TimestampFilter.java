package org.jboss.byteman.charts.filter;

import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.DateTimeConfigEntry;

import java.util.Date;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
abstract class TimestampFilter implements ChartFilter {
    protected final DateTimeConfigEntry en;

    protected TimestampFilter(String label, Date defaultValue) {
        en = new DateTimeConfigEntry(label, defaultValue, new java.util.Date(0), new Date());
    }

    protected TimestampFilter(DateTimeConfigEntry en) {
        this.en = en;
    }

    @Override
    public ChartConfigEntry<?> configEntry() {
        return en;
    }

    public DateTimeConfigEntry getEntry() {
        return en;
    }
}
