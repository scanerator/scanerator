package org.scanerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class for dealing with {@link Iterable}s
 * @author robin
 *
 */
public class Scanerator {
	
	/**
	 * Return an {@link Iterable} that wraps (and checks) the {@link Iterable} argument,
	 * using the specified {@link Comparator}, and the specified behavior
	 * if out-of-order elements are encountered.
	 * @param itr The {@link Iterator} to wrap
	 * @param cmp The {@link Comparator} to compare with
	 * @param dropDescending {@code true} to silently drop out-of-order elements.
	 * {@code false} to throw an {@link IllegalStateException} when out-of-order
	 * elements are encountered
	 * @return A new {@link Iterable}
	 */
	public static <T> Iterable<T> checked(Comparator<? super T> cmp, Iterable<T> itr, boolean dropDescending) {
		return new CheckedIterable<T>(cmp, itr, dropDescending);
	}
	
	/**
	 * Return an {@link Iterable} that wraps (and checks) the {@link Iterable} argument
	 * using the "natural order" {@link Comparator}, {@link Comparators#NATURAL_ORDER},
	 * throwing an {@link IllegalStateException} if the wrapped {@link Iterable}
	 * returns an out-of-order element.
	 * @param itr
	 * @return
	 * @see #checked(Iterable, Comparator, boolean)
	 */
	public static <T> Iterable<T> checked(Iterable<T> itr) {
		return checked(Comparators.<T>naturalOrder(), itr, false);
	}
	
	/**
	 * Return an {@link Iterable} that has no elements
	 * @return
	 */
	public static <T> Iterable<T> empty() {
		return new EmptyIterable<T>();
	}
	
	public static <T> Iterable<T> all(Iterable<Iterable<T>> itrs) {
		return all(Comparators.naturalOrder(), itrs);
	}
	
	/**
	 * Return an {@link Iterable} that is the logical intersection
	 * of all {@link Iterable}s in the argument {@code itrs}.  If {@code itrs}
	 * is empty, returns an empty {@link Iterable}.  Otherwise, construct
	 * a binary tree of {@link IntersectionIterable} from the
	 * elements of {@code itrs} and return that.
	 * @param itrs
	 * @return
	 * @see IntersectionIterable
	 */
	public static <T> Iterable<T> all(Comparator<? super T> cmp, Iterable<Iterable<T>> itrs) {
		List<Iterable<T>> list = list(itrs);
		if(list.size() == 0) // edge case, no elements
			return new EmptyIterable<T>();
		if(list.size() == 1) // base case, return the only Iterable
			return list.get(0);
		// divide and conquer
		List<Iterable<T>> left = list.subList(0, list.size() / 2);
		List<Iterable<T>> right = list.subList(list.size() / 2, list.size());
		// return intersection of all(left) and all(right)
		return new IntersectionIterable<T>(cmp, all(cmp, left), all(cmp, right));
	}
	
	public static <T> Iterable<T> any(Iterable<Iterable<T>> itrs) {
		return any(Comparators.naturalOrder(), itrs);
	}
	
	/**
	 * Return an {@link Iterable} that is the logical union
	 * of all {@link Iterable}s in the argument {@code itrs}.  If
	 * {@code itrs} is empty, returns an empty {@link Iterable}.
	 * Otherwise, construct a binary tree of {@link UnionIterable} from
	 * the elements of {@code itrs} and return that.
	 * @param itrs
	 * @return
	 * @see UnionIterable
	 */
	public static <T> Iterable<T> any(Comparator<? super T> cmp, Iterable<Iterable<T>> itrs) {
		List<Iterable<T>> list = list(itrs);
		if(list.size() == 0) // edge case, no elements
			return new EmptyIterable<T>();
		if(list.size() == 1) // base case, return the only Iterable
			return list.get(0);
		// divide an conquery
		List<Iterable<T>> left = list.subList(0, list.size() / 2);
		List<Iterable<T>> right = list.subList(list.size() / 2, list.size());
		// return union of any(left) and any(right)
		return new UnionIterable<T>(cmp, any(cmp, left), any(cmp, right));
	}
	
	public static <T> Iterable<T> not(Iterable<T> lhs, Iterable<T> rhs) {
		return not(Comparators.naturalOrder(), lhs, rhs);
	}

	/**
	 * Return an {@link Iterable} that is the logical
	 * subtraction of {@code rhs} from {@code lhs}.
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see SubtractionIterable
	 */
	public static <T> Iterable<T> not(Comparator<? super T> cmp, Iterable<T> lhs, Iterable<T> rhs) {
		return new SubtractionIterable<T>(cmp, lhs, rhs);
	}
	
	public static <T> Iterable<T> dedup(Iterable<T> itr) {
		return dedup(Comparators.naturalOrder(), itr);
	}
	
	/**
	 * Return an {@link Iterable} that removes duplicate elements
	 * returned by {@code itr}
	 * @param itr
	 * @return
	 * @see DedupIterable
	 */
	public static <T> Iterable<T> dedup(Comparator<? super T> cmp, Iterable<T> itr) {
		return new DedupIterable<T>(cmp, itr);
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
