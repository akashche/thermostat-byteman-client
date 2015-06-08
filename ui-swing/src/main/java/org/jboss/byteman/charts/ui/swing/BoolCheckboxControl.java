package org.jboss.byteman.charts.ui.swing;

import org.jboss.byteman.charts.ui.BoolConfigEntry;

import javax.swing.*;

/**
 * User: alexkasko
 * Date: 6/8/15
 */
public class BoolCheckboxControl extends ChartConfigSwingControl<BoolConfigEntry> {

    public BoolCheckboxControl(BoolConfigEntry entry) {
        super(entry);
    }

    @Override
    public JComponent createComponent() {
        return new JCheckBox("", entry.getDefaultValue());
    }
}
