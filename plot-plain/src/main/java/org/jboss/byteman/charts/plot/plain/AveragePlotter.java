package org.jboss.byteman.charts.plot.plain;

import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.filter.ChartFilteredIterator;
import org.jboss.byteman.charts.plot.PlotConfig;
import org.jboss.byteman.charts.plot.PlotException;
import org.jboss.byteman.charts.plot.PlotRecord;
import org.jboss.byteman.charts.plot.Plotter;

import java.util.*;

import static java.util.Collections.emptyList;
import static org.jboss.byteman.charts.utils.collection.SingleUseIterable.singleUseIterable;

/**
 * User: alexkasko
 * Date: 9/26/15
 */
public class AveragePlotter implements Plotter {
    @Override
    public String getName() {
        return "Average chart";
    }

    @Override
    public String getType() {
        return "PLAIN";
    }

    @Override
    public String getDescription() {
        return "Shows average values";
    }

    @Override
    public Collection<PlotRecord> createPlot(PlotConfig config, long minTimestamp, long maxTimestamp, 
            Iterator<DataRecord> data, Collection<? extends ChartFilter> filters) {
        if (null == config) throw new PlotException("Invalid null config specified");
        if (null == data) throw new PlotException("Invalid null data specified");
        if (null == filters) throw new PlotException("Invalid null filters specified");
        if (maxTimestamp <= minTimestamp) return emptyList();

        long period = maxTimestamp - minTimestamp;
        int maxRecords = Math.min((int) period, config.getMaxRecords());
        long step = period/maxRecords;
        Bucket[] buckets = new Bucket[maxRecords];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new Bucket(minTimestamp + i*step, minTimestamp + (i+1)*step);
        }

        Iterator<DataRecord> filtered = new ChartFilteredIterator(data, filters);
        for (DataRecord re : singleUseIterable(filtered)) {
            // period filter
            if (re.getTimestamp() < minTimestamp || re.getTimestamp() >= maxTimestamp) continue;
            // value filter
            Object valueObj = re.getData().get(config.getValueAttributeName());
            if (null == valueObj && config.isIgnoreAbsentValue()) continue;
            if (!(valueObj instanceof Number) && config.isIgnoreInvalidValue()) continue;
            if (null == re.getData().get(config.getCategoryAttributeName()) && config.isIgnoreAbsentCategory()) continue;
            // obtain bucket
            long relTs = re.getTimestamp() - minTimestamp;
            int bucketInd = (int)(relTs/step);
            Bucket bucket = buckets[bucketInd];
            // add record
            bucket.append(config, re);
        }

        ArrayList<PlotRecord> res = new ArrayList<PlotRecord>();
        for (Bucket bu : buckets) {
            // think: maybe move this into swing chart impl
            res.add(new FakePlotRecord(bu));
            for (Bar ba : bu.bars.values()) {
                res.add(ba);
            }
        }
        return res;
    }

    // todo: inheritance
    static double extractValue(PlotConfig config, DataRecord rec) {
        Object valueObj = rec.getData().get(config.getValueAttributeName());
        if (null == valueObj) return 0;
        if (!(valueObj instanceof Number)) return 1;
        Number valueNum = (Number) valueObj;
        return valueNum.doubleValue();
    }

    static String extractCategory(PlotConfig config, DataRecord rec) {
        Object catObj = rec.getData().get(config.getCategoryAttributeName());
        if (null == catObj) return "UNKNOWN";
        return catObj.toString();
    }

    private static class FakePlotRecord implements PlotRecord {
        final Bucket bucket;

        private FakePlotRecord(Bucket bucket) {
            this.bucket = bucket;
        }

        @Override
        public double getValue() {
            return 0;
        }

        @Override
        public String getCategory() {
            return "";
        }

        @Override
        public long getPeriodStart() {
            return bucket.periodStart;
        }

        @Override
        public long getPeriodEnd() {
            return bucket.periodEnd;
        }

        @Override
        public List<DataRecord> getDataRecords() {
            return emptyList();
        }
    }

    private static class Bar implements PlotRecord {
        final Bucket bucket;
        final String category;
        double value;
        List<DataRecord> dataRecords = new ArrayList<DataRecord>();

        private Bar(PlotConfig config, Bucket bucket, String category, DataRecord record) {
            this.bucket = bucket;
            this.category = category;
            this.value = extractValue(config, record);
            dataRecords.add(record);
        }

        void append(PlotConfig config, DataRecord record) {
            this.value += extractValue(config, record);
            dataRecords.add(record);
        }

        @Override
        public double getValue() {
            // avg
            return value/dataRecords.size();
        }

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public long getPeriodStart() {
            return bucket.periodStart;
        }

        @Override
        public long getPeriodEnd() {
            return bucket.periodEnd;
        }

        @Override
        public List<DataRecord> getDataRecords() {
            return dataRecords;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bar");
            sb.append("{periodStart=").append(bucket.periodStart);
            sb.append(", periodEnd='").append(bucket.periodEnd).append('\'');
            sb.append(", category='").append(category).append('\'');
            sb.append(", value=").append(value);
            sb.append(", count=").append(dataRecords.size());
            sb.append("}\n");
            return sb.toString();
        }
    }

    private static class Bucket {
        final long periodStart;
        final long periodEnd;
        final Map<String, Bar> bars = new LinkedHashMap<String, Bar>();

        Bucket(long periodStart, long periodEnd) {
            this.periodStart = periodStart;
            this.periodEnd = periodEnd;
        }

        void append(PlotConfig config, DataRecord record) {
            String category = extractCategory(config, record);
            Bar existing = bars.get(category);
            if (null != existing) {
                existing.append(config, record);
            } else {
                Bar bar = new Bar(config, this, category, record);
                bars.put(category, bar);
            }
        }
    }

}
