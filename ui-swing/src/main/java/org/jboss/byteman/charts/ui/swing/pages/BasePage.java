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
package org.jboss.byteman.charts.ui.swing.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: alexkasko
 * Date: 6/16/15
 */
abstract class BasePage implements ContentPage {

    protected final ChartsAppContext ctx;
    protected final String name;
    protected final String label;
    protected final String icon;
    protected final List<String> children;

    protected BasePage(ChartsAppContext ctx, String name, String label, String icon, String... children) {
        this.ctx = ctx;
        this.name = name;
        this.label = label;
        this.icon = icon;
        List<String> chList = new ArrayList<String>();
        Collections.addAll(chList, children);
        this.children = chList;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public List<String> getChildren() {
        return children;
    }

    @Override
    public boolean isUserPage() {
        return false;
    }

    @Override
    public ChartsAppContext getAppContext() {
        return ctx;
    }
}
