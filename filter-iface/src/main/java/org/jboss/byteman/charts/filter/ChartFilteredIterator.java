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
import org.jboss.byteman.charts.utils.collection.SingleMethodIterator;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: alexkasko
 * Date: 6/8/15
 */
public class ChartFilteredIterator extends SingleMethodIterator<DataRecord> {

    private final Iterator<DataRecord> source;
    private final Collection<? extends ChartFilter> filters;

    public ChartFilteredIterator(Iterator<DataRecord> source, Collection<? extends ChartFilter> filters) {
        this.source = source;
        this.filters = filters;
    }

    @Override
    protected DataRecord computeNext() {
        while (source.hasNext()) {
            DataRecord cr = source.next();
            boolean passed = true;
            for (ChartFilter fi : filters) {
                passed = fi.apply(cr);
                if (!passed) break;
            }
            if (passed) return cr;
        }
        return endOfData();
    }
}
