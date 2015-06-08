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
package org.jboss.byteman.charts.ui.swing;

import org.jboss.byteman.charts.ui.*;
import org.junit.Test;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;

import static org.jboss.byteman.charts.utils.SwingUtils.createCloseListener;
import static org.jboss.byteman.charts.utils.SwingUtils.enableLafIfAvailable;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public class UiTest {

    @Test
    public void dummy() {
//        I am dummy
    }

//    @Test
    public void test() throws InvocationTargetException, InterruptedException, UnsupportedLookAndFeelException {
        enableLafIfAvailable("Nimbus");
        final Thread[] edtThreadHolder = new Thread[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                MainFrame mf = new MainFrame(ChartConfigPanel.builder().build(Arrays.asList(
                        new StringConfigEntry("foo", "bar"),
                        new StringConfigEntry("baz", "42"),
                        new IntConfigEntry("some field with very long label", 42, 41, 43, 1),
                        new DoubleConfigEntry("double field", 42.1, 41.0, 43.0, 0.1),
                        new ComboBoxConfigEntry("list field", Arrays.asList("foo", "bar", "baz", "42")),
                        new DateTimeConfigEntry("date field", new Date(), new Date(0), new Date()),
                        new BoolConfigEntry("bool field", true),
                        new BoolConfigEntry("bool field 2", false),
                        new StringConfigEntry("baz", "42")
                )).getPanel());
                mf.setVisible(true);
                edtThreadHolder[0] = Thread.currentThread();
            }
        });
        // join on EDT thread here for test-only purposes
        edtThreadHolder[0].join();
    }

    private static class MainFrame extends JFrame {
        MainFrame(JPanel panel) {
            addWindowListener(createCloseListener());
            setContentPane(panel);
            pack();
            setLocationRelativeTo(null);
        }
    }


}