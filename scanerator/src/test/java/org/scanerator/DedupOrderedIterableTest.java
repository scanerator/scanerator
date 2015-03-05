package org.scanerator;

import static org.scanerator.Scanerator.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class DedupOrderedIterableTest {
	@Test
	public void testDuplicates() {
		OrderedIterable<Integer> mul2 = itr(Arrays.asList(2, 2, 4, 4, 6, 8, 10, 10, 10));
		Assert.assertEquals(
				Arrays.asList(2, 4, 6, 8, 10),
				list(new DedupOrderedIterable<Integer>(mul2)));
	}
}
