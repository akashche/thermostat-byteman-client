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
import org.jboss.byteman.charts.data.ChartRecord;
import org.jboss.byteman.charts.utils.string.StrSubstitutor;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static org.jboss.byteman.charts.utils.CollectionUtils.toMap;
import static org.jboss.byteman.charts.utils.StringUtils.defaultString;
import static org.jboss.byteman.charts.utils.StringUtils.isEmpty;
import static org.jboss.byteman.charts.utils.StringUtils.stripFilenameExtension;
import static org.jboss.byteman.charts.utils.SwingUtils.boldify;
import static org.jboss.byteman.charts.utils.SwingUtils.createFormSectionBorder;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
class DataRootPage extends BasePage {

    public static final String NAME = "data";

    private JTextField nameField = new JTextField();
    private JTextField pathField = new JTextField();

    DataRootPage(ChartsAppContext ctx) {
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
                "[]"
        ));
        jp.setBorder(createFormSectionBorder(jp.getBackground().darker(), "Load chart data"));
        // first row
        JLabel nameLabel = new JLabel("Dataset name:");
        boldify(nameLabel);
        jp.add(nameLabel, "width ::160lp");
        jp.add(nameField, "width 160lp::, span 2, wrap");
        // second row
        JLabel chooserLabel = new JLabel("Data file:");
        boldify(chooserLabel);
        jp.add(chooserLabel, "width ::160lp");
        pathField.setEditable(false);
        jp.add(pathField, "width 160lp::");
        JButton chooseButton = new JButton("...");
        chooseButton.addActionListener(new ChooseFileListener());
        jp.add(chooseButton, "width pref!, wrap");
        return jp;
    }

    private JPanel createButtonsPanel() {
        JPanel jp = new JPanel(new MigLayout(
                "fillx, insets n 0 0 0",
                "push[right][right]",
                "[]"
        ));
        JButton loadButton = new JButton("Load data");
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

    private String formatDatasetName(File fi) {
        SimpleDateFormat sdf = new SimpleDateFormat(ctx.getProp("byteman_charts.dataset_name_date_format"));
        String date = sdf.format(new Date(fi.lastModified()));
        String filename = stripFilenameExtension(fi.getName());
        String template = ctx.getProp("byteman_charts.dataset_name_format");
        return StrSubstitutor.replace(template, toMap("filename", filename, "date", date));
    }

    private void fileLoaded(List<ChartRecord> records, String dsname) {
        ContentPage page = new DatasetPage(ctx, dsname, records);
        ctx.getPageManager().addPage(page, NAME);
        clearForm();
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
            new LoadFileWorker(new File(pathField.getText()), nameField.getText()).execute();
        }
    }

    private class ClearFormListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clearForm();
        }
    }

    private class LoadFileWorker extends SwingWorker<Void, List<ChartRecord>> {

        private final File file;
        private final String dsname;

        private LoadFileWorker(File file, String dsname) {
            this.file = file;
            this.dsname = dsname;
        }

        @Override
        protected Void doInBackground() throws Exception {
            publish(Collections.<ChartRecord>emptyList());
            return null;
        }

        @Override
        protected void process(List<List<ChartRecord>> chunks) {
            if (1 == chunks.size()) {
                fileLoaded(chunks.get(0), dsname);
            }

        }
    }
}
