package org.jboss.byteman.charts.ui.swing.settings;

import org.jboss.byteman.charts.ui.*;

import java.util.*;

/**
 * User: alexkasko
 * Date: 10/20/15
 */
public class ChartSettings implements ConfigurableChart {
    // todo: revisit serialization
    LinkedHashMap<String, DoubleConfigEntry> doubleEntries = new LinkedHashMap<String, DoubleConfigEntry>();
    LinkedHashMap<String, BoolConfigEntry> boolEntries = new LinkedHashMap<String, BoolConfigEntry>();
    LinkedHashMap<String, StringConfigEntry> stringEntries = new LinkedHashMap<String, StringConfigEntry>();
    LinkedHashMap<String, RegexConfigEntry> regexEntries = new LinkedHashMap<String, RegexConfigEntry>();
    ArrayList<String> order = new ArrayList<String>();

    public ChartSettings() {
    }

    ChartSettings(Collection<? extends ChartConfigEntry<?>> entries) {
        for (ChartConfigEntry en : entries) {
            if (en instanceof StringConfigEntry) {
                stringEntries.put(en.getName(), (StringConfigEntry) en);
            } else if (en instanceof RegexConfigEntry) {
                regexEntries.put(en.getName(), (RegexConfigEntry) en);
            } else if (en instanceof DoubleConfigEntry) {
                doubleEntries.put(en.getName(), (DoubleConfigEntry) en);
            } else if (en instanceof BoolConfigEntry) {
                boolEntries.put(en.getName(), (BoolConfigEntry) en);
            }
            order.add(en.getName());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<? extends ChartConfigEntry<?>> availableConfig() {
        List<ChartConfigEntry> res = new ArrayList<ChartConfigEntry>();
        for (String name : order) {
            res.add(getByName(name));
        }
        return (Collection) res;
    }

    @Override
    public ConfigurableChart applyConfig(Map<String, ChartConfigEntry<?>> entries) {
        return this;
    }

    private ChartConfigEntry getByName(String name) {
        ChartConfigEntry en = stringEntries.get(name);
        if (null != en) return en;
        en = doubleEntries.get(name);
        if (null != en) return en;
        en = boolEntries.get(name);
        if (null != en) return en;
        en = stringEntries.get(name);
        if (null != en) return en;
        en = regexEntries.get(name);
        if (null != en) return en;
        throw new SettingsException("Invalid setting name: [" + name + "]");
    }
}
