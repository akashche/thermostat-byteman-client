/*
 * Copyright 2012-2015 Red Hat, Inc.
 *
 * This file is part of Thermostat.
 *
 * Thermostat is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2, or (at your
 * option) any later version.
 *
 * Thermostat is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Thermostat; see the file COPYING.  If not see
 * <http://www.gnu.org/licenses/>.
 *
 * Linking this code with other modules is making a combined work
 * based on this code.  Thus, the terms and conditions of the GNU
 * General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this code give
 * you permission to link this code with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also
 * meet, for each linked independent module, the terms and conditions
 * of the license of that module.  An independent module is a module
 * which is not derived from or based on this code.  If you modify
 * this code, you may extend this exception to your version of the
 * library, but you are not obligated to do so.  If you do not wish
 * to do so, delete this exception statement from your version.
 */
package com.redhat.thermostat.byteman.ui.swing;

import com.redhat.thermostat.byteman.ui.swing.pages.ContentPage;

import javax.swing.*;

import java.awt.*;
import java.net.URL;

import static com.redhat.thermostat.byteman.utils.SwingUtils.createCloseListener;

/**
 * @author akashche
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
        jf.pack();
        jf.setSize(1024, 600);
        jf.setLocationRelativeTo(null);
        return jf;
    }
}
