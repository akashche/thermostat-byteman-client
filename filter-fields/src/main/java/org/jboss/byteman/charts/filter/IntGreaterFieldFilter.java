package org.jboss.byteman.charts.filter;

import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.IntConfigEntry;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
public class IntGreaterFieldFilter extends FieldFilter {
    private final IntConfigEntry en;

    public IntGreaterFieldFilter(String fieldName) {
        super(fieldName);
        en = new IntConfigEntry(fieldName, 0, 0, Integer.MAX_VALUE, 1);
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
}
