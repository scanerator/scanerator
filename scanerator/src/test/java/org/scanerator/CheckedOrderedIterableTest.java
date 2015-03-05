package org.scanerator;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import static org.scanerator.Scanerator.*;

public class CheckedOrderedIterableTest {
	@Test
	public void testDropDescending() {
		OrderedIterable<Integer> mul2 = new CheckedOrderedIterable<Integer>(
				Arrays.asList(2, 4, 0, 6, 8),
				Comparators.naturalOrder(),
				true);
		Assert.assertEquals(Arrays.asList(2, 4, 6, 8), list(mul2));
	}
	
	@Test
	public void testFailDescending() {
		OrderedIterable<Integer> mul2 = new CheckedOrderedIterable<Integer>(
				Arrays.asList(2, 4, 0, 6, 8),
				Comparators.naturalOrder(),
				false);
		try {
			list(mul2);
			Assert.fail();
		} catch(IllegalStateException e) {
			// expected
		}
	}
}
