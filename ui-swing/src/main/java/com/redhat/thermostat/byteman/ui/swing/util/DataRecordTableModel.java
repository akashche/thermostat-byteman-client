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

import com.redhat.thermostat.byteman.data.DataRecord;
import com.redhat.thermostat.byteman.filter.ChartFilter;
import com.redhat.thermostat.byteman.filter.ChartFilteredIterator;
import com.redhat.thermostat.byteman.utils.date.FastDateFormat;

import javax.swing.table.AbstractTableModel;
import java.util.*;

import static com.redhat.thermostat.byteman.utils.string.StringUtils.EMPTY_STRING;
import static com.redhat.thermostat.byteman.utils.collection.SingleUseIterable.singleUseIterable;

/**
 * Table model for data records with arbitrary fields
 *
 * @author akashche
 * Date: 8/28/15
 */
public class DataRecordTableModel extends AbstractTableModel {
    private final ArrayList<DataRecord> data = new ArrayList<DataRecord>();
    private final int columnCount;
    private final FastDateFormat dtf;

    /**
     * Constructor
     *
     * @param records data records iterator
     * @param filters set of filters that should be applied before the table display
     * @param dateFormat format string for date fields
     */
    public DataRecordTableModel(Iterator<DataRecord> records, Collection<? extends ChartFilter> filters,
                                String dateFormat) {
        int maxCols = 0;
        Iterator<DataRecord> filtered = new ChartFilteredIterator(records, filters);
        for (DataRecord cr : singleUseIterable(filtered)) {
            int size = cr.getData().size();
            if (size > maxCols) {
                maxCols = size;
            }
            data.add(cr);
        }
        this.columnCount = maxCols + DataRecord.STATIC_FIELDS_COUNT;
        this.dtf = FastDateFormat.getInstance(dateFormat);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getRowCount() {
        return data.size();
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * @inheritDoc
     */
    @Override
    @SuppressWarnings("unchecked") // map entry
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object res = data.get(rowIndex).getValueAt(columnIndex);
        if (0 == columnIndex && res instanceof Long) {
            Long lo = (Long) res;
            return dtf.format(lo.longValue());
        }
        if (res instanceof Map.Entry) {
            Map.Entry<String, Object> en = (Map.Entry) res;
            return new MapEntryCell(en);
        }
        return res;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Date";
            case 1: return "VM ID";
            case 2: return "Agent ID";
            case 3: return "Marker";
            default: return EMPTY_STRING;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
            case 3: return String.class;
            default: return MapEntryCell.class;
        }
    }

    static class MapEntryCell implements Comparable<MapEntryCell> {
        private final Map.Entry<String, Object> en;

        MapEntryCell(Map.Entry<String, Object> en) {
            this.en = en;
        }

        Object getValue() {
            return en.getValue();
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("[")
                    .append(en.getKey())
                    .append("] ")
                    .append(en.getValue())
                    .toString();
        }

        @Override
        public int compareTo(MapEntryCell o) {
            Object val1;
            Object val2;
            val1 = this.en.getValue();
            val2 = o.en.getValue();
            if (null == val1) return -1;
            if (null == val2) return 1;
            if (val1 instanceof Number && val2 instanceof Number) {
                Number num1 = (Number) val1;
                Number num2 = (Number) val2;
                double db1 = num1.doubleValue();
                double db2 = num2.doubleValue();
                if (db1 > db2) return 1;
                if (db1 < db2) return -1;
                return 0;
            } else if (val1 instanceof String && val2 instanceof String) {
                String st1 = (String) val1;
                String st2 = (String) val2;
                return st1.compareTo(st2);
            } else if (val1 instanceof Boolean && val2 instanceof Boolean) {
                Boolean b1 = (Boolean) val1;
                Boolean b2 = (Boolean) val2;
                return b1.compareTo(b2);
            }
            return 0;
        }
    }
}
