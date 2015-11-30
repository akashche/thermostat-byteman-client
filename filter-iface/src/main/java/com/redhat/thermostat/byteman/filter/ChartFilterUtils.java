package com.redhat.thermostat.byteman.filter;

import com.redhat.thermostat.byteman.config.ChartConfigEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: alexkasko
 * Date: 6/30/15
 */
public class ChartFilterUtils {
    public static List<ChartConfigEntry<?>> toEntries(Collection<? extends ChartFilter> filters) {
        List<ChartConfigEntry<?>> entries = new ArrayList<ChartConfigEntry<?>>();
        for (ChartFilter fi : filters) {
            entries.add(fi.configEntry());
        }
        return entries;
    }
}
