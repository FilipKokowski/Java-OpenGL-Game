package org.gameobjects;

import java.io.IOException;

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
	
	public BodyPart(String src, String sideID, String partID, String xOffset, String yOffset) throws IOException {
		super(0, 0, 0, 0, src);
		
		this.texture = new ImageResource(src);
		this.textureSrc = src;
		this.sideID = sideID;
		this.partID = partID;
		this.xOffset = xOffset;
		this.yOffset = yOffset;

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
		
		applyPhysics(false, true);
		draggable();
		drawBounds();
		
		System.out.println(
			"ID: " + partID +
			"x = " + (parentX - x) +
			"y = " + (parentY +- y)
		);
		
		x = parentX + Float.valueOf(xOffset);
		y = parentY+ Float.valueOf(yOffset);
		//rotation = parentAngle;
		
		//System.out.println("Body part collisionD = " + collisionD);
		
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
	
}
