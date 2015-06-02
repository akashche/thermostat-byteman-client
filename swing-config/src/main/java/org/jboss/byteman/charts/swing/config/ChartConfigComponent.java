/*
* JBoss, Home of Professional Open Source
* Copyright 2010 Red Hat and individual contributors
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
package org.jboss.byteman.charts.swing.config;

import javax.swing.*;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public interface ChartConfigComponent<T> {

    // todo: deacouple cleanly from swing
    //    "text field"
    //    "int field"
    //    "float field"
    //    "combo box"
    //    "slider"
    //    "date picker"


    public static final String DEFAULT_CONTROL_LAYOUT_OPTIONS = "pushx, growx, width :200lp:, wrap";

    JComponent createComponent();

    String getName();

    String getLabel();

    String getLayoutOptions();

    T getValue();

    void setValue(T value);
}