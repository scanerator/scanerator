package org.scanerator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link OrderedIterable} consisting of the intersection of elements
 * of two other {@link OrderedIterable}s.  If elements are duplicated
 * in the left- or right-hand {@link OrderedIterable}s then this 
 * {@link OrderedIterable} will return one of the duplicate object
 * for each pair-wise duplication.  Equality is determined by
 * the {@link Comparator} returned by {@link #cmp()}, not by
 * calling {@link Object#equals(Object)}.
 * @author robin
 *
 * @param <T>
 */
public class IntersectionOrderedIterable<T> extends AbstractOrderedIterable<T> {

	/**
	 * The left-hand side of the intersection
	 */
	protected OrderedIterable<T> lhs;
	/**
	 * The right-hand side of the intersection
	 */
	protected OrderedIterable<T> rhs;
	
	/**
	 * Create a new {@link IntersectionOrderedIterable}, which returns elements
	 * that are the intersection of those found in {@code lhs} and {@code rhs}
	 * @param lhs
	 * @param rhs
	 */
	public IntersectionOrderedIterable(OrderedIterable<T> lhs, OrderedIterable<T> rhs) {
		super(Comparators.combine(lhs.cmp(), rhs.cmp()));
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public Iterator<T> iterator() {
		return new Itr();
	}

	/**
	 * {@link Iterator} that actually does the intersection
	 * @author robin
	 *
	 */
	protected class Itr implements Iterator<T> {
		/**
		 * {@link Iterator} of left-hand side
		 */
		protected Iterator<T> litr = lhs.iterator();
		/**
		 * {@link Iterator} of right-hand side
		 */
		protected Iterator<T> ritr = rhs.iterator();
		
		/**
		 * {@code true} if this {@link Iterator} has no more elements
		 */
		protected boolean empty;
		
		/**
		 * The next value from {@link #litr}
		 */
		protected T lnext;
		/**
		 * The next value from {@link #ritr}
		 */
		protected T rnext;
		
		public Itr() {
			if(!litr.hasNext() || !ritr.hasNext()) {
				// if either lhs or rhs is empty then the intersection is empty
				empty = true;
			} else {
				lnext = litr.next();
				rnext = ritr.next();
			}
		}
		
		public boolean hasNext() {
			if(empty)
				return false;
			int c;
			// while not empty and the lhs and rhs don't match...
			while(!empty && (c = cmp().compare(lnext, rnext)) != 0) {
				if(c < 0) { // if lhs is less than rhs
					// go to the next lhs element if possible
					if(!litr.hasNext())
						empty = true;
					else
						lnext = litr.next();
				}
				if(c > 0) { // if rhs is less than lhs
					// go to the next rhs element if possible
					if(!ritr.hasNext())
						empty = true;
					else
						rnext = ritr.next();
				}
			}
			return !empty;
		}
	
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			T next = lnext;
			// advance to the next item
			if(!litr.hasNext() || !ritr.hasNext()) // if either lhs or rhs is empty
				empty = true; // then this Iterator is empty 
			else {
				// otherwise since lhs == rhs (via cmp), advance both of them
				lnext = litr.next();
				rnext = ritr.next();
			}
			return next;
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String toString() {
		return "(all " + lhs + " " + rhs + ")";
	}
}
