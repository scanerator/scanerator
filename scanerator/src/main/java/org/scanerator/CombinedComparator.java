package org.scanerator;

import java.util.Comparator;

/**
 * {@link Comparator} that delegates its comparison to
 * two child {@link Comparator}s.  If the child {@link Comparator}s
 * do not agree on a comparison then {@link IllegalStateException} is
 * thrown by {@link #compare(Object, Object)}
 * @author robin
 *
 * @param <T>
 */
public class CombinedComparator<T> implements Comparator<T> {

	/**
	 * The left-hand {@link Comparator}
	 */
	protected Comparator<? super T> lhs;
	/**
	 * The right-hand {@link Comparator}
	 */
	protected Comparator<? super T> rhs;
	
	/**
	 * Create a {@link CombinedComparator}, which delegates its
	 * comparison to both {@code lhs} and {@code rhs}.  If they
	 * disagree then {@link IllegalStateException} is thrown
	 * by {@link #compare(Object, Object)}.
	 * @param lhs
	 * @param rhs
	 */
	public CombinedComparator(Comparator<? super T> lhs, Comparator<? super T> rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	/**
	 * Compare {@code o1} with {@code o2} using both {@link #lhs} and {@link #rhs}.
	 * If they agree on the comparison, return {@link #lhs}'s return value.
	 * If they do not agree on a comparison, throws {@link IllegalStateException}
	 */
	public int compare(T o1, T o2) {
		int lc = lhs.compare(o1, o2);
		int rc = rhs.compare(o1, o2);
		if(Math.signum(lc) != Math.signum(rc))
			throw new IllegalStateException("Comparators " + lhs + " and " + rhs + " disagree on " + o1 + " vs " + o2);
		return lc;
	}
	
	@Override
	public String toString() {
		return "(both " + lhs + " " + rhs + ")";
	}
	
}
