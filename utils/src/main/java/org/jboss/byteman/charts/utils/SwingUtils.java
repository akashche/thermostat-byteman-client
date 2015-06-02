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
package org.jboss.byteman.charts.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Contains swing-related utility methods
 *
 * User: alexkasko
 * Date: 6/2/15
 */
public class SwingUtils {

    /**
     * Enables look and feel with specified name only if such LaF is available
     *
     * @param lafName look and feel name
     */
    public static void enableLafIfAvailable(String lafName) {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (lafName.equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    /**
     * Creates a close listener that will dispose all create Frames
     *
     * @return close listener
     */
    public static WindowListener createCloseListener() {
        return new CloseListener();
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
