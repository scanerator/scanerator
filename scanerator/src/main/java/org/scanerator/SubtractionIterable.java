package org.scanerator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link Iterable} that is the logical subtraction
 * of the left-hand side from the right-hand side; this {@link Iterable}
 * contains all elements of the left-hand side that are not
 * contained in the right-hand side.
 * @author robin
 *
 * @param <T>
 */
public class SubtractionIterable<T> extends AbstractOrderedIterable<T> {

	/**
	 * The left-hand side {@link Iterable}
	 */
	protected Iterable<T> lhs;
	/**
	 * The right-hand side {@link Iterable}
	 */
	protected Iterable<T> rhs;
	
	public SubtractionIterable(Iterable<T> lhs, Iterable<T> rhs) {
		this(Comparators.naturalOrder(), lhs, rhs);
	}
	
	/**
	 * Create an {@link Iterable} that is the logical subtraction
	 * of {@code lhs} from {@code rhs}.
	 * @param lhs
	 * @param rhs
	 */
	public SubtractionIterable(Comparator<? super T> cmp, Iterable<T> lhs, Iterable<T> rhs) {
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
	 * {@link Iterator} which does the subtraction
	 * @author robin
	 *
	 */
	protected class Itr implements Iterator<T> {
		/**
		 * LHS {@link Iterator}
		 */
		protected Iterator<T> litr = lhs.iterator();
		/**
		 * RHS {@link Iterator}
		 */
		protected Iterator<T> ritr = rhs.iterator();

		/**
		 * The next element from the left-hand side
		 */
		protected Box lnext = new Box(1);
		/**
		 * The next element from the right-hand side
		 */
		protected Box rnext = new Box(1);
		
		/**
		 * Advance {@link #litr} and {@link #ritr} past
		 * any duplicated elements
		 */
		protected void pull() {
			while(!lnext.isEmpty() && !rnext.isEmpty()) { // only need to advance if both are not empty
				int c = cmp().compare(lnext.peek(), rnext.peek());
				if(c < 0) // if lhs is less than RHS then it is not in RHS
					return;
				else if(c == 0) { // if lhs == rhs then drop it and try again
					lnext.poll();
					if(litr.hasNext())
						lnext.offer(litr.next());
				} else { // if lhs is greater than RHS then advance RHS
					rnext.poll();
					if(ritr.hasNext())
						rnext.offer(ritr.next());
				}
			}
		}
		
		public Itr() {
			if(litr.hasNext())
				lnext.offer(litr.next());
			if(ritr.hasNext())
				rnext.offer(ritr.next());
		}

		public boolean hasNext() {
			pull();
			return !lnext.isEmpty();
		}
	
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			T next = lnext.poll();
			if(litr.hasNext())
				lnext.offer(litr.next());
			return next;
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String toString() {
		return "(not " + lhs + " " + rhs + ")";
	}
}
