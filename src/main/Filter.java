package main;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface Filter {
	boolean check(Map<String, List<String>> object);

	
	/*
	 * STATIC CONSTRUCTOR
	 */
	public static Filter contain(String att, String value) {
		return object -> {
			for (String attValue : object.getOrDefault(att, Collections.emptyList())) {
				if (attValue.contains(value)) {
					return true;
				}
			}
			return false;
		};
	}

	public static Filter all(){
		return o->true;
	}
	
	
	/*
	 * Modifier
	 */
	public default Filter and(Filter f){
		return o->  check(o) && f.check(o);
	}
	
	public default Filter or(Filter f){
		return o->  check(o) || f.check(o);
	}
}
