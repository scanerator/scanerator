package org.scanerator;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.scanerator.list.Lists;

import static org.scanerator.Scanerator.*;

public class CheckedIterableTest {
	@Test
	public void testDropDescending() {
		Iterable<Integer> mul2 = new CheckedIterable<Integer>(
				Comparators.naturalOrder(),
				Arrays.asList(2, 4, 0, 6, 8),
				true);
		Assert.assertEquals(Arrays.asList(2, 4, 6, 8), Lists.toList(mul2));
	}
	
	@Test
	public void testFailDescending() { 
		Iterable<Integer> mul2 = new CheckedIterable<Integer>(
				Comparators.naturalOrder(),
				Arrays.asList(2, 4, 0, 6, 8),
				false);
		try {
			Lists.toList(mul2).size();
			Assert.fail();
		} catch(IllegalStateException e) {
			// expected
		}
	}
}
