package org.gameobjects;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;

import org.engine.AnimationHandler;
import org.engine.Handler;
import org.graphics.Graphics;
import org.graphics.Renderer;
import org.input.KeyInput;
import org.input.MouseInput;
import org.scene.entities.Camera;

public class GameObject {
	protected float x = 0;
	protected float y = 0;
	
	protected float velocityX = 0;
	protected float velocityY = 0;
	
	protected float width = 1;
	protected float height = 1;
	
	protected float crouchHeight = (height / 3) * 2;
	
	public float red = 1;
	public float green = 1;
	public float blue = 1;
	public float alpha = 1;
	
	public float rotation = 0;
	
	public boolean dragged;
	
	public AnimationHandler animation;
	public int currentAnimation = 0;
	
	public boolean showBounds;
	
	public ID id = null;
	
	public GameObject(float x, float y, float width, float height, String animationPath) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		try {
			animation = new AnimationHandler(animationPath);
		} catch (IOException e) {
			
		}
	}
		
	public void update() {};

	public void render() {
		
		animation.get(currentAnimation).play();
		
		Graphics.Rotate(-rotation);
		Graphics.drawImage(animation.get(currentAnimation).getImage(), x, y, width, height);
		Graphics.Rotate(0);
		
		if(showBounds)
			drawBounds();
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
		float[] bounds = new float[8];
		//x1 & y1
		bounds[0] = (float)((-width / 2) * Math.cos(Math.toRadians(-rotation)) - (height / 2) * Math.sin(Math.toRadians(-rotation)) + x);
		bounds[1] = (float)((-width / 2) * Math.sin(Math.toRadians(-rotation)) + (height / 2) * Math.cos(Math.toRadians(-rotation)) + y);
		
		//x2 & y2
		bounds[2] = (float)((width / 2) * Math.cos(Math.toRadians(-rotation)) - (height / 2) * Math.sin(Math.toRadians(-rotation)) + x);
		bounds[3] = (float)((width / 2) * Math.sin(Math.toRadians(-rotation)) + (height / 2) * Math.cos(Math.toRadians(-rotation)) + y);
		
		//x3 & y3
		bounds[4] = (float)((width / 2) * Math.cos(Math.toRadians(-rotation)) - (-height / 2) * Math.sin(Math.toRadians(-rotation)) + x);
		bounds[5] = (float)((width / 2) * Math.sin(Math.toRadians(-rotation)) + (-height / 2) * Math.cos(Math.toRadians(-rotation)) + y);
		
		//x4 & y4
		bounds[6] = (float)((-width / 2) * Math.cos(Math.toRadians(-rotation)) - (-height / 2) * Math.sin(Math.toRadians(-rotation)) + x);
		bounds[7] = (float)((-width / 2) * Math.sin(Math.toRadians(-rotation)) + (-height / 2) * Math.cos(Math.toRadians(-rotation)) + y);
		
		return bounds;
	}
	
	public boolean doOverlap(float[] rec1, float[] rec2) {
	
		System.out.println(
			"Player {" +
			" x1: " + rec1[0] + 
			" y1: " + rec1[1] + 
			" x2: " + rec1[2] + 
			" y2: " + rec1[3] + 
			" x3: " + rec1[4] + 
			" y3: " + rec1[5] + 
			" x4: " + rec1[6] + 
			" y4: " + rec1[7] +
		"}");
		
		System.out.println("\n\n");		
		
		System.out.println(
			"Obstacle {" +
			"x1: " + rec2[0] + 
			" y1: " + rec2[1] + 
			" x2: " + rec2[2] + 
			" y2: " + rec2[3] + 
			" x3: " + rec2[4] + 
			" y3: " + rec2[5] + 
			" x4: " + rec2[6] + 
			" y4: " + rec2[7] +
		"}");
		
		/*if(rec1[0] + rec1[2] / 2 >= rec2[0] - rec2[2] / 2 && rec1[0] - rec1[2] <= rec2[0] + rec2[2] / 2 - rec1[2] / 2 
				&& rec1[1] - rec1[3] / 2 <= rec2[1] + rec2[3] / 2 && rec1[1] + rec1[3] / 2 >= rec2[1] - rec2[3] / 2) {
			return true;
		}*/
		
		return false;
	}
	
	public void drawBounds() {
		showBounds = true;
		float[] bounds = getBounds();
		float x1 = bounds[0];
		float y1 = bounds[1];
		
		float x2 = bounds[2];
		float y2 = bounds[3];
		
		float x3 = bounds[4];
		float y3 = bounds[5];
		
		float x4 = bounds[6];
		float y4 = bounds[7];
		
		Graphics.setColor(1, 0, 0, 1);
		Graphics.drawLine(x1, y1, x2, y2);
		Graphics.drawLine(x2, y2, x3, y3);
		Graphics.drawLine(x3, y3, x4, y4);
		Graphics.drawLine(x4, y4, x1, y1);
		Graphics.setColor(1, 1, 1, 1);
	}
	
	public void draggable() {
		if((MouseInput.getMouseX() > x - width / 2 && MouseInput.getMouseX() < x + width / 2
				&& MouseInput.getMouseY() > y - height / 2 && MouseInput.getMouseY() < y + height / 2 && MouseInput.pressed && !MouseInput.draggingSmth) || dragged) {
			
			System.out.println("MouseInput.getMouseX() = " + MouseInput.getMouseX());
			System.out.println("x = " + x + "\n\n");
			
			x = MouseInput.getMouseX();
			y = MouseInput.getMouseY();
			
			rotation += MouseInput.rotation;
			
			if(KeyInput.getKey(KeyEvent.VK_K)) {
				rotation -= MouseInput.rotationSpeed;
			}
			else if(KeyInput.getKey(KeyEvent.VK_L)) {
				rotation += MouseInput.rotationSpeed;
			}
			
			velocityY = 0;
			velocityX = 0;
			dragged = true;
			MouseInput.draggingSmth = true;
			MouseInput.rotation = 0;
		}
		if(!MouseInput.pressed) {
			dragged = false;
			MouseInput.draggingSmth = false;
		}
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
	
	public void reloadCrouchHeight() {
		crouchHeight = (height / 3) * 2;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
}
