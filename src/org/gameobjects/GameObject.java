package org.gameobjects;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.engine.AnimationHandler;
import org.engine.Handler;
import org.graphics.EventListener;
import org.graphics.Graphics;
import org.graphics.Renderer;
import org.input.KeyInput;
import org.input.MouseInput;
import org.resource.ImageResource;
import org.scene.entities.Camera;
import org.engine.Collider;

import com.jogamp.opengl.util.awt.TextRenderer;

public class GameObject {
	protected float x = 0;
	protected float y = 0;
	
	protected Point lastPos = new Point();
	
	protected float velocityX = 0;
	protected float velocityY = 0;
	
	protected float width = 1;
	protected float height = 1;
	
	protected float offsetFromMiddleX = 0;
	protected float offsetFromMiddleY = 0;
	
	protected float crouchHeight = (height / 3) * 2;
	
	public float red = 1;
	public float green = 1;
	public float blue = 1;
	public float alpha = 1;
	
	public float textRed = 1;
	public float textGreen = 0;
	public float textBlue = 0;
	public float textAlpha = 1;
	
	public float rotation = 0;

	public boolean dragged;
	public boolean isDraggable = true;
	
	public AnimationHandler animation;
	public int currentAnimation = 0;
	
	public String animationPath;
	
	public ImageResource txt;
	
	public Font font = new Font("Comic Sans MS", Font.BOLD, 12);
	public TextRenderer textRenderer = new TextRenderer(font);
	public Rectangle2D textBounds = null;
	public float fontSize = 12;
	
	public String text = "";
	public float textWidth = 0;
	public float textHeight = 0;
	public float textOffsetX = 0;
	public float textOffsetY = 0;
	
	public ArrayList<Point> bounds;
	public ArrayList<Point> boundsOffsets;
	
	public Collider collider;
	
	public ArrayList<Vertex> verticesOffsets;
	public ArrayList<Vertex> vertices;
	public float verticesAngle = 0;
	
	public boolean outOfView = false;

	public boolean showBounds;
	public boolean showJoints;
	
	public float jointPointX;
	public float jointPointY;
	
	public ID id = ID.GameObject;
	public String uuid = UUID.randomUUID().toString().replaceAll("_", "");
	
	public static int updated = 0;
	
	public GameObject(float x, float y, float width, float height, String src) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		lastPos.x = this.x;
		lastPos.y = this.y;
		
		offsetFromMiddleX = width / 2;
		offsetFromMiddleY = height / 2;
		
		//if(src.equals("")) src = "res/org/scene/defaultTexture.png";
		
		this.animationPath = src;
		this.txt = new ImageResource(src);
		
		boundsOffsets = txt.getImageBounds();
		
		if(boundsOffsets.size() == 0)
			boundsOffsets = getBounds();
		
		bounds = new ArrayList<Point>();
		
		for(Point point : boundsOffsets) {
			bounds.add(point.clone());
		}
		
		collider = new Collider(bounds, this);
		
		//System.out.println(bounds.size() + ": " + this.getClass().getSimpleName());
		
		//System.out.println(this.getClass().getSimpleName() + ": " + bounds);
	}
	
	public void update() {}

	public void render() {
		if(!outOfView) {
			Graphics.Rotate(-rotation);
			Graphics.drawImage(txt, x, y, width, height, id);
			Graphics.Rotate(0);
			
			if(EventListener.renderBounds && showBounds) {
				//System.out.println(this.getClass().getSimpleName() + " complex bounds rendering (UUID: " + uuid + ")");
				
				Graphics.setColor(1, 0, 0, 1);
				
				for(int point = 0; point < bounds.size() - 1; point++) {
					//Calculating position of x and y after rotating
					float x = (float)((bounds.get(point).x) * Math.cos(Math.toRadians(-rotation)) - (bounds.get(point).y) * Math.sin(Math.toRadians(-rotation)) + this.x);
					float y = (float)((bounds.get(point).x) * Math.sin(Math.toRadians(-rotation)) + (bounds.get(point).y) * Math.cos(Math.toRadians(-rotation)) + this.y);
					Graphics.drawRect(x, y, .01f, .01f);
					
					//Graphics.drawLine(x + bounds.get(point).x, y + bounds.get(point).y, x + bounds.get(point + 1).x , y + bounds.get(point + 1).y, id);
				}
				
				Graphics.setColor(1, 1, 1, 1);
			}
			
			if(EventListener.renderJoints && showJoints)
				drawJoints();
			
		}
	}
	
	public void renderText() {
		Graphics.Rotate(-rotation);
		Graphics.setTextColor(textRed, textGreen, textBlue, textAlpha);
		Graphics.drawString(x + textOffsetX, y + textOffsetY, text, this, id);
		Graphics.Rotate(0);
	}
	
	public void setFont(Font font) {
		this.font = font;
		textRenderer = new TextRenderer(font);
	}
	
	public void setCustomFont(String path) {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream(path)).deriveFont(fontSize);
			
			textRenderer = new TextRenderer(font);
		} catch (FontFormatException | IOException e) {}
	}
	
	public void centerTextHorizontally() {
		if(textBounds != null) {
			textWidth = (float)(Renderer.unitsWide / Renderer.getWindowWidth() * textBounds.getWidth());
		
			textOffsetX = -textWidth / 2;
		}
	}
	
	public void centerTextVertically() {
		if(textBounds != null) {
			textHeight = (float)(Renderer.unitsTall / Renderer.getWindowHeight() * textBounds.getHeight());
		
			textOffsetY = -textHeight / 4;
		}
	}
	
	public void placeTextAbove() {
		if(textBounds != null) {
			textHeight = (float)(Renderer.unitsTall / Renderer.getWindowHeight() * textBounds.getHeight());
		
			textOffsetY = height / 1.75f;
		}
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
	
	public void toggleMask() { txt.switchViewMode(); }
	
	public void setColor(float r, float g, float b, float a) {
		red = Math.max(0, Math.min(1, r));
		green = Math.max(0, Math.min(1, g));
		blue = Math.max(0, Math.min(1, b));
		alpha = Math.max(0, Math.min(1, a));
	}
	
	public void deactiveteWhenOutOfView() {
		if((x - width / 2 - Camera.x > Renderer.unitsWide / 2 || x + width / 2 - Camera.x < -Renderer.unitsWide / 2)
				|| (y - height / 2 - Camera.y > Renderer.unitsTall / 2 || y + height / 2 - Camera.y < -Renderer.unitsTall / 2)) {
			outOfView = true;
		}
		else 
			outOfView = false;
	}
	
	public void setTextColor(float r, float g, float b, float a) {
		textRed = Math.max(0, Math.min(1, r));
		textGreen = Math.max(0, Math.min(1, g));
		textBlue = Math.max(0, Math.min(1, b));
		textAlpha = Math.max(0, Math.min(1, a));
	}
	
	public void scaleToImageSize() {
		width = txt.getWidth() / Renderer.pixelsPerUnit;
		height = txt.getWidth() / Renderer.pixelsPerUnit;
	}
	
	public ArrayList<Point> getBounds() {
		ArrayList<Point> bounds = new ArrayList<Point>();
		bounds.add(new Point((float)((-width / 2) * Math.cos(Math.toRadians(-rotation)) - (height / 2) * Math.sin(Math.toRadians(-rotation)) + x), (float)((-width / 2) * Math.sin(Math.toRadians(-rotation)) + (height / 2) * Math.cos(Math.toRadians(-rotation)) + y)));
		bounds.add(new Point((float)((width / 2) * Math.cos(Math.toRadians(-rotation)) - (height / 2) * Math.sin(Math.toRadians(-rotation)) + x), (float)((width / 2) * Math.sin(Math.toRadians(-rotation)) + (height / 2) * Math.cos(Math.toRadians(-rotation)) + y)));
		bounds.add(new Point((float)((width / 2) * Math.cos(Math.toRadians(-rotation)) - (-height / 2) * Math.sin(Math.toRadians(-rotation)) + x), (float)((width / 2) * Math.sin(Math.toRadians(-rotation)) + (-height / 2) * Math.cos(Math.toRadians(-rotation)) + y)));
		bounds.add(new Point((float)((-width / 2) * Math.cos(Math.toRadians(-rotation)) - (-height / 2) * Math.sin(Math.toRadians(-rotation)) + x), (float)((-width / 2) * Math.sin(Math.toRadians(-rotation)) + (-height / 2) * Math.cos(Math.toRadians(-rotation)) + y)));
		
		return bounds;
	}
	
	public boolean doOverlap(ArrayList<Point> rec1, ArrayList<Point> rec2) {
	
		/*System.out.println(
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
		"}");*/
		
		/*if(rec1[0] + rec1[2] / 2 >= rec2[0] - rec2[2] / 2 && rec1[0] - rec1[2] <= rec2[0] + rec2[2] / 2 - rec1[2] / 2 
				&& rec1[1] - rec1[3] / 2 <= rec2[1] + rec2[3] / 2 && rec1[1] + rec1[3] / 2 >= rec2[1] - rec2[3] / 2) {
			return true;
		}*/
		
		return false;
	}
	
	public void drawBounds() {
		showBounds = true;
		ArrayList<Point> bounds = getBounds();
		float x1 = bounds.get(0).x;
		float y1 = bounds.get(0).y;
		
		float x2 = bounds.get(1).x;
		float y2 = bounds.get(1).y;
		
		float x3 = bounds.get(2).x;
		float y3 = bounds.get(2).y;
		
		float x4 = bounds.get(3).x;
		float y4 = bounds.get(3).y;
		
		Graphics.setColor(1, 0, 0, 1);
		Graphics.drawLine(x1, y1, x2, y2, id);
		Graphics.drawLine(x2, y2, x3, y3, id);
		Graphics.drawLine(x3, y3, x4, y4, id);
		Graphics.drawLine(x4, y4, x1, y1, id);
		Graphics.setColor(1, 1, 1, 1);
	}
	
	public void hideBounds() { showBounds = false; }
	
	public void drawJoints() {
		showJoints = true;
		Graphics.setColor(.8f, .6f, .8f, 1);
		Graphics.drawRect(jointPointX, jointPointY, .04f, .04f);
		Graphics.setColor(1, 1, 1, 1);
	}
	
	public boolean hover() {
		return (MouseInput.getMouseX() > x - width / 2 && MouseInput.getMouseX() < x + width / 2 && 
				MouseInput.getMouseY() < y + height / 2 && MouseInput.getMouseY() > y - height / 2);
	}
	
	public boolean onClick() {
		if(hover())
			return MouseInput.pressed;
		
		return false;
	}
	
	public void setImage(String path) {
		this.txt = new ImageResource(path);
	}
	
	public void draggable() {
		
		if(isDraggable && (MouseInput.getMouseWorldX() > x - width / 2 && MouseInput.getMouseWorldX() < x + width / 2
				&& MouseInput.getMouseWorldY() > y - height / 2 && MouseInput.getMouseWorldY() < y + height / 2 && MouseInput.pressed && !MouseInput.draggingSmth) || dragged) {
			
			//System.out.println("MouseInput.getMouseX() = " + MouseInput.getMouseX());
			//System.out.println(this.getClass().getSimpleName());
			//System.out.println("MouseInput.rotationSpeed = " + MouseInput.rotationSpeed + "\n\n");
			
			x = MouseInput.getMouseWorldX();
			y = MouseInput.getMouseWorldY();
			
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
