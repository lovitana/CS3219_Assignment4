import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.json.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class App {

	private static JsonParser parser;

	public static void main(String[] args) {
		try (InputStream input = new FileInputStream("src/resources/extractedData.json")) {
			parser = Json.createParser(input);
			
			// TODO switch arg[0] to call the rigth function with arguments arg[1..n] (and maybe a file.csv)
			
			//TODO remove
			System.out.println("Q1:");
			List<Pair<String,Integer>> l =findWithMostAttr("author", "venue", "arXiv", 10);
			System.out.println(l.toString());
			parser.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Pair<String, Integer>> findWithMostAttr(String ref, String att, String value, int n) {
		Map<String, Integer> count = new HashMap<>();
		while (parser.hasNext()) {
			Event e = parser.next();
			if (e != Event.START_OBJECT) {
				continue;
			}
			JsonObject publication = parser.getObject();
			if (getAttributes(publication, att).contains(value)) {
				for (String reference : getAttributes(publication, ref)) {
					count.put(reference, count.getOrDefault(reference,0)+1);
				}
			}
		}
		TreeSet<Entry<String, Integer>> top= new TreeSet<>((e1,e2)->{
			return Integer.compare(e1.getValue(), e2.getValue());
		});
		List<Pair<String,Integer>> result = new ArrayList<>();
		int c = 0;
		for(Entry<String, Integer> ent :top ){
			if(c>n){
				return result;
			}
			result.add(new Pair<>(ent.getKey(),ent.getValue()));
			c++;
		}

		return result;
	}

	private static List<String> getAttributes(JsonObject publication, String att) {
		List<String> values = new ArrayList<>();
		switch (att) {
		case "author":
		case "authors":
		case "name":
		case "names":
			for (JsonObject o : publication.getJsonArray("authors").getValuesAs(JsonObject.class)) {
				values.add(o.getString("name"));
			}
			return values;

		case "ids":
			for (JsonObject o : publication.getJsonArray("authors").getValuesAs(JsonObject.class)) {
				values.add(o.getString("ids"));
			}
			return values;
		case "id":
			values.add(publication.getString("id"));
			return values;
		case "title":
			values.add(publication.getString("title"));
			return values;
		case "year":
			values.add(publication.getString("year"));
			return values;
		case "s2Url":
			values.add(publication.getString("s2Url"));
			return values;
		case "venue":
			values.add(publication.getString("venue"));
			return values;

		default:
			throw new IllegalArgumentException("this attribute doesn't exist");
		}
	}
}