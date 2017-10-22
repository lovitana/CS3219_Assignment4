package main;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public interface Finalizer<T> {

	void out(T val);

	/*
	 * PRECONSTRUCTED INTERFACE
	 */
	public static Finalizer<Map<String, Integer>> printTop(int n) {
		return count -> {
			Comparator<Entry<String, Integer>> comp = (e1, e2) -> Integer.compare(e1.getValue(), e2.getValue());
			Comparator<Entry<String, Integer>> comp2 = comp.reversed()
					.thenComparing((e1, e2) -> e1.getKey().compareTo(e2.getKey()));
			TreeSet<Entry<String, Integer>> top = new TreeSet<>(comp2);
			top.addAll(count.entrySet());
			int c = 1;
			for (Entry<String, Integer> ent : top) {
				if (c > n) {
					return;
				}
				System.out.println(ent.getKey() + "," + ent.getValue());
				c++;
			}
		};
	}

	public static Finalizer<Map<String, Integer>> printAll() {
		return count -> {
			TreeSet<Entry<String, Integer>> top = new TreeSet<>((e1, e2) -> e1.getKey().compareTo(e2.getKey()));
			top.addAll(count.entrySet());
			for (Entry<String, Integer> ent : top) {
				System.out.println(ent.getKey() + "," + ent.getValue());
			}
		};
	}
	
	public static <T>Finalizer<T>doNothing(){
		return o->{};
	}
	
	public static Finalizer<Map<String,Pair<Map<String,List<String>>,List<String>>>> graphConstructor(int n,String root){
		return map ->{
			String trueRoot= null;
			for(Entry<String,Pair<Map<String,List<String>>,List<String>>> e: map.entrySet()){
				if(e.getValue().el1.get("title").get(0).equals(root)){
					trueRoot = e.getKey();
				}
			}
			if(trueRoot==null){
				throw new IllegalArgumentException("wrong root name");
			}
			Set<String> last = new HashSet<>();
			last.add(trueRoot);
			Set<String> see = new HashSet<>();
			Set<String> next = new HashSet<>();
			Map<String,List<String>> graph = new HashMap<>();
			
			for(int i = 0; i<n;i++){
				for(String s : last){
					if(!see.contains(s) && map.containsKey(s)){
						List<String> newEl2 = new LinkedList<>();
						for(String leaf: map.get(s).el2){
							if(map.containsKey(leaf)){
								newEl2.add(leaf);
							}
						}
						graph.put(s, newEl2);
						see.add(s);
						next.addAll(newEl2);
					}
				}
				last = next;
				next = new HashSet<>();
			}
			System.out.println("title,targets,authors");
			for(Entry<String,List<String>> e:graph.entrySet()){
				StringBuilder output = new StringBuilder();
				String src = map.get(e.getKey()).el1.get("title").get(0).replace(',',' ');
				for(String id: e.getValue()){
					output.append(src);
					output.append(',');	
					output.append(map.get(id).el1.get("title").get(0).replace(',',' '));
					boolean start = false;
					output.append(",\"");
					for(String author:map.get(e.getKey()).el1.get("name")){
						if(start){
							output.append(',');
						}
						start = true;
						output.append(author.replace(',',' '));
					}
					output.append("\"");
					start = false;
					output.append(",\"");
					for(String author:map.get(id).el1.get("name")){
						if(start){
							output.append(',');
						}
						start = true;
						output.append(author.replace(',',' '));
					}
					output.append("\"");
					System.out.println(output.toString());
					output = new StringBuilder();
				}
			}
		};
	}
	
	public static Finalizer<Map<String,Pair<Map<String,List<String>>,List<String>>>> graphConstructorEdges(int n,String root){
		return map ->{
			String trueRoot= null;
			for(Entry<String,Pair<Map<String,List<String>>,List<String>>> e: map.entrySet()){
				if(e.getValue().el1.get("title").get(0).equals(root)){
					trueRoot = e.getKey();
				}
			}
			if(trueRoot==null){
				throw new IllegalArgumentException("wrong root name");
			}
			Set<String> last = new HashSet<>();
			last.add(trueRoot);
			Set<String> see = new HashSet<>();
			Set<String> next = new HashSet<>();
			Map<String,List<String>> graph = new HashMap<>();
			
			for(int i = 0; i<n;i++){
				for(String s : last){
					if(!see.contains(s) && map.containsKey(s)){
						List<String> newEl2 = new LinkedList<>();
						for(String leaf: map.get(s).el2){
							if(map.containsKey(leaf)){
								newEl2.add(leaf);
							}
						}
						graph.put(s, newEl2);
						see.add(s);
						next.addAll(newEl2);
					}
				}
				last = next;
				next = new HashSet<>();
			}
			System.out.println("source,target");
			for(Entry<String,List<String>> e:graph.entrySet()){
				StringBuilder output = new StringBuilder();
				String src = map.get(e.getKey()).el1.get("title").get(0).replace(',',' ');
				for(String id: e.getValue()){
					output.append(src);
					output.append(',');	
					output.append(map.get(id).el1.get("title").get(0).replace(',',' '));
					System.out.println(output.toString());
					output = new StringBuilder();
				}
				
				/*
				start = false;
				for(String author:map.get(e.getKey()).el1.get("name")){
					if(start){
						output.append(',');
					}
					start = true;
					output.append(author.replace(',',' '));
				}
				output.append("\"");
				System.out.println(output.toString());
				*/
			}
		};
	}
	
	public static Finalizer<Map<String,Pair<Map<String,List<String>>,List<String>>>> graphConstructorNodes(int n,String root){
		return map ->{
			String trueRoot= null;
			for(Entry<String,Pair<Map<String,List<String>>,List<String>>> e: map.entrySet()){
				if(e.getValue().el1.get("title").get(0).equals(root)){
					trueRoot = e.getKey();
				}
			}
			if(trueRoot==null){
				throw new IllegalArgumentException("wrong root name");
			}
			Set<String> last = new HashSet<>();
			last.add(trueRoot);
			Set<String> see = new HashSet<>();
			Set<String> next = new HashSet<>();
			Map<String,List<String>> graph = new HashMap<>();
			
			for(int i = 0; i<n;i++){
				for(String s : last){
					if(!see.contains(s) && map.containsKey(s)){
						List<String> newEl2 = new LinkedList<>();
						for(String leaf: map.get(s).el2){
							if(map.containsKey(leaf)){
								newEl2.add(leaf);
							}
						}
						graph.put(s, newEl2);
						see.add(s);
						next.addAll(newEl2);
					}
				}
				last = next;
				next = new HashSet<>();
			}
			System.out.println("node,authors");
			see.addAll(last);
			for(String node:see){
				StringBuilder output = new StringBuilder();
				String src = map.get(node).el1.get("title").get(0).replace(',',' ');
				boolean start = false;
				output.append(src);
				output.append(",\"");
				for(String author:map.get(node).el1.get("name")){
					if(start){
						output.append(',');
					}
					start = true;
					output.append(author.replace(',',' '));
				}
				output.append("\"");
				System.out.println(output.toString());
				
			}
		};
	}

	/*
	 * Combiner / Modifier
	 */
	public default Finalizer<T> addAtt(Collection<String> l) {
		return val -> {
			StringBuilder output = new StringBuilder();
			for (String s : l) {
				output.append(s);
				output.append(',');
			}
			output.deleteCharAt(output.length() - 1);
			System.out.println(output.toString());
			out(val);
		};
	}

}
