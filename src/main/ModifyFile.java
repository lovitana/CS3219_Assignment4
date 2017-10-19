package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModifyFile {

	public static void main(String[] args) {
		try (BufferedReader input = new BufferedReader(new FileReader("src/resources/data.json"));
				BufferedWriter bw = new BufferedWriter(new FileWriter("src/resources/dataModified.json"));) {
			String line = null;
			bw.write("[");
			line=input.readLine();
			bw.write(line);
			while((line=input.readLine()) != null){
				if(!line.isEmpty() && line.charAt(0)=='{'){
					bw.write(","+line);
				}else{
					bw.write(line);
					System.out.println(line);
				}
			}
			bw.write("]");
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
