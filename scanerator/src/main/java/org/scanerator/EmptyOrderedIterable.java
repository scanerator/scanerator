package org.scanerator;

import java.util.Collections;
import java.util.Iterator;

public class EmptyOrderedIterable<T> extends AbstractOrderedIterable<T> {

	public EmptyOrderedIterable() {
		super(null);
	}

	public Iterator<T> iterator() {
		return Collections.<T>emptySet().iterator();
	}
}
