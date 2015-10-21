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

import org.jboss.byteman.charts.ui.DateTimeConfigEntry;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Date;

/**
 * User: alexkasko
 * Date: 6/3/15
 */
public class DateTimeSpinnerControl extends ChartConfigSwingControl<DateTimeConfigEntry> {

    public DateTimeSpinnerControl(DateTimeConfigEntry entry) {
        super(entry);
    }

    @Override
    public JComponent createComponent() {
        SpinnerDateModel model = new SpinnerDateModel(entry.getDefaultValue(), entry.getMinValue(),
                entry.getMaxValue(), 0);
        JSpinner sp = new JSpinner();
        sp.setModel(model);
        sp.setEditor(new JSpinner.DateEditor(sp, "yyyy-MM-dd HH:mm:ss"));
        sp.addFocusListener(new Listener(sp));
        return sp;
    }

    private class Listener extends FocusAdapter {
        private final JSpinner spinner;

        private Listener(JSpinner spinner) {
            this.spinner = spinner;
        }

        @Override
        public void focusLost(FocusEvent e) {
            Date date = (Date) spinner.getValue();
            entry.setValue(date);
        }
    }
}
