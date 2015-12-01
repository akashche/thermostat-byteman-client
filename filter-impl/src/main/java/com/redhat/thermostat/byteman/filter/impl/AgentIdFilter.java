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

import com.redhat.thermostat.byteman.data.DataRecord;
import com.redhat.thermostat.byteman.config.ChartConfigEntry;
import com.redhat.thermostat.byteman.config.RegexConfigEntry;
import com.redhat.thermostat.byteman.filter.ChartFilter;

/**
 * Data record field filter implementation that uses
 * regular expression matching against "agentId" fixed
 * field of the data record
 *
 * @author akashche
 * Date: 10/7/15
 */
public class AgentIdFilter implements ChartFilter {
    protected final RegexConfigEntry en;

    /**
     * Constructor
     *
     * @param name name of the config entry that corresponds to
     *             this filter
     */
    public AgentIdFilter(String name) {
        this(new RegexConfigEntry(name, "^.*$"));
    }

    /**
     * Constructor
     *
     * @param en configuration entry to use for managing state of this filter
     */
    private AgentIdFilter(RegexConfigEntry en) {
        this.en = en;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean apply(DataRecord record) {
        if (null == record.getAgentId()) return false;
        return en.getPattern().matcher(record.getAgentId()).matches();
    }

    /**
     * @inheritDoc
     */
    @Override
    public ChartConfigEntry<?> configEntry() {
        return en;
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ChartFilter> T copy() {
        RegexConfigEntry cp = en.copy();
        return (T) new AgentIdFilter(cp);
    }
}
