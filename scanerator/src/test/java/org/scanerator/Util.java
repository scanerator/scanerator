package org.scanerator;

import java.util.ArrayList;
import java.util.List;

public class Util {
	public static <T> List<T> list(Iterable<T> itr) {
		List<T> ret = new ArrayList<T>();
		for(T obj : itr)
			ret.add(obj);
		return ret;
	}

	private Util() {}
}
