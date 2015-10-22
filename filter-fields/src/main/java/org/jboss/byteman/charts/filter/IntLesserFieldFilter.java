package org.jboss.byteman.charts.filter;

import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.IntConfigEntry;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
public class IntLesserFieldFilter extends FieldFilter {
    private final IntConfigEntry en;

    public IntLesserFieldFilter(String fieldName) {
        this(fieldName, new IntConfigEntry(fieldName, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 1));
    }

    private IntLesserFieldFilter(String fieldName, IntConfigEntry en) {
        super(fieldName);
        this.en = en;
    }

    @Override
    protected boolean applyInternal(Object field) {
        if (!(field instanceof Number)) return false;
        Number num = (Number) field;
        return num.intValue() <= en.getValue();
    }

    @Override
    public ChartConfigEntry<?> configEntry() {
        return en;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ChartFilter> T copy() {
        IntConfigEntry cp = en.copy();
        return (T) new IntLesserFieldFilter(fieldName, cp);
    }
}

