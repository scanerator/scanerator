package org.scanerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * {@link Iterable} that wraps an {@link Iterable} and maintains
 * the {@link Iterable} ascending-order contract by checking
 * returned elements with a {@link Comparator}.  If an out-of-order
 * element is encountered it is either dropped or an {@link IllegalStateException}
 * is thrown, depending on the configuration of this {@link CheckedIterable}.
 * @author robin
 *
 * @param <T>
 */
public class CheckedIterable<T> extends AbstractOrderedIterable<T> {

	/**
	 * The wrapped {@link Iterable}
	 */
	protected Iterable<T> wrapped;
	/**
	 * Drop out-of-order elements? {@code true} == drop, {@code false} == exception
	 */
	protected boolean dropDescending;
	
	/**
	 * Create a new {@link CheckedIterable} using {@link Comparators#naturalOrder()}
	 * @param itr The {@link Iterable} to wrap
	 * @param dropDescending {@code true} to drop descending elements, {@code false} to
	 * throw an exception on descending elements
	 */
	public CheckedIterable(Iterable<T> itr, boolean dropDescending) {
		this(Comparators.naturalOrder(), itr, dropDescending);
	}
	
	/**
	 * Create an {@link Iterable} wrapping an {@link Iterable}, enforced
	 * by a {@link Comparator}, that either drops out-of-order elements
	 * or throws an exception, depending on {@code dropDescending}
	 * @param cmp {@link Comparator} for element ordering
	 * @param itr The {@link Iterable} to wrap
	 * @param dropDescending {@code true} to drop descending elements, {@code false} to
	 * throw an exception on descending elements
	 */
	public CheckedIterable(Comparator<? super T> cmp, Iterable<T> itr, boolean dropDescending) {
		super(cmp);
		if(itr == null)
			throw new IllegalArgumentException();
		this.wrapped = itr;
		this.dropDescending = dropDescending;
	}

	public Iterator<T> iterator() {
		return new Itr();
	}

	/**
	 * Iterator that does the wrapping and checking
	 * @author robin
	 *
	 */
	protected class Itr implements Iterator<T> {
		/**
		 * Wrapped {@link Iterator}
		 */
		protected Iterator<T> itr = wrapped.iterator();
		
		/**
		 * Upcoming elements
		 */
		protected List<T> next = new ArrayList<T>(2);
		
		public Itr() {
			if(itr.hasNext())
				next.add(itr.next());
			if(itr.hasNext())
				next.add(itr.next());
		}
		
		public boolean hasNext() {
			return next.size() > 0;
		}
	
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			T n = next.remove(0);
			if(next.size() > 0 && cmp().compare(n, next.get(0)) > 0) { // if the elements are out of order
				if(!dropDescending) // and if not dropping out-of-order elements
					// throw an exception
					throw new IllegalStateException("Not an ascending iterator: " + itr + " found " + n + " followed by " + next.get(0));
				// otherwise drop elements until an in-order element is found 
				do {
					next.remove(0);
					if(itr.hasNext())
						next.add(itr.next());
				} while(next.size() > 0 && cmp().compare(n, next.get(0)) > 0);
			}
			if(itr.hasNext())
				next.add(itr.next());
			return n;
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String toString() {
		return "(checked " + wrapped + ")";
	}
}
