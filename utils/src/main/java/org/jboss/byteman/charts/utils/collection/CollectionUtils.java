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
package org.jboss.byteman.charts.utils.collection;

import org.jboss.byteman.charts.utils.UtilsException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author alexkasko
 *         Date: 7/7/14
 */
public class CollectionUtils {

    public static void consumeQuietly(Iterator<?> iter) {
        if (null == iter) return;
        try {
            while (iter.hasNext()) iter.next();
        } catch (Exception e) {
            // be quiet
        }
    }

    public static Map<String, Object> toMap(Object[] arr){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        if (0 != arr.length % 2) throw new UtilsException("Invalid odd parameters count");
        for (int i = 0; i < arr.length; i += 2) {
            Object objKey = arr[i];
            if (!(objKey instanceof String)) throw new UtilsException("Invalid key: [" + objKey + "]");
            map.put((String) objKey, arr[i+1]);
        }
        return map;
    }
}
