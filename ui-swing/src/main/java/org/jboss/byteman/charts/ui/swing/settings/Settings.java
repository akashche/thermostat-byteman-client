package org.jboss.byteman.charts.ui.swing.settings;

import java.util.LinkedHashMap;

/**
 * User: alexkasko
 * Date: 10/20/15
 */
public class Settings {
    private SystemSettings system;
    private LinkedHashMap<String, ChartSettings> charts;

    Settings() {
    }

    public Settings(SystemSettings system, LinkedHashMap<String, ChartSettings> charts) {
        this.system = system;
        this.charts = charts;
    }

    public SystemSettings getSystem() {
        return system;
    }

    public LinkedHashMap<String, ChartSettings> getCharts() {
        return charts;
    }
}
