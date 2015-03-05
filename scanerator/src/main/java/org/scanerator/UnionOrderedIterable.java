package org.scanerator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

public class UnionOrderedIterable<T> extends AbstractOrderedIterable<T> {

	protected OrderedIterable<T> lhs;
	protected OrderedIterable<T> rhs;
	
	public UnionOrderedIterable(OrderedIterable<T> lhs, OrderedIterable<T> rhs) {
		super(Comparators.combine(lhs.cmp(), rhs.cmp()));
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public Iterator<T> iterator() {
		return new Itr();
	}

	protected class Itr implements Iterator<T> {
		protected Iterator<T> litr = lhs.iterator();
		protected Iterator<T> ritr = rhs.iterator();

		protected Queue<T> next = new PriorityQueue<T>(2, cmp());
		protected T llast;
		protected T rlast;
		
		public Itr() {
			if(litr.hasNext())
				next.offer(llast = litr.next());
			if(ritr.hasNext())
				next.offer(rlast = ritr.next());
		}

		public boolean hasNext() {
			return !next.isEmpty();
		}
	
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			
			T n = next.poll();
			
			if(litr.hasNext() && ritr.hasNext()) {
				if(cmp().compare(llast, rlast) <= 0)
					next.offer(llast = litr.next());
				else
					next.offer(rlast = ritr.next());
			} else if(litr.hasNext()) {
				next.offer(litr.next());
			} else if(ritr.hasNext()) {
				next.offer(ritr.next());
			}
			
			return n;
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
