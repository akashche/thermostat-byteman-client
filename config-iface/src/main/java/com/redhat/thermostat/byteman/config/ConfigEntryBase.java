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
package com.redhat.thermostat.byteman.config;

import static com.redhat.thermostat.byteman.utils.string.StringUtils.EMPTY_STRING;
import static com.redhat.thermostat.byteman.utils.string.StringUtils.defaultString;

/**
 * Abstract base class for the ordinary configuration entries
 *
 * @author akashche
 * Date: 6/2/15
 */
public abstract class ConfigEntryBase<T> implements ChartConfigEntry<T> {

    protected T value;
    protected T defaultValue;
    protected String type = EMPTY_STRING;
    protected String name = EMPTY_STRING;
    protected String label = EMPTY_STRING;
    protected String layoutOptions = DEFAULT_CONTROL_LAYOUT_OPTIONS;

    /**
     * Constructor
     */
    protected ConfigEntryBase() {
    }

    /**
     * Constructor
     *
     * @param type entry UI-specific type
     * @param label entry label
     * @param defaultValue default value
     */
    protected ConfigEntryBase(String type, String label, T defaultValue) {
        if (null == type) throw new ChartConfigException("specified type is null");
        if (null == label) throw new ChartConfigException("specified label is null");
        if (null == defaultValue) throw new ChartConfigException("specified defaultValue is null");
        this.type = type;
        this.name = label;
        this.label = label;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
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
    protected ConfigEntryBase(T value, T defaultValue, String type, String name, String label, String layoutOptions) {
        this.value = value;
        this.defaultValue = defaultValue;
        this.type = type;
        this.name = name;
        this.label = label;
        this.layoutOptions = layoutOptions;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getName() {
        return defaultString(name);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getLabel() {
        return defaultString(label);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getLayoutOptions() {
        return defaultString(layoutOptions);
    }

    /**
     * @inheritDoc
     */
    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * @inheritDoc
     */
    @Override
    public T getValue() {
        return value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("{name='").append(name).append('\'');
        sb.append(", label='").append(label).append('\'');
        sb.append(", layoutOptions='").append(layoutOptions).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
