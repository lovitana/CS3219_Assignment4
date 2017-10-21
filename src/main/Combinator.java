package main;

import java.util.Collections;
import java.util.HashMap;
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
	
	public static Combinator< Map<String,Pair<Map<String,List<String>>,List<String>>> > graph(boolean inCitation,List<String> infos) {
		return (map,o)->{
			Map<String,List<String>> info = new HashMap<String,List<String>>();
			for(String s:infos){
				info.put(s,o.get(s));
			}
			map.put(o.get("id").get(0),new Pair<>(info,o.get(inCitation?"inCitations":"outCitations")));
			return map;
		};
	}

}
