# Scanerator

Library for manipulating `Iterable` objects that iterate over their
elements in ascending order, according to some `Comparator`.
Provides interface [`OrderedIterable`](scanerator/src/main/java/org/scanerator/OrderedIterable.java)
to be implemented by these ascending-order `Iterable`s, as well as utility functions and
class implementations of common operations which can be performed
on ordered `Iterable`s.

	<dependency>
		<groupId>org.scanerator</groupId>
		<artifactId>scanerator</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>

## Usage

The utility class [`Scanerator`](scanerator/src/main/java/org/scanerator/Scanerator.java) can be used for most
operations on `OrderedIterable` instances, as well as for wrapping
existing `Iterable` instances.

	List<Integer> mul2 = Arrays.asList(2, 4, 6, 8, 10);
	List<Integer> mul3 = Arrays.asList(3, 6, 9);
	
	// Create OrderedIterable's from the Iterable's
	OrderedIterable<Integer> ord2 = Scanerator.itr(mul2);
	OrderedIterable<Integer> ord3 = Scanerator.itr(mul3);
	
	// Get an OrderedIterable that is the intersection of ord2 and ord3
	OrderedIterable<Integer> ord6 = Scanerator.all(Arrays.asList(ord2, ord3));
	
	// Equivalent to...
	ord6 = ord2.and(ord3);
	
	// Convert back to a list
	List<Integer> mul6 = Scanerator.list(ord6); // List contains only '6'

## Laziness

Because `OrderedIterable` always iterates over its elements in
ascending order, boolean operations can be performed on `OrderedIterable`
instances in a lazy manner, e.g. without having to load the
entire list of elements into memory.  This makes `OrderedIterable`
especially useful for "Big Data" applications, such as processing
results returned by HBase scans, which return rows in ascending
order by row-key.  Data can be processed using boolean expressions
implemented using **Scanerator** and sent back to the client
incrementally.

## OrderedIterable Interface

The [`OrderedIterable`](scanerator/src/main/java/org/scanerator/OrderedIterable.java) 
interface, with comments removed, is reproduced below:

	public interface OrderedIterable<T> extends Iterable<T> {
		public Comparator<T> cmp();
		public OrderedIterable<T> or(OrderedIterable<T> i);
		public OrderedIterable<T> and(OrderedIterable<T> i);
		public OrderedIterable<T> not(OrderedIterable<T> i);
		public OrderedIterable<T> dedup();
	}

The methods are briefly described below:

*	`cmp()`
	
	Returns the `Comparator` used by this `OrderedIterable`.


*	`or(OrderedIterable)`
	
	Returns a new `OrderedIterable` that is the union of this `OrderedIterable` and the argument.


*	`and(OrderedIterable)`
	
	Returns a new `OrderedIterable` that is the intersection of this `OrderedIterable` and the argument.


*	`not(OrderedIterable)`
	
	Returns a new `OrderedIterable` that is the subtraction of the argument from this `OrderedIterable`.


*	`dedup()`
	
	Returns a new `OrderedIterable` that de-duplicates this `OrderedIterable`, so equal elements
	are returned only once.

