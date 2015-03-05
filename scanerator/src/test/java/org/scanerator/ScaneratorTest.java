package org.scanerator;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import static org.scanerator.Scanerator.*;

@SuppressWarnings("unchecked")
public class ScaneratorTest {
	private static final OrderedIterable<Integer> mul2 = itr(Arrays.asList(2, 4, 6, 8, 10, 12));
	private static final OrderedIterable<Integer> mul3 = itr(Arrays.asList(3, 6, 9, 12));

	@Test
	public void testItr() {
		try {
			list(itr(Arrays.asList(3, 2, 1)));
			Assert.fail();
		} catch(IllegalStateException e) {
			// expected
		}
	}
	
	@Test
	public void testAll() {
		Assert.assertEquals(Arrays.asList(6, 12), list(all(Arrays.asList(mul2, mul3))));
		
		Assert.assertEquals(list(mul2), list(all(Arrays.asList(mul2, mul2, mul2, mul2, mul2))));
	}
	
	@Test
	public void testAny() {
		Assert.assertEquals(Arrays.asList(2,3,4,6,8,9,10,12), list(any(Arrays.asList(mul2, mul3))));
		
		Assert.assertEquals(list(mul2), list(any(Arrays.asList(mul2, mul2, mul2, mul2, mul2))));
	}
	
	@Test
	public void testNot() {
		Assert.assertEquals(Arrays.asList(2, 4, 8, 10), list(mul2.not(mul3)));
	}
}
