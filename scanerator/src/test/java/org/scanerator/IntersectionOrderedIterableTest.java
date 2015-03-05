package org.scanerator;

import static org.scanerator.Scanerator.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class IntersectionOrderedIterableTest {
	@Test
	public void testDuplication() {
		OrderedIterable<Integer> left = checked(Arrays.asList(1, 2, 3, 3, 4, 5));
		OrderedIterable<Integer> right = checked(Arrays.asList(2, 3, 3, 3, 4));
		Assert.assertEquals(
				Arrays.asList(2, 3, 3, 4),
				list(new IntersectionOrderedIterable<Integer>(left, right)));
	}
}
