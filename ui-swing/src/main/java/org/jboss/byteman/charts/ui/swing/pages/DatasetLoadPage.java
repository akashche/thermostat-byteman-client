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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.miginfocom.swing.MigLayout;
import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.filter.*;
import org.jboss.byteman.charts.plot.Plotter;
import org.jboss.byteman.charts.ui.*;
import org.jboss.byteman.charts.ui.swing.util.ColumnFitTable;
import org.jboss.byteman.charts.ui.swing.util.PlotterTableModel;
import org.jboss.byteman.charts.utils.string.StrSubstitutor;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import static org.jboss.byteman.charts.utils.CollectionUtils.toMap;
import static org.jboss.byteman.charts.utils.IOUtils.closeQuietly;
import static org.jboss.byteman.charts.utils.StringUtils.*;
import static org.jboss.byteman.charts.utils.SwingUtils.boldify;
import static org.jboss.byteman.charts.utils.SwingUtils.createFormSectionBorder;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
class DatasetLoadPage extends BasePage {

    public static final String NAME = "data";

    private final AtomicInteger datasetNumber = new AtomicInteger(0);

    private JTextField nameField = new JTextField();
    private JTextField pathField = new JTextField();
    private JTable plotsTable = new JTable();

    DatasetLoadPage(ChartsAppContext ctx) {
        super(ctx, NAME, "Data Sets", "app_database_16.png");
    }

    @Override
    public Component createPane() {
        JPanel jp = new JPanel(new MigLayout(
                "fill",
                "[]",
                "[top][bottom]"
        ));
        jp.add(createLoadFilePanel(), "growx, wrap");
//        jp.add(createLoadStoragePanel(), "growx, wrap");
        jp.add(createButtonsPanel(), "growx");

        return jp;
    }

    private JPanel createLoadFilePanel() {
        JPanel jp = new JPanel(new MigLayout(
                "",
                "[right][grow, fill][]",
                "[center][center][top][top]"
        ));
        jp.setBorder(createFormSectionBorder(jp.getBackground().darker(), "Load chart data"));
        // first row
        jp.add(boldify(new JLabel("Dataset Name:")), "width ::160lp");
        jp.add(nameField, "width 160lp::, span 2, wrap");
        // second row
        jp.add(boldify(new JLabel("Data file:")), "width ::160lp");
        pathField.setEditable(false);
        jp.add(pathField, "width 160lp::");
        JButton chooseButton = new JButton("...");
        chooseButton.addActionListener(new ChooseFileListener());
        jp.add(chooseButton, "width pref!, wrap");
        // grid
        jp.add(boldify(new JLabel("Chart:")), "width ::160lp");
        jp.add(createPlotsTable(), "height ::128lp, span 2, wrap");
        return jp;
    }

    private JPanel createButtonsPanel() {
        JPanel jp = new JPanel(new MigLayout(
                "fillx, insets n 0 0 0",
                "push[right][right]",
                "[]"
        ));
        JButton loadButton = new JButton("Load Data");
        loadButton.addActionListener(new LoadFileListener());
        jp.add(loadButton);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ClearFormListener());
        jp.add(clearButton);
        jp.setBorder(createFormSectionBorder(jp.getBackground().darker(), ""));
        return jp;
    }

    private JFileChooser createFileChooser() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(ctx.getProp("byteman_charts.last_chosen_data_file")));
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle("Choose chart data file");
        FileFilter all = fc.getAcceptAllFileFilter();
        fc.removeChoosableFileFilter(all);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("JSON chart data files", "json"));
        fc.addChoosableFileFilter(all);
        return fc;
    }

    private Component createPlotsTable() {
        JPanel jp = new JPanel(new MigLayout(
                "fill, insets 0",
                "[]",
                "[top]"
        ));
        PlotterTableModel tm = new PlotterTableModel(ContentPagesRegister.PLOTS);
        plotsTable = new ColumnFitTable(tm);
        plotsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        JScrollPane sp = new JScrollPane(plotsTable, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setBorder(createMatteBorder(1, 1, 1, 1, plotsTable.getBackground().darker()));
        jp.add(sp, "grow");
        return jp;
    }

    private String formatDatasetName(File fi) {
        SimpleDateFormat sdf = new SimpleDateFormat(ctx.getProp("byteman_charts.dataset_name_date_format"));
        String date = sdf.format(new Date(fi.lastModified()));
        String filename = stripFilenameExtension(fi.getName());
        String template = ctx.getProp("byteman_charts.dataset_name_format");
        String formatted = StrSubstitutor.replace(template, toMap("filename", filename, "date", date));
        return formatted + "_" + datasetNumber.incrementAndGet();
    }

    private void clearForm() {
        nameField.setText("");
        pathField.setText("");
    }

    private class ChooseFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = createFileChooser();
            int ret = fc.showOpenDialog(pathField);
            if (JFileChooser.APPROVE_OPTION == ret) {
                ctx.setProp("byteman_charts.last_chosen_data_file", fc.getSelectedFile().getParentFile().getAbsolutePath());
                pathField.setText(fc.getSelectedFile().getAbsolutePath());
                if (isEmpty(nameField.getText())) {
                    File fi = fc.getSelectedFile();
                    String name = formatDatasetName(fi);
                    nameField.setText(name);
                }
            }
        }
    }

    private class LoadFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Plotter plotter = ContentPagesRegister.PLOTS.get(plotsTable.getSelectedRow());
//            Plotter plotter = ContentPagesRegister.PLOTS.get(0);
            ContentPage page = new DatasetPage(ctx, nameField.getText(), new File(pathField.getText()), plotter);
//            ContentPage page = new DatasetPage(ctx, "reports_data_20150926_190707_2", new File("/home/alex/projects/redhat/byteman-charts/plot-aggregate/src/test/resources/org/jboss/byteman/charts/plot/aggregate/reports_data.json"), plotter);
            ctx.getPageManager().addPageAsync(page, NAME);
            clearForm();
        }
    }

    private class ClearFormListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clearForm();
        }
    }
}
