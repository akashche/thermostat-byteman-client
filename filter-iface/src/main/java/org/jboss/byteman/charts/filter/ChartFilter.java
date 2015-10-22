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
package org.jboss.byteman.charts.filter;

import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.ui.ChartConfigEntry;

/**
 * Interface for filtering predicate for ChartRecords
 *
 * @author akashche
 * Date: 5/25/15
 */
public interface ChartFilter {

    /**
     * Checks whether specified record passes the filter
     *
     * @param record input record
     * @return true if filter passed, false otherwise
     */
    boolean apply(DataRecord record);

    /**
     * Returns config entry object used by UI to display this filter
     *
     * @return config entry object
     */
    ChartConfigEntry<?> configEntry();

    <T extends ChartFilter> T copy();
}
