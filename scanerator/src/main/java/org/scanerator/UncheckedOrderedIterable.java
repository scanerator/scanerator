package org.scanerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * {@link OrderedIterable} that wraps an {@link Iterable} and maintains
 * the {@link OrderedIterable} ascending-order contract by checking
 * returned elements with a {@link Comparator}.  If an out-of-order
 * element is encountered it is either dropped or an {@link IllegalStateException}
 * is thrown, depending on the configuration of this {@link UncheckedOrderedIterable}.
 * @author robin
 *
 * @param <T>
 */
public class UncheckedOrderedIterable<T> extends AbstractOrderedIterable<T> {

	/**
	 * The wrapped {@link Iterable}
	 */
	protected Iterable<T> wrapped;
	
	/**
	 * Create an {@link OrderedIterable} wrapping an {@link Iterable}, enforced
	 * by a {@link Comparator}, that either drops out-of-order elements
	 * or throws an exception, depending on {@code dropDescending}
	 * @param itr
	 * @param cmp
	 * @param dropDescending
	 */
	public UncheckedOrderedIterable(Iterable<T> itr, Comparator<? super T> cmp) {
		super(cmp);
		this.wrapped = itr;
	}

	public Iterator<T> iterator() {
		return wrapped.iterator();
	}

	@Override
	public String toString() {
		return "(unchecked " + wrapped + ")";
	}
}
