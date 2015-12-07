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
package com.redhat.thermostat.byteman.plot.impl;

import com.redhat.thermostat.byteman.plot.PlotConfig;

/**
 * @author akashche
 * Date: 9/27/15
 */
class TestPlotConfig implements PlotConfig {
    private final String valueAttributeName;
    private final String categoryAttributeName;
    private final int maxRecords;
    private final boolean ignoreAbsentCategory;
    private final boolean ignoreAbsentValue;
    private final boolean ignoreInvalidValue;

    TestPlotConfig(String valueAttributeName, String categoryAttributeName, int maxRecords,
                   boolean ignoreAbsentCategory, boolean ignoreAbsentValue, boolean ignoreInvalidValue) {
        this.valueAttributeName = valueAttributeName;
        this.categoryAttributeName = categoryAttributeName;
        this.maxRecords = maxRecords;
        this.ignoreAbsentCategory = ignoreAbsentCategory;
        this.ignoreAbsentValue = ignoreAbsentValue;
        this.ignoreInvalidValue = ignoreInvalidValue;
    }

    @Override
    public String getValueAttributeName() {
        return valueAttributeName;
    }

    @Override
    public String getCategoryAttributeName() {
        return categoryAttributeName;
    }

    @Override
    public int getMaxRecords() {
        return maxRecords;
    }

    @Override
    public boolean isIgnoreAbsentCategory() {
        return ignoreAbsentCategory;
    }

    @Override
    public boolean isIgnoreAbsentValue() {
        return ignoreAbsentValue;
    }

    @Override
    public boolean isIgnoreInvalidValue() {
        return ignoreInvalidValue;
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private String valueAttributeName = "value";
        private String categoryAttributeName = "category";
        private int maxRecords = 24;
        private boolean ignoreAbsentCategory = false;
        private boolean ignoreAbsentValue = false;
        private boolean ignoreInvalidValue = false;

        public Builder withValueAttributeName(String valueAttributeName) {
            this.valueAttributeName = valueAttributeName;
            return this;
        }

        public Builder withMaxRecords(int maxRecords) {
            this.maxRecords = maxRecords;
            return this;
        }

        public Builder withIgnoreAbsentCategory(boolean ignoreAbsentCategory) {
            this.ignoreAbsentCategory = ignoreAbsentCategory;
            return this;
        }

        public Builder withIgnoreAbsentValue(boolean ignoreAbsentValue) {
            this.ignoreAbsentValue = ignoreAbsentValue;
            return this;
        }

        public Builder withIgnoreInvalidValue(boolean ignoreInvalidValue) {
            this.ignoreInvalidValue = ignoreInvalidValue;
            return this;
        }

        TestPlotConfig build() {
            return new TestPlotConfig(valueAttributeName, categoryAttributeName, maxRecords,
                    ignoreAbsentCategory, ignoreAbsentValue, ignoreInvalidValue);
        }
    }
}
