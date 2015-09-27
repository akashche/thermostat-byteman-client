package org.jboss.byteman.charts.ui.swing.util;

import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.filter.ChartFilteredIterator;
import org.jboss.byteman.charts.utils.date.FastDateFormat;

import javax.swing.table.AbstractTableModel;
import java.util.*;

import static org.jboss.byteman.charts.utils.StringUtils.EMPTY_STRING;
import static org.jboss.byteman.charts.utils.collection.SingleUseIterable.singleUseIterable;

/**
 * User: alexkasko
 * Date: 8/28/15
 */
public class ChartRecordTableModel extends AbstractTableModel {
    private final ArrayList<DataRecord> data = new ArrayList<DataRecord>();
    private final int columnCount;
    private final FastDateFormat dtf;

    public ChartRecordTableModel(Iterator<DataRecord> records, Collection<? extends ChartFilter> filters,
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

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

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
