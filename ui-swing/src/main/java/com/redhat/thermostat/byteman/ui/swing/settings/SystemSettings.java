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
package com.redhat.thermostat.byteman.ui.swing.settings;

import com.redhat.thermostat.byteman.chart.ChartConfigEntry;
import com.redhat.thermostat.byteman.chart.Configurable;
import com.redhat.thermostat.byteman.chart.StringConfigEntry;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Settings implementation for system settinfs
 *
 * @author akashche
 * Date: 10/20/15
 */
public class SystemSettings implements Configurable {

    private String lastChosenDataFile = "";
    private String datasetNameFormat = "${filename}_${date}";
    private String datasetNameDateFormat = "yyyyMMdd_HHmmss";


    /**
     * @inheritDoc
     */
    @Override
    public Collection<? extends ChartConfigEntry<?>> availableConfig() {
        return Arrays.asList(
                new StringConfigEntry("lastChosenDataFile", lastChosenDataFile),
                new StringConfigEntry("datasetNameFormat", datasetNameFormat),
                new StringConfigEntry("datasetNameDateFormat", datasetNameDateFormat)
        );
    }

    /**
     * @inheritDoc
     */
    @Override
    public Configurable applyConfig(Map<String, ChartConfigEntry<?>> entries) {
        this.lastChosenDataFile = entries.get("lastChosenDataFile").getValue().toString();
        this.datasetNameFormat = entries.get("datasetNameFormat").getValue().toString();
        this.datasetNameDateFormat = entries.get("datasetNameDateFormat").getValue().toString();
        return this;
    }
}
