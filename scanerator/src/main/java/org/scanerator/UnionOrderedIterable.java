package org.scanerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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

		protected List<T> lnext = new ArrayList<T>(1);
		protected List<T> rnext = new ArrayList<T>(1);
		
		public Itr() {
			if(litr.hasNext())
				lnext.add(litr.next());
			if(ritr.hasNext())
				rnext.add(ritr.next());
		}

		public boolean hasNext() {
			return lnext.size() + rnext.size() > 0;
		}
	
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			T next;
			if(lnext.size() == 0) {
				next = rnext.remove(0);
				if(ritr.hasNext())
					rnext.add(ritr.next());
			} else if(rnext.size() == 0) {
				next = lnext.remove(0);
				if(litr.hasNext())
					lnext.add(litr.next());
			} else {
				T left = lnext.get(0);
				T right = rnext.get(0);
				int c = cmp.compare(left, right);
				if(c < 0) {
					next = left;
					lnext.remove(0);
					if(litr.hasNext())
						lnext.add(litr.next());
				} else if(c > 0) {
					next = right;
					rnext.remove(0);
					if(ritr.hasNext())
						rnext.add(ritr.next());
				} else {
					next = left;
					lnext.remove(0);
					rnext.remove(0);
					if(litr.hasNext())
						lnext.add(litr.next());
					if(ritr.hasNext())
						rnext.add(ritr.next());
				}
			}
			return next;
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
