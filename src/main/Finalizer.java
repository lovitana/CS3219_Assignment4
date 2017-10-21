package main;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

public interface Finalizer<T> {
	
	void out(T val);
	
	/*
	 * PRECONSTRUCTED INTERFACE
	 */
	public static Finalizer<Map<String,Integer>> printTop(int n){
		return count ->{
			Comparator<Entry<String,Integer>> comp = (e1,e2)-> Integer.compare(e1.getValue(), e2.getValue());
			Comparator<Entry<String,Integer>> comp2 = comp.reversed().thenComparing((e1,e2)->e1.getKey().compareTo(e2.getKey()));
			TreeSet<Entry<String, Integer>> top = new TreeSet<>(comp2);
			top.addAll(count.entrySet());
			int c = 1;
			for (Entry<String, Integer> ent : top) {
				if (c > n) {
					return;
				}
			System.out.println(ent.getKey()+","+ ent.getValue() ) ;
			c++;
			}
		};
	}
	
	/*
	 * Combiner / Modifier
	 */
	public default Finalizer<T> addAtt(Collection<String> l){
		return val -> {
			StringBuilder output = new StringBuilder();
			for(String s:l){
				output.append(s);
				output.append(',');
			}
			output.deleteCharAt(output.length()-1);
			System.out.println(output.toString());
			out(val);
		};
	}

}
