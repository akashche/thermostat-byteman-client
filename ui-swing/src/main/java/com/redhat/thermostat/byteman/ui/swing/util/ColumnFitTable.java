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

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * JTable extension that takes care over the records with too mny fields
 *
 * @author akashche
 * Date: 8/28/15
 */
// http://stackoverflow.com/a/25570812/314015
public class ColumnFitTable extends JTable {
    private final int maxColWidth;

    /**
     * Constructor
     *
     * @param dm table model
     */
    public ColumnFitTable(TableModel dm) {
        this(dm, 256);
    }

    /**
     * Constructor
     *
     * @param dm table model
     * @param maxColWidth max width of a single column in pixels
     */
    public ColumnFitTable(TableModel dm, int maxColWidth) {
        super(dm);
        this.maxColWidth = maxColWidth;

        setShowVerticalLines(true);
        setGridColor(getBackground().darker());
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setAutoCreateRowSorter(true);
    }

    /**
     * @inheritDoc
     */
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
