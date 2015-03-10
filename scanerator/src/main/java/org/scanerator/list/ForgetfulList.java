package org.scanerator.list;

import java.util.AbstractList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * {@link List} that retains only a certain number of element values.
 * Attempts to retrieve an un-retained element value will
 * result in a thrown {@link IllegalStateException}.<p>
 * 
 * Elements are added to the cache using {@link #add(int, Object)}.
 * Adding an element when the cache is full will result in the
 * least-recently-accessed element being dropped from the cache.<p>
 * 
 * Elements are removed from the cache using {@link #remove(int)}.<p>
 * 
 * Calls to {@link #get(int)} or {@link #set(int, Object)} will
 * move an element to the most-recently-used position in the cache.
 * 
 * @author robin
 *
 * @param <E>
 */
public class ForgetfulList<E> extends AbstractList<E> {
	
	/**
	 * A cached list element. 
	 * {@link #hashCode()} and {@link #equals(Object)} consider
	 * only {@link #index}, and do not consider {@link #value}.
	 * @author robin
	 *
	 */
	protected class Memory {
		/**
		 * The index in the list of the cached element
		 */
		public int index;
		/**
		 * The value at the index in the list
		 */
		public E value;
		
		/**
		 * Create a new cache element
		 * @param index The index in the list
		 * @param value The value at the index
		 */
		public Memory(int index, E value) {
			this.index = index;
			this.value = value;
		}
		
		@Override
		public int hashCode() {
			return index;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ForgetfulList.Memory))
				return false;
			return index == ((ForgetfulList<?>.Memory) obj).index;
		}
	}

	/**
	 * The logical size of this {@link List}
	 */
	protected int size;
	/**
	 * The maximum size of the element cache
	 */
	protected int capacity;
	/**
	 * The element cache
	 */
	protected Deque<Memory> memory;
	
	/**
	 * Create a {@link ForgetfulList} with a maximum cache size of {@code capacity}
	 * @param capacity The maximum cache size
	 */
	public ForgetfulList(int capacity) {
		if(capacity <= 0)
			throw new IllegalArgumentException();
		this.capacity = capacity;
		size = 0;
		memory = new ArrayDeque<Memory>();
	}
	
	@Override
	public E get(int index) {
		if(index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		for(Memory m : memory) {
			if(index == m.index) {
				memory.remove(m);
				memory.offerLast(m);
				return m.value;
			}
		}
		throw new IllegalStateException();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public E set(int index, E element) {
		if(index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		for(Memory m : memory) {
			if(index == m.index) {
				memory.remove(m);
				memory.offerLast(m);
				E old = m.value;
				m.value = element;
				return old;
			}
		}
		throw new IllegalStateException();
	}
	
	@Override
	public void add(int index, E element) {
		if(index < 0 || index > size())
			throw new IndexOutOfBoundsException();
		for(Memory m : memory) {
			if(index <= m.index)
				m.index++;
		}
		memory.offerLast(new Memory(index, element));
		while(memory.size() > capacity)
			memory.pollFirst();
		size++;
	}
	
	@Override
	public E remove(int index) {
		if(index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		for(Memory m : memory) {
			if(index == m.index) {
				memory.remove(m);
				for(Memory mm : memory) {
					if(index < mm.index)
						mm.index--;
				}
				size--;
				return m.value;
			}
		}
		throw new IllegalStateException();
	}
}
