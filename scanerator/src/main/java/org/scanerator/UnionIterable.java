package org.scanerator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link Iterable} that is the logical union of
 * the left-hand side and the right-hand side.  Elements
 * duplicated in lhs and rhs will be duplicated in this
 * {@link Iterable}.
 * @author robin
 *
 * @param <T>
 */
public class UnionIterable<T> extends AbstractOrderedIterable<T> {

	/**
	 * The left-hand side of the union
	 */
	protected Iterable<T> lhs;
	/**
	 * The right-hand side of the union
	 */
	protected Iterable<T> rhs;
	
	/**
	 * Create a {@link UnionIterable} using {@link Comparators#naturalOrder()}
	 * @param lhs Left-hand {@link Iterable} for union
	 * @param rhs Right-hand {@link Iterable} for union
	 */
	public UnionIterable(Iterable<T> lhs, Iterable<T> rhs) {
		this(Comparators.naturalOrder(), lhs, rhs);
	}
	
	/**
	 * Create a new {@link Iterable} that is the logical union
	 * of {@code lhs} and {@code rhs}
	 * @param cmp The {@link Comparator} for equality
	 * @param lhs Left-hand {@link Iterable} for union
	 * @param rhs Right-hand {@link Iterable} for union
	 */
	public UnionIterable(Comparator<? super T> cmp, Iterable<T> lhs, Iterable<T> rhs) {
		super(cmp);
		if(lhs == null || rhs == null)
			throw new IllegalArgumentException();
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public Iterator<T> iterator() {
		return new Itr();
	}

	/**
	 * {@link Iterator} that computes the union
	 * @author robin
	 *
	 */
	protected class Itr implements Iterator<T> {
		/**
		 * left-hand side {@link Iterator}
		 */
		protected Iterator<T> litr = lhs.iterator();
		/**
		 * right-hand side {@link Iterator}
		 */
		protected Iterator<T> ritr = rhs.iterator();

		/**
		 * Upcoming elements
		 */
		protected Box next = new Box(2);
		/**
		 * Most recent lhs element
		 */
		protected T llast;
		/**
		 * Most recent rhs element
		 */
		protected T rlast;
		
		public Itr() {
			if(litr.hasNext())
				next.offer(llast = litr.next());
			if(ritr.hasNext())
				next.offer(rlast = ritr.next());
		}

		public boolean hasNext() {
			return !next.isEmpty();
		}
	
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			
			T n = next.poll();
			
			if(litr.hasNext() && ritr.hasNext()) { // if both rhs and lhs have another element
				// grab the next element from whichever's last was lower
				if(cmp().compare(llast, rlast) <= 0)
					next.offer(llast = litr.next());
				else
					next.offer(rlast = ritr.next());
			} else if(litr.hasNext()) { // if lhs has an element but rhs doesn't
				// grab the next from lhs
				next.offer(litr.next());
			} else if(ritr.hasNext()) { // if rhs has an element but lhs doesn't
				// grab the next from rhs
				next.offer(ritr.next());
			}
			
			return n;
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String toString() {
		return "(any " + lhs + " " + rhs + ")";
	}
}
