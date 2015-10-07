package org.jboss.byteman.charts.ui.swing.util;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * User: alexkasko
 * Date: 8/28/15
 */
// http://stackoverflow.com/a/25570812/314015
public class ColumnFitTable extends JTable {
    private final int maxColWidth;

    public ColumnFitTable(TableModel dm) {
        this(dm, 256);
    }

    public ColumnFitTable(TableModel dm, int maxColWidth) {
        super(dm);
        this.maxColWidth = maxColWidth;

        setShowVerticalLines(true);
        setGridColor(getBackground().darker());
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setAutoCreateRowSorter(true);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component component = super.prepareRenderer(renderer, row, column);
        int rendererWidth = component.getPreferredSize().width;
        TableColumn tableColumn = getColumnModel().getColumn(column);
        int contentWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
        tableColumn.setPreferredWidth(Math.min(contentWidth, maxColWidth));
        return component;
    }
}
