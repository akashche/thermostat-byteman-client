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
        super(fieldName);
        en = new RegexConfigEntry(fieldName, "^.*$");
    }

    @Override
    protected boolean applyInternal(Object field) {
        return en.getPattern().matcher(field.toString()).matches();
    }

    @Override
    public ChartConfigEntry<?> configEntry() {
        return en;
    }
}
