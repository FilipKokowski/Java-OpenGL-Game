package org.gameobjects;

import java.io.IOException;
import java.util.Random;

import org.engine.GameLoop;
import org.engine.Handler;
import org.resource.ImageResource;

public class BodyPart extends Entities{

	public String sideID;
	public String partID;
	public String textureSrc;
	public ImageResource texture;
	
	public String xOffset;
	public String yOffset;
	
	public float parentX;
	public float parentY;
	public float parentAngle;
	
	public boolean collapse;
	
	public BodyPart(String src, String sideID, String partID, String xOffset, String yOffset) throws IOException {
		super(0, 0, 0, 0, src);
		
		this.texture = new ImageResource(src);
		this.textureSrc = src;
		this.sideID = sideID;
		this.partID = partID;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		isDraggable = false;

		float pixelWidth = texture.getWidth();
		float pixelHeight = texture.getHeight();
		float WHRatio = pixelWidth / pixelHeight;
		
		width = pixelWidth / 250;
		height = width / WHRatio;
		
		//Default width and height
		WIDTH = width;
		HEIGHT = height;	
		
		if(Integer.parseInt(sideID) == 0)
			Handler.addGO(this);
		
		System.out.println("\nxOffset = " + xOffset + "\nyOffset = " + yOffset);
	}
	
	public void update() {
		
		applyPhysics(true, true);
		draggable();
		drawBounds();
		
		/*System.out.println(
			"ID: " + partID +
			"x = " + (parentX - x) +
			"y = " + (parentY +- y)
		);*/
		
		if(!collapse) {
			rotation += 2.5f;
			x = parentX - Float.valueOf(xOffset);
			y = parentY - Float.valueOf(yOffset);
		}
		else {
			x -= velocityX * GameLoop.updateDelta();
		}
		//rotation = parentAngle;
		
	
		
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void printValues() {
		System.out.println(
			"x = " + xOffset +
			"\ny = " + yOffset +
			"\nside ID = " + sideID +
			"\npart ID = " + partID + 
			"\ntexture src = " + textureSrc +
			"\nParent position (" + parentX + "/" + parentY + "/" + parentAngle + ")\n"
		);
	}
	
	public void setVelocity(float velocityX, float velocityY) {
		
		Random rand = new Random();
		
		Random direction = new Random();
		
		this.velocityX = rand.nextFloat(-4, 4);
		this.velocityY = rand.nextFloat(5, 8);
		
		System.out.println("dawda");
	}
	
}
