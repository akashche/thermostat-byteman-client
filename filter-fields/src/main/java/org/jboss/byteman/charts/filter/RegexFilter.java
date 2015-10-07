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
        en = new RegexConfigEntry(name, "^.*$");
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
}
