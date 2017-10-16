import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.json.Json;
import javax.json.stream.JsonParser;



public class App {

	private static JsonParser parser;
	
	public static void main(String[] args) {
		try(InputStream input = new FileInputStream("")){
			parser = Json.createParser(input);
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}