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
package com.redhat.thermostat.byteman.utils.collection;

import com.redhat.thermostat.byteman.utils.UtilsException;

import java.util.LinkedHashMap;

/**
 * Collection utilities
 *
 * @author akashche
 *         Date: 5/25/15
 */
public class CollectionUtils {

    /**
     * Created an instance of strin->object map from "key", "value", "key", "value" array
     *
     * @param elems keys and values varargs
     * @return map instance
     */
    public static LinkedHashMap<String, Object> toMap(Object... elems) {
        return arrayToMap(elems);
    }

    /**
     * Created an instance of strin->object map from "key", "value", "key", "value" array
     *
     * @param dataArray keys and values array
     * @return map instance
     */
    public static LinkedHashMap<String, Object> arrayToMap(Object[] dataArray) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        if (0 != dataArray.length % 2) throw new UtilsException("Invalid odd elements count");
        for (int i = 0; i < dataArray.length; i += 2) {
            Object objKey = dataArray[i];
            if (!(objKey instanceof String)) throw new UtilsException("Invalid key: [" + objKey + "]");
            map.put((String) objKey, dataArray[i + 1]);
        }
        return map;
    }
}
