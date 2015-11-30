package com.redhat.thermostat.byteman.filter.impl;

import com.redhat.thermostat.byteman.config.ChartConfigEntry;
import com.redhat.thermostat.byteman.config.IntConfigEntry;
import com.redhat.thermostat.byteman.filter.ChartFilter;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
public class IntGreaterFieldFilter extends FieldFilter {
    private final IntConfigEntry en;

    public IntGreaterFieldFilter(String fieldName) {
        this(fieldName, new IntConfigEntry(fieldName, 0, 0, Integer.MAX_VALUE, 1));
    }

    private IntGreaterFieldFilter(String fieldName, IntConfigEntry en) {
        super(fieldName);
        this.en = en;
    }

    @Override
    protected boolean applyInternal(Object field) {
        if (!(field instanceof Number)) return false;
        Number num = (Number) field;
        return num.intValue() >= en.getValue();
    }

    @Override
    public ChartConfigEntry<?> configEntry() {
        return en;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ChartFilter> T copy() {
        IntConfigEntry cp = en.copy();
        return (T) new IntGreaterFieldFilter(fieldName, cp);
    }

}
