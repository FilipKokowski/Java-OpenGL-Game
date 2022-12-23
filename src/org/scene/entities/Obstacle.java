package org.scene.entities;

import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.graphics.Graphics;

public class Obstacle extends GameObject{

	public Obstacle(float x, float y, float width, float height) {
		super(x,y,width,height);
		
		id = ID.Obstacle;
	}
	
	public void render() {
		Graphics.setColor(red, green, blue, alpha);
		Graphics.fillRect(x, y, width, height);
		Graphics.setColor(1, 1, 1, 1);
	}
	

	

	
}
