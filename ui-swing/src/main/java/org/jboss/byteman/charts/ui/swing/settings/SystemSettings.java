package org.jboss.byteman.charts.ui.swing.settings;

import org.jboss.byteman.charts.ui.ChartConfigEntry;
import org.jboss.byteman.charts.ui.ConfigurableChart;
import org.jboss.byteman.charts.ui.StringConfigEntry;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * User: alexkasko
 * Date: 10/20/15
 */
public class SystemSettings implements ConfigurableChart {

    private String lastChosenDataFile = "";
    private String datasetNameFormat = "${filename}_${date}";
    private String datasetNameDateFormat = "yyyyMMdd_HHmmss";


    @Override
    public Collection<? extends ChartConfigEntry<?>> availableConfig() {
        return Arrays.asList(
                new StringConfigEntry("lastChosenDataFile", lastChosenDataFile),
                new StringConfigEntry("datasetNameFormat", datasetNameFormat),
                new StringConfigEntry("datasetNameDateFormat", datasetNameDateFormat)
        );
    }

    @Override
    public ConfigurableChart applyConfig(Map<String, ChartConfigEntry<?>> entries) {
        this.lastChosenDataFile = entries.get("lastChosenDataFile").getValue().toString();
        this.datasetNameFormat = entries.get("datasetNameFormat").getValue().toString();
        this.datasetNameDateFormat = entries.get("datasetNameDateFormat").getValue().toString();
        return this;
    }
}
