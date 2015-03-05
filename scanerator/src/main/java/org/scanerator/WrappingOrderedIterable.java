package org.scanerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class WrappingOrderedIterable<T> extends AbstractOrderedIterable<T> {

	protected Iterable<T> itr;
	protected boolean dropDescending;
	
	public WrappingOrderedIterable(Iterable<T> itr, Comparator<? super T> cmp, boolean dropDescending) {
		super(cmp);
		this.itr = itr;
		this.dropDescending = dropDescending;
	}

	public Iterator<T> iterator() {
		return new Itr();
	}

	protected class Itr implements Iterator<T> {
		protected Iterator<T> itr = WrappingOrderedIterable.this.itr.iterator();
		
		protected List<T> next = new ArrayList<T>(2);
		
		public Itr() {
			if(itr.hasNext())
				next.add(itr.next());
			if(itr.hasNext())
				next.add(itr.next());
		}
		
		public boolean hasNext() {
			return next.size() > 0;
		}
	
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			T n = next.remove(0);
			if(next.size() > 0 && cmp.compare(n, next.get(0)) > 0) {
				if(!dropDescending)
					throw new IllegalStateException("Not an ascending iterator: " + itr + " found " + n + " followed by " + next.get(0));
				do {
					next.remove(0);
					if(itr.hasNext())
						next.add(itr.next());
				} while(next.size() > 0 && cmp.compare(n, next.get(0)) > 0);
			}
			if(itr.hasNext())
				next.add(itr.next());
			return n;
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
