package org.scanerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scanerator {
	
	public static <T> OrderedIterable<T> itr(Iterable<T> itr, Comparator<? super T> cmp, boolean dropDescending) {
		return new WrappingOrderedIterable<T>(itr, cmp, dropDescending);
	}
	
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
