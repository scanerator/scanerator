package org.scanerator.list;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ForgetfulListTest {
	@Test
	public void testForgetting() {
		List<Integer> list = new ForgetfulList<Integer>(1);
		list.add(0);
		Assert.assertEquals(0, (int) list.get(0));
		list.add(1);
		Assert.assertEquals(1, (int) list.get(1));
		try {
			list.get(0);
			Assert.fail();
		} catch(IllegalStateException e) {
			// expected
		}
		list.set(1, 2);
		Assert.assertEquals(2, (int) list.get(1));
	}
}
