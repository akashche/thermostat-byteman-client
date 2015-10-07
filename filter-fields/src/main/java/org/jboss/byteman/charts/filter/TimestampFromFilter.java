package org.jboss.byteman.charts.filter;

import org.jboss.byteman.charts.data.DataRecord;

import java.util.Date;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
public class TimestampFromFilter extends TimestampFilter {
    public TimestampFromFilter(String label, Date defaultValue) {
        super(label, defaultValue);
    }

    @Override
    public boolean apply(DataRecord record) {
        return record.getTimestamp() >= en.getValue().getTime();
    }
}
