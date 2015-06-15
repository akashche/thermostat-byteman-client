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
package org.jboss.byteman.charts.utils;


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
        return str != null ? str : "";
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
