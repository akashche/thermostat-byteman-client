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
package org.jboss.byteman.charts.plot;

import org.jboss.byteman.charts.utils.UtilsException;

import java.awt.*;

/**
 * User: alexkasko
 * Date: 6/8/15
 */
@Deprecated // use ColorUtils
public class PlotUtils {

    /**
     * Converts specified "#AARRGGBB" string into java.awt.Color instance
     *
     * @param rgba string in "#AARRGGBB" format
     * @return color instance
     */
    @Deprecated
    public static Color toColor(String rgba) {
        String unprefixed = rgba.startsWith("#") ? rgba.substring(1) : rgba;
        if (!(6 == unprefixed.length() || 8 == unprefixed.length())) throw new UtilsException("Invalid color: [" + rgba + "]");
        int hex = (int) Long.parseLong(unprefixed, 16);
        if (6 == unprefixed.length()) {
            return new Color(hex);
        }
        return new Color(hex, true);
    }

}
