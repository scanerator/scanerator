package org.scanerator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link Iterable} that removes duplicates from
 * a wrapped {@link Iterable}.
 * @author robin
 *
 * @param <T>
 */
public class DedupIterable<T> extends AbstractOrderedIterable<T> {

	/**
	 * The wrapped {@link Iterable}
	 */
	protected Iterable<T> wrapped;

	public DedupIterable(Iterable<T> wrapped) {
		this(Comparators.naturalOrder(), wrapped);
	}
	
	/**
	 * Create an {@link Iterable} that removes duplicate elements
	 * from the wrapped {@link Iterable}, comparing according
	 * to the argument {@link Comparator}
	 * @param wrapped
	 * @param cmp
	 */
	public DedupIterable(Comparator<? super T> cmp, Iterable<T> wrapped) {
		super(cmp);
		this.wrapped = wrapped;
	}

	public Iterator<T> iterator() {
		return new Itr();
	}

	/**
	 * {@link Iterator} which does the deduplication
	 * @author robin
	 *
	 */
	protected class Itr implements Iterator<T> {
		/**
		 * Iterator of wrapped elements
		 */
		protected Iterator<T> itr = wrapped.iterator();

		/**
		 * Upcoming unique elements
		 */
		protected Box next = new Box(2);

		public Itr() {
			if(itr.hasNext()) { // if there's at least one wrapped element
				// add it
				T n = itr.next();
				next.offer(n);
				
				if(itr.hasNext()) { // if there are at least 2 wrapped elements
					// locate the next unique element and add it
					T n2 = itr.next();
					while(cmp().compare(n, n2) == 0 && itr.hasNext())
						n2 = itr.next();
					if(cmp().compare(n, n2) != 0)
						next.offer(n2);
				}
			}
		}

		public boolean hasNext() {
			return !next.isEmpty();
		}

		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			T n = next.poll();
			// locate the next unique element and add it
			if(next.size() == 0 && itr.hasNext()) {
				T n2 = itr.next();
				while(cmp().compare(n, n2) == 0 && itr.hasNext())
					n2 = itr.next();
				if(cmp().compare(n, n2) != 0)
					next.offer(n2);
			}
			return n;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String toString() {
		return "(dedup " + wrapped + ")";
	}
}
