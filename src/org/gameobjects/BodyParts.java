package org.gameobjects;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.engine.AnimationHandler;
import org.engine.GameLoop;
import org.graphics.Graphics;
import org.graphics.Renderer;
import org.input.MouseInput;

public class BodyParts extends Entities{
	BufferedImage texture;
	
	public BodyParts(String path) throws IOException {
		super(0, 0, 0, 0, "/res/org/animations/Arm.txt");
		
		URL url = AnimationHandler.class.getResource(path);
		String urlStr = url.getPath();
		
		texture = ImageIO.read(new File(urlStr));

		float pixelWidth = texture.getWidth();
		float pixelHeight = texture.getHeight();
		float WHRatio = pixelWidth / pixelHeight;
		
		width = pixelWidth / 250;
		height = width / WHRatio;
		
		//Default width and height
		WIDTH = width;
		HEIGHT = height;
		
	}
	
	public void update() {
		
		applyPhysics(true, true);
		draggable();
		
		//System.out.println("Body part collisionD = " + collisionD);
		
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
}
