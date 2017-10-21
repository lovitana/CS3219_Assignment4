
package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

/**
 * ARGS Q1: top name venue arXiv 10 findWithMostAttr("name", "venue", "arXiv",
 * 10);
 * 
 * Q2: top title venue arXiv 5 inCitations
 * 
 * Q3: count year venue ICSE id
 * 
 * Q4: graph
 * the result is:
 * 
 * nodeSource, target1, target2, ...
 * author1,author2,...
 * nodeSource,...
 * author...
 * ...
 * (will be improved later)
 */
public class App {

	private static JsonParser parser;
	private static BufferedReader reader;

	public static void main(String[] args) {
		try (BufferedReader input = new BufferedReader(new FileReader("src/resources/data.json"));) {
			reader = input;
			if (args.length == 0) {
				throw new IllegalArgumentException("Wrong number of arguments");
			}
			// TODO switch arg[0] to call the right function with arguments
			// arg[1..n] (and maybe a file.csv)
			switch (args[0]) {
			case "top":
				if (args.length == 5) {
					List<Pair<String, Integer>> l = findWithMostAttr(args[1], args[2], args[3],
							Integer.parseInt(args[4]));
					System.out.println(args[1] + "," + "count");
					for (Pair<String, Integer> p : l) {
						System.out.println(p.el1 + "," + p.el2);
					}
				}
				if (args.length == 6) {
					List<Pair<String, Integer>> l = findTop(args[1], args[2], args[3], args[5],
							Integer.parseInt(args[4]));
					System.out.println(args[1] + "," + "count");
					for (Pair<String, Integer> p : l) {
						System.out.println(p.el1 + "," + p.el2);
					}
				}
				break;
			case "count":
				findGeneric(new HashMap<String, Integer>(), Combinator.count(args[1], args[4]),
						Filter.contain(args[2], args[3]),
						Finalizer.printAll().addAtt(Arrays.asList(args[1], "nbPublications")));
				break;
			case "graph":
				findGeneric(new HashMap<>(),
						Combinator.graph(true,Arrays.asList("title","name")),
						Filter.all(),
						Finalizer.graphConstructor(4,"Low-density parity check codes over GF(q)")
						);
				break;
			case "test":
				List<String> l = new ArrayList<>();
				l.add("id");
				l.add("title");
				findGeneric(new HashMap<String,List<String>>()
						, (map,o)->{map.put(o.get("id").get(0),o.get("title"));return map;}
						, Filter.contain("title", "A kernel"),
						(map)->{
							for(Entry<String,List<String>> e:map.entrySet()){
								System.out.println(e);
							}
						});
			}

			parser.close();
		} catch (FileNotFoundException e) {
			parser.close();
			e.printStackTrace();
		} catch (IOException e) {
			parser.close();
			e.printStackTrace();
		}
	}

	public static List<Pair<String, Integer>> findWithMostAttr(String ref, String att, String value, int n) throws IOException {
		return findTop(ref, att, value, "id", n);
	}

	public static List<Pair<String, Integer>> findTop(String ref, String conditionAtt, String value, String countAtt,
			int n) throws IOException {
		Map<String, Integer> count = new HashMap<>();
		Map<String, List<String>> object;
		while ((object = nextPublication()) != null) {
			boolean check = false;
			for (String attValue : object.getOrDefault(conditionAtt, Collections.emptyList())) {
				if (attValue.contains(value)) {
					check = true;
				}
			}
			if (check) {
				for (String refValue : object.getOrDefault(ref, Collections.emptyList())) {
					count.put(refValue, count.getOrDefault(refValue, 0)
							+ object.getOrDefault(countAtt, Collections.emptyList()).size());
				}
			}

		}
		Comparator<Entry<String, Integer>> comp = (e1, e2) -> Integer.compare(e1.getValue(), e2.getValue());
		Comparator<Entry<String, Integer>> comp2 = comp.reversed()
				.thenComparing((e1, e2) -> e1.getKey().compareTo(e2.getKey()));
		TreeSet<Entry<String, Integer>> top = new TreeSet<>(comp2);
		top.addAll(count.entrySet());
		List<Pair<String, Integer>> result = new ArrayList<>();
		int c = 1;
		for (Entry<String, Integer> ent : top) {
			if (c > n) {
				return result;
			}
			result.add(new Pair<>(ent.getKey(), ent.getValue()));
			c++;
		}

		return result;
	}

	public static <C> void findGeneric(C zero, Combinator<C> comb, Filter filter, Finalizer<C> finalizer) throws IOException {
		C val = zero;
		Map<String, List<String>> object;
		while ((object = nextPublication()) != null) {
			if (filter.check(object)) {
				val = comb.combine(val, object);
			}
		}
		finalizer.out(val);
	}

	private static Map<String, List<String>> nextPublication() throws IOException {
		
		String line = reader.readLine();
		
		if(line == null){
			return null;
		}
		parser =  Json.createParser(new StringReader(line));
		while (parser.hasNext() && parser.next() != Event.START_OBJECT) {
		}
		if (!parser.hasNext()) {
			return Collections.emptyMap();
		}
		Map<String, List<String>> object = new HashMap<>();
		while (parser.hasNext()) {
			Event e = parser.next();
			if (e == Event.KEY_NAME) {
				String att = parser.getString();
				switch (att) {
				case "id":
				case "title":
				case "paperAbstract":
				case "year":
				case "s2Url":
				case "venue":
					parser.next();
					object.put(att, Collections.singletonList(parser.getString()));
					break;
				case "keyPhrases":
				case "inCitations":
				case "outCitations":
					while (parser.hasNext() && parser.next() != Event.START_ARRAY) {
					}
					List<String> list = new ArrayList<>();
					while (parser.hasNext() && parser.next() != Event.END_ARRAY) {
						list.add(parser.getString());
					}
					object.put(att, list);
					break;

				case "authors":
					while (parser.hasNext() && parser.next() != Event.START_ARRAY) {
					}
					List<String> ids = new ArrayList<>();
					List<String> name = new ArrayList<>();
					Event ev;
					while (parser.hasNext() && (ev =parser.next()) != Event.END_ARRAY) {
						while (parser.hasNext() && ev != Event.START_OBJECT) {
							ev = parser.next();
						}
						while (parser.hasNext()&& (ev= parser.next()) != Event.END_OBJECT) {
							if (ev == Event.KEY_NAME) {
								switch (parser.getString()) {
								case "ids":
									int prevSize = ids.size();
									while (parser.hasNext() && parser.next() != Event.START_ARRAY) {
									}
									while (parser.hasNext() && parser.next() != Event.END_ARRAY) {
										ids.add(parser.getString());
									}
									if (ids.size() == prevSize) {
										ids.add(null);
									}
									break;
								case "name":
									parser.next();
									name.add(parser.getString());
								}
							}
						}
					}
					object.put("ids", ids);
					object.put("name", name);
				}
			}
		}
		return object;

	}
}