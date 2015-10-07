package org.jboss.byteman.charts.ui.swing.controls;

import org.jboss.byteman.charts.ui.RegexConfigEntry;

import javax.swing.*;

import static org.jboss.byteman.charts.utils.StringUtils.defaultString;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
public class RegexFieldControl  extends ChartConfigSwingControl<RegexConfigEntry> {

    public RegexFieldControl(RegexConfigEntry entry) {
        super(entry);
    }

    @Override
    public JComponent createComponent() {
        return new JTextField(defaultString(entry.getDefaultValue()));
    }
}