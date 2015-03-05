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
	/**
	 * Returns the {@link Comparator} used when comparing
	 * elements to ensure {@link #iterator()} returns elements
	 * in ascending order
	 * @return
	 */
	public Comparator<T> cmp();
	
	/**
	 * Returns a new {@link OrderedIterable} that is the logical
	 * "union" of this {@link OrderedIterable} and the argument.
	 * Elements of this {@link OrderedIterable} or the argument that 
	 * are duplicated will be duplicated in the returned
	 * {@link OrderedIterable}
	 * @param i
	 * @return
	 * @see UnionOrderedIterable
	 */
	public OrderedIterable<T> or(OrderedIterable<T> i);
	
	/**
	 * Returns a new {@link OrderedIterable} that is the logical
	 * "intersection" of this {@link OrderedIterable} and the
	 * argument.  Elements of this {@link OrderedIterable} that
	 * are duplicated in the argument will be returned once
	 * per pair-wise duplication.
	 * @param i
	 * @return
	 * @see IntersectionOrderedIterable
	 */
	public OrderedIterable<T> and(OrderedIterable<T> i);
	
	/**
	 * Returns a new {@link OrderedIterable} that is the logical
	 * "subtraction" of the argument from this {@link OrderedIterable}.
	 * The returned {@link OrderedIterable} will contain only those
	 * elements found in this {@link OrderedIterable} but not found
	 * in the argument.
	 * @param i
	 * @return
	 * @see SubtractionOrderedIterable
	 */
	public OrderedIterable<T> not(OrderedIterable<T> i);
	
	public OrderedIterable<T> dedup();
}
