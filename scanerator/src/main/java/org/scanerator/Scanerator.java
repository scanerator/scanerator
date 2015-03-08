package org.scanerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class for dealing with {@link OrderedIterable}s
 * @author robin
 *
 */
public class Scanerator {
	
	/**
	 * Return an {@link OrderedIterable} that wraps (and checks) the {@link Iterable} argument,
	 * using the specified {@link Comparator}, and the specified behavior
	 * if out-of-order elements are encountered.
	 * @param itr The {@link Iterator} to wrap
	 * @param cmp The {@link Comparator} to compare with
	 * @param dropDescending {@code true} to silently drop out-of-order elements.
	 * {@code false} to throw an {@link IllegalStateException} when out-of-order
	 * elements are encountered
	 * @return A new {@link OrderedIterable}
	 */
	public static <T> OrderedIterable<T> checked(Iterable<T> itr, Comparator<? super T> cmp, boolean dropDescending) {
		return new CheckedOrderedIterable<T>(itr, cmp, dropDescending);
	}
	
	/**
	 * Return an {@link OrderedIterable} that wraps (and checks) the {@link Iterable} argument
	 * using the "natural order" {@link Comparator}, {@link Comparators#NATURAL_ORDER},
	 * throwing an {@link IllegalStateException} if the wrapped {@link Iterable}
	 * returns an out-of-order element.
	 * @param itr
	 * @return
	 * @see #checked(Iterable, Comparator, boolean)
	 */
	public static <T> OrderedIterable<T> checked(Iterable<T> itr) {
		return checked(itr, Comparators.<T>naturalOrder(), false);
	}
	
	/**
	 * Return an {@link OrderedIterable} that wraps but does not check the
	 * {@link Iterable} argument.  The {@link Comparator} argument is still
	 * required for use during later boolean operations.
	 * @param itr
	 * @param cmp
	 * @return
	 */
	public static <T> OrderedIterable<T> unchecked(Iterable<T> itr, Comparator<? super T> cmp) {
		return new UncheckedOrderedIterable<T>(itr, cmp);
	}
	
	/**
	 * Return an {@link OrderedIterable} that wraps but does not check the
	 * {@link Iterable} argument.  Uses {@link Comparators#naturalOrder()} as
	 * the {@link Comparator} for use during later boolean operations.
	 * @param itr
	 * @return
	 */
	public static <T> OrderedIterable<T> unchecked(Iterable<T> itr) {
		return unchecked(itr, Comparators.naturalOrder());
	}
	
	/**
	 * Return an {@link OrderedIterable} that has no elements
	 * @return
	 */
	public static <T> OrderedIterable<T> empty() {
		return new EmptyOrderedIterable<T>();
	}
	
	/**
	 * Return an {@link OrderedIterable} that is the logical intersection
	 * of all {@link OrderedIterable}s in the argument {@code itrs}.  If {@code itrs}
	 * is empty, returns an empty {@link OrderedIterable}.  Otherwise, construct
	 * a binary tree of {@link IntersectionOrderedIterable} from the
	 * elements of {@code itrs} and return that.
	 * @param itrs
	 * @return
	 * @see IntersectionOrderedIterable
	 */
	public static <T> OrderedIterable<T> all(Iterable<OrderedIterable<T>> itrs) {
		List<OrderedIterable<T>> list = list(itrs);
		if(list.size() == 0) // edge case, no elements
			return new EmptyOrderedIterable<T>();
		if(list.size() == 1) // base case, return the only OrderedIterable
			return list.get(0);
		// divide and conquer
		List<OrderedIterable<T>> left = list.subList(0, list.size() / 2);
		List<OrderedIterable<T>> right = list.subList(list.size() / 2, list.size());
		// return intersection of all(left) and all(right)
		return new IntersectionOrderedIterable<T>(all(left), all(right));
	}
	
	public static <T> OrderedIterable<T> checkedAll(Iterable<Iterable<T>> itrs, Comparator<? super T> cmp, boolean dropDescending) {
		List<OrderedIterable<T>> list = new ArrayList<OrderedIterable<T>>();
		for(Iterable<T> itr : itrs) {
			list.add(checked(itr, cmp, dropDescending));	
		}
		return all(list);
	}
	
	public static <T> OrderedIterable<T> uncheckedAll(Iterable<Iterable<T>> itrs, Comparator<? super T> cmp) {
		List<OrderedIterable<T>> list = new ArrayList<OrderedIterable<T>>();
		for(Iterable<T> itr : itrs) {
			list.add(unchecked(itr, cmp));	
		}
		return all(list);
	}
	
	/**
	 * Return an {@link OrderedIterable} that is the logical union
	 * of all {@link OrderedIterable}s in the argument {@code itrs}.  If
	 * {@code itrs} is empty, returns an empty {@link OrderedIterable}.
	 * Otherwise, construct a binary tree of {@link UnionOrderedIterable} from
	 * the elements of {@code itrs} and return that.
	 * @param itrs
	 * @return
	 * @see UnionOrderedIterable
	 */
	public static <T> OrderedIterable<T> any(Iterable<OrderedIterable<T>> itrs) {
		List<OrderedIterable<T>> list = list(itrs);
		if(list.size() == 0) // edge case, no elements
			return new EmptyOrderedIterable<T>();
		if(list.size() == 1) // base case, return the only OrderedIterable
			return list.get(0);
		// divide an conquery
		List<OrderedIterable<T>> left = list.subList(0, list.size() / 2);
		List<OrderedIterable<T>> right = list.subList(list.size() / 2, list.size());
		// return union of any(left) and any(right)
		return new UnionOrderedIterable<T>(any(left), any(right));
	}

	public static <T> OrderedIterable<T> checkedAny(Iterable<Iterable<T>> itrs, Comparator<? super T> cmp, boolean dropDescending) {
		List<OrderedIterable<T>> list = new ArrayList<OrderedIterable<T>>();
		for(Iterable<T> itr : itrs) {
			list.add(checked(itr, cmp, dropDescending));	
		}
		return any(list);
	}
	
	public static <T> OrderedIterable<T> uncheckedAny(Iterable<Iterable<T>> itrs, Comparator<? super T> cmp) {
		List<OrderedIterable<T>> list = new ArrayList<OrderedIterable<T>>();
		for(Iterable<T> itr : itrs) {
			list.add(unchecked(itr, cmp));	
		}
		return any(list);
	}
	
	
	/**
	 * Return an {@link OrderedIterable} that is the logical
	 * subtraction of {@code rhs} from {@code lhs}.
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see SubtractionOrderedIterable
	 */
	public static <T> OrderedIterable<T> not(OrderedIterable<T> lhs, OrderedIterable<T> rhs) {
		return new SubtractionOrderedIterable<T>(lhs, rhs);
	}
	
	/**
	 * Return an {@link OrderedIterable} that removes duplicate elements
	 * returned by {@code itr}
	 * @param itr
	 * @return
	 * @see DedupOrderedIterable
	 */
	public static <T> OrderedIterable<T> dedup(OrderedIterable<T> itr) {
		return new DedupOrderedIterable<T>(itr);
	}
	
	/**
	 * Build a {@link List} from the contents of an {@link Iterable}
	 * and return that {@link List}.
	 * @param itr
	 * @return
	 */
	public static <T> List<T> list(Iterable<T> itr) {
		List<T> ret = new ArrayList<T>();
		for(T obj : itr)
			ret.add(obj);
		return ret;
	}
	
	private Scanerator() {}
}
