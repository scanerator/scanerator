package org.scanerator;

import java.util.Comparator;

public interface OrderedIterable<T> extends Iterable<T> {
	public Comparator<T> cmp();
	public OrderedIterable<T> any(OrderedIterable<T> i);
	public OrderedIterable<T> all(OrderedIterable<T> i);
	public OrderedIterable<T> not(OrderedIterable<T> i);
}
