package org.scene.entities;

import java.awt.Font;

import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.graphics.EventListener;
import org.graphics.Graphics;


public class Obstacle extends GameObject{

	private String imagePath = "";
	
	private boolean collider = false;
	
	public boolean verticallyCenteredText = false;
	
	private float textOffsetX = 0;
	private float textOffsetY = 0;
	
	public Obstacle(float x, float y, float width, float height, String path) {
		super(x, y, width, height, path);
		
		id = ID.Obstacle;
		
		imagePath = path;
		
		setFont(new Font("SansSerif", Font.BOLD, (int)fontSize));
	
		showBounds = true;
		
		text = uuid;
	}
	
	public void update() {
		deactiveteWhenOutOfView();
		if(!outOfView) {
			centerTextHorizontally();
			
			//System.out.println("Updating " + uuid);
			
			
			if(!verticallyCenteredText)
				placeTextAbove();
			else {
				centerTextVertically();
			}
			
			//text = this.getClass().getSimpleName();
			
			super.textOffsetX += textOffsetX;
			super.textOffsetY += textOffsetY;
		}
	}
	
	public void offsetText(float xOffset, float yOffset) {
		textOffsetX = xOffset;
		textOffsetY = yOffset;
	}
	
	public void render() {
		if(imagePath.equals("")) {
			Graphics.setColor(red, green, blue, alpha);
			Graphics.drawRect(x, y, width, height);
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
