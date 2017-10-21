package main;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface Combinator<T> {

	T combine(T val, Map<String, List<String>> object);

	public static Combinator<Map<String, Integer>> count(String ref, String countAtt) {
		return (map, object) -> {
			for (String refValue : object.getOrDefault(ref, Collections.emptyList())) {
				map.put(refValue,
						map.getOrDefault(refValue, 0) + object.getOrDefault(countAtt, Collections.emptyList()).size());
			}
			return map;
		};
	}

}
