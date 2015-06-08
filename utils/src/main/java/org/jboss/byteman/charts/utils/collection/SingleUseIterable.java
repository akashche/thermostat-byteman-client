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

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: alexkasko
 * Date: 7/24/14
 */
public class SingleUseIterable<T> implements Iterable<T> {
    private final Iterator<T> delegate;
    private AtomicBoolean used = new AtomicBoolean(false);

    public SingleUseIterable(Iterator<T> delegate) {
        if(null == delegate) throw new IllegalArgumentException("delegate");
        this.delegate = delegate;
    }

    public static <T> SingleUseIterable<T> singleUseIterable(Iterator<T> iter) {
        return new SingleUseIterable<T>(iter);
    }

    @Override
    public Iterator<T> iterator() {
        if(used.getAndSet(true)) throw new IllegalStateException("Iterable already used");
        return delegate;
    }
}
