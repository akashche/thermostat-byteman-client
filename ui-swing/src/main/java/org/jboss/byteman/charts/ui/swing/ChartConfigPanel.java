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
package org.jboss.byteman.charts.ui.swing;

import net.miginfocom.swing.MigLayout;
import org.jboss.byteman.charts.ui.ChartConfigEntry;

import javax.swing.*;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public class ChartConfigPanel {

    private final JPanel panel;
    private final Map<String, ChartConfigEntry<?>> components;

    public ChartConfigPanel(JPanel panel, Map<String, ChartConfigEntry<?>> components) {
        this.panel = panel;
        this.components = components;
    }

    public static Builder builder() {
        return new Builder();
    }

    public JPanel getPanel() {
        return panel;
    }

    public Map<String, ChartConfigEntry<?>> getComponents() {
        return components;
    }

    public static class Builder {

        protected String layoutGeneralOptions = "fillx, debug";
        protected String layoutColumnsOptions = "[right][left]";
        protected String layoutRowsOptions = "";
        protected ChartConfigLabelBuilder labelBuilder = new DefaultLabelBuilder();

        public ChartConfigPanel build(Collection<? extends ChartConfigEntry<?>> entries) {
            JPanel jp = new JPanel(new MigLayout(layoutGeneralOptions, layoutColumnsOptions, layoutRowsOptions));
            Map<String, ChartConfigEntry<?>> components = new LinkedHashMap<String, ChartConfigEntry<?>>();
            for(ChartConfigEntry en : entries) {
                jp.add(labelBuilder.build(en.getLabel()), labelBuilder.layoutOptions());
                jp.add(createComponent(en), en.getLayoutOptions());
                components.put(en.getName(), en);
            }
            return new ChartConfigPanel(jp, components);
        }

        @SuppressWarnings("unchecked") // type extends CCSC
        private JComponent createComponent(ChartConfigEntry<?> en) {
            try {
                Class<ChartConfigSwingControl> compClass = (Class) Class.forName(en.getType());
                Constructor<ChartConfigSwingControl> ctor = compClass.getConstructor(en.getClass());
                ChartConfigSwingControl ccsc = ctor.newInstance(en);
                return ccsc.createComponent();
            } catch (Exception e) {
                throw new UiSwingException("Cannot create component for entry: [" + en + "]", e);
            }
        }
    }

}
