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
		
	}
	
	public void update() {
		if((MouseInput.getMouseX() > x - width / 2 && MouseInput.getMouseX() < x + width / 2
				&& MouseInput.getMouseY() > y - height / 2 && MouseInput.getMouseY() < y + height / 2 && MouseInput.pressed && !MouseInput.draggingSmth) || dragged) {
			x = MouseInput.getMouseX();
			y = MouseInput.getMouseY();
			velocityY = 0;
			velocityX = 0;
			dragged = true;
			MouseInput.draggingSmth = true;
			//System.out.println("Over player");
		}
		if(!MouseInput.pressed) {
			dragged = false;
			MouseInput.draggingSmth = false;
		}
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
}
