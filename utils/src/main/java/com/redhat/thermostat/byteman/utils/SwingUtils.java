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
package com.redhat.thermostat.byteman.utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static javax.swing.BorderFactory.*;

/**
 * Contains swing-related utility functions
 *
 * @author akashche
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

    /**
     * Made font bold for the specified component
     *
     * @param comp swing component
     * @return component itself
     */
    public static <T extends JComponent> T boldify(T comp) {
        Font font = comp.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        comp.setFont(boldFont);
        return comp;
    }

    /**
     * Creates border to separate form sections
     *
     * @param color border color
     * @param title section title
     * @return border instance
     */
    public static Border createFormSectionBorder(Color color, String title) {
        return createCompoundBorder(
                createEmptyBorder(5, 0, 0, 0),
                createTitledBorder(
                        createMatteBorder(1, 0, 0, 0, color)
                        , title)
        );
    }

    /**
     * Checks the time passed from the specified
     * timestamp and end ensures that this function this
     * exit no earlier than 500 milliseconds after that moment
     * sleeping for some time if required
     *
     * @param start start timestamp
     */
    public static void sleepNonFlicker(long start) {
        try {
            long end = System.currentTimeMillis();
            long diff = end - start;
            if (diff < 500) {
                Thread.sleep(500 - diff);
            }
        } catch (InterruptedException e) {
            throw new UtilsException("Non-flicker sleep fail", e);
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
