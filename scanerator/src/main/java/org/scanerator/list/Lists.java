package org.scanerator.list;

import java.util.List;

/**
 * Utility functions for creating and using lazy lists.
 * @author robin
 *
 */
public class Lists {

	/**
	 * Return a {@link List} that populates itself lazily from
	 * the {@link Iterable} argument.  Note that certain {@link List}
	 * methods, such as {@link List#size()}, will force a full
	 * evaluation of {@code itr}.
	 * @param itr The {@link Iterable} to wrap as a {@link List}
	 * @return A new {@link List}
	 * @see IteratorList
	 */
	public static <T> List<T> toList(Iterable<T> itr) {
		return new IteratorList<T>(itr.iterator());
	}
	
	/**
	 * Return a {@link List} that populates itself lazily from
	 * the {@link Iterable} argument.  Note that certain {@link List}
	 * methods, such as {@link List#size()}, will force a full
	 * evaluation of {@code itr}.  The list is forgetful;
	 * only the most recent {@code history} elements will
	 * be retained.
	 * @param itr The {@link Iterable} to wrap as a {@link List}
	 * @param history The number of elements to retain
	 * @return A new {@link List}
	 * @see IteratorList
	 * @see ForgetfulList
	 */
	public static <T> List<T> toList(Iterable<T> itr, int history) {
		return new IteratorList<T>(new ForgetfulList<T>(history), itr.iterator());
	}
	
	private Lists() {}
}
