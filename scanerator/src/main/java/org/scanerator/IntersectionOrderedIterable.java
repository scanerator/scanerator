package org.scanerator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IntersectionOrderedIterable<T> extends AbstractOrderedIterable<T> {

	protected OrderedIterable<T> lhs;
	protected OrderedIterable<T> rhs;
	
	public IntersectionOrderedIterable(OrderedIterable<T> lhs, OrderedIterable<T> rhs) {
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
		
		protected boolean empty;
		
		protected T lnext;
		protected T rnext;
		
		public Itr() {
			if(!litr.hasNext() || !ritr.hasNext())
				empty = true;
			else {
				lnext = litr.next();
				rnext = ritr.next();
			}
		}
		
		public boolean hasNext() {
			if(empty)
				return false;
			int c;
			while(!empty && (c = cmp.compare(lnext, rnext)) != 0) {
				if(c < 0) {
					if(!litr.hasNext())
						empty = true;
					else
						lnext = litr.next();
				}
				if(c > 0) {
					if(!ritr.hasNext())
						empty = true;
					else
						rnext = ritr.next();
				}
			}
			return !empty;
		}
	
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			T next = lnext;
			if(!litr.hasNext() || !ritr.hasNext())
				empty = true;
			else {
				lnext = litr.next();
				rnext = ritr.next();
			}
			return next;
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
