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
package org.jboss.byteman.charts.plot;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 * User: alexkasko
 * Date: 6/8/15
 */
public class BoundedCategoryDataset {
    private final DefaultCategoryDataset dataset;
    private final double min;
    private final double max;

    public BoundedCategoryDataset(DefaultCategoryDataset dataset, double min, double max) {
        this.dataset = dataset;
        this.min = min;
        this.max = max;
    }

    public DefaultCategoryDataset getDataset() {
        return dataset;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BoundedCategoryDataset");
        sb.append("{dataset=").append(dataset);
        sb.append(", min=").append(min);
        sb.append(", max=").append(max);
        sb.append('}');
        return sb.toString();
    }
}
