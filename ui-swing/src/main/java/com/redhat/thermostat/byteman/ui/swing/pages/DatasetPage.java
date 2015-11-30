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
package com.redhat.thermostat.byteman.ui.swing.pages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.redhat.thermostat.byteman.filter.ChartFilter;
import com.redhat.thermostat.byteman.filter.impl.*;
import net.miginfocom.swing.MigLayout;
import com.redhat.thermostat.byteman.data.DataRecord;
import com.redhat.thermostat.byteman.plot.Plotter;
import com.redhat.thermostat.byteman.ui.swing.UiSwingException;
import com.redhat.thermostat.byteman.ui.swing.settings.ChartSettings;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.redhat.thermostat.byteman.ui.swing.pages.FiltersetPage.ALL_RECORDS_LABEL;
import static com.redhat.thermostat.byteman.utils.IOUtils.closeQuietly;
import static com.redhat.thermostat.byteman.utils.string.StringUtils.UTF_8;
import static com.redhat.thermostat.byteman.utils.SwingUtils.boldify;
import static com.redhat.thermostat.byteman.utils.SwingUtils.createFormSectionBorder;

/**
 * User: alexkasko
 * Date: 6/10/15
 */

// subnodes for each applied filter set
class DatasetPage extends BasePage {
    public static final Gson GSON = new Gson();
    private static final Type CHART_RECORD_LIST_TYPE = new TypeToken<ArrayList<DataRecord>>(){}.getType();

    private final String datasetName;
    private final File file;
    private final Plotter plotter;

    private int recordsCount = 0;
    ContentPage filterpage = null;

    public DatasetPage(ChartsAppContext ctx, String name, File file, Plotter plotter) {
        super(ctx, name, name, "filesystem_folder_blue_16.png");
        this.datasetName = name;
        this.file = file;
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
        java.util.List<DataRecord> records = readData(file);
        recordsCount = records.size();
        String filtername = datasetName + "_" + ALL_RECORDS_LABEL;
        List<? extends ChartFilter> filters = createFilters(records);
        ChartSettings chartSettings = ctx.loadSettings().getCharts().get(plotter.getName());
        filterpage = new FiltersetPage(ctx, chartSettings.deepCopy(), filtername, ALL_RECORDS_LABEL,
                datasetName, plotter, records, filters, new AtomicInteger(0));
        parent.add(createDetailsPanel(), "growx");

        return parent;
    }

    @Override
    public void onInit() {
        ctx.getPageManager().addPageAsync(filterpage, datasetName);
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
        jp.add(new JLabel(file.getAbsolutePath()), "width 160lp::, span 2, wrap");
        // filesize
        jp.add(boldify(new JLabel("File Size (bytes):")), "width ::160lp");
        jp.add(new JLabel(Long.toString(file.length())), "width 160lp::, span 2, wrap");
        // recordsCount
        jp.add(boldify(new JLabel("Records Count:")), "width ::160lp");
        jp.add(new JLabel(Long.toString(recordsCount)), "width 160lp::, span 2, wrap");
        // chart type
        jp.add(boldify(new JLabel("Chart:")), "width ::160lp");
        jp.add(new JLabel(plotter.getName()), "width 160lp::, span 2, wrap");

        return jp;
    }

    private ArrayList<DataRecord> readData(File file) {
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

    private List<? extends ChartFilter> createFilters(List<DataRecord> records) {
        List<ChartFilter> res = new ArrayList<ChartFilter>();
        TimestampFromFilter tsFrom = new TimestampFromFilter("timestampFrom", new Date());
        res.add(tsFrom);
        TimestampToFilter tsTo = new TimestampToFilter("timestampTo", new Date());
        res.add(tsTo);
        res.add(new RegexFilter("marker"));
        res.add(new RegexFilter("agentId"));
        res.add(new RegexFilter("vmId"));
        Set<String> existing = new HashSet<String>();
        long timestampFrom = Long.MAX_VALUE;
        long timestampTo = 0;
        for (DataRecord re : records) {
            long ts = re.getTimestamp();
            if (ts < timestampFrom) timestampFrom = ts;
            if (ts > timestampTo) timestampTo = ts;
            // todo: default values tracking
            for (Map.Entry<String, Object> en : re.getData().entrySet()) {
                if (null != en.getValue() && !existing.contains(en.getKey())) {
                    existing.add(en.getKey());
                    if (en.getValue() instanceof Number) {
                        res.add(new IntGreaterFieldFilter(en.getKey()));
                        res.add(new IntLesserFieldFilter(en.getKey()));
                    } else {
                        res.add(new RegexFieldFilter(en.getKey()));
                    }
                }
            }
        }
        tsFrom.getEntry().setValue(new Date(timestampFrom));
        tsTo.getEntry().setValue(new Date(timestampTo));
        return res;
    }

}
