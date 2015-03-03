package org.scanerator;

import java.util.Comparator;

public class Comparators {
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static final Comparator NATURAL_ORDER = new Comparator() {
		public int compare(Object o1, Object o2) {
			return ((Comparable) o1).compareTo(o2);
		}
	};
	
	@SuppressWarnings("unchecked")
	public static <T> Comparator<T> naturalOrder() {
		return NATURAL_ORDER;
	}
	
	public static <T> Comparator<T> combine(Comparator<T> lhs, Comparator<T> rhs) {
		if(lhs == rhs)
			return lhs;
		if(lhs == null)
			return rhs;
		if(rhs == null)
			return lhs;
		return new CombinedComparator<T>(lhs, rhs);
	}
	
	private Comparators() {}
}
