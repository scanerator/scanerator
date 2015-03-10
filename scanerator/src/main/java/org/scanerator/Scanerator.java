package org.scanerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.scanerator.list.IteratorList;

/**
 * Utility class for dealing with {@link Iterable}s
 * @author robin
 *
 */
public class Scanerator {
	
	/**
	 * Return an {@link Iterable} that wraps (and checks) the {@link Iterable} argument
	 * using the "natural order" {@link Comparator}, {@link Comparators#naturalOrder()},
	 * throwing an {@link IllegalStateException} if the wrapped {@link Iterable}
	 * returns an out-of-order element.
	 * @param itr The {@link Iterable} to be wrapped
	 * @return A new {@link Iterable}
	 * @see #checked(Comparator, Iterable, boolean)
	 */
	public static <T> Iterable<T> checked(Iterable<T> itr) {
		return checked(Comparators.<T>naturalOrder(), itr, false);
	}

	/**
	 * Return an {@link Iterable} that wraps (and checks) the {@link Iterable} argument,
	 * using the specified {@link Comparator}, and the specified behavior
	 * if out-of-order elements are encountered.
	 * @param cmp The {@link Comparator} to compare with
	 * @param itr The {@link Iterator} to wrap
	 * @param dropDescending {@code true} to silently drop out-of-order elements.
	 * {@code false} to throw an {@link IllegalStateException} when out-of-order
	 * elements are encountered
	 * @return A new {@link Iterable}
	 */
	public static <T> Iterable<T> checked(Comparator<? super T> cmp, Iterable<T> itr, boolean dropDescending) {
		return new CheckedIterable<T>(cmp, itr, dropDescending);
	}
	
	/**
	 * Return an {@link Iterable} that is the logical intersection
	 * of all {@link Iterable}s in the argument {@code itrs}.  If {@code itrs}
	 * is empty, returns an empty {@link Iterable}.  Otherwise, construct
	 * a binary tree of {@link IntersectionIterable} from the
	 * elements of {@code itrs} and return that.  Uses {@link Comparators#naturalOrder()}
	 * to determine element equality.
	 * @param itrs {@link Iterable}s to be intersected
	 * @return A new {@link Iterable}
	 * @see IntersectionIterable
	 */
	public static <T> Iterable<T> all(Iterable<Iterable<T>> itrs) {
		return all(Comparators.naturalOrder(), itrs);
	}

	/**
	 * Returns an {@link Iterable} that is the logical intersection
	 * of {@code lhs} and {@code rhs}, using {@link Comparators#naturalOrder()} to determine
	 * equality.
	 * @param lhs Left-hand {@link Iterable} for intersection
	 * @param rhs Right-hand {@link Iterable} for intersection
	 * @return A new {@link Iterable}
	 */

	public static <T> Iterable<T> all(Iterable<T> lhs, Iterable<T> rhs) {
		return all(Comparators.naturalOrder(), lhs, rhs);
	}
	
	/**
	 * Return an {@link Iterable} that is the logical intersection
	 * of all {@link Iterable}s in the argument {@code itrs}.  If {@code itrs}
	 * is empty, returns an empty {@link Iterable}.  Otherwise, construct
	 * a binary tree of {@link IntersectionIterable} from the
	 * elements of {@code itrs} and return that.
	 * @param cmp {@link Comparator} for equality
	 * @param itrs {@link Iterable}s to be intersected
	 * @return A new {@link Iterable}
	 * @see IntersectionIterable
	 */
	public static <T> Iterable<T> all(Comparator<? super T> cmp, Iterable<Iterable<T>> itrs) {
		return all(cmp, list(itrs));
	}
	
