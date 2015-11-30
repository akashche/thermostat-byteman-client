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
package com.redhat.thermostat.byteman.ui.swing.panels;

import com.redhat.thermostat.byteman.ui.swing.pages.ContentPagesRegister;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static com.redhat.thermostat.byteman.ui.swing.TestSwingUtils.showAndWait;
import static com.redhat.thermostat.byteman.utils.SwingUtils.enableLafIfAvailable;

/**
 * User: alexkasko
 * Date: 6/11/15
 */
public class PanelsTest {

    @Before
    public void setup() {
        enableLafIfAvailable("Nimbus");
    }

    @Test
    public void test() {
        JPanel panel = new MainPaneBuilder().build(ContentPagesRegister.PAGES);

        // note: uncomment me for manual testing
        showAndWait(panel);
    }
}
