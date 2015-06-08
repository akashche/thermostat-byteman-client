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

/**
 * User: alexkasko
 * Date: 6/3/15
 */
public class DoubleConfigEntry extends ConfigEntryBase<Double> {

    protected double minValue;
    protected double maxValue;
    protected double step;

    public DoubleConfigEntry() {
    }

    public DoubleConfigEntry(String label, double defaultValue, double minValue, double maxValue, double step) {
        super("org.jboss.byteman.charts.ui.swing.DoubleSpinnerControl", label, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getStep() {
        return step;
    }

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
