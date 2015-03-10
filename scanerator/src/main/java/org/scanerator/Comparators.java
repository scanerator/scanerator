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
	 * @return The natural-ordering {@link Comparator}
	 */
	@SuppressWarnings("unchecked")
	public static <T> Comparator<T> naturalOrder() {
		return NATURAL_ORDER;
	}
	
	private Comparators() {}
}
