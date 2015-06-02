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

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import static org.jboss.byteman.charts.utils.SwingUtils.enableLafIfAvailable;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public class ConfigTest {

    @Test
    public void test() throws InvocationTargetException, InterruptedException, UnsupportedLookAndFeelException {
        enableLafIfAvailable("Nimbus");
        final Thread[] edtThreadHolder = new Thread[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                MainFrame mf = new MainFrame(new ChartConfigPanelBuilder().build(Collections.<ChartConfigField>emptyList()));
                mf.setVisible(true);
                edtThreadHolder[0] = Thread.currentThread();
            }
        });
        // join on EDT thread here for test-only purposes
        edtThreadHolder[0].join();
    }

    private static class MainFrame extends JFrame {
        MainFrame(JPanel panel) {
            addWindowListener(new CloseListener());
            setContentPane(panel);
            pack();
            setLocationRelativeTo(null);
        }
    }

    private static class CloseListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            for (Frame fr : Frame.getFrames()) {
                fr.dispose();
            }
        }
    }

}
