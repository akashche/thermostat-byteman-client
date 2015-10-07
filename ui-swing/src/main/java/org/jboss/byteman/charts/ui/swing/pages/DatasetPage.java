/*
* JBoss, Home of Professional Open Source
* Copyright 2015 Red Hat and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.byteman.charts.ui.swing.pages;

import net.miginfocom.swing.MigLayout;
import org.jboss.byteman.charts.plot.Plotter;

import javax.swing.*;
import java.awt.*;

import static org.jboss.byteman.charts.utils.SwingUtils.boldify;
import static org.jboss.byteman.charts.utils.SwingUtils.createFormSectionBorder;

/**
 * User: alexkasko
 * Date: 6/10/15
 */

// subnodes for each applied filter set
class DatasetPage extends BasePage {

    private final String datasetName;
    private final String filename;
    private final long filesize;
    private final int recordsCount;
    private final Plotter plotter;

    public DatasetPage(ChartsAppContext ctx, String name, String filename, long filesize, int recordsCount, Plotter plotter) {
        super(ctx, name, name, "filesystem_folder_blue_16.png");
        this.datasetName = name;
        this.filename = filename;
        this.filesize = filesize;
        this.recordsCount = recordsCount;
        this.plotter = plotter;
    }

    @Override
    public boolean isUserPage() {
        return true;
    }

    @Override
    public Component createPane() {
        JPanel parent = new JPanel(new MigLayout(
                "fill",
                "[]",
                "[top]"
        ));
        parent.add(createDetailsPanel(), "growx");
        return parent;

    }

    private JPanel createDetailsPanel() {
        JPanel jp = new JPanel(new MigLayout(
                "",
                "[right][grow, fill]",
                "[]"
        ));
        jp.setBorder(createFormSectionBorder(jp.getBackground().darker(), "Dataset details"));
        // datasetName
        jp.add(boldify(new JLabel("Dataset Name:")), "width ::160lp");
        jp.add(new JLabel(datasetName), "width 160lp::, span 2, wrap");
        // filename
        jp.add(boldify(new JLabel("File Name:")), "width ::160lp");
        jp.add(new JLabel(filename), "width 160lp::, span 2, wrap");
        // filesize
        jp.add(boldify(new JLabel("File Size (bytes):")), "width ::160lp");
        jp.add(new JLabel(Long.toString(filesize)), "width 160lp::, span 2, wrap");
        // recordsCount
        jp.add(boldify(new JLabel("Records Count:")), "width ::160lp");
        jp.add(new JLabel(Long.toString(recordsCount)), "width 160lp::, span 2, wrap");
        // chart type
        jp.add(boldify(new JLabel("Chart:")), "width ::160lp");
        jp.add(new JLabel(plotter.getName()), "width 160lp::, span 2, wrap");

        return jp;
    }


}
