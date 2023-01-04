package org.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.graphics.Animation;
import org.resource.ImageResource;

public class AnimationHandler {

	private File animationFile;
	private BufferedReader reader;
	
	private short index = -1;
	private short line = 1;
	
	private ArrayList<Animation> animations =  new ArrayList<>();
	
	
	public AnimationHandler(String path) throws IOException {
		URL url = AnimationHandler.class.getResource(path);
		String urlStr = url.getPath();
		
		animationFile = new File(urlStr);
		
		reader = new BufferedReader(new FileReader(animationFile));
		String str;
		
		while((str = reader.readLine()) != null) {
			if(str.length() == 1 && str.compareTo("@") == 0) {
				System.out.println(str + " on line " + line);
				animations.add(new Animation());
				index++;
			}
			else {
				System.out.println(str);
				Animation animation = animations.get(index);
				animation.frames.add(new ImageResource(str));
			}
			line++;
		}
	}
	
	public Animation get(int index) {
		return animations.get(index);
	}
	
}
