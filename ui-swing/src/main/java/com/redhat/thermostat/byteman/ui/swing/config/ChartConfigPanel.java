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
package com.redhat.thermostat.byteman.ui.swing.config;

import net.miginfocom.swing.MigLayout;
import com.redhat.thermostat.byteman.chart.ChartConfigEntry;
import com.redhat.thermostat.byteman.ui.swing.UiSwingException;
import com.redhat.thermostat.byteman.ui.swing.controls.ChartConfigSwingControl;

import javax.swing.*;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * UI panel, contains a list of "label"->"input field" widgets
 * for every specified configuration entry
 *
 * @author akashche
 * Date: 6/2/15
 */
public class ChartConfigPanel {

    private final JPanel panel;
    private final Map<String, ChartConfigEntry<?>> components;

    /**
     * Constructor
     *
     * @param panel UI panel that will contain widgets list
     * @param components "name"->"config entry" mapping
     */
    public ChartConfigPanel(JPanel panel, Map<String, ChartConfigEntry<?>> components) {
        this.panel = panel;
        this.components = components;
    }

    /**
     * Factory function for the builder class
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * UI panel accessor
     *
     * @return UI panel that contains widgets list
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Components accessor, will return actual UI components with
     * possibly updates state
     *
     * @return components
     */
    public Map<String, ChartConfigEntry<?>> getComponents() {
        return components;
    }

    /**
     * Builder class for the panel instance
     */
    public static class Builder {

        protected String layoutGeneralOptions = "fillx";
        protected String layoutColumnsOptions = "[right][left]";
        protected String layoutRowsOptions = "[]";
        protected ChartConfigLabelBuilder labelBuilder = new DefaultLabelBuilder();

        /**
         * Builds an instance of the widgets list panel
         *
         * @param entries Set of the configuration entries to create a panel with widgets for
         * @return widgets list panel
         */
        public ChartConfigPanel build(Collection<? extends ChartConfigEntry<?>> entries) {
            JPanel jp = new JPanel(new MigLayout(layoutGeneralOptions, layoutColumnsOptions, layoutRowsOptions));
            Map<String, ChartConfigEntry<?>> components = new LinkedHashMap<String, ChartConfigEntry<?>>();
            for(ChartConfigEntry en : entries) {
                jp.add(labelBuilder.build(en.getLabel()), labelBuilder.getLayoutOptions());
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
