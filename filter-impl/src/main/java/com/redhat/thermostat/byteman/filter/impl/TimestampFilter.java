package com.redhat.thermostat.byteman.filter.impl;

import com.redhat.thermostat.byteman.config.ChartConfigEntry;
import com.redhat.thermostat.byteman.config.DateTimeConfigEntry;
import com.redhat.thermostat.byteman.filter.ChartFilter;

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