	/**
	 * Returns an {@link Iterable} that is the logical intersection
	 * of {@code lhs} and {@code rhs}, using {@code cmp} to determine
	 * equality.
	 * @param cmp {@link Comparator} for equality
	 * @param lhs Left-hand {@link Iterable} for intersection
	 * @param rhs Right-hand {@link Iterable} for intersection
	 * @return A new {@link Iterable}
	 */
	public static <T> Iterable<T> all(Comparator<? super T> cmp, Iterable<T> lhs, Iterable<T> rhs) {
		return new IntersectionIterable<T>(cmp, lhs, rhs);
	}
	
	/**
	 * Return an {@link Iterable} that is the logical intersection
	 * of all {@link Iterable}s in the argument {@code list}.  If {@code list}
	 * is empty, returns an empty {@link Iterable}.  Otherwise, construct
	 * a binary tree of {@link IntersectionIterable} from the
	 * elements of {@code list} and return that.
	 * @param cmp {@link Comparator} for equality
	 * @param list {@link Iterable}s to be intersected
	 * @return A new {@link Iterable}
	 * @see IntersectionIterable
	 */
	public static <T> Iterable<T> all(Comparator<? super T> cmp, List<Iterable<T>> list) {
		if(list.size() == 0) // edge case, no elements
			return empty();
		if(list.size() == 1) // base case, return the only Iterable
			return list.get(0);
		// divide and conquer
		List<Iterable<T>> left = list.subList(0, list.size() / 2);
		List<Iterable<T>> right = list.subList(list.size() / 2, list.size());
		// return intersection of all(left) and all(right)
		Iterable<T> lhs = all(cmp, left);
		Iterable<T> rhs = all(cmp, right);
		return new IntersectionIterable<T>(cmp, lhs, rhs);
	}

	/**
	 * Return an {@link Iterable} that is the logical union
	 * of all {@link Iterable}s in the argument {@code itrs}.  If
	 * {@code itrs} is empty, returns an empty {@link Iterable}.
	 * Otherwise, construct a binary tree of {@link UnionIterable} from
	 * the elements of {@code itrs} and return that.  Uses
	 * {@link Comparators#naturalOrder()} to determine element
	 * equality.
	 * @param itrs {@link Iterable}s to be unioned
	 * @return A new {@link Iterable}
	 * @see UnionIterable
	 */
	public static <T> Iterable<T> any(Iterable<Iterable<T>> itrs) {
		return any(Comparators.naturalOrder(), itrs);
	}
	
	/**
	 * Returns an {@link Iterable} that is the logical union
	 * of {@code lhs} and {@code rhs}, using {@link Comparators#naturalOrder()} to determine
	 * equality.
	 * @param lhs Left-hand {@link Iterable} for union
	 * @param rhs Right-hand {@link Iterable} for union
	 * @return A new {@link Iterable}
	 */
	public static <T> Iterable<T> any(Iterable<T> lhs, Iterable<T> rhs) {
		return any(Comparators.naturalOrder(), lhs, rhs);
	}
	
	/**
	 * Return an {@link Iterable} that is the logical union
	 * of all {@link Iterable}s in the argument {@code itrs}.  If
	 * {@code itrs} is empty, returns an empty {@link Iterable}.
	 * Otherwise, construct a binary tree of {@link UnionIterable} from
	 * the elements of {@code itrs} and return that.
	 * @param cmp {@link Comparator} for equality
	 * @param itrs {@link Iterable}s to be unioned
	 * @return A new {@link Iterable}
	 * @see UnionIterable
	 */
	public static <T> Iterable<T> any(Comparator<? super T> cmp, Iterable<Iterable<T>> itrs) {
		return any(cmp, list(itrs));
	}
	
	/**
	 * Returns an {@link Iterable} that is the logical union
	 * of {@code lhs} and {@code rhs}, using {@code cmp} to determine
	 * equality.
	 * @param cmp {@link Comparator} for equality
	 * @param lhs Left-hand {@link Iterable} for union
	 * @param rhs Right-hand {@link Iterable} for union
	 * @return A new {@link Iterable}
	 */
	public static <T> Iterable<T> any(Comparator<? super T> cmp, Iterable<T> lhs, Iterable<T> rhs) {
		return new UnionIterable<T>(cmp, lhs, rhs);
	}
	
