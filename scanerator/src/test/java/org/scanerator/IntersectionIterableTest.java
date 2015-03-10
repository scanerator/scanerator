package org.scanerator;

import static org.scanerator.Scanerator.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.scanerator.list.Lists;

public class IntersectionIterableTest {
	@Test
	public void testDuplication() {
		Iterable<Integer> left = checked(Arrays.asList(1, 2, 3, 3, 4, 5));
		Iterable<Integer> right = checked(Arrays.asList(2, 3, 3, 3, 4));
		Assert.assertEquals(
				Arrays.asList(2, 3, 3, 4),
				Lists.toList(new IntersectionIterable<Integer>(left, right)));
	}
}
