package org.scanerator;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link Iterable} that returns an empty {@link Iterator} from {@link #iterator()}
 * @author robin
 *
 * @param <T>
 */
public class EmptyIterable<T> implements Iterable<T> {

	/**
	 * Create an empty {@link Iterable}
	 */
	public EmptyIterable() {
	}

	public Iterator<T> iterator() {
		return new Itr();
	}
	
	@Override
	public String toString() {
		return "(empty)";
	}

	protected class Itr implements Iterator<T> {
		@Override
		public boolean hasNext() {
			return false;
		}
	
		@Override
		public T next() {
			throw new NoSuchElementException();
		}
	
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
