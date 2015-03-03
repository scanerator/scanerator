package org.scanerator;

import java.util.Comparator;

public class CombinedComparator<T> implements Comparator<T> {

	protected Comparator<? super T> lhs;
	protected Comparator<? super T> rhs;
	
	public CombinedComparator(Comparator<? super T> lhs, Comparator<? super T> rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	public int compare(T o1, T o2) {
		int lc = lhs.compare(o1, o2);
		int rc = rhs.compare(o1, o2);
		if(Math.signum(lc) != Math.signum(rc))
			throw new IllegalStateException("Comparators " + lhs + " and " + rhs + " disagree on " + o1 + " vs " + o2);
		return lc;
	}
	
}
