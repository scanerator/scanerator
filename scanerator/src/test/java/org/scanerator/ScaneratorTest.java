package org.scanerator;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import static org.scanerator.Scanerator.*;

@SuppressWarnings("unchecked")
public class ScaneratorTest {
	private static final OrderedIterable<Integer> mul2 = itr(Arrays.asList(2, 4, 6, 8, 10, 12, 14));
	private static final OrderedIterable<Integer> mul3 = itr(Arrays.asList(3, 6, 9, 12, 15));
	private static final OrderedIterable<Integer> mul4 = itr(Arrays.asList(4, 8, 12));

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
		
		Assert.assertEquals(Arrays.asList(12), list(all(Arrays.asList(mul2, mul3, mul4))));
	}
	
	@Test
	public void testAny() {
		Assert.assertEquals(Arrays.asList(2,3,4,6,6,8,9,10,12,12,14, 15), list(any(Arrays.asList(mul2, mul3))));
	}
	
	@Test
	public void testNot() {
		Assert.assertEquals(Arrays.asList(2, 4, 8, 10, 14), list(mul2.not(mul3)));
	}
	
	@Test
	public void testStrings() {
		OrderedIterable<Object> all = all(Arrays.asList(empty(), empty(), empty(), empty()));
		Assert.assertEquals("(all (all (empty) (empty)) (all (empty) (empty)))", all.toString());
	}
}
