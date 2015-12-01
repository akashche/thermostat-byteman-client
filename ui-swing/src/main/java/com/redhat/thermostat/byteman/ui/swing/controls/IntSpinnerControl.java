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
package com.redhat.thermostat.byteman.ui.swing.controls;

import com.redhat.thermostat.byteman.chart.IntConfigEntry;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * User: alexkasko
 * Date: 6/3/15
 */
public class IntSpinnerControl extends ChartConfigSwingControl<IntConfigEntry> {

    public IntSpinnerControl(IntConfigEntry entry) {
        super(entry);
    }

    @Override
    public JComponent createComponent() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel((int) entry.getValue(), entry.getMinValue(),
                entry.getMaxValue(), entry.getStep()));
        JSpinner.DefaultEditor ed = (JSpinner.DefaultEditor) spinner.getEditor();
        ed.getTextField().addFocusListener(new Listener(spinner));
        return spinner;
    }

    private class Listener extends FocusAdapter {
        private final JSpinner spinner;

        private Listener(JSpinner spinner) {
            this.spinner = spinner;
        }

        @Override
        public void focusLost(FocusEvent e) {
            Number num = (Number) spinner.getValue();
            entry.setValue(num.intValue());
        }
    }
}
