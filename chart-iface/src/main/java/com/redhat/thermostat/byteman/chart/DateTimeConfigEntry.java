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

import java.util.Date;

/**
 * Configuration entry for date and time values
 *
 * @author akashche
 * Date: 6/3/15
 */
public class DateTimeConfigEntry extends ConfigEntryBase<Date> {
    protected Date minValue = new Date(0);
    protected Date maxValue = new Date();

    /**
     * Constructor
     */
    public DateTimeConfigEntry() {
    }

    /**
     * Constructor
     *
     * @param label entry label
     * @param defaultValue default value
     * @param minValue minimum date value that will be allowed
     * @param maxValue maximum date value that will be allowed
     */
    public DateTimeConfigEntry(String label, Date defaultValue, Date minValue, Date maxValue) {
        super("com.redhat.thermostat.byteman.ui.swing.controls.DateTimeSpinnerControl", label, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
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
    private DateTimeConfigEntry(Date value, Date defaultValue, String type, String name, String label, String layoutOptions, Date minValue, Date maxValue) {
        super(value, defaultValue, type, name, label, layoutOptions);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Access for the minimum date value that will be allowed
     *
     * @return minimum date value that will be allowed
     */
    public Date getMinValue() {
        return null != minValue ? minValue : new Date(0);
    }

    /**
     * Access for the maximum date value that will be allowed
     *
     * @return maximum date value that will be allowed
     */
    public Date getMaxValue() {
        return null != maxValue ? maxValue : new Date();
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T1 extends ChartConfigEntry<Date>> T1 copy() {
        return (T1) new DateTimeConfigEntry(value, defaultValue, type, name, label, layoutOptions, minValue, maxValue);
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
        sb.append(", minValue='").append(minValue).append('\'');
        sb.append(", maxValue='").append(maxValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
