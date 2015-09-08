package org.jboss.byteman.charts.ui.swing.util;

import org.jboss.byteman.charts.ui.swing.pages.ContentPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

/**
 * User: alexkasko
 * Date: 9/7/15
 */

// http://coderazzi.net/glassedpane/index.html
public class SplashablePane extends JComponent {

    private final Splash splash;
    private Component content;

    public SplashablePane() {
        this(new JPanel());
    }

    public SplashablePane(Component content) {
        this.content = content;
        this.splash = new Splash();
        add(content, -1);
        propagateSize(content, getWidth(), getHeight());
        add(splash, 0);
        propagateSize(splash, getWidth(), getHeight());
        splash.setVisible(false);
    }

    public void setContent(Component content) {
        remove(this.content);
        this.content = content;
        add(content, -1);
        propagateSize(content, getWidth(), getHeight());
    }

    public void showSplash() {
        this.splash.setVisible(true);
    }

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
