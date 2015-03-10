package org.scanerator;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Abstract utility class for an orderable {@link Iterable}.  Extensions of this
 * class must supply the {@link #iterator()} method.
 * @author robin
 *
 * @param <T>
 */
public abstract class AbstractOrderedIterable<T> implements Iterable<T> {

	/**
	 * Simple wrapper around {@link PriorityQueue} that enforces
	 * a max capacity
	 * @author robin
	 *
	 */
	protected class Box {
		/**
		 * The max capacity of this {@link Box}
		 */
		private int capacity;
		/**
		 * The {@link PriorityQueue} backing this {@link Box}
		 */
		private PriorityQueue<T> items = new PriorityQueue<T>(1, cmp());
		
		/**
		 * Create a new {@link Box} with a specified maximum capacity
		 * @param capacity the max capacity
		 */
		public Box(int capacity) {
			if(capacity <= 0)
				throw new IllegalArgumentException("Box capacity must be positive");
			this.capacity = capacity;
		}
		
		/**
		 * Returns whether this {@link Box} is full
		 * @return {@code true} if full
		 */
		public boolean isFull() {
			return items.size() == capacity;
		}
		
		/**
		 * Returns whether this {@link Box} is empty
		 * @return {@code true} if empty
		 * @see PriorityQueue#isEmpty()
		 */
		public boolean isEmpty() {
			return items.size() == 0;
		}
		
		/**
		 * Returns the maximum capacity of this {@link Box}
		 * @return the max capacity
		 */
		public int capacity() {
			return capacity;
		}
		
		/**
		 * Returns the current size of this {@link Box}
		 * @return the current size
		 * @see PriorityQueue#size()
		 */
		public int size() {
			return items.size();
		}
		
		/**
		 * Offer an item to this {@link Box}.  Throws {@link IllegalStateException} if
		 * this {@link Box} is full.
		 * @param item The item to put in the {@link Box}
		 * @see PriorityQueue#offer(Object)
		 */
		public void offer(T item) {
			if(isFull())
				throw new IllegalStateException("Box " + items + " already full, cannot add " + item);
			items.add(item);
		}
		
		/**
		 * Poll an item from this {@link Box}.  Throws {@link IllegalStateException} if
		 * this {@link Box} is empty.
		 * @return The item removed from the {@link Box}
		 * @see PriorityQueue#poll()
		 */
		public T poll() {
			if(isEmpty())
				throw new IllegalStateException("Box empty, cannot take item");
			return items.poll();
		}
		
		/**
		 * Peek at the head item from this {@link Box}.  Throws {@link IllegalStateException}
		 * if this {@link Box} is empty.
		 * @return The next item that would be removed by a call to {@link #poll()}
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
	 * Create an {@link AbstractOrderedIterable} given a {@link Comparator}.
	 * @param cmp The {@link Comparator} for ordering
	 */
	public AbstractOrderedIterable(Comparator<? super T> cmp) {
		if(cmp == null)
			throw new IllegalArgumentException();
		this.cmp = cmp;
	}
	
	/**
	 * Returns the {@link Comparator} for ordering
	 * @return The {@link Comparator} that orders this {@link Iterable}
	 */
	protected Comparator<? super T> cmp() {
		return cmp;
	}
	
	@Override
	public abstract String toString();
}
