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

import com.redhat.thermostat.byteman.ui.swing.settings.Settings;

/**
 * User: alexkasko
 * Date: 6/16/15
 */
public interface ChartsAppContext {

    String getProp(String propName);

    void setProp(String propName, String propValue);

    void init(PageManager pm);

    PageManager getPageManager();

    void setStatus(String text);

    public Settings loadSettings();

    public void saveSettings(Settings settings);
}