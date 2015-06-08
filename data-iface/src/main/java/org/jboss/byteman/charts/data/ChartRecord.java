/*
* JBoss, Home of Professional Open Source
* Copyright 2015 Red Hat and individual contributors
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
package org.jboss.byteman.charts.data;

import java.util.LinkedHashMap;

import static org.jboss.byteman.charts.utils.CollectionUtils.arrayToMap;
import static org.jboss.byteman.charts.utils.StringUtils.defaultString;

/**
 * Data structure for records collected in application and used to display the chart
 *
 * @author akashche
 * Date: 5/25/15
 */
public class ChartRecord {
    private long ts;
    private String vmId;
    private String agentId;
    private String marker;
    private LinkedHashMap<String, Object> data;

    /**
     * Private constructor for JSON tools
     */
    private ChartRecord() {
    }

    /**
     * Constructor
     *
     * @param ts timestamp
     * @param marker marker string
     * @param data data related to this record
     */
    public ChartRecord(long ts, String vmId, String agentId, String marker, LinkedHashMap<String, Object> data) {
        this.ts = ts;
        this.vmId = defaultString(vmId);
        this.agentId = defaultString(agentId);
        this.marker = defaultString(marker);
        this.data = data;
    }

    /**
     * Constructor with varargs support
     *
     * @param ts timestamp
     * @param marker marker string
     * @param dataFields data related to this record in the form of
     *                   "key", "value", "key", "value" syntax
     */
    public ChartRecord(long ts, String vmId, String agentId, String marker, Object... dataFields) {
        this.ts = ts;
        this.vmId = defaultString(vmId);
        this.agentId = defaultString(agentId);
        this.marker = defaultString(marker);
        this.data = arrayToMap(dataFields);
    }

    /**
     * Timestamp accessor
     *
     * @return ts field
     */
    public long getTs() {
        return ts;
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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ChartRecord");
        sb.append("{ts=").append(ts);
        sb.append(", vmId='").append(vmId).append('\'');
        sb.append(", agentId='").append(agentId).append('\'');
        sb.append(", marker='").append(marker).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
