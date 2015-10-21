/*
* JBoss, Home of Professional Open Source
* Copyright 2015 Red Hat and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.byteman.charts.ui.swing.controls;

import org.jboss.byteman.charts.ui.StringConfigEntry;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static org.jboss.byteman.charts.utils.StringUtils.defaultString;

/**
 * User: alexkasko
 * Date: 6/3/15
 */
public class TextFieldControl extends ChartConfigSwingControl<StringConfigEntry> {

    public TextFieldControl(StringConfigEntry entry) {
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
