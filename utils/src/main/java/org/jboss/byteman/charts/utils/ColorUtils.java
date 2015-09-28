package org.jboss.byteman.charts.utils;

import java.awt.*;

/**
 * User: alexkasko
 * Date: 9/28/15
 */
public class ColorUtils {

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
