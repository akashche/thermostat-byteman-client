package org.jboss.byteman.charts.plot.swing;

/**
 * User: alexkasko
 * Date: 9/28/15
 */
public interface ZoomManager {
    void zoom(float lower, float upper);

    void reset();
}
