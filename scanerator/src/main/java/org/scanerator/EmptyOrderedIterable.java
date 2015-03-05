package org.scanerator;

import java.util.Collections;
import java.util.Iterator;

/**
 * {@link OrderedIterable} that returns an empty {@link Iterator} from {@link #iterator()}
 * @author robin
 *
 * @param <T>
 */
public class EmptyOrderedIterable<T> extends AbstractOrderedIterable<T> {

	/**
	 * Create an empty {@link OrderedIterable}
	 */
	public EmptyOrderedIterable() {
		super(null);
	}

	public Iterator<T> iterator() {
		return Collections.<T>emptySet().iterator();
	}
}
