package org.jboss.byteman.charts.ui.swing.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jboss.byteman.charts.plot.Plotter;
import org.jboss.byteman.charts.plot.swing.JFreeChartBuilder;
import org.jboss.byteman.charts.ui.swing.pages.ContentPagesRegister;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;

import static org.jboss.byteman.charts.utils.IOUtils.closeQuietly;

/**
 * User: alexkasko
 * Date: 10/20/15
 */
public class SettingsManager {
    private static final File SETTINGS_FILE = new File(System.getProperty("user.home") +
            File.separator + ".thermostat" + File.separator + "byteman" + File.separator + "settings.json");
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

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
            charts.put(pl.getName(), new ChartSettings(JFreeChartBuilder.defaultConfig()));
        }
        Settings settings = new Settings(new SystemSettings(), charts);
        saveSettings(settings);
        return settings;
    }
}
