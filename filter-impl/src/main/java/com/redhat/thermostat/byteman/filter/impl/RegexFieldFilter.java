package com.redhat.thermostat.byteman.filter.impl;

import com.redhat.thermostat.byteman.config.ChartConfigEntry;
import com.redhat.thermostat.byteman.config.RegexConfigEntry;
import com.redhat.thermostat.byteman.filter.ChartFilter;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
public class RegexFieldFilter extends FieldFilter {
    private final RegexConfigEntry en;

    public RegexFieldFilter(String fieldName) {
        this(fieldName, new RegexConfigEntry(fieldName, "^.*$"));
    }

    public RegexFieldFilter(String fieldName, RegexConfigEntry en) {
        super(fieldName);
        this.en = en;
    }

    @Override
    protected boolean applyInternal(Object field) {
//        return en.getPattern().matcher(field.toString()).matches();
        // todo: check me for multiline stacktrace strings
        return true;
    }

    @Override
    public ChartConfigEntry<?> configEntry() {
        return en;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ChartFilter> T copy() {
        RegexConfigEntry cp = en.copy();
        return (T) new RegexFieldFilter(fieldName, cp);
    }

}
