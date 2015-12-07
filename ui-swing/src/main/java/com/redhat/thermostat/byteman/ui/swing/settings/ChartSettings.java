/*
 * Copyright 2012-2015 Red Hat, Inc.
 *
 * This file is part of Thermostat.
 *
 * Thermostat is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2, or (at your
 * option) any later version.
 *
 * Thermostat is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Thermostat; see the file COPYING.  If not see
 * <http://www.gnu.org/licenses/>.
 *
 * Linking this code with other modules is making a combined work
 * based on this code.  Thus, the terms and conditions of the GNU
 * General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this code give
 * you permission to link this code with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also
 * meet, for each linked independent module, the terms and conditions
 * of the license of that module.  An independent module is a module
 * which is not derived from or based on this code.  If you modify
 * this code, you may extend this exception to your version of the
 * library, but you are not obligated to do so.  If you do not wish
 * to do so, delete this exception statement from your version.
 */
package com.redhat.thermostat.byteman.ui.swing.settings;

import com.redhat.thermostat.byteman.chart.*;

import java.util.*;

/**
 * Settings implementation for the charts that supports
 * configuration
 *
 * @author akashche
 * Date: 10/20/15
 */
public class ChartSettings implements Configurable {
    // todo: revisit serialization
    LinkedHashMap<String, DoubleConfigEntry> doubleEntries = new LinkedHashMap<String, DoubleConfigEntry>();
    LinkedHashMap<String, BoolConfigEntry> boolEntries = new LinkedHashMap<String, BoolConfigEntry>();
    LinkedHashMap<String, StringConfigEntry> stringEntries = new LinkedHashMap<String, StringConfigEntry>();
    LinkedHashMap<String, RegexConfigEntry> regexEntries = new LinkedHashMap<String, RegexConfigEntry>();
    ArrayList<String> order = new ArrayList<String>();

    /**
     * Constructor for the deserialization access
     */
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

    private ChartSettings(LinkedHashMap<String, DoubleConfigEntry> doubleEntries, LinkedHashMap<String, BoolConfigEntry> boolEntries, LinkedHashMap<String, StringConfigEntry> stringEntries, LinkedHashMap<String, RegexConfigEntry> regexEntries, ArrayList<String> order) {
        this.doubleEntries = doubleEntries;
        this.boolEntries = boolEntries;
        this.stringEntries = stringEntries;
        this.regexEntries = regexEntries;
        this.order = order;
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<? extends ChartConfigEntry<?>> availableConfig() {
        List<ChartConfigEntry> res = new ArrayList<ChartConfigEntry>();
        for (String name : order) {
            res.add(getByName(name));
        }
        return (Collection) res;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Configurable applyConfig(Map<String, ChartConfigEntry<?>> entries) {
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

    public ChartSettings deepCopy() {
        LinkedHashMap<String, DoubleConfigEntry> de = new LinkedHashMap<String, DoubleConfigEntry>();
        for (Map.Entry<String, DoubleConfigEntry> en : doubleEntries.entrySet()) {
            DoubleConfigEntry cp = en.getValue().copy();
            de.put(en.getKey(), cp);
        }
        LinkedHashMap<String, BoolConfigEntry> be = new LinkedHashMap<String, BoolConfigEntry>();
        for (Map.Entry<String, BoolConfigEntry> en : boolEntries.entrySet()) {
            BoolConfigEntry cp = en.getValue().copy();
            be.put(en.getKey(), cp);
        }
        LinkedHashMap<String, StringConfigEntry> se = new LinkedHashMap<String, StringConfigEntry>();
        for (Map.Entry<String, StringConfigEntry> en : stringEntries.entrySet()) {
            StringConfigEntry cp = en.getValue().copy();
            se.put(en.getKey(), cp);
        }
        LinkedHashMap<String, RegexConfigEntry> re = new LinkedHashMap<String, RegexConfigEntry>();
        for (Map.Entry<String, RegexConfigEntry> en : regexEntries.entrySet()) {
            RegexConfigEntry cp = en.getValue().copy();
            re.put(en.getKey(), cp);
        }
        ArrayList<String> or = new ArrayList<String>(order);
        return new ChartSettings(de, be, se, re, or);
    }

    public Map<String, ChartConfigEntry<?>> configAsMap() {
        Map<String, ChartConfigEntry<?>> res = new LinkedHashMap<String, ChartConfigEntry<?>>();
        for (ChartConfigEntry<?>  en : availableConfig()) {
            res.put(en.getName(), en);
        }
        return res;
    }
}
