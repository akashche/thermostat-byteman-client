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
package org.jboss.byteman.charts.plot.aggregate;

import com.google.gson.reflect.TypeToken;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jboss.byteman.charts.data.ChartRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.DateTimeConfigEntry;
import org.jboss.byteman.charts.ui.StringConfigEntry;
import org.jfree.chart.JFreeChart;
import org.junit.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;

import static java.util.Arrays.asList;
import static org.jboss.byteman.charts.plot.aggregate.BucketedStackedCountPlotter.GSON;
import static org.jboss.byteman.charts.utils.IOUtils.closeQuietly;
import static org.jboss.byteman.charts.utils.StringUtils.UTF_8;

/**
 * User: alexkasko
 * Date: 6/8/15
 */
public class BucketedStackedCountPlotterTest {

    private static final Type CHART_RECORD_LIST_TYPE = new TypeToken<ArrayList<ChartRecord>>(){}.getType();

    @Test
    public void dummy() {
//        I am dummy
//        List<ChartRecord> recs = readData();
//        List<ChartRecord> filtered = new ArrayList<ChartRecord>();
//        for (ChartRecord cr : recs) {
//            if ("reportRenderTime".equals(cr.getMarker())) {
//                filtered.add(cr);
//            }
//        }
//        writeData(filtered);
    }

//    @Test
    public void test() throws Exception {
        List<ChartRecord> records = readData();
        Map<String, ChartConfigEntry<?>> conf = new HashMap<String, ChartConfigEntry<?>>();
        conf.put("categoryAttributeName", new StringConfigEntry("categoryAttributeName", "reportId"));
        JFreeChart chart = new BucketedStackedCountPlotter()
                .applyConfig(conf)
                .build(records.iterator(), asList(RenderTimeFilter.INSTANCE));
        chartToSvg(chart, 1024, 480);
    }

    private static void chartToSvg(JFreeChart chart, int width, int height) throws Exception {
        Writer writer = null;
        try {
            DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
            Document document = domImpl.createDocument(null, "svg", null);
            SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
            svgGenerator.setSVGCanvasSize(new Dimension(width, height));
            chart.draw(svgGenerator, new Rectangle(width, height));
            OutputStream os = new FileOutputStream("BucketedStackedCountPlotterTest.svg");
            writer = new BufferedWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
            svgGenerator.stream(writer);
        } finally {
            closeQuietly(writer);
        }
    }

    private static ArrayList<ChartRecord> readData() {
        InputStream is = null;
        try {
            is = BucketedStackedCountPlotterTest.class.getResourceAsStream("reports_data.json");
            Reader reader = new InputStreamReader(is, UTF_8);
            return GSON.fromJson(reader, CHART_RECORD_LIST_TYPE);
        } finally {
            closeQuietly(is);
        }
    }

    private static void writeData(List<ChartRecord> records) {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream("data.json"), UTF_8);
            GSON.toJson(records, writer);
            writer.close();
        } catch (Exception e) {
            closeQuietly(writer);
        }
    }

    private enum RenderTimeFilter implements ChartFilter {
        INSTANCE;

        @Override
        public boolean apply(ChartRecord record) {
            return "reportRenderTime".equals(record.getMarker());
        }

        @Override
        public ChartConfigEntry configEntry() {
            return new DateTimeConfigEntry();
        }
    }

}
