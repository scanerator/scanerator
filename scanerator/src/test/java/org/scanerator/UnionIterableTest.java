package org.scanerator;

import static org.scanerator.Scanerator.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class UnionIterableTest {
	@Test
	public void testDuplication() {
		Iterable<Integer> mul2 = checked(Arrays.asList(2, 2, 4, 6));
		Iterable<Integer> mul3 = checked(Arrays.asList(3, 6, 9, 9));
		Assert.assertEquals(
				Arrays.asList(2, 2, 3, 4, 6, 6, 9, 9),
				list(new UnionIterable<Integer>(mul2, mul3)));
	}
}
