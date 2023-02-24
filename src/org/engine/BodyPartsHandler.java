package org.engine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BodyPartsHandler {
	
	public BodyPartsHandler(String path) {
		URL url = BodyPartsHandler.class.getResource(path);
		String urlStr = url.getPath();
		
		try {
			Object jsonFile = new JSONParser().parse(new FileReader(urlStr));
			JSONObject jsonObject = (JSONObject) jsonFile;
			
			for(Object key : jsonObject.keySet()) {
				JSONObject side = (JSONObject) jsonObject.get(key);
				
				for(Object part : side.keySet()) {
					JSONObject bodyPart = (JSONObject) side.get(part);
					
					try{
						for(Object partElement : bodyPart.keySet()) {
							JSONObject element = (JSONObject) bodyPart.get(partElement);
							
							
							
							System.out.println(element);
						}	
					}
					catch(ClassCastException e) {
						System.out.println(bodyPart + "Dawdjahwvgdvawj");
					}
				}
			}
			
		} 
		catch (FileNotFoundException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); } 
		catch (ParseException e) { e.printStackTrace(); }	
		
	}
	
}
