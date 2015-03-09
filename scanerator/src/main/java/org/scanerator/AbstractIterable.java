package org.scanerator;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Base implementation of {@link Iterable}.  Extensions of this
 * class must supply the {@link #iterator()} method.
 * @author robin
 *
 * @param <T>
 */
public abstract class AbstractIterable<T> implements Iterable<T> {

	/**
	 * Simple wrapper around {@link PriorityQueue} that enforces
	 * a max capacity
	 * @author robin
	 *
	 */
	protected class PQ {
		/**
		 * The max capacity of this {@link PQ}
		 */
		private int capacity;
		/**
		 * The {@link PriorityQueue} backing this {@link PQ}
		 */
		private PriorityQueue<T> items = new PriorityQueue<T>(1, cmp());
		
		/**
		 * Create a new {@link PQ} with a specified maximum capacity
		 * @param capacity
		 */
		public PQ(int capacity) {
			if(capacity <= 0)
				throw new IllegalArgumentException("Box capacity must be positive");
			this.capacity = capacity;
		}
		
		/**
		 * Returns whether this {@link PQ} is full
		 * @return
		 */
		public boolean isFull() {
			return items.size() == capacity;
		}
		
		/**
		 * Returns whether this {@link PQ} is empty
		 * @return
		 * @see PriorityQueue#isEmpty()
		 */
		public boolean isEmpty() {
			return items.size() == 0;
		}
		
		/**
		 * Returns the maximum capacity of this {@link PQ}
		 * @return
		 */
		public int capacity() {
			return capacity;
		}
		
		/**
		 * Returns the current size of this {@link PQ}
		 * @return
		 * @see PriorityQueue#size()
		 */
		public int size() {
			return items.size();
		}
		
		/**
		 * Offer an item to this {@link PQ}.  Throws {@link IllegalStateException} if
		 * this {@link PQ} is full.
		 * @param item
		 * @see PriorityQueue#offer(Object)
		 */
		public void offer(T item) {
			if(isFull())
				throw new IllegalStateException("Box " + items + " already full, cannot add " + item);
			items.add(item);
		}
		
		/**
		 * Poll an item from this {@link PQ}.  Throws {@link IllegalStateException} if
		 * this {@link PQ} is empty.
		 * @return
		 * @see PriorityQueue#poll()
		 */
		public T poll() {
			if(isEmpty())
				throw new IllegalStateException("Box empty, cannot take item");
			return items.poll();
		}
		
		/**
		 * Peek at the head item from this {@link PQ}.  Throws {@link IllegalStateException}
		 * if this {@link PQ} is empty.
		 * @return
		 * @see PriorityQueue#peek()
		 */
		public T peek() {
			if(isEmpty())
				throw new IllegalStateException("Box empty, cannot take item");
			return items.peek();
		}
	}
	
	/**
	 * The {@link Comparator} for this {@link Iterable}
	 */
	protected Comparator<? super T> cmp;
	
	/**
	 * Create an {@link AbstractIterable} given a {@link Comparator}.
	 * @param cmp
	 */
	public AbstractIterable(Comparator<? super T> cmp) {
		this.cmp = cmp;
	}
	
	protected Comparator<? super T> cmp() {
		return cmp;
	}
	
	@Override
	public abstract String toString();
}
