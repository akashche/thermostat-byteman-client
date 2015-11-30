package com.redhat.thermostat.byteman.filter.impl;

import com.redhat.thermostat.byteman.data.DataRecord;
import com.redhat.thermostat.byteman.config.DateTimeConfigEntry;
import com.redhat.thermostat.byteman.filter.ChartFilter;

import java.util.Date;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
public class TimestampToFilter extends TimestampFilter {
    public TimestampToFilter(String label, Date defaultValue) {
        super(label, defaultValue);
    }

    private TimestampToFilter(DateTimeConfigEntry en) {
        super(en);
    }

    @Override
    public boolean apply(DataRecord record) {
        return record.getTimestamp() <= en.getValue().getTime();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ChartFilter> T copy() {
        DateTimeConfigEntry cp = en.copy();
        return (T) new TimestampToFilter(cp);
    }
}
