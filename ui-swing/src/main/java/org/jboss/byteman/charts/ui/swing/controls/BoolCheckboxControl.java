package org.jboss.byteman.charts.ui.swing.controls;

import org.jboss.byteman.charts.ui.BoolConfigEntry;

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
        JCheckBox cb = new JCheckBox("", entry.getDefaultValue());
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
