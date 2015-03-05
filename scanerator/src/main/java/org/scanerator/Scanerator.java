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
	 * Return an {@link OrderedIterable} that wraps the {@link Iterable} argument,
	 * using the specified {@link Comparator}, and the specified behavior
	 * if out-of-order elements are encountered.
	 * @param itr The {@link Iterator} to wrap
	 * @param cmp The {@link Comparator} to compare with
	 * @param dropDescending {@code true} to silently drop out-of-order elements.
	 * {@code false} to throw an {@link IllegalStateException} when out-of-order
	 * elements are encountered
	 * @return A new {@link OrderedIterable}
	 */
	public static <T> OrderedIterable<T> itr(Iterable<T> itr, Comparator<? super T> cmp, boolean dropDescending) {
		return new WrappingOrderedIterable<T>(itr, cmp, dropDescending);
	}
	
	/**
	 * Return an {@link OrderedIterable} that wraps the {@link Iterable} argument
	 * using the "natural order" {@link Comparator}, {@link Comparators#NATURAL_ORDER},
	 * throwing an {@link IllegalStateException} if the wrapped {@link Iterable}
	 * returns an out-of-order element.
	 * @param itr
	 * @return
	 * @see #itr(Iterable, Comparator, boolean)
	 */
	public static <T> OrderedIterable<T> itr(Iterable<T> itr) {
		return itr(itr, Comparators.<T>naturalOrder(), false);
	}
	
	public static <T> OrderedIterable<T> all(List<OrderedIterable<T>> itrs) {
		if(itrs.size() == 0)
			return new EmptyOrderedIterable<T>();
		if(itrs.size() == 1)
			return itrs.get(0);
		List<OrderedIterable<T>> left = itrs.subList(0, itrs.size() / 2);
		List<OrderedIterable<T>> right = itrs.subList(itrs.size() / 2, itrs.size());
		return new IntersectionOrderedIterable<T>(all(left), all(right));
	}
	
	public static <T> OrderedIterable<T> any(List<OrderedIterable<T>> itrs) {
		if(itrs.size() == 0)
			return new EmptyOrderedIterable<T>();
		if(itrs.size() == 1)
			return itrs.get(0);
		List<OrderedIterable<T>> left = itrs.subList(0, itrs.size() / 2);
		List<OrderedIterable<T>> right = itrs.subList(itrs.size() / 2, itrs.size());
		return new UnionOrderedIterable<T>(any(left), any(right));
	}
	
	public static <T> List<T> list(Iterable<T> itr) {
		List<T> ret = new ArrayList<T>();
		for(T obj : itr)
			ret.add(obj);
		return ret;
	}
	
	private Scanerator() {}
}
