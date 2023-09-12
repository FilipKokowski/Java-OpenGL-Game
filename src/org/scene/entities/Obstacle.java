package org.scene.entities;

import java.awt.Font;

import org.gameobjects.Entities;
import org.gameobjects.ID;
import org.graphics.EventListener;
import org.graphics.Graphics;


public class Obstacle extends Entities{

	private String imagePath = "";
	
	private boolean collisionEnabled = true;
	
	public boolean verticallyCenteredText = false;
	
	private float textOffsetX = 0;
	private float textOffsetY = 0;
	
	public Obstacle(float x, float y, float width, float height, boolean moveable, String path) {
		super(x, y, width, height, path, ID.Obstacle);
		
		this.moveable = moveable;
		
		imagePath = path;
		
		setFont(new Font("SansSerif", Font.BOLD, (int)fontSize));
	
		showBounds = true;
		
		text = uuid;
		
		if(imagePath.equals("")) bounds.vertices = getBounds();
		else scaleBounds(width, height);
		
		if(collider.pointsOffsets.size() != 4)
			collider.update();
	}
	
	public void update() {
		
		applyPhysics(moveable, moveable);
		
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
		
		super.update();
		
	}
	
	public void offsetText(float xOffset, float yOffset) {
		textOffsetX = xOffset;
		textOffsetY = yOffset;
	}
	
	public void render() {
		/*Graphics.setColor(new Color(255,128,128,255));
		Graphics.drawCircle(position.x, position.y, collisionFieldRadius, ID.HUD);
		Graphics.setColor(Color.clear());*/
		
		if(imagePath.equals("")) {
			Graphics.setColor(color.red, color.green, color.blue, color.alpha);
			Graphics.drawRect(position.x, position.y, width, height);
			Graphics.setColor(1, 1, 1, 1);
			
			if(EventListener.renderBounds)
				drawBounds();
			
			if(EventListener.renderJoints)
				drawJoints();

		}
		else {
			super.render();
		}
		
		//collider.renderAxes(255,255,255,255);
	
	}
	
	public void collisionOn() { collisionEnabled = true; }
	public void collisionOff() { collisionEnabled = false; }
}
