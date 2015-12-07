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
package com.redhat.thermostat.byteman.ui.swing.pages;

import com.redhat.thermostat.byteman.ui.swing.settings.Settings;

/**
 * Interface for the application context object that should
 * provide a set of functions that are used by the UI pages
 *
 * @author akashche
 * Date: 6/16/15
 */
public interface ChartsAppContext {

    /**
     * Configuration properties accessor
     *
     * @param propName property name
     * @return property value
     */
    String getProp(String propName);

    /**
     * Setter for configuration properties
     *
     * @param propName property name
     * @param propValue property value
     */
    void setProp(String propName, String propValue);

    /**
     * Startup method that triggers initialization
     * of this context
     *
     * @param pm page manager instance
     */
    void init(PageManager pm);

    /**
     * Page manager accessor
     *
     * @return page manager
     */
    PageManager getPageManager();

    /**
     * Sets the status text for the UI status field
     *
     * @param text status text
     */
    void setStatus(String text);

    /**
     * Loads configuration settings
     *
     * @return configuration settings
     */
    public Settings loadSettings();

    /**
     * Saves configuration settings
     *
     * @param settings configuration settings
     */
    public void saveSettings(Settings settings);
}
