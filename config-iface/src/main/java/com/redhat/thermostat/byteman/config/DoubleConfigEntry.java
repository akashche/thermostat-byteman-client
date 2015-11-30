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

/**
 * Configuration entry for double-precision floating point values
 *
 * @author akashche
 * Date: 6/3/15
 */
public class DoubleConfigEntry extends ConfigEntryBase<Double> {

    protected double minValue;
    protected double maxValue;
    protected double step;

    /**
     * Constructor
     */
    public DoubleConfigEntry() {
    }

    /**
     * Constructor
     *
     * @param label entry label
     * @param defaultValue default value
     * @param minValue minimum value that will be allowed
     * @param maxValue maximum value that will be allowed
     * @param step step value that will be used for "more" and
     *             "less" UI actions
     */
    public DoubleConfigEntry(String label, double defaultValue, double minValue, double maxValue, double step) {
        super("com.redhat.thermostat.byteman.ui.swing.controls.DoubleSpinnerControl", label, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
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
    private DoubleConfigEntry(Double value, Double defaultValue, String type, String name, String label, String layoutOptions, double minValue, double maxValue, double step) {
        super(value, defaultValue, type, name, label, layoutOptions);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
    }

    /**
     * Access for the minimum value that will be allowed
     *
     * @return minimum value that will be allowed
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * Access for the maximum value that will be allowed
     *
     * @return maximum value that will be allowed
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * Accessor for the step value  that will be used for "more" and
     * "less" UI actions
     *
     * @return step value  that will be used for "more" and
     *         "less" UI actions
     */
    public double getStep() {
        return step;
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T1 extends ChartConfigEntry<Double>> T1 copy() {
        return (T1) new DoubleConfigEntry(value, defaultValue, type, name, label, layoutOptions, minValue, maxValue, step);
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
        sb.append(", step='").append(step).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
