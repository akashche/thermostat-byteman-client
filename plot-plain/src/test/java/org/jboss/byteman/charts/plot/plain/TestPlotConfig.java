package org.jboss.byteman.charts.plot.plain;

import org.jboss.byteman.charts.plot.PlotConfig;

/**
 * User: alexkasko
 * Date: 9/27/15
 */
class TestPlotConfig implements PlotConfig {
    private final String valueAttributeName;
    private final int maxRecords;
    private final long minTimestamp;
    private final long maxTimestamp;
    private final boolean ignoreAbsentMarker;
    private final boolean ignoreAbsentValue;
    private final boolean ignoreInvalidValue;

    TestPlotConfig(String valueAttributeName, int maxRecords, long minTimestamp, long maxTimestamp,
                   boolean ignoreAbsentMarker, boolean ignoreAbsentValue, boolean ignoreInvalidValue) {
        this.valueAttributeName = valueAttributeName;
        this.maxRecords = maxRecords;
        this.minTimestamp = minTimestamp;
        this.maxTimestamp = maxTimestamp;
        this.ignoreAbsentMarker = ignoreAbsentMarker;
        this.ignoreAbsentValue = ignoreAbsentValue;
        this.ignoreInvalidValue = ignoreInvalidValue;
    }

    @Override
    public String getValueAttributeName() {
        return valueAttributeName;
    }

    @Override
    public int getMaxRecords() {
        return maxRecords;
    }

    @Override
    public long getMinTimestamp() {
        return minTimestamp;
    }

    @Override
    public long getMaxTimestamp() {
        return maxTimestamp;
    }

    @Override
    public boolean isIgnoreAbsentMarker() {
        return ignoreAbsentMarker;
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
        private int maxRecords = 24;
        private long minTimestamp = 0;
        private long maxTimestamp = Long.MAX_VALUE;
        private boolean ignoreAbsentMarker = false;
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

        public Builder withMinTimestamp(long minTimestamp) {
            this.minTimestamp = minTimestamp;
            return this;
        }

        public Builder withMaxTimestamp(long maxTimestamp) {
            this.maxTimestamp = maxTimestamp;
            return this;
        }

        public Builder withIgnoreAbsentMarker(boolean ignoreAbsentMarker) {
            this.ignoreAbsentMarker = ignoreAbsentMarker;
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
            return new TestPlotConfig(valueAttributeName, maxRecords, minTimestamp,
                    maxTimestamp, ignoreAbsentMarker, ignoreAbsentValue, ignoreInvalidValue);
        }
    }
}
