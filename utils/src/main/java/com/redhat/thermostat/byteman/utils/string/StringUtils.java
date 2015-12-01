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
package com.redhat.thermostat.byteman.utils.string;


import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String utilities
 *
 * @author akashche
 * Date: 5/25/15
 */
public class StringUtils {

    public static final String EMPTY_STRING = "";
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final Pattern FILE_EXTENSION_STRIP_PATTERN = Pattern.compile("\\.[^.]+$");

    /**
     * <p>Returns either the passed in String, or if the String is
     * <code>null</code>, the empty string
     *
     * @param str  the String to check, may be null
     * @return the passed in String, or the empty string if it was <code>null</code>
     */
    public static String defaultString(String str) {
        return defaultString(str, EMPTY_STRING);
    }

    /**
     * <p>Returns either the passed in String, or if the String is
     * <code>null</code>, the default string
     *
     * @param str  the String to check, may be null
     * @param defstr  default string
     * @return the passed in String, or the empty string if it was <code>null</code>
     */
    public static String defaultString(String str, String defstr) {
        return str != null ? str : defstr;
    }

    /**
     * Returns whether specified string is null or empty
     *
     * @param str string to check
     * @return whether specified string is null or empty
     */
    public static boolean isEmpty(String str) {
        return null == str || 0 == str.length();
    }

    /**
     * Strips filename extension from specified file name
     *
     * @param filename filename
     * @return filename without extension
     */
    public static String stripFilenameExtension(String filename) {
        if (null == filename) return filename;
        Matcher ma = FILE_EXTENSION_STRIP_PATTERN.matcher(filename);
        if (ma.find()) {
            return ma.replaceFirst("");
        } else {
            return filename;
        }
    }

}
