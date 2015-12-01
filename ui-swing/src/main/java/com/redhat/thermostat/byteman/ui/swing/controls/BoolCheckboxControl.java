package com.redhat.thermostat.byteman.ui.swing.controls;

import com.redhat.thermostat.byteman.config.BoolConfigEntry;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
        JCheckBox cb = new JCheckBox("", entry.getValue());
        cb.addFocusListener(new Listener(cb));
        return cb;
    }

    private class Listener extends FocusAdapter {
        private final JCheckBox cb;

        private Listener(JCheckBox cb) {
            this.cb = cb;
        }

        @Override
        public void focusLost(FocusEvent e) {
            entry.setValue(cb.isSelected());
        }
    }
}