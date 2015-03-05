package org.scanerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SubtractionOrderedIterable<T> extends AbstractOrderedIterable<T> {

	protected OrderedIterable<T> lhs;
	protected OrderedIterable<T> rhs;
	
	public SubtractionOrderedIterable(OrderedIterable<T> lhs, OrderedIterable<T> rhs) {
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

		protected Box lnext = new Box(1);
		protected Box rnext = new Box(1);
		
		protected void pull() {
			while(!lnext.isEmpty() && !rnext.isEmpty()) {
				int c = cmp().compare(lnext.peek(), rnext.peek());
				if(c < 0)
					return;
				else if(c == 0) {
					lnext.remove();
					if(litr.hasNext())
						lnext.add(litr.next());
				} else {
					rnext.remove();
					if(ritr.hasNext())
						rnext.add(ritr.next());
				}
			}
		}
		
		public Itr() {
			if(litr.hasNext())
				lnext.add(litr.next());
			if(ritr.hasNext())
				rnext.add(ritr.next());
		}

		public boolean hasNext() {
			pull();
			return !lnext.isEmpty();
		}
	
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			T next = lnext.remove();
			if(litr.hasNext())
				lnext.add(litr.next());
			return next;
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
