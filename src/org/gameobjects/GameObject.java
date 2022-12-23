package org.gameobjects;

import java.util.Iterator;

import org.engine.Handler;
import org.graphics.Animation;
import org.graphics.Graphics;
import org.graphics.Renderer;
import org.scene.entities.Camera;

public class GameObject {
	protected float x = 0;
	protected float y = 0;
	
	protected float width = 1;
	protected float height = 1;
	
	public float red = 1;
	public float green = 1;
	public float blue = 1;
	public float alpha = 1;
	
	public float rotation = 0;
	
	public Animation[] animations;
	public int currentAnimation = 0;
	
	public ID id = null;
	
	public GameObject(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
		
	public void update() {};

	public void render() {
		animations[currentAnimation].play();
		
		Graphics.Rotate(-rotation);
		Graphics.drawImage(animations[currentAnimation].getImage(), x, y, width, height);
		Graphics.Rotate(0);
	}
	
	public float getWorldX() {return ((Renderer.unitsWide / Renderer.getWindowWidth()) * x - Renderer.unitsWide/2) + Camera.x;}
	
	public float getWorldY() {
		float unitsTall = Renderer.unitsWide * (float) ((float)Renderer.getWindowHeight() / (float)Renderer.getWindowWidth());
		return -(unitsTall / Renderer.getWindowHeight() * y - unitsTall/2) + Camera.y;
	}
	
	public float getX() { return x; };
	public float getY() { return y; };
	
	public float getWidth() { return width; };
	public float getHeight() { return height; };
	
	public void setColor(float r, float g, float b, float a) {
		red = Math.max(0, Math.min(1, r));
		green = Math.max(0, Math.min(1, g));
		blue = Math.max(0, Math.min(1, b));
		alpha = Math.max(0, Math.min(1, a));
	}
	
	public float[] getBounds() {
		float[] bounds = new float[4];
		bounds[0] = x;
		bounds[1] = y;
		bounds[2] = width;
		bounds[3] = height;
		
		return bounds;
	}
	
	public boolean doOverlap(float[] rec1, float[] rec2) {
		if(rec1[0] + rec1[2] / 2 >= rec2[0] - rec2[2] / 2 && rec1[0] - rec1[2] < rec2[0] + rec2[2] / 2 - rec1[2] / 2 
				&& rec1[1] - rec1[3] / 2 < rec2[1] + rec2[3] / 2) {
			return true;
		}
		
		return false;
	}
	
	public GameObject getAt(int index) {
		int count = 0;
		Iterator<GameObject> itr = Handler.gameObjects.iterator();
		while(itr.hasNext()) {
			GameObject tempObject = itr.next();
			
			if(count == index) return tempObject;
			
			count++;
		}
		return null;
	}
	

	
	
}
