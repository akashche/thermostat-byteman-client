package org.jboss.byteman.charts.plot.plain;

import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.filter.ChartFilteredIterator;
import org.jboss.byteman.charts.plot.PlotConfig;
import org.jboss.byteman.charts.plot.PlotRecord;
import org.jboss.byteman.charts.plot.Plotter;

import java.util.*;

import static org.jboss.byteman.charts.utils.collection.SingleUseIterable.singleUseIterable;

/**
 * User: alexkasko
 * Date: 9/26/15
 */
public class AveragePlotter implements Plotter<PlotConfig> {
    @Override
    public ArrayList<PlotRecord> createPlot(PlotConfig config, Iterator<DataRecord> data, Collection<? extends ChartFilter> filters) {

        // todo: corner cases, proper rounding
        long period = config.getMaxTimestamp() - config.getMinTimestamp();
        long step = period/config.getMaxRecords();
        Bucket[] buckets = new Bucket[config.getMaxRecords()];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new Bucket(config.getMinTimestamp() + i*step, config.getMinTimestamp() + (i+1)*step);
        }

        Iterator<DataRecord> filtered = new ChartFilteredIterator(data, filters);
        for (DataRecord re : singleUseIterable(filtered)) {
            // period filter
            if (re.getTimestamp() < config.getMinTimestamp() || re.getTimestamp() >= config.getMaxTimestamp()) continue;
            // value filter
            Object valueObj = re.getData().get(config.getValueAttributeName());
            if (null == valueObj && config.isIgnoreAbsentValue()) continue;
            if (!(valueObj instanceof Number) && config.isIgnoreInvalidValue()) continue;
            // marker filter
            Object markerObj = re.getMarker();
            if (null == markerObj && config.isIgnoreAbsentMarker()) continue;
            // final marker and value
            double value = extractValue(valueObj);
            String marker = null != markerObj ? markerObj.toString() : "UNKNOWN";
            // obtain bucket
            long relTs = re.getTimestamp() - config.getMinTimestamp();
            int bucketInd = (int)(relTs/step);
            Bucket bucket = buckets[bucketInd];
            // add record
            bucket.append(marker, value);
        }

        ArrayList<PlotRecord> res = new ArrayList<PlotRecord>();
        for (Bucket bu : buckets) {
            for (Bar ba : bu.bars.values()) {
                res.add(ba);
            }
        }
        return res;
    }

    double extractValue(Object valueObj) {
        if (null == valueObj) return 0;
        if (!(valueObj instanceof Number)) return 1;
        Number valueNum = (Number) valueObj;
        return valueNum.doubleValue();
    }

    private static class Bar implements PlotRecord {
        final Bucket bucket;
        final String marker;
        double value;
        int count;

        private Bar(Bucket bucket, String marker, double value) {
            this.bucket = bucket;
            this.marker = marker;
            this.value = value;
            this.count = 1;
        }

        void append(double value) {
            this.value += value;
            this.count += 1;
        }

        @Override
        public double getValue() {
            // avg
            return value/count;
        }

        @Override
        public String getMarker() {
            return marker;
        }

        @Override
        public long getPeriodStart() {
            return bucket.periodStart;
        }

        @Override
        public long getPeriodEnd() {
            return bucket.periodEnd;
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

        void append(String marker, double value) {
            Bar existing = bars.get(marker);
            if (null != existing) {
                existing.append(value);
            } else {
                Bar bar = new Bar(this, marker, value);
                bars.put(marker, bar);
            }
        }
    }

}
