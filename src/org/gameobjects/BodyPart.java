package org.gameobjects;

import java.io.IOException;

import org.engine.GameLoop;
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
	
	public String jointWithID;
	public String jointWithX;
	public String jointWithY;
	public float jointOffsetX;
	public float jointOffsetY;
	public boolean jointRelocating;
	public boolean hasJoints = true;
	
	private static boolean basePart;
	private static String basePartID;
	
	public boolean collapse;
	
	public BodyPart(String src, String sideID, String partID, String xOffset, String yOffset, String jointWithID, String jointOffsetX, String jointOffsetY) throws IOException {
		super(0, 0, 0, 0, src);
		
		this.texture = new ImageResource(src);
		this.textureSrc = src;
		this.sideID = sideID;
		this.partID = partID;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.jointWithID = jointWithID;
		//this.jointOffsetX = jointOffsetX;
		//this.jointOffsetY = jointOffsetY;
		
		isDraggable = false;

		float pixelWidth = texture.getWidth();
		float pixelHeight = texture.getHeight();
		float WHRatio = pixelWidth / pixelHeight;
		
		width = pixelWidth / 250;
		height = width / WHRatio;
		
		//Default width and height
		WIDTH = width;
		HEIGHT = height;	
		//System.out.println("\nxOffset = " + xOffset + "\nyOffset = " + yOffset);
		
		if(!hasJoints) showJoints = false;
		else showJoints = true;
		
		showBounds = true;
		
	}
	
	public void update() {
		applyPhysics(true, true);
		draggable();
		
		
		
		if(!collapse) {
			//rotation += 2.5f;
			x = parentX - Float.valueOf(xOffset);
			y = parentY - Float.valueOf(yOffset);
		}
		else {
			x -= velocityX * GameLoop.updateDelta();
		}

		jointPointX = x + jointOffsetX;
		jointPointY = y + jointOffsetY;
		
		if(partID.equals("2A")) {
			//System.out.println(
				//"ID: " + partID +
				//"velocityX : " + velocityX
				//"jointOffsetX = " + jointOffsetX +
				//"jointOffsetY = " + jointOffsetY +
				//" \"x\":\"" + (xOffset) + "\"  /  \"y\":\"" + (yOffset) + "\""+
				//"  parent pos =  " + parentX + "  /  " + parentY
			//);	
		}
		//rotation = parentAngle;
	}
	
	/*public void render() {
		Graphics.setColor(1, 1, 1, 1);
		Graphics.fillRect(x, y, width, height);
	}*/
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void printValues() {
		System.out.println(
			"\nx = " + xOffset +
			"\ny = " + yOffset +
			"\nside ID = " + sideID +
			"\npart ID = " + partID + 
			"\ntexture src = " + textureSrc +
			"\nParent position (" + parentX + "/" + parentY + "/" + parentAngle + ")" +
			"\nJoint with " + jointWithID +
			"\nJoint offset x =" + jointOffsetX +
			"\nJoint offset y =" + jointOffsetY 
		);
	}
	
	public String getBasePartID() {
		return basePartID;
	}
	
	public void setBasePart() {
		if(!basePart) {
			basePart = true;
			basePartID = partID;
		}
		else
			System.out.println("Part with ID " + basePartID + " is base part");
	}
	
	public void setVelocity(float velocityX, float velocityY) {
		this.velocityX = (float) (-2 + Math.random() * (2 + 2));
		this.velocityY = (float) (5 + Math.random() * (8 - 5));
	}
}
