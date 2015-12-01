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

/**
 * Base interface for all configuration entries.
 * Configuration entry is a "name->value" pair that can be displayed
 * using an unspecified UI tool. Display details are controlled by the
 * client using {@code String} layout options field that may contain arbitrary
 * display details for the chosen UI tool. By default Swing UI with
 * Mig layout framework is used.
 *
 * @author akashche
 * Date: 6/2/15
 */
public interface ChartConfigEntry<T> {

    /**
     * Layout options that are used by default. These options are used only
     * if entry-specific layout options are not specified in constructor.
     */
    public static final String DEFAULT_CONTROL_LAYOUT_OPTIONS = "pushx, growx, width 180lp:180lp:, wrap";

    /**
     * Access for the UI-specific type of this entry.
     *
     * @return UI-specific type of this entry
     */
    String getType();

    /**
     * Accessor for the name of this entry
     *
     * @return name of this entry
     */
    String getName();

    /**
     * Accessor for the label of this entry
     *
     * @return label of this entry
     */
    String getLabel();

    /**
     * Accessor for the layout options of this entry
     *
     * @return layout options of this entry
     */
    String getLayoutOptions();

    /**
     * Accessor for the default value of this entry
     *
     * @return default value of this entry
     */
    T getDefaultValue();

    /**
     * Accessor for the value of this entry
     *
     * @return value of this entry
     */
    T getValue();

    /**
     * Sets the value for this entry
     *
     * @param value new value for this entry
     */
    void setValue(T value);

    /**
     * Deep-copies this entry into the new one
     *
     * @param <T1> entry type
     * @return new entry
     */
    <T1 extends ChartConfigEntry<T>> T1 copy();
}
