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
package com.redhat.thermostat.byteman.ui.swing.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.redhat.thermostat.byteman.plot.Plotter;
import com.redhat.thermostat.byteman.chart.swing.SwingBarChart;
import com.redhat.thermostat.byteman.ui.swing.pages.ContentPagesRegister;

import java.io.*;
import java.util.LinkedHashMap;

import static com.redhat.thermostat.byteman.utils.IOUtils.closeQuietly;

/**
 * Implementation of the manager that manages
 * persistent settings instances
 *
 * @author akashche
 * Date: 10/20/15
 */
public class SettingsManager {
    private static final File SETTINGS_FILE = new File(System.getProperty("user.home") +
            File.separator + ".thermostat" + File.separator + "byteman" + File.separator + "settings.json");
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Loads settings from the persistent storage
     *
     * @return settings instance
     */
    public Settings loadSettings() {
        if (!SETTINGS_FILE.exists()) {
            return createAndPersistDefault();
        }
        InputStream is = null;
        try {
            is = new FileInputStream(SETTINGS_FILE);
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            return new Gson().fromJson(reader, Settings.class);
        } catch (IOException e) {
            throw new SettingsException("Settings load error", e);
        } finally {
            closeQuietly(is);
        }
    }

    /**
     * Persists settings
     *
     * @param settings settings instance
     */
    public void saveSettings(Settings settings) {
        Writer writer = null;
        try {
            OutputStream os = new FileOutputStream(SETTINGS_FILE);
            writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            GSON.toJson(settings, writer);
            writer.close();
        } catch (Exception e) {
            closeQuietly(writer);
            throw new SettingsException("Settings save error", e);
        }
    }

    private Settings createAndPersistDefault() {
        boolean res = SETTINGS_FILE.getParentFile().mkdirs();
//        if (!res) throw new SettingsException("Error creating dirs for file: [" + SETTINGS_FILE.getAbsolutePath() + "]");
        LinkedHashMap<String, ChartSettings> charts = new LinkedHashMap<String, ChartSettings>();
        for (Plotter pl : ContentPagesRegister.PLOTS) {
            charts.put(pl.getName(), new ChartSettings(SwingBarChart.defaultConfig()));
        }
        Settings settings = new Settings(new SystemSettings(), charts);
        saveSettings(settings);
        return settings;
    }
}
