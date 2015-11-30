/*
* JBoss, Home of Professional Open Source
* Copyright 2015 Red Hat and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.byteman.charts.ui;

import static org.jboss.byteman.charts.utils.string.StringUtils.EMPTY_STRING;
import static org.jboss.byteman.charts.utils.string.StringUtils.defaultString;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public abstract class ConfigEntryBase<T> implements ChartConfigEntry<T> {

    protected T value;
    protected T defaultValue;
    protected String type = EMPTY_STRING;
    protected String name = EMPTY_STRING;
    protected String label = EMPTY_STRING;
    protected String layoutOptions = DEFAULT_CONTROL_LAYOUT_OPTIONS;

    protected ConfigEntryBase() {
    }

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

    protected ConfigEntryBase(T value, T defaultValue, String type, String name, String label, String layoutOptions) {
        this.value = value;
        this.defaultValue = defaultValue;
        this.type = type;
        this.name = name;
        this.label = label;
        this.layoutOptions = layoutOptions;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return defaultString(name);
    }

    @Override
    public String getLabel() {
        return defaultString(label);
    }

    @Override
    public String getLayoutOptions() {
        return defaultString(layoutOptions);
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

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
