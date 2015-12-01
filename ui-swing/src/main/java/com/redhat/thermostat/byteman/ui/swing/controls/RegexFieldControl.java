package com.redhat.thermostat.byteman.ui.swing.controls;

import com.redhat.thermostat.byteman.chart.RegexConfigEntry;

import javax.swing.*;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import static com.redhat.thermostat.byteman.utils.string.StringUtils.defaultString;

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
        JTextField tf = new JTextField(defaultString(entry.getValue()));
        tf.addFocusListener(new Listener(tf));
        return tf;
    }

    private class Listener extends FocusAdapter {
        private final JTextField tf;

        private Listener(JTextField tf) {
            this.tf = tf;
        }

        @Override
        public void focusLost(FocusEvent e) {
            entry.setValue(tf.getText());
        }
    }
}