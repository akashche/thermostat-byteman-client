package org.jboss.byteman.charts.ui.swing.controls;

import org.jboss.byteman.charts.ui.RegexConfigEntry;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
        JTextField tf = new JTextField(defaultString(entry.getDefaultValue()));
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