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
package com.redhat.thermostat.byteman.ui.swing.config;

import javax.swing.*;

import static com.redhat.thermostat.byteman.utils.string.StringUtils.defaultString;
import static com.redhat.thermostat.byteman.utils.SwingUtils.boldify;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
class DefaultLabelBuilder implements ChartConfigLabelBuilder {

//    this allows auto text wrapping but brings unneeded vertical gaps, css doesn't help
//    protected String textPrefix = "<html>";
    protected String textPrefix = "";
    protected String textPostfix = ":";
    protected boolean bold = true;
    protected String layoutOptions = "width ::180lp";

    public DefaultLabelBuilder() {
    }

    @Override
    public JLabel build(String text) {
        JLabel jl = new JLabel(textPrefix + defaultString(text) + textPostfix);
        if (bold) {
            boldify(jl);
        }
        return jl;
    }

    @Override
    public String getLayoutOptions() {
        return layoutOptions;
    }

}