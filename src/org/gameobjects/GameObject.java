package org.gameobjects;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import org.engine.AnimationHandler;
import org.engine.BodyPartsHandler;
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
	public Point position = new Point(0,0);
	
	public float velocityX = 0;
	public float velocityY = 0;
	
	protected float width = 1;
	protected float height = 1;
	
	public Vector movementVector = new Vector(0,0);
	public Point lastObjPosition = new Point(0,0);
	
	public boolean onGround = false;
	
	protected float offsetFromMiddleX = 0;
	protected float offsetFromMiddleY = 0;
	
	protected float crouchHeight = (height / 3) * 2;
	
	public Color color;
	
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
	
	public Polygon bounds;
	public ArrayList<Polygon> triangulatedBounds = new ArrayList<Polygon>();

	public Point centerOfMass = new Point(0,0);
	
	public BodyPartsHandler bodyPartsHandler;
	
	public Collider collider;
	public boolean collisionOn = true;
	public boolean collides = false;
	
	private Point lastColliderPos = new Point();
	private float lastColliderRotation = 0;
	
	public boolean moveable = false;
	
	public boolean outOfView = false;

	public boolean showBounds;
	public boolean showJoints;
	
	public float jointPointX;
	public float jointPointY;
	
	public ID id = ID.GameObject;
	public String uuid = UUID.randomUUID().toString().replaceAll("_", "");
	
	public static int updated = 0;
	
	public ArrayList<Point> splitPoints = new ArrayList<Point>();
	
	public GameObject(float x, float y, float width, float height, String src, ID id) {
		this.position.x = x;
		this.position.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
		
		Random random = new Random();
		
		color = new Color(255, 255, 255, 255);
		
		lastColliderPos.x = this.position.x;
		lastColliderPos.y = this.position.y;
		
		offsetFromMiddleX = width / 2;
		offsetFromMiddleY = height / 2;
		
		//if(src.equals("")) src = "res/org/scene/defaultTexture.png";
		
		this.animationPath = src;
		this.txt = new ImageResource(src);
		
		bounds = new Polygon(txt.getImageBounds());
		
		if(bounds.vertices.size() == 0) 
			bounds.vertices = getBounds();
		
		if(id != ID.HUD)
			bounds.vertices = ImageResource.organizePoints(bounds.vertices, this);
		
		/*for(int vertex = 0; vertex < bounds.vertices.size() - 2; vertex++) {
			Point p1 = bounds.vertices.get(vertex);
			Point p2 = bounds.vertices.get(vertex + 1);
			Point p3 = bounds.vertices.get(vertex + 2);
			
			double a = Math.sqrt(Math.pow(p3.x - p1.x, 2) + Math.pow(p3.y - p1.y, 2));
			double b = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
			double c = Math.sqrt(Math.pow(p3.x - p2.x, 2) + Math.pow(p3.y - p2.y, 2));
			
			double cosine = (Math.pow(a, 2) - Math.pow(b, 2) - Math.pow(c, 2)) / (-2 * b * c);
			
			double angle = 360 - Math.toDegrees(Math.acos(cosine));
			
			//System.out.println("a: " + a + " b: " + b +" c: " + c + "    " +  angle);
			System.out.println(angle);
			
			if(angle > 180 && angle != 360) {
				p2.color = new Color(0,255,0,255);
				splitPoints.add(p2);
			}
			
		}*/
		
		ArrayList<Point> boundsToTriangulate = new ArrayList<>();
		boundsToTriangulate.addAll(bounds.vertices);
		
		for(int vertex = 0; vertex < boundsToTriangulate.size(); vertex++) {
			
			Point p1 = boundsToTriangulate.get((vertex == 0) ? boundsToTriangulate.size() - 1 : vertex - 1);
			Point p2 = boundsToTriangulate.get(vertex);
			Point p3 = boundsToTriangulate.get((vertex == boundsToTriangulate.size() - 1) ? 0 : vertex + 1);
			
			double a = Math.sqrt(Math.pow(p3.x - p1.x, 2) + Math.pow(p3.y - p1.y, 2));
			double b = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
			double c = Math.sqrt(Math.pow(p3.x - p2.x, 2) + Math.pow(p3.y - p2.y, 2));
			
			double angle = Math.toDegrees(Math.acos((Math.pow(a, 2) - Math.pow(b, 2) - Math.pow(c, 2)) / (-2 * b * c)));
			
			if(angle < 180 && angle != 0) {
				double p = (a + b + c) / 2;
				double ABCarea = Math.sqrt(p * (p - a) * (p - b) * (p - c));
				
				for(int pointIndex = 0; pointIndex < boundsToTriangulate.size(); pointIndex++) {
					Point point = boundsToTriangulate.get(pointIndex);
					
					if(point.equals(p1) || point.equals(p2) || point.equals(p3))
						continue;
					
					double p1Q = Math.sqrt(Math.pow(point.x - p1.x, 2) + Math.pow(point.y - p1.y, 2));
					double p2Q = Math.sqrt(Math.pow(point.x - p2.x, 2) + Math.pow(point.y - p2.y, 2));
					double p3Q = Math.sqrt(Math.pow(point.x - p3.x, 2) + Math.pow(point.y - p3.y, 2));
					
					double ABQp = (b + p1Q + p2Q) / 2;
					double ABQarea = Math.sqrt(ABQp * (ABQp - b) * (ABQp - p1Q) * (ABQp - p2Q));
					
					double ACQp = (a + p1Q + p3Q) / 2;
					double ACQarea = Math.sqrt(ACQp * (ACQp - a) * (ACQp - p1Q) * (ACQp - p3Q));
					
					double BCQp = (c + p2Q + p3Q) / 2;
					double BCQarea = Math.sqrt(BCQp * (BCQp - c) * (BCQp - p2Q) * (BCQp - p3Q));
					
					if(ABCarea != ABQarea + ACQarea + BCQarea) {
						//System.out.println("Found an ear, I guess");
						triangulatedBounds.add(new Polygon(new Point[]{p1,p2,p3}));
						
						boundsToTriangulate.remove(p2);
						
						vertex = 0;
					}
				}
				//System.out.println("a: " + a + "b: " + b + "c: " + c + " area: " + abcArea);
			}
			
		}
		
		
		System.out.println(this.getClass().getSimpleName() + " has " + triangulatedBounds.size() + " triangles");
		

		this.txt.addToStash();

		collider = new Collider(bounds.vertices, this);
		
		centerOfMass = txt.centerOfMass;
		
		
	}
	
	public void update() {
		movementVector.x = position.x - lastObjPosition.x;
		movementVector.y = position.y - lastObjPosition.y;
		
		lastObjPosition.x = position.x;
		lastObjPosition.y = position.y;
		
		if(lastColliderPos.x != position.x || lastColliderPos.y != position.y || lastColliderRotation != rotation) {
			lastColliderPos.x = position.x;
			lastColliderPos.y = position.y;
			lastColliderRotation = rotation;
			
			collider.update();
		}
	}

	public void render() {
		if(!outOfView) {
			Graphics.Rotate(-rotation);
			Graphics.drawImage(txt, position.x, position.y, width, height, id);
			Graphics.Rotate(0);
			
			if(EventListener.renderBounds && showBounds) {
				//System.out.println(this.getClass().getSimpleName() + " complex bounds rendering (UUID: " + uuid + ")");
				
				for(int point = 0; point < bounds.vertices.size() - 1; point++) {
					Graphics.setColor(bounds.vertices.get(point).color);
					//Calculating position of x and y after rotating
					float x = (float)((bounds.vertices.get(point).x) * Math.cos(Math.toRadians(-rotation)) - (bounds.vertices.get(point).y) * Math.sin(Math.toRadians(-rotation)) + this.position.x);
					float y = (float)((bounds.vertices.get(point).x) * Math.sin(Math.toRadians(-rotation)) + (bounds.vertices.get(point).y) * Math.cos(Math.toRadians(-rotation)) + this.position.y);
					
					Graphics.drawRect(x, y, .01f, .01f);
					//Graphics.drawLine(x + bounds.get(point).x, y + bounds.get(point).y, x + bounds.get(point + 1).x , y + bounds.get(point + 1).y, id);
				}
				
				Graphics.setColor(1, 1, 1, 1);
			}
			
			if(EventListener.renderJoints && showJoints)
				drawJoints();
			
			/*if(id == ID.Obstacle) {
				Graphics.setColor(new Color(255,0,0,255));
				for(Polygon triangle : triangulatedBounds) {
					for(int vertex = 0; vertex < 3; vertex++) {
						Graphics.drawLine(position.x + triangle.vertices.get(vertex).x, position.y + triangle.vertices.get(vertex).y, position.x + triangle.vertices.get((vertex == 2) ? 0 : vertex + 1).x, position.y + triangle.vertices.get((vertex == 2) ? 0 : vertex + 1).y, id);
					}
				}
				Graphics.setColor(Color.clear());
			}*/
			
		}
	}
	
	public void renderText() {
		Graphics.Rotate(-rotation);
		Graphics.setTextColor(textRed, textGreen, textBlue, textAlpha);
		Graphics.drawString(position.x + textOffsetX, position.y + textOffsetY, text, this, id);
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
	
	public float getWorldX() {return ((Renderer.unitsWide / Renderer.getWindowWidth()) * position.x - Renderer.unitsWide/2) + Camera.x;}
	
	public float getWorldY() {
		float unitsTall = Renderer.unitsWide * (float) ((float)Renderer.getWindowHeight() / (float)Renderer.getWindowWidth());
		return -(unitsTall / Renderer.getWindowHeight() * position.y - unitsTall/2) + Camera.y;
	}
	
	public float getX() { return position.x; };
	public float getY() { return position.y; };
	
	public float getWidth() { return width; };
	public float getHeight() { return height; };
	
	public void toggleMask() { txt.switchViewMode(); }
	
	public void setColor(int r, int g, int b, int a) {
		color.red = (float) Math.max(0, Math.min(255, r)) / 255;
		color.green = (float) Math.max(0, Math.min(255, g)) / 255;
		color.blue = (float) Math.max(0, Math.min(255, b)) / 255;
		color.alpha = (float) Math.max(0, Math.min(255, a)) / 255;
	}
	
	public void deactiveteWhenOutOfView() {
		if((position.x - width / 2 - Camera.x > Renderer.unitsWide / 2 || position.x + width / 2 - Camera.x < -Renderer.unitsWide / 2)
				|| (position.y - height / 2 - Camera.y > Renderer.unitsTall / 2 || position.y + height / 2 - Camera.y < -Renderer.unitsTall / 2)) {
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
	
	//Scales points inside bounds array to fit rescaled object
	public void scaleBounds(float width, float height) {	
		try {
			for(Point point : bounds.vertices) {
				point.x *= width / (txt.getWidth() / Renderer.pixelsPerUnit);
				point.y *= height / (txt.getHeight() / Renderer.pixelsPerUnit);
			}
			
		} catch(NullPointerException e) {
			System.out.println(this.getClass().getSimpleName());
		}
	}
	
	//Returns simple rectangular bounds
	public ArrayList<Point> getBounds() {
		ArrayList<Point> bounds = new ArrayList<Point>();
		float wSpacing = width / Collider.minColliderPointSpacing;
		float hSpacing = height / Collider.minColliderPointSpacing;
		
		for(int point = 0; point <= wSpacing; point++) {
			
			bounds.add(new Point((float)((-width / 2 + Collider.minColliderPointSpacing * point) * Math.cos(Math.toRadians(-rotation)) - (height / 2) * Math.sin(Math.toRadians(-rotation))), (float)((-width / 2 + Collider.minColliderPointSpacing * point) * Math.sin(Math.toRadians(-rotation)) + (height / 2) * Math.cos(Math.toRadians(-rotation)))));
			bounds.add(new Point((float)((-width / 2 + Collider.minColliderPointSpacing * point) * Math.cos(Math.toRadians(-rotation)) - (-height / 2) * Math.sin(Math.toRadians(-rotation))), (float)((-width / 2 + Collider.minColliderPointSpacing * point) * Math.sin(Math.toRadians(-rotation)) + (-height / 2) * Math.cos(Math.toRadians(-rotation)))));
		}
		for(int point = 0; point <= hSpacing; point++) {
			
			bounds.add(new Point((float)((width / 2) * Math.cos(Math.toRadians(-rotation)) - (-height / 2 + Collider.minColliderPointSpacing * point) * Math.sin(Math.toRadians(-rotation))), (float)((width / 2) * Math.sin(Math.toRadians(-rotation)) + (-height / 2 + Collider.minColliderPointSpacing * point) * Math.cos(Math.toRadians(-rotation)))));
			bounds.add(new Point((float)((-width / 2) * Math.cos(Math.toRadians(-rotation)) - (-height / 2 + Collider.minColliderPointSpacing * point) * Math.sin(Math.toRadians(-rotation))), (float)((-width / 2) * Math.sin(Math.toRadians(-rotation)) + (-height / 2 + Collider.minColliderPointSpacing * point) * Math.cos(Math.toRadians(-rotation)))));
		}
		/*
		 * x′=(x−p)cos(θ)−(y−q)sin(θ)+p,
		 * y'=(x−p)sin(θ)+(y−q)cos(θ)+q.
		 */
		
		return bounds;
	}
	
	//Renders simple rectangular bounds
	public void drawBounds() {
		ArrayList<Point> collisionPoints = collider.getCollisonPoints();
		
		Graphics.setColor(1, 0, 0, 1);
		
		for(int point = 0; point < collisionPoints.size(); point++) {
			Graphics.drawRect(collisionPoints.get(point).x, collisionPoints.get(point).y, .01f, .01f);
		}
		
		Graphics.setColor(1, 1, 1, 1);
	}
	
	//Highlights places where object have joints connecting to other objects
	public void drawJoints() {
		Graphics.setColor(.8f, .6f, .8f, 1);
		Graphics.drawRect(jointPointX, jointPointY, .04f, .04f);
		Graphics.setColor(1, 1, 1, 1);
	}
	
	//Checks if mouse is hovering over the object
	public boolean hover() {
		return (MouseInput.getMouseX() > position.x - width / 2 && MouseInput.getMouseX() < position.x + width / 2 && 
				MouseInput.getMouseY() < position.y + height / 2 && MouseInput.getMouseY() > position.y - height / 2);
	}
	
	public boolean onClick() {
		if(hover())
			return MouseInput.pressed;
		
		return false;
	}
	
	//Sets new texture to the object
	public void setImage(String path) {
		this.txt = new ImageResource(path);
	}
	
	//Allows object to be dragged
	public void draggable() {
		
		if(isDraggable && (MouseInput.getMouseWorldX() > position.x - width / 2 && MouseInput.getMouseWorldX() < position.x + width / 2
				&& MouseInput.getMouseWorldY() > position.y - height / 2 && MouseInput.getMouseWorldY() < position.y + height / 2 && MouseInput.pressed && !MouseInput.draggingSmth) || dragged) {
			
			position.x = MouseInput.getMouseWorldX();
			position.y = MouseInput.getMouseWorldY();
			
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
	
	public void getChildsVelocity() {
		if(bodyPartsHandler != null) {
			ArrayList<Float> velocities = new ArrayList<Float>();
			
			for(BodyPart bodyPart : bodyPartsHandler.bodyParts) {
				velocities.add(bodyPart.velocityX);
			}
			
			velocities.add(velocityX);
			
			float biggestVelocity = 0;
			
			for(float velocity : velocities) {
				biggestVelocity = Math.abs(biggestVelocity) < Math.abs(velocity) ? velocity : biggestVelocity;
			}			
			//System.out.println(biggestVelocity);
			
			velocityX = biggestVelocity;
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
