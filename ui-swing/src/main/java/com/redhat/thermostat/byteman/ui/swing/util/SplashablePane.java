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
package com.redhat.thermostat.byteman.ui.swing.util;

import com.redhat.thermostat.byteman.ui.swing.pages.ContentPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

/**
 * Generic panel implementation that can show a splash over
 * the target component
 *
 * @author akashche
 * Date: 9/7/15
 */

// http://coderazzi.net/glassedpane/index.html
public class SplashablePane extends JComponent {

    private final Splash splash;
    private Component content;

    /**
     * Constructor, creates instance with empty {@code JPanel} as
     * a target component
     */
    public SplashablePane() {
        this(new JPanel());
    }

    /**
     * Constructor
     *
     * @param content target component to show splash over
     */
    public SplashablePane(Component content) {
        this.content = content;
        this.splash = new Splash();
        add(content, -1);
        propagateSize(content, getWidth(), getHeight());
        add(splash, 0);
        propagateSize(splash, getWidth(), getHeight());
        splash.setVisible(false);
    }

    /**
     * Setter for target component
     *
     * @param content target component
     */
    public void setContent(Component content) {
        remove(this.content);
        this.content = content;
        add(content, -1);
        propagateSize(content, getWidth(), getHeight());
    }

    public void showSplash() {
        this.splash.setVisible(true);
    }

    /**
     * Shows splash over the target component
     */
    public void hideSplash() {
        this.splash.setVisible(false);
    }

    /**
     * Overrides the parent definition to disable optimized drawing when the
     * glass panel is visible
     */
    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    /**
     * Overrides the parent definition to make the content and glass panes
     * occupy the whole component area.
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        propagateSize(content, width, height);
        propagateSize(splash, width, height);
    }

    /**
     * Overrides the default implementation, to return the preferred size of
     * the content panel.
     */
    @Override
    public Dimension getPreferredSize() {
        return addInsets(content.getPreferredSize());
    }

    /**
     * Overrides the default implementation, to return the minimum size of
     * the content panel.
     */
    @Override
    public Dimension getMinimumSize() {
        return addInsets(content.getMinimumSize());
    }

    private void propagateSize(Component target, int width, int height) {
        if (target != null) {
            Insets insets = getInsets();
            width -= insets.left + insets.right;
            height -= insets.top + insets.bottom;
            target.setBounds(insets.left, insets.top, width, height);
        }
    }

    private Dimension addInsets(Dimension s) {
        Insets insets = getInsets();
        return new Dimension(s.width + insets.left + insets.right, s.height + insets.top + insets.bottom);
    }

    private static class Splash extends JPanel implements MouseListener, KeyListener {
        private final JLabel label;
        private final Color color = new Color(192, 192, 192, 96);

        Splash() {
            super(null);
            setOpaque(false);
            addMouseListener(this);
            addKeyListener(this);
            this.label = new JLabel();
            URL url = ContentPage.class.getResource("icons/loading_32.gif");
            label.setIcon(new ImageIcon(url));
//            label.setOpaque(true);
//            label.setBackground(color);
            add(label);
            recenter();
        }

        /**
         * Overrides the basic implementation,
         * to automatically center the label in the pane.
         */
        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            if (label != null) {
                recenter();
            }
        }

        /**
         * Overrides the default implementation to visualize the defined color
         */
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(color);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        /**
         * If the user updates the label's text, icon, position, etc, this
         * method should be called to recenter the label in the pane
         */
        private void recenter() {
            Dimension size = getSize();
            Dimension psize = label.getPreferredSize();
            label.setBounds((size.width - psize.width) / 2,
                    (size.height - psize.height) / 2,
                    psize.width,
                    psize.height);
        }

        /**
         * Captures this event, doing nothing with it
         */
        public void keyPressed(KeyEvent e) {}

        /**
         * Captures this event, doing nothing with it
         */
        public void keyReleased(KeyEvent e) {}

        /**
         * Captures this event, doing nothing with it
         */
        public void keyTyped(KeyEvent e) {}

        /**
         * Captures this event, doing nothing with it
         */
        public void mouseClicked(MouseEvent e) {}

        /**
         * Captures this event, doing nothing with it
         */
        public void mouseEntered(MouseEvent e) {}

        /**
         * Captures this event, doing nothing with it
         */
        public void mouseExited(MouseEvent e) {}

        /**
         * Captures this event, doing nothing with it
         */
        public void mousePressed(MouseEvent e) {}

        /**
         * Captures this event, doing nothing with it
         */
        public void mouseReleased(MouseEvent e) {}
    }
}
