package org.jboss.byteman.charts.plot.plain;

import org.jboss.byteman.charts.plot.PlotConfig;

/**
 * User: alexkasko
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
