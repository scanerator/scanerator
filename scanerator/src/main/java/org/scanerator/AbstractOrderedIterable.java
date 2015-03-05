package org.scanerator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class AbstractOrderedIterable<T> implements OrderedIterable<T> {

	protected class Box {
		protected int capacity;
		protected SortedSet<T> items = new TreeSet<T>(cmp());
		
		public Box(int capacity) {
			if(capacity <= 0)
				throw new IllegalArgumentException("Box capacity must be positive");
			this.capacity = capacity;
		}
		
		public boolean isFull() {
			return items.size() == capacity;
		}
		
		public boolean isEmpty() {
			return items.size() == 0;
		}
		
		public boolean add(T item) {
			if(isFull() && !items.contains(item))
				throw new IllegalStateException("Box " + items + " already full, cannot add " + item);
			return items.add(item);
		}
		
		public T remove() {
			if(isEmpty())
				throw new IllegalStateException("Box empty, cannot take item");
			Iterator<T> ii = items.iterator();
			T item = ii.next();
			ii.remove();
			return item;
		}
		
		public T peek() {
			if(isEmpty())
				throw new IllegalStateException("Box empty, cannot take item");
			Iterator<T> ii = items.iterator();
			T item = ii.next();
			return item;
		}
	}
	
	private Comparator<T> cmp;
	
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
