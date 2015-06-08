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
import java.util.NoSuchElementException;

/**
 * User: alexkasko
 * Date: 7/24/14
 */
public abstract class SingleMethodIterator<T> implements Iterator<T> {
    private enum State {NOT_READY, READY, DONE}

    private State state = State.NOT_READY;
    private T next;

    @Override
    public boolean hasNext() {
        switch (state) {
            case DONE: return false;
            case NOT_READY:
                this.next = computeNext();
                if(State.DONE == this.state) return false;
                this.state = State.READY;
            case READY:
                return true;
            default: throw new IllegalStateException(String.valueOf(state));
        }
    }

    @Override
    public T next() {
        switch (state) {
            case DONE: throw new NoSuchElementException();
            case NOT_READY:
                this.next = computeNext();
                if(State.DONE == this.state) throw new NoSuchElementException();
                this.state = State.READY;
            case READY:
                this.state = State.NOT_READY;
                return this.next;
            default: throw new IllegalStateException(String.valueOf(state));
        }
    }

    protected abstract T computeNext();

    protected final T endOfData() {
        this.state = State.DONE;
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
