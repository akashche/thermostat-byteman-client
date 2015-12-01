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
package com.redhat.thermostat.byteman.chart;

import java.util.regex.Pattern;

/**
 * Configuration entry for the string values that represents regular expressions
 *
 * @author akashche
 * Date: 10/7/15
 */
public class RegexConfigEntry extends ConfigEntryBase<String> {
    private Pattern pattern;

    /**
     * Constructor
     */
    public RegexConfigEntry() {
    }

    /**
     * Constructor
     *
     * @param label entry label
     * @param defaultValue default value
     */
    public RegexConfigEntry(String label, String defaultValue) {
        super("com.redhat.thermostat.byteman.ui.swing.controls.RegexFieldControl", label, defaultValue);
        pattern = Pattern.compile(defaultValue);
    }

    /**
     * Constructor
     *
     * @param value entry value
     * @param defaultValue entry default value
     * @param type entry UI-specific type
     * @param name entry name
     * @param label entry label
     * @param layoutOptions entry UI-specific layout options
     */
    private RegexConfigEntry(String value, String defaultValue, String type, String name, String label, String layoutOptions, Pattern pattern) {
        super(value, defaultValue, type, name, label, layoutOptions);
        this.pattern = pattern;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setValue(String value) {
        super.setValue(value);
        try {
            pattern = Pattern.compile(value, Pattern.DOTALL | Pattern.MULTILINE);
        } catch (Exception e) {
            // todo: visual validation
        }
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T1 extends ChartConfigEntry<String>> T1 copy() {
        return (T1) new RegexConfigEntry(value, defaultValue, type, name, label, layoutOptions, pattern);
    }

    /**
     * Accessor for the compiled regular expression pattern
     *
     * @return compiled regular expression pattern
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("{name='").append(name).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", label='").append(label).append('\'');
        sb.append(", layoutOptions='").append(layoutOptions).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", defaultValue='").append(defaultValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
