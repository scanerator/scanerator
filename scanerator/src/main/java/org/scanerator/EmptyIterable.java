package org.scanerator;

import java.util.Collections;
import java.util.Iterator;

/**
 * {@link Iterable} that returns an empty {@link Iterator} from {@link #iterator()}
 * @author robin
 *
 * @param <T>
 */
public class EmptyIterable<T> extends AbstractIterable<T> {

	/**
	 * Create an empty {@link Iterable}
	 */
	public EmptyIterable() {
		super(null);
	}

	public Iterator<T> iterator() {
		return Collections.<T>emptySet().iterator();
	}
	
	@Override
	public String toString() {
		return "(empty)";
	}
}
