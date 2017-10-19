
package main;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class App {

	private static JsonParser parser;

	public static void main(String[] args) {
		try (InputStream input = new FileInputStream("src/resources/extractedData.json")) {
			parser = Json.createParser(input);
			// TODO switch arg[0] to call the right function with arguments
			// arg[1..n] (and maybe a file.csv)
			// TODO remove
			System.out.println("Q1:");
			List<Pair<String, Integer>> l = findWithMostAttr("author", "venue", "arXiv", 10);
			System.out.println(l.toString());
			parser.close();
		} catch (FileNotFoundException e) {
			parser.close();
			e.printStackTrace();
		} catch (IOException e) {
			parser.close();
			e.printStackTrace();
		}
	}

	public static List<Pair<String, Integer>> findWithMostAttr(String ref, String att, String value, int n) {
		Map<String, Integer> count = new HashMap<>();
		Map<String,List<String>> object;
		while ((object = nextPublication()) != null) {
			if(object.getOrDefault(att,Collections.emptyList()).contains(value)){
				for(String refValue:object.getOrDefault(ref, Collections.emptyList())){
					count.put(refValue, count.getOrDefault(refValue, 0)+1);
				}
			}
			
		}
		TreeSet<Entry<String, Integer>> top = new TreeSet<>((e1, e2) -> {
			return Integer.compare(e1.getValue(), e2.getValue());
		});
		List<Pair<String, Integer>> result = new ArrayList<>();
		int c = 0;
		for (Entry<String, Integer> ent : top) {
			if (c > n) {
				return result;
			}
			result.add(new Pair<>(ent.getKey(), ent.getValue()));
			c++;
		}

		return result;
	}

	/*
	 * private static List<String> getAttributes(JsonObject publication, String
	 * att) { try { List<String> values = new ArrayList<>(); switch (att) { case
	 * "author": case "authors": case "name": case "names": for (JsonObject o :
	 * publication.getJsonArray("authors").getValuesAs(JsonObject.class)) {
	 * values.add(o.getString("name")); } return values;
	 * 
	 * case "ids": for (JsonObject o :
	 * publication.getJsonArray("authors").getValuesAs(JsonObject.class)) {
	 * values.add(o.getString("ids")); } return values; case "id":
	 * values.add(publication.getString("id")); return values; case "title":
	 * values.add(publication.getString("title")); return values; case "year":
	 * values.add(publication.getString("year")); return values; case "s2Url":
	 * values.add(publication.getString("s2Url")); return values; case "venue":
	 * values.add(publication.getString("venue")); return values;
	 * 
	 * default: throw new IllegalArgumentException(
	 * "this attribute doesn't exist"); } } catch (NullPointerException e) {
	 * return Collections.emptyList(); } }
	 */

	private static Map<String, List<String>> nextPublication() {
		while (parser.hasNext() && parser.next() != Event.START_OBJECT) {
		}
		if (!parser.hasNext()) {
			return null;
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
					while (parser.hasNext() && parser.next() != Event.END_ARRAY) {
						while (parser.hasNext() && parser.next() != Event.START_OBJECT) {
						}
						while (parser.hasNext()) {
							Event ev = parser.next();
							if (ev == Event.KEY_NAME) {
								switch (parser.getString()) {
								case "ids":
									int prevSize = ids.size();
									while (parser.hasNext() && parser.next() != Event.START_ARRAY) {
									}while (parser.hasNext() && parser.next() != Event.END_ARRAY) {
										ids.add(parser.getString());
									}
									if(ids.size()==prevSize){ ids.add(null);}
									//FIXME delete this
									if(ids.size()-prevSize>1){ System.out.println("warning 2 ids");}
									break;
								case "name":
									parser.next();
									name.add(parser.getString());
								}
							}
							if (ev == Event.END_OBJECT) {
								break;
							}
						}
					}
					object.put("ids", ids);
					object.put("name", name);
				}
			}
			if (e == Event.END_OBJECT) {
				return object;
			}
		}
		return object;

	}
}