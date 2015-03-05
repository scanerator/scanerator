package org.scanerator;

import java.util.Comparator;
import java.util.Iterator;

/**
 * {@link OrderedIterable} that wraps an {@link Iterable} but does not
 * do any checking to ensure that the returned elements are ordered.
 * Use only if you are sure that the wrapped {@link Iterable} is already
 * ordered; if not, subsequent boolean operations will behave unpredictably.
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
	 * Create an {@link OrderedIterable} wrapping an {@link Iterable},
	 * with no enforcement of element ordering.
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
