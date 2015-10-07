package org.jboss.byteman.charts.filter;

import org.jboss.byteman.charts.data.DataRecord;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
abstract class FieldFilter implements ChartFilter {
    protected final String fieldName;

    protected FieldFilter(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean apply(DataRecord record) {
        Object field = record.getData().get(fieldName);
        return applyInternal(field);
    }

    protected abstract boolean applyInternal(Object field);
}
