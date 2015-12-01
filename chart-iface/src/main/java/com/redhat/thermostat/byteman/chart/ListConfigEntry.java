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

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration entry for the lists of the string values
 *
 * @author akashche
 * Date: 6/3/15
 */
public class ListConfigEntry extends ConfigEntryBase<String> {

    protected List<String> valuesList = new ArrayList<String>();

    /**
     * Constructor
     */
    public ListConfigEntry() {
    }

    /**
     * Constructor
     *
     * @param label entry label
     * @param valuesList default values
     */
    public ListConfigEntry(String label, List<String> valuesList) {
        super("com.redhat.thermostat.byteman.ui.swing.controls.ComboBoxControl", label, valuesList.get(0));
        this.valuesList = valuesList;
    }

    /**
     * Accessor for the default list of values
     *
     * @return default list of values
     */
    public List<String> getValuesList() {
        return null != valuesList ? valuesList : new ArrayList<String>();
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
        sb.append(", valuesList='").append(valuesList).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * @inheritDoc
     */
    @Override
    public <T1 extends ChartConfigEntry<String>> T1 copy() {
        // todo
        return null;
    }
}
