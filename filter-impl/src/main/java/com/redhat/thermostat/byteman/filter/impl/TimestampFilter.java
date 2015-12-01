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
package com.redhat.thermostat.byteman.filter.impl;

import com.redhat.thermostat.byteman.chart.ChartConfigEntry;
import com.redhat.thermostat.byteman.chart.DateTimeConfigEntry;
import com.redhat.thermostat.byteman.filter.ChartFilter;

import java.util.Date;

/**
 * Base data record field filter implementation to work
 * over the timestamp data record fields
 *
 * @author akashche
 * Date: 10/7/15
 */
abstract class TimestampFilter implements ChartFilter {
    protected final DateTimeConfigEntry en;

    /**
     * Constructor
     *
     * @param label name of the corresponding configuration entry
     * @param defaultValue default value for the corresponding configuration entry
     */
    protected TimestampFilter(String label, Date defaultValue) {
        en = new DateTimeConfigEntry(label, defaultValue, new java.util.Date(0), new Date());
    }

    /**
     * Constructor
     *
     * @param en configuration entry to use for managing state of this filter
     */
    protected TimestampFilter(DateTimeConfigEntry en) {
        this.en = en;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ChartConfigEntry<?> configEntry() {
        return en;
    }

    /**
     * Type-specific accessor for the corresponding config entry
     *
     * @return corresponding config entry
     */
    public DateTimeConfigEntry getEntry() {
        return en;
    }
}
