[*Issue Tracker*](http://jira.robindps.com/browse/SCAN/?selectedTab=com.atlassian.jira.jira-projects-plugin:summary-panel)

# Scanerator
Library for manipulating `Iterable` objects that iterate over their
elements in ascending order, according to some `Comparator`.
Provides utility functions and
class implementations of common operations which can be performed
on ordered `Iterable`s.

	<dependency>
		<groupId>org.scanerator</groupId>
		<artifactId>scanerator</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>

**Scanerator** is especially useful for "Big Data" applications where
the result of a query will not fit in available memory, provided the
results are returned in a consistent ordering.  [HBase](http://hbase.apache.org/) 
scans are one example; because they are always returned ordered by row key, HBase
scan results can be easily processed by **Scanerator** to produce
boolean expressions composed of multiple scans.

**Scanerator** has no runtime dependencies.

## Usage
The utility class [`Scanerator`](scanerator/src/main/java/org/scanerator/Scanerator.java) can be used for most
operations on `Iterable` instances, as well as for wrapping
existing `Iterable` instances.

Basic Usage:

	List<Integer> mul2 = Arrays.asList(2, 4, 6, 8, 10);
	List<Integer> mul3 = Arrays.asList(3, 6, 9);
	
	// Get an Iterable that is the intersection of mul2 and mul3
	Iterable<Integer> ord6 = Scanerator.all(ord2, ord3);
	
	// Convert back to a list
	List<Integer> mul6 = Lists.toList(ord6); // List contains only '6'

A more practical example uses [HBase](http://hbase.apache.org/).  The following
snippet finds all row keys that have a first name and a last name, but do
not have an address:

	/*
	 * Locate all rows that have a first name and a last name but no address.
	 */
	
	HTable table = ...;
	
	// Create Scan objects for first name, last name, and address
	Scan firstNamesScan = new Scan().addColumn(Bytes.toBytes("person"), Bytes.toBytes("first-name"));
	Scan lastNamesScan = new Scan().addColumn(Bytes.toBytes("person"), Bytes.toBytes("last-name"));
	Scan addressesScan = new Scan().addColumn(Bytes.toBytes("person"), Bytes.toBytes("address"));
	
	// Need a comparator for Result objects
	Comparator<Result> rowOrder = new Comparator<Result>() {
		@Override
		public void compare(Result r1, Result r2) {
			return Bytes.BYTES_COMPARATOR.compare(r1.getRow(), r2.getRow());
		}
	};
	
	// Get the ResultScanners and convert them to unchecked OrderedIterables.
	// Unchecked is okay because ResultScanner always returns its elements in
	// row-key order, consistent with the above comparator.
	Iterable<Result> firstNames = table.getScanner(firstNamesScan);
	Iterable<Result> lastNames = table.getScanner(lastNamesScan);
	Iterable<Result> addresses = table.getScanner(addressesScan;
	
	// Perform the boolean operation on the Iterables
	Iterable<Result> named = Scanerator.all(rowOrder, firstNames, lastNames);
	Iterable<Result> missingAddress = Scanerator.not(rowOrder, named, addresses);

This snippet, also using [HBase](http://hbase.apache.org/), demonstrates a solution
to the "pick 2 of 3" problem using [Expression](scanerator/src/main/java/org/scanerator/Expression.java)
objects, which themselves implement `Iterable`.

	/*
	 * Locate all rows that are combinations of fast, easy, and accurate
	 */
	
	HTable table = ...;
	
	// Create Scan objects for first name, last name, and address
	Scan fastScan = new Scan().addColumn(Bytes.toBytes("task"), Bytes.toBytes("fast"));
	Scan easyScan = new Scan().addColumn(Bytes.toBytes("task"), Bytes.toBytes("easy"));
	Scan accurateScan = new Scan().addColumn(Bytes.toBytes("task"), Bytes.toBytes("accurate"));
	
	// Need a comparator for Result objects
	Comparator<Result> rowOrder = new Comparator<Result>() {
		@Override
		public void compare(Result r1, Result r2) {
			return Bytes.BYTES_COMPARATOR.compare(r1.getRow(), r2.getRow());
		}
	};
	
	// Get the ResultScanners and convert them to unchecked OrderedIterables.
	// Unchecked is okay because ResultScanner always returns its elements in
	// row-key order, consistent with the above comparator.
	Iterable<Result> fast = table.getScanner(firstNamesScan);
	Iterable<Result> easy = table.getScanner(lastNamesScan);
	Iterable<Result> accurate = table.getScanner(addressesScan;
	
	// Perform the boolean operations with Expression objects
	ExpressionRoot root = Scanerator.with(rowOrder);
	
	Expression<Result> pick3 = root.express(fast).and(easy).and(accurate);
	
	Expression<Result> pick2 =
		root.express(
			root.express(fast).and(easy).not(accurate)
		).or(
			root.express(easy).and(accurate).not(fast)
		).or(
			root.express(accurate).and(fast).not(easy)
		);
	
	Expression<Result> pick1 = 
		root.express(
			root.express(fast).or(easy).or(accurate)
		).not(
			pick2.or(pick3)
		);

## Maven Repository
**Scanerator** is not yet on maven central, so a `<repository>` element is
required to depend on it.  The following snippet may be used:

	<repository>
		<id>robin-kirkman-nexus</id>
		<url>http://nexus.robindps.com/nexus/content/groups/local/</url>
		<snapshots><enabled>true</enabled></snapshots>
		<releases><enabled>true</enabled></releases>
	</repository>

Best practices for Maven are to include `<repository>` elements as profiles
in your local `settings.xml`, and **not** to include them in your `pom.xml`.

## Laziness
Because an ordered `Iterable` always iterates over its elements in
ascending order, boolean operations can be performed on these
instances in a lazy manner, e.g. without having to load the
entire list of elements into memory.  This makes ordered `Iterable`s
especially useful for "Big Data" applications, such as processing
results returned by [HBase](http://hbase.apache.org/) scans, which 
return rows in ascending order by row-key.  Data can be processed using
boolean expressions implemented using **Scanerator** and sent back to the client
incrementally.

## Development
**Scanerator** is developed on an open-source-licensed [Atlassian](https://www.atlassian.com/)
stack hosted on my own server.  (The [stash repository](http://stash.robindps.com/projects/SCAN/repos/scanerator/browse)
is mirrored to [Github](https://github.com/scanerator/scanerator).)

Continuous integration is performed by [my Bamboo instance](http://bamboo.robindps.com/browse/SCAN).

Bug reports, feature requests, and any other issues should be submitted
to [my JIRA instance](http://jira.robindps.com/browse/SCAN/?selectedTab=com.atlassian.jira.jira-projects-plugin:summary-panel).
