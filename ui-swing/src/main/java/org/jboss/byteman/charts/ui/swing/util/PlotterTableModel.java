package org.jboss.byteman.charts.ui.swing.util;

import org.jboss.byteman.charts.plot.Plotter;

import javax.swing.table.AbstractTableModel;
import java.util.List;

import static org.jboss.byteman.charts.utils.StringUtils.EMPTY_STRING;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
public class PlotterTableModel extends AbstractTableModel {
    private final List<? extends Plotter> plots;

    public PlotterTableModel(List<? extends Plotter> plots) {
        this.plots = plots;
    }

    @Override
    public int getRowCount() {
        return plots.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

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
