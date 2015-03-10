package org.scanerator;

import java.util.Comparator;
import java.util.Iterator;

/**
 * {@link Iterable} with additional methods for combining (via {@link Scanerator})
 * with other {@link Iterable}s using some {@link Comparator}.  {@link Expression} can
 * be used to build complex boolean expressions from {@link Iterable} data.
 * @author robin
 *
 * @param <T>
 */
public class Expression<T> implements Iterable<T> {
	
	/**
	 * Return a new {@link ExpressionRoot} for beginning {@link Expression} sequences,
	 * using the given {@link Comparator}.
	 * @param cmp The {@link Comparator} for the {@link Expression}
	 * @return A new {@link ExpressionRoot}
	 * @see ExpressionRoot#ExpressionRoot(Comparator)
	 */
	public static <T> ExpressionRoot<T> with(Comparator<? super T> cmp) {
		return new ExpressionRoot<T>(cmp);
	}
	
	/**
	 * Possible types of input validation for {@link Iterable} instances
	 * given as arguments to an {@link Expression}.  Validation means
	 * ensuring that the elements are in ascending order according
	 * to some {@link Comparator}.
	 * @author robin
	 *
	 */
	public static enum OrderChecking {
		/**
		 * No input validation; returns the argument from {@link #check(Comparator, Iterable)}
		 */
		UNCHECKED {
			@Override
			public <T> Iterable<T> check(Comparator<? super T> cmp, Iterable<T> itr) {
				return itr;
			}
		},
		/**
		 * Validate input, dropping out-of-order elements from the iteration
		 */
		CHECKED_DROPPING {
			@Override
			public <T> Iterable<T> check(Comparator<? super T> cmp, Iterable<T> itr) {
				return Scanerator.checked(cmp, itr, true);
			}
		},
		/**
		 * Validate input, throwing an exception if an out-of-order element is encountered
		 */
		CHECKED_THROWING {
			@Override
			public <T> Iterable<T> check(Comparator<? super T> cmp, Iterable<T> itr) {
				return Scanerator.checked(cmp, itr, false);
			}
		}
		;
		
		/**
		 * Return a "validated" version of the argument {@link Iterable}, according
		 * to this {@link OrderChecking} instance.
		 * @param cmp The {@link Comparator} for validation
		 * @param itr The {@link Iterable} to validate
		 * @return A validated {@link Iterable}
		 */
		public abstract <T> Iterable<T> check(Comparator<? super T> cmp, Iterable<T> itr);
	}
	
	/**
	 * The "root" of any {@link Expression} or sub-{@link Expression}.
	 * @author robin
	 *
	 * @param <T>
	 */
	public static class ExpressionRoot<T> implements Comparator<T> {
		/**
		 * The {@link Comparator} for created {@link Expression}s
		 */
		protected Comparator<? super T> cmp;
		/**
		 * The input validation
		 */
		protected OrderChecking ordering;
		
		/**
		 * Create an {@link ExpressionRoot} from a {@link Comparator} with 
		 * {@link OrderChecking#UNCHECKED} validation
		 * @param cmp The {@link Comparator} for the {@link Expression}
		 */
		public ExpressionRoot(Comparator<? super T> cmp) {
			this(cmp, OrderChecking.UNCHECKED);
		}
		
		/**
		 * Create an {@link ExpressionRoot} from a {@link Comparator} with
		 * a specified {@link OrderChecking} validation
		 * @param cmp The {@link Comparator} for the {@link Expression}
		 * @param odering The validation for {@link Iterable} arguments to the {@link Expression}
		 */
		public ExpressionRoot(Comparator<? super T> cmp, OrderChecking odering) {
			if(cmp == null || odering == null)
				throw new IllegalArgumentException();
			this.cmp = cmp;
			this.ordering = odering;
		}
		
		/**
		 * Create and return a new {@link ExpressionRoot} with the same {@link Comparator}
		 * as this one but a different validation type.
		 * @param ordering The desired validation type
		 * @return A new {@link ExpressionRoot}
		 */
		public ExpressionRoot<T> order(OrderChecking ordering) {
			return new ExpressionRoot<T>(cmp, ordering);
		}
		
		/**
		 * Begin an expression, starting with an {@link Iterable} that will be validated
		 * @param itr The {@link Iterable} to begin the expression
		 * @return A new {@link Expression}
		 */
		public Expression<T> express(Iterable<T> itr) {
			return new Expression<T>(cmp, ordering, ordering.check(cmp, itr));
		}

		@Override
		public int compare(T o1, T o2) {
			return cmp.compare(o1, o2);
		}
	}
	
	/**
	 * The {@link Comparator} passed to {@link Scanerator} methods
	 */
	protected Comparator<? super T> cmp;
	/**
	 * The wrapped {@link Iterable}
	 */
	protected Iterable<T> itr;
	/**
	 * The validation type
	 */
	protected OrderChecking ordering;
	
	/**
	 * Create a new {@link Expression} with specified validation, wrapping an {@link Iterable}
	 * @param cmp The {@link Comparator} for use with {@link Scanerator}
	 * @param ordering The validation type
	 * @param itr The {@link Iterable} to wrap
	 */
	public Expression(Comparator<? super T> cmp, OrderChecking ordering, Iterable<T> itr) {
		this.cmp = cmp;
		this.ordering = ordering;
		this.itr = itr;
	}
	
	@Override
	public Iterator<T> iterator() {
		return itr.iterator();
	} 
	
	/**
	 * Wrap an {@link Iterable} as an {@link Expression} using this
	 * object's {@link Comparator} and {@link OrderChecking}
	 * @param itr The {@link Iterable} to wrap
	 * @return A new {@link Expression}
	 */
	protected Expression<T> wrap(Iterable<T> itr) {
		return new Expression<T>(cmp, ordering, itr);
	}
	
	/**
	 * Return the logical intersection of this {@link Iterable} and the argument
	 * @param rhs The right-hand side of the intersection
	 * @return A new {@link Expression}
	 * @see Scanerator#all(Comparator, Iterable, Iterable)
	 */
	public Expression<T> and(Iterable<T> rhs) {
		return wrap(Scanerator.all(cmp, this, ordering.check(cmp, rhs)));
	}
	
	/**
	 * Return the logical union of this {@link Iterable} and the argument
	 * @param rhs The right-hand side of the union
	 * @return A new {@link Expression}
	 * @see Scanerator#any(Comparator, Iterable, Iterable)
	 */
	public Expression<T> or(Iterable<T> rhs) {
		return wrap(Scanerator.any(cmp, this, ordering.check(cmp, rhs)));
	}
	
	/**
	 * Return the logical subtraction of the argument from this {@link Iterable}
	 * @param rhs The right-hand side of the subtraction
	 * @return A new {@link Expression}
	 * @see Scanerator#not(Comparator, Iterable, Iterable)
	 */
	public Expression<T> not(Iterable<T> rhs) {
		return wrap(Scanerator.not(cmp, this, ordering.check(cmp, rhs)));
	}
	
	/**
	 * Return a de-duplicated version of this {@link Iterable}
	 * @return A new {@link Expression}
	 * @see Scanerator#dedup(Comparator, Iterable)
	 */
	public Expression<T> dedup() {
		return wrap(Scanerator.dedup(cmp, this));
	}
}
