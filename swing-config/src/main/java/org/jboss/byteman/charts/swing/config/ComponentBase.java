/*
* JBoss, Home of Professional Open Source
* Copyright 2010 Red Hat and individual contributors
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
package org.jboss.byteman.charts.swing.config;

import static org.jboss.byteman.charts.utils.StringUtils.defaultString;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public abstract class ComponentBase<T> implements ChartConfigComponent<T> {

    protected String name = "";
    protected String label = "";
    protected String layoutOptions = DEFAULT_CONTROL_LAYOUT_OPTIONS;

    @Override
    public String getName() {
        return defaultString(name);
    }

    @Override
    public String getLabel() {
        return defaultString(label);
    }

    @Override
    public String getLayoutOptions() {
        return defaultString(layoutOptions);
    }
}
