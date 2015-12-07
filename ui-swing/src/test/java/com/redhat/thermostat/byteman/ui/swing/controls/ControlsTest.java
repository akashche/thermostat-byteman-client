/*
 * Copyright 2012-2015 Red Hat, Inc.
 *
 * This file is part of Thermostat.
 *
 * Thermostat is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2, or (at your
 * option) any later version.
 *
 * Thermostat is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Thermostat; see the file COPYING.  If not see
 * <http://www.gnu.org/licenses/>.
 *
 * Linking this code with other modules is making a combined work
 * based on this code.  Thus, the terms and conditions of the GNU
 * General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this code give
 * you permission to link this code with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also
 * meet, for each linked independent module, the terms and conditions
 * of the license of that module.  An independent module is a module
 * which is not derived from or based on this code.  If you modify
 * this code, you may extend this exception to your version of the
 * library, but you are not obligated to do so.  If you do not wish
 * to do so, delete this exception statement from your version.
 */
package com.redhat.thermostat.byteman.ui.swing.controls;

import com.redhat.thermostat.byteman.chart.*;
import com.redhat.thermostat.byteman.ui.swing.config.ChartConfigPanel;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;

import static com.redhat.thermostat.byteman.utils.SwingUtils.enableLafIfAvailable;

/**
 * @author akashche
 * Date: 6/2/15
 */
public class ControlsTest {

    @Before
    public void setup() {
        enableLafIfAvailable("Nimbus");
    }

    @Test
    public void test() throws InvocationTargetException, InterruptedException, UnsupportedLookAndFeelException {
        JPanel panel = ChartConfigPanel.builder().build(Arrays.asList(
                new StringConfigEntry("foo", "bar"),
                new StringConfigEntry("baz", "42"),
                new IntConfigEntry("some field with very long label", 42, 41, 43, 1),
                new DoubleConfigEntry("double field", 42.1, 41.0, 43.0, 0.1),
                new ListConfigEntry("list field", Arrays.asList("foo", "bar", "baz", "42")),
                new DateTimeConfigEntry("date field", new Date(), new Date(0), new Date()),
                new BoolConfigEntry("bool field", true),
                new BoolConfigEntry("bool field 2", false),
                new StringConfigEntry("baz", "42")
        )).getPanel();
        // note: uncomment me for manual testing
//        showAndWait(panel);
    }

}
