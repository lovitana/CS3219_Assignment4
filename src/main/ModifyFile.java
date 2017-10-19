package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModifyFile {

	public static void main(String[] args) {
		try (BufferedReader input = new BufferedReader(new FileReader("src/resources/extractedData.json"));
				BufferedWriter bw = new BufferedWriter(new FileWriter("src/resources/extractedDataModified.json"));) {
			String line = null;
			bw.write("[");
			line=input.readLine();
			bw.write(line+"\n");
			while((line=input.readLine()) != null){
				bw.write(",\n"+line);
			}
			bw.write("]");
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
