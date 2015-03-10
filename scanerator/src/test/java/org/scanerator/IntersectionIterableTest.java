package org.scanerator;

import static org.scanerator.Scanerator.*;
import static org.scanerator.Util.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class IntersectionIterableTest {
	@Test
	public void testDuplication() {
		Iterable<Integer> left = checked(Arrays.asList(1, 2, 3, 3, 4, 5));
		Iterable<Integer> right = checked(Arrays.asList(2, 3, 3, 3, 4));
		Assert.assertEquals(
				Arrays.asList(2, 3, 3, 4),
				list(new IntersectionIterable<Integer>(left, right)));
	}
}
