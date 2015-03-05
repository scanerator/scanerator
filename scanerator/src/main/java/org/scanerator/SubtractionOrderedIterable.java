package org.scanerator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link OrderedIterable} that is the logical subtraction
 * of the left-hand side from the right-hand side; this {@link OrderedIterable}
 * contains all elements of the left-hand side that are not
 * contained in the right-hand side.
 * @author robin
 *
 * @param <T>
 */
public class SubtractionOrderedIterable<T> extends AbstractOrderedIterable<T> {

	/**
	 * The left-hand side {@link OrderedIterable}
	 */
	protected OrderedIterable<T> lhs;
	/**
	 * The right-hand side {@link OrderedIterable}
	 */
	protected OrderedIterable<T> rhs;
	
	/**
	 * Create an {@link OrderedIterable} that is the logical subtraction
	 * of {@code lhs} from {@code rhs}.
	 * @param lhs
	 * @param rhs
	 */
	public SubtractionOrderedIterable(OrderedIterable<T> lhs, OrderedIterable<T> rhs) {
		super(Comparators.combine(lhs.cmp(), rhs.cmp()));
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
		protected PQ lnext = new PQ(1);
		/**
		 * The next element from the right-hand side
		 */
		protected PQ rnext = new PQ(1);
		
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
