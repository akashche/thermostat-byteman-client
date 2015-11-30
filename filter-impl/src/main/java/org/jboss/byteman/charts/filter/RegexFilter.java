package org.jboss.byteman.charts.filter;

import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.RegexConfigEntry;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
public class RegexFilter implements ChartFilter {
    protected final RegexConfigEntry en;

    public RegexFilter(String name) {
        this(new RegexConfigEntry(name, "^.*$"));
    }

    private RegexFilter(RegexConfigEntry en) {
        this.en = en;
    }

    @Override
    public boolean apply(DataRecord record) {
        if (null == record.getAgentId()) return false;
        return en.getPattern().matcher(record.getAgentId()).matches();
    }

    @Override
    public ChartConfigEntry<?> configEntry() {
        return en;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ChartFilter> T copy() {
        RegexConfigEntry cp = en.copy();
        return (T) new RegexFilter(cp);
    }
}
