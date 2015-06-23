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

import org.jboss.byteman.charts.data.ChartRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.plot.aggregate.BucketedStackedCountPlotter;
import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.StringConfigEntry;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: alexkasko
 * Date: 6/23/15
 */
class FiltersetPage extends BasePage {

    private final Iterable<ChartRecord> records;
    private final Collection<? extends ChartFilter> filters;

    protected FiltersetPage(ChartsAppContext ctx, String name, String label, Iterable<ChartRecord> records,
            Collection<? extends ChartFilter> filters) {
        super(ctx, name, label, "mimetype_log_16.png");
        this.records = records;
        this.filters = filters;
    }

    @Override
    public Component createPane() {
        // todo: temporary harcoded
        Map<String, ChartConfigEntry<?>> conf = new HashMap<String, ChartConfigEntry<?>>();
        conf.put("categoryAttributeName", new StringConfigEntry("categoryAttributeName", "reportId"));
        conf.put("domainAxisLabel", new StringConfigEntry("domainAxisLabel", "[TODO] incomplete charts panel, should support filters"));
        JFreeChart chart = new BucketedStackedCountPlotter()
                .applyConfig(conf)
                .build(records.iterator(), filters);
        return new ChartPanel(chart);
    }
}
