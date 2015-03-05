package org.scanerator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link OrderedIterable} that removes duplicates from
 * a wrapped {@link OrderedIterable}.
 * @author robin
 *
 * @param <T>
 */
public class DedupOrderedIterable<T> extends AbstractOrderedIterable<T> {

	/**
	 * The wrapped {@link OrderedIterable}
	 */
	protected OrderedIterable<T> wrapped;

	/**
	 * Create an {@link OrderedIterable} that removes duplicate elements
	 * according to the argument's {@link OrderedIterable#cmp()}
	 * @param wrapped
	 */
	public DedupOrderedIterable(OrderedIterable<T> wrapped) {
		this(wrapped, wrapped.cmp());
	}
	
	/**
	 * Create an {@link OrderedIterable} that removes duplicate elements
	 * from the wrapped {@link OrderedIterable}, comparing according
	 * to the argument {@link Comparator}
	 * @param wrapped
	 * @param cmp
	 */
	public DedupOrderedIterable(OrderedIterable<T> wrapped, Comparator<? super T> cmp) {
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
		protected PQ next = new PQ(2);

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

}