	/**
	 * Return an {@link Iterable} that is the logical union
	 * of all {@link Iterable}s in the argument {@code list}.  If
	 * {@code list} is empty, returns an empty {@link Iterable}.
	 * Otherwise, construct a binary tree of {@link UnionIterable} from
	 * the elements of {@code list} and return that.
	 * @param cmp {@link Comparator} for equality
	 * @param list {@link Iterable}s to be unioned
	 * @return A new {@link Iterable}
	 * @see UnionIterable
	 */
	public static <T> Iterable<T> any(Comparator<? super T> cmp, List<Iterable<T>> list) {
		if(list.size() == 0) // edge case, no elements
			return empty();
		if(list.size() == 1) // base case, return the only Iterable
			return list.get(0);
		// divide an conquery
		List<Iterable<T>> left = list.subList(0, list.size() / 2);
		List<Iterable<T>> right = list.subList(list.size() / 2, list.size());
		// return union of any(left) and any(right)
		Iterable<T> lhs = any(cmp, left);
		Iterable<T> rhs = any(cmp, right);
		return new UnionIterable<T>(cmp, lhs, rhs);
	}
	
	/**
	 * Return an {@link Iterable} that is the logical
	 * subtraction of {@code rhs} from {@code lhs}.  Uses
	 * {@link Comparators#naturalOrder()} to determine
	 * element equality.
	 * @param lhs {@link Iterable} in which returned elements must be found
	 * @param rhs {@link Iterable} in which returned elements must not be found
	 * @return A new {@link Iterable}
	 * @see SubtractionIterable
	 */
	public static <T> Iterable<T> not(Iterable<T> lhs, Iterable<T> rhs) {
		return not(Comparators.naturalOrder(), lhs, rhs);
	}

	/**
	 * Return an {@link Iterable} that is the logical
	 * subtraction of {@code rhs} from {@code lhs}.
	 * @param cmp {@link Comparator} for equality
	 * @param lhs {@link Iterable} in which returned elements must be found
	 * @param rhs {@link Iterable} in which returned elements must not be found
	 * @return A new {@link Iterable}
	 * @see SubtractionIterable
	 */
	public static <T> Iterable<T> not(Comparator<? super T> cmp, Iterable<T> lhs, Iterable<T> rhs) {
		return new SubtractionIterable<T>(cmp, lhs, rhs);
	}
	
	/**
	 * Return an {@link Iterable} that is the logical
	 * subtraction of {@code rhs} from {@code lhs}.
	 * Uses {@link Comparators#naturalOrder()} to determine
	 * element equality.
	 * @param itr {@link Iterable} to de-duplicate
	 * @return A new {@link Iterable}
	 * @see SubtractionIterable
	 */
	public static <T> Iterable<T> dedup(Iterable<T> itr) {
		return dedup(Comparators.naturalOrder(), itr);
	}
	
	/**
	 * Return an {@link Iterable} that removes duplicate elements
	 * returned by {@code itr}
	 * @param cmp {@link Comparator} for equality
	 * @param itr {@link Iterable} to de-duplicate
	 * @return A new {@link Iterable}
	 * @see DedupIterable
	 */
	public static <T> Iterable<T> dedup(Comparator<? super T> cmp, Iterable<T> itr) {
		return new DedupIterable<T>(cmp, itr);
	}
	
	/**
	 * Returns an {@link Iterable} that has no elements.
	 * @return A new {@link Iterable}
	 */
	public static <T> Iterable<T> empty() {
		return new EmptyIterable<T>();
	}

	private Scanerator() {}

	private static <T> List<T> list(Iterable<T> itr) {
		List<T> list = new ArrayList<T>();
		for(T e : itr)
			list.add(e);
		return list;
	}
}
