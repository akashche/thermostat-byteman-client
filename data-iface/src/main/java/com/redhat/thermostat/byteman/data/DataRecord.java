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
package com.redhat.thermostat.byteman.data;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.redhat.thermostat.byteman.utils.collection.CollectionUtils.arrayToMap;
import static com.redhat.thermostat.byteman.utils.string.StringUtils.EMPTY_STRING;
import static com.redhat.thermostat.byteman.utils.string.StringUtils.defaultString;

/**
 * Data structure for records collected in application and used to display the chart
 *
 * @author akashche
 * Date: 5/25/15
 */
public class DataRecord {
    public static final int STATIC_FIELDS_COUNT = 4;

    private long timestamp;
    private String vmId;
    private String agentId;
    private String marker;
    private LinkedHashMap<String, Object> data;

    /**
     * Private constructor for JSON tools
     */
    private DataRecord() {
    }

    /**
     * Constructor
     *
     * @param timestamp timestamp
     * @param marker marker string
     * @param data data related to this record
     */
    public DataRecord(long timestamp, String vmId, String agentId, String marker, LinkedHashMap<String, Object> data) {
        this.timestamp = timestamp;
        this.vmId = defaultString(vmId);
        this.agentId = defaultString(agentId);
        this.marker = defaultString(marker);
        this.data = data;
    }

    /**
     * Constructor with varargs support
     *
     * @param timestamp timestamp
     * @param marker marker string
     * @param dataFields data related to this record in the form of
     *                   "key", "value", "key", "value" syntax
     */
    public DataRecord(long timestamp, String vmId, String agentId, String marker, Object... dataFields) {
        this.timestamp = timestamp;
        this.vmId = defaultString(vmId);
        this.agentId = defaultString(agentId);
        this.marker = defaultString(marker);
        this.data = arrayToMap(dataFields);
    }

    /**
     * Timestamp accessor
     *
     * @return timestamp field
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * VM ID accessor
     *
     * @return VM ID
     */
    public String getVmId() {
        return vmId;
    }

    /**
     * Agent ID accessor
     *
     * @return Agent ID
     */
    public String getAgentId() {
        return agentId;
    }

    /**
     * Marker accessor
     *
     * @return  market field
     */
    public String getMarker() {
        return marker;
    }

    /**
     * Data accessor
     *
     * @return data field
     */
    public LinkedHashMap<String, Object> getData() {
        return data;
    }

    /**
     * Indexed accessor for record fields
     *
     * @param columnIndex column index
     * @return value at specified index or empty string for no value at that index
     */
    public Object getValueAt(int columnIndex) {
        if (columnIndex < 0) throw new IllegalArgumentException("Invalid columnIndex: [" + columnIndex + "]");
        switch (columnIndex) {
            case 0: return timestamp;
            case 1: return vmId;
            case 2: return agentId;
            case 3: return marker;
            default:
                Iterator<Map.Entry<String, Object>> it = data.entrySet().iterator();
                for (int i = 4; i < columnIndex; i++) {
                    if(it.hasNext()) {
                        it.next();
                    } else return EMPTY_STRING;
                }
                return it.hasNext() ?  it.next() : EMPTY_STRING;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ChartRecord");
        sb.append("{timestamp=").append(timestamp);
        sb.append(", vmId='").append(vmId).append('\'');
        sb.append(", agentId='").append(agentId).append('\'');
        sb.append(", marker='").append(marker).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
