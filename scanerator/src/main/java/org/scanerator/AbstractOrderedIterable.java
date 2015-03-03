package org.scanerator;

import java.util.Comparator;

public abstract class AbstractOrderedIterable<T> implements OrderedIterable<T> {

	protected Comparator<T> cmp;
	
	@SuppressWarnings("unchecked")
	public AbstractOrderedIterable(Comparator<? super T> cmp) {
		this.cmp = (Comparator<T>) cmp;
	}
	
	public Comparator<T> cmp() {
		return cmp;
	}
	
	public OrderedIterable<T> all(OrderedIterable<T> i) {
		return new IntersectionOrderedIterable<T>(this, i);
	}
	
	public OrderedIterable<T> any(OrderedIterable<T> i) {
		return new UnionOrderedIterable<T>(this, i);
	}
	
	public OrderedIterable<T> not(OrderedIterable<T> i) {
		return new SubtractionOrderedIterable<T>(this, i);
	}
}
