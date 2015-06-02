package org.jboss.byteman.charts.swing.config;

import javax.swing.*;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public interface ChartConfigControlBuilder {

    JComponent build();

    String layoutOptions();
}
