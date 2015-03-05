package org.scanerator;

import java.util.Comparator;

/**
 * Utility class for dealing with {@link Comparator}s
 * @author robin
 *
 */
public class Comparators {
	
	/**
	 * {@link Comparator} which uses "natural ordering"; compares by
	 * casting objects to {@link Comparable} and calling {@link Comparable#compareTo(Object)}
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static final Comparator NATURAL_ORDER = new Comparator() {
		public int compare(Object o1, Object o2) {
			return ((Comparable) o1).compareTo(o2);
		}
	};
	
	/**
	 * {@link Comparator} which uses "natural ordering"; compares by
	 * casting objects to {@link Comparable} and calling {@link Comparable#compareTo(Object)}
	 */
	@SuppressWarnings("unchecked")
	public static <T> Comparator<T> naturalOrder() {
		return NATURAL_ORDER;
	}
	
	/**
	 * "Combine" two {@link Comparator}s.  If the two arguments are
	 * the same object then just return it.  Otherwise, create
	 * a new {@link CombinedComparator} and return that.  If
	 * either argument is {@code null} then return the other argument.
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static <T> Comparator<T> combine(Comparator<T> lhs, Comparator<T> rhs) {
		if(lhs == null)
			return rhs;
		if(rhs == null)
			return lhs;
		if(lhs == rhs)
			return lhs;
		return new CombinedComparator<T>(lhs, rhs);
	}
	
	private Comparators() {}
}
