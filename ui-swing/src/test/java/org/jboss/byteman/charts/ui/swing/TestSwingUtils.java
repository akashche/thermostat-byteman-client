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

import org.jboss.byteman.charts.ui.swing.pages.ContentPage;

import javax.swing.*;

import java.awt.*;
import java.net.URL;

import static org.jboss.byteman.charts.utils.SwingUtils.createCloseListener;

/**
 * User: alexkasko
 * Date: 6/11/15
 */
public class TestSwingUtils {

    public static void showAndWait(final Container panel) {
        try {
            final Thread[] edtThreadHolder = new Thread[1];
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JFrame mf = createMainFrame(panel);
                    mf.setVisible(true);
                    edtThreadHolder[0] = Thread.currentThread();
                }
            });
            // join on EDT thread here for test-only purposes
            edtThreadHolder[0].join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static JFrame createMainFrame(Container content) {
        JFrame jf = new JFrame();
        URL url = ContentPage.class.getResource("icons/byteman_48.png");
        jf.setIconImage(new ImageIcon(url).getImage());
        jf.addWindowListener(createCloseListener());
        jf.setContentPane(content);
        jf.setSize(1024, 600);
        jf.pack();
        jf.setLocationRelativeTo(null);
        return jf;
    }
}
