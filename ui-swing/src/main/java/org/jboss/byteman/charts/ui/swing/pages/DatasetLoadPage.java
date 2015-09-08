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
import org.jboss.byteman.charts.data.ChartRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.ui.*;
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

import static org.jboss.byteman.charts.ui.swing.pages.FiltersetPage.ALL_RECORDS_LABEL;
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

    public static final Gson GSON = new Gson();
    private static final Type CHART_RECORD_LIST_TYPE = new TypeToken<ArrayList<ChartRecord>>(){}.getType();

    public static final String NAME = "data";

    private final AtomicInteger datasetNumber = new AtomicInteger(0);

    private JTextField nameField = new JTextField();
    private JTextField pathField = new JTextField();

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

        // todo: removeme
        String testpath = "/home/alex/projects/redhat/byteman-charts/plot-aggregate/src/test/resources/org/jboss/byteman/charts/plot/aggregate/reports_data.json";
        new LoadFileWorker(new File(testpath), "test").execute();
        // end: removeme

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
        JLabel nameLabel = new JLabel("Dataset Name:");
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

    private String formatDatasetName(File fi) {
        SimpleDateFormat sdf = new SimpleDateFormat(ctx.getProp("byteman_charts.dataset_name_date_format"));
        String date = sdf.format(new Date(fi.lastModified()));
        String filename = stripFilenameExtension(fi.getName());
        String template = ctx.getProp("byteman_charts.dataset_name_format");
        String formatted = StrSubstitutor.replace(template, toMap("filename", filename, "date", date));
        return formatted + "_" + datasetNumber.incrementAndGet();
    }

    private void fileLoaded(File file, List<ChartRecord> records, String dsname) {
        PageManager pm = ctx.getPageManager();
        ContentPage page = new DatasetPage(ctx, dsname, file.getName(), file.length(), records.size());
        pm.addPage(page, NAME);
        String filtername = dsname + "_" + ALL_RECORDS_LABEL;
        // todo: where to define filters?
        ContentPage filterpage = new FiltersetPage(ctx, filtername, ALL_RECORDS_LABEL, dsname, records, Arrays.asList(
                new StringTestFilter(),
                new ListTestFilter(),
                new DatetimeTestFilter()));
        pm.addPage(filterpage, dsname);
//        pm.switchPage(dsname);
        pm.switchPage(filtername);
        clearForm();
    }

    private void clearForm() {
        nameField.setText("");
        pathField.setText("");
    }

    private ArrayList<ChartRecord> readData(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            Reader reader = new InputStreamReader(is, UTF_8);
            return GSON.fromJson(reader, CHART_RECORD_LIST_TYPE);
        } catch (FileNotFoundException e) {
            throw new UiSwingException("Data load error for file: [" + file.getAbsolutePath() + "]", e);
        } finally {
            closeQuietly(is);
        }
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
        @SuppressWarnings("unchecked") // publish call
        protected Void doInBackground() throws Exception {
            List<ChartRecord> recs = readData(file);
            publish(recs);
            return null;
        }

        @Override
        protected void process(List<List<ChartRecord>> chunks) {
            if (1 == chunks.size()) {
                fileLoaded(file, chunks.get(0), dsname);
            }

        }
    }

    static class StringTestFilter implements ChartFilter {
        @Override
        public boolean apply(ChartRecord record) {
            return true;
        }

        @Override
        public ChartConfigEntry<?> configEntry() {
            return new StringConfigEntry("Test string", "some default");
        }
    }

    static class ListTestFilter implements ChartFilter {

        @Override
        public boolean apply(ChartRecord record) {
            return true;
        }

        @Override
        public ChartConfigEntry<?> configEntry() {
            return new ListConfigEntry("Test combobox", Arrays.asList("foo", "bar", "baz"));
        }
    }

    static class DatetimeTestFilter implements ChartFilter {
        @Override
        public boolean apply(ChartRecord record) {
            return true;
        }

        @Override
        public ChartConfigEntry<?> configEntry() {
            return new DateTimeConfigEntry("Test date", new Date(), new Date(0), new Date());
        }
    }
}
