package org.scanerator;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.scanerator.list.Lists;

import static org.scanerator.Scanerator.*;

@SuppressWarnings("unchecked")
public class ScaneratorTest {
	private static final Iterable<Integer> mul2 = checked(Arrays.asList(2, 4, 6, 8, 10, 12, 14));
	private static final Iterable<Integer> mul3 = checked(Arrays.asList(3, 6, 9, 12, 15));
	private static final Iterable<Integer> mul4 = checked(Arrays.asList(4, 8, 12));

	@Test
	public void testChecked() {
		try {
			Lists.toList(checked(Arrays.asList(3, 2, 1))).size();
			Assert.fail();
		} catch(IllegalStateException e) {
			// expected
		}
	}
	
	@Test
	public void testAll() {
		Assert.assertEquals(Arrays.asList(6, 12), Lists.toList(all(Arrays.asList(mul2, mul3))));
		
		Assert.assertEquals(Lists.toList(mul2), Lists.toList(all(Arrays.asList(mul2, mul2, mul2, mul2, mul2))));
		
		Assert.assertEquals(Arrays.asList(12), Lists.toList(all(Arrays.asList(mul2, mul3, mul4))));
	}
	
	@Test
	public void testAny() {
		Assert.assertEquals(Arrays.asList(2,3,4,6,6,8,9,10,12,12,14, 15), Lists.toList(any(Arrays.asList(mul2, mul3))));
	}
	
	@Test
	public void testNot() {
		Assert.assertEquals(Arrays.asList(2, 4, 8, 10, 14), Lists.toList(not(mul2, mul3)));
	}
	
	@Test
	public void testStrings() {
		Iterable<Object> all = all(Arrays.asList(empty(), empty(), empty(), empty()));
		Assert.assertEquals("(all (all [] []) (all [] []))", all.toString());
	}
}
