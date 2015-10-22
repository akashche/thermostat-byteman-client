package org.jboss.byteman.charts.filter;

import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.RegexConfigEntry;

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
        return en.getPattern().matcher(field.toString()).matches();
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
