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
package com.redhat.thermostat.byteman.ui.swing.util;

import com.redhat.thermostat.byteman.plot.Plotter;

import javax.swing.table.AbstractTableModel;
import java.util.List;

import static com.redhat.thermostat.byteman.utils.string.StringUtils.EMPTY_STRING;

/**
 * Table model implementation for the list of {@code Plotter} instances
 *
 * @author akashche
 * Date: 10/7/15
 */
public class PlotterTableModel extends AbstractTableModel {
    private final List<? extends Plotter> plots;

    /**
     * Constructor
     *
     * @param plots list of {@code Plotter} instances
     */
    public PlotterTableModel(List<? extends Plotter> plots) {
        this.plots = plots;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getRowCount() {
        return plots.size();
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getColumnCount() {
        return 3;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Plotter pl = plots.get(rowIndex);
        switch (columnIndex) {
            case 0: return pl.getName();
            case 1: return pl.getType();
            case 2: return pl.getDescription();
            default: return EMPTY_STRING;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Name";
            case 1: return "Type";
            case 2: return "Description";
            default: return EMPTY_STRING;
        }
    }
}
