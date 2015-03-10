package org.scanerator.list;

import java.util.AbstractList;
import java.util.ArrayDeque;
import java.util.Deque;

public class ForgetfulList<E> extends AbstractList<E> {
	
	protected class Memory {
		public int index;
		public E value;
		
		public Memory(int index) {
			this(index, null);
		}
		
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

	protected int size;
	protected int capacity;
	protected Deque<Memory> memory;
	
	public ForgetfulList(int capacity) {
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
