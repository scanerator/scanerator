package org.scanerator;

import java.util.Comparator;

/**
 * {@link Iterable} whose {@link #iterator()} method is
 * required to return elements in ascending order according
 * to some {@link Comparator}.<p>
 * 
 * Additional useful methods for combining {@link OrderedIterable}
 * instances are provided.  When two {@link OrderedIterable} instances
 * are combined with different {@link Comparator}s, the resulting 
 * {@link OrderedIterable} has for its  {@link Comparator} an
 * instance of {@link CombinedComparator}, which consults
 * both {@link Comparator}s for the comparison and throws an
 * {@link IllegalStateException} if they do not agree.
 * @author robin
 *
 * @param <T>
 * 
 * @see Scanerator
 * @see CombinedComparator
 * @see AbstractOrderedIterable
 */
public interface OrderedIterable<T> extends Iterable<T> {
	public Comparator<T> cmp();
	public OrderedIterable<T> any(OrderedIterable<T> i);
	public OrderedIterable<T> all(OrderedIterable<T> i);
	public OrderedIterable<T> not(OrderedIterable<T> i);
}
