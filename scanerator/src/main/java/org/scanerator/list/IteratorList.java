package org.scanerator.list;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * {@link List} which lazily retrieves elements from an {@link Iterator},
 * caching them in another {@link List}.  {@link IteratorList} is a
 * read-only {@link List}.<p>
 * 
 * {@link IteratorList} is lazy where possible, but certain methods
 * (such as {@link #size()}) will always force a complete evaluation
 * of the {@link Iterator} being wrapped.<p>
 * 
 * A common use case is to create an {@link IteratorList} using a {@link ForgetfulList}
 * as its cache.
 *  
 * @author robin
 *
 * @param <E>
 */
public class IteratorList<E> extends AbstractList<E> implements RandomAccess {
	/**
	 * The wrapped {@link Iterator}
	 */
	protected Iterator<E> itr;
	/**
	 * The cache of elements already retrieved from {@link #itr}
	 */
	protected List<E> cache;
	
	/**
	 * Create a new {@link IteratorList} using an {@link ArrayList} as its cache
	 * @param itr The {@link Iterator} to wrap
	 */
	public IteratorList(Iterator<E> itr) {
		this(new ArrayList<E>(), itr);
	}
	
	/**
	 * Create a new {@link IteratorList} using the specified cache and wrapped {@link Iterator}
	 * @param cache The {@link List} in which to cache iterated elements
	 * @param itr The {@link Iterator} to wrap
	 */
	public IteratorList(List<E> cache, Iterator<E> itr) {
		if(cache == null || itr == null)
			throw new IllegalArgumentException();
		this.itr = itr;
		this.cache = cache;
	}
	
	@Override
	public E get(int index) {
		while(cache.size() <= index) {
			if(!itr.hasNext())
				throw new IndexOutOfBoundsException();
			cache.add(itr.next());
		}
		return cache.get(index);
	}

	@Override
	public int size() {
		while(itr.hasNext())
			cache.add(itr.next());
		return cache.size();
	}

	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(Object o) {
		Iterator<E> i = iterator();
		int idx = 0;
		while(i.hasNext()) {
			E e = i.next();
			if(o == null ? e == null : o.equals(e))
				return idx;
			idx++;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		return super.lastIndexOf(o);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<E> iterator() {
		return new LazyItr(0);
	}

	/**
	 * Unidirectional iterator
	 * @author robin
	 *
	 */
	protected class LazyItr implements Iterator<E> {
		/**
		 * The index of the next element
		 */
		protected int idx;
		
		public LazyItr(int idx) {
			this.idx = idx;
		}
		
		@Override
		public boolean hasNext() {
			return cache.size() > idx || itr.hasNext();
		}
	
		@Override
		public E next() {
			if(!hasNext())
				throw new NoSuchElementException();
			return get(idx++);
		}
	
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * Bidirectional iterator
	 * @author robin
	 *
	 */
	protected class LazyListItr extends LazyItr implements ListIterator<E> {

		/**
		 * Create a {@link LazyListItr} from the specified starting index
		 * @param idx
		 */
		public LazyListItr(int idx) {
			super(idx);
		}

		@Override
		public boolean hasPrevious() {
			return idx > 0;
		}

		@Override
		public E previous() {
			if(!hasPrevious())
				throw new NoSuchElementException();
			return get(--idx);
		}

		@Override
		public int nextIndex() {
			return idx;
		}

		@Override
		public int previousIndex() {
			return idx - 1;
		}

		@Override
		public void set(E e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(E e) {
			throw new UnsupportedOperationException();
		}
		
	}

	@Override
	public ListIterator<E> listIterator() {
		return new LazyListItr(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new LazyListItr(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return new LazySubList(fromIndex, toIndex);
	}
	
	/**
	 * Sub-list that doesn't force evaluation of {@link IteratorList#itr} by avoiding
	 * calls to {@link IteratorList#size()}
	 * @author robin
	 *
	 */
	protected class LazySubList extends AbstractList<E> implements RandomAccess {
		/**
		 * The starting offset in the parent {@link IteratorList}
		 */
		protected int offset;
		/**
		 * The size of this sublist
		 */
		protected int size;
		
		public LazySubList(int fromIndex, int toIndex) {
			// calling get(int) will do bounds checking for us
			IteratorList.this.get(fromIndex);
			IteratorList.this.get(toIndex - 1);
			if(fromIndex > toIndex)
				throw new IllegalArgumentException();
			offset = fromIndex;
			size = toIndex - fromIndex;
		}

		@Override
		public E get(int index) {
			return IteratorList.this.get(index + offset);
		}

		@Override
		public int size() {
			return size;
		}
		
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		return cache.size() == 0 && !itr.hasNext();
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	@Override
	public Object[] toArray() {
		return super.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return super.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return super.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
