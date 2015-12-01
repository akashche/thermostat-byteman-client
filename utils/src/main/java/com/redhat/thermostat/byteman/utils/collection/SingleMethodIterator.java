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
package com.redhat.thermostat.byteman.utils.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Base iterator implementation that allow inheritors to
 * implement "single-method" iterators
 *
 * @author akashche
 * Date: 7/24/14
 */
public abstract class SingleMethodIterator<T> implements Iterator<T> {
    private enum State {NOT_READY, READY, DONE}

    private State state = State.NOT_READY;
    private T next;

    /**
     * @inheritDoc
     */
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

    /**
     * @inheritDoc
     */
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

    /**
     * Inheritor must return the next element instance
     * or "call-return" "endOfData" method
     *
     * @return next element or "endOfData"
     */
    protected abstract T computeNext();

    /**
     * This method must be called from
     * "computeNext" method on iterator exhaustion
     *
     * @return null
     */
    protected final T endOfData() {
        this.state = State.DONE;
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
