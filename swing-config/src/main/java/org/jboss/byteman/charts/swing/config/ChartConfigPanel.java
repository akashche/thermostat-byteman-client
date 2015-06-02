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

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public class ChartConfigPanel {

    private final JPanel panel;
    private final Map<String, ChartConfigComponent> components;

    public ChartConfigPanel(JPanel panel, Map<String, ChartConfigComponent> components) {
        this.panel = panel;
        this.components = components;
    }

    public static Builder builder() {
        return new Builder();
    }

    public JPanel getPanel() {
        return panel;
    }

    public Map<String, ChartConfigComponent> getComponents() {
        return components;
    }

    public static class Builder {

        protected String layoutGeneralOptions = "fillx";
        protected String layoutColumnsOptions = "[right][left]";
        protected String layoutRowsOptions = "[]";
        protected ChartConfigLabelBuilder labelBuilder = new DefaultLabelBuilder();

        public ChartConfigPanel build(Collection<? extends ChartConfigComponent<?>> controls) {
            JPanel jp = new JPanel(new MigLayout(layoutGeneralOptions, layoutColumnsOptions, layoutRowsOptions));
            Map<String, ChartConfigComponent> components = new LinkedHashMap<String, ChartConfigComponent>();
            for(ChartConfigComponent co : controls) {
                jp.add(labelBuilder.build(co.getLabel()), labelBuilder.layoutOptions());
                jp.add(co.createComponent(), co.getLayoutOptions());
                components.put(co.getName(), co);
            }
            return new ChartConfigPanel(jp, components);
        }
    }

}
