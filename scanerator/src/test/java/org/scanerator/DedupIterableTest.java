package org.scanerator;

import static org.scanerator.Scanerator.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.scanerator.list.Lists;

public class DedupIterableTest {
	@Test
	public void testDuplicates() {
		Iterable<Integer> mul2 = checked(Arrays.asList(2, 2, 4, 4, 6, 8, 10, 10, 10));
		Assert.assertEquals(
				Arrays.asList(2, 4, 6, 8, 10),
				Lists.toList(new DedupIterable<Integer>(mul2)));
	}
}
