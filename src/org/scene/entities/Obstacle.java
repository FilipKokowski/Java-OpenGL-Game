package org.scene.entities;

import java.awt.Font;

import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.graphics.EventListener;
import org.graphics.Graphics;


public class Obstacle extends GameObject{

	private String imagePath = "";
	
	private boolean collider = false;
	
	public Obstacle(float x, float y, float width, float height, String path) {
		super(x, y, width, height, path);
		
		id = ID.Obstacle;
		
		imagePath = path;
		
		setFont(new Font("SansSerif", Font.BOLD, (int)fontSize));
	
		showBounds = true;
	}
	
	public void update() {
		centerTextHorizontally();
		placeTextAbove();
		
		text = this.getClass().getSimpleName();
	}
	
	public void render() {
		if(imagePath.equals("")) {
			Graphics.setColor(red, green, blue, alpha);
			Graphics.fillRect(x, y, width, height);
			Graphics.setColor(1, 1, 1, 1);
			
			if(EventListener.renderBounds && showBounds)
				drawBounds();
			
			if(EventListener.renderJoints && showJoints)
				drawJoints();
		}
		else {
			super.render();
		}
	}
	
	public void collisionOn() { collider = true; }
	public void collisionOff() { collider = false; }
}
