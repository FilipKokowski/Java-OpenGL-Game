package org.gameobjects;

import org.graphics.Animation;
import org.graphics.Graphics;

public class GameObject {
	public float x = 0;
	public float y = 0;
	
	public float width = 1;
	public float height = 1;
	
	public float red = 1;
	public float green = 1;
	public float blue = 1;
	public float alpha = 1;
	
	public float rotation = 0;
	
	public Animation[] animations;
	public int currentAnimation = 0;
		
	public void update() {};

	public void render() {
		animations[currentAnimation].play();
		
		Graphics.Rotate(-rotation);
		Graphics.drawImage(animations[currentAnimation].getImage(), x, y, width, height);
		Graphics.Rotate(0);
	}
	
}
