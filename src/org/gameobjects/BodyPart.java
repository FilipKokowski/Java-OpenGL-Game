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
	
	public GameObject parentObject;
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
	
	public boolean collides = false;
	
	public BodyPart(String src, String sideID, String partID, String xOffset, String yOffset, String jointWithID, String jointOffsetX, String jointOffsetY) throws IOException {
		super(0, 0, 0, 0, src, ID.BodyPart);
		
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
		collides = false;
		
		if(parentObject != null && collider.objectsToIgnore.isEmpty()) {
			for(BodyPart bodyPart : parentObject.bodyPartsHandler.bodyParts) {
				collider.objectsToIgnore.add(bodyPart);	
			}
		}
		
		super.update();
		
		deactiveteWhenOutOfView();
		
		//If Player is in one piece, gravity for body parts is disabledd
		applyPhysics(collapse, true);
		
		draggable();
		
		if(!collapse) {
			//rotation += 2.5f;
			position.x = parentX - Float.valueOf(xOffset);
			position.y = parentY - Float.valueOf(yOffset);
		}
		else {
			position.x -= velocityX * GameLoop.updateDelta();
		}

		jointPointX = position.x + jointOffsetX;
		jointPointY = position.y + jointOffsetY;

		if(collapse)
			text = String.valueOf(velocityY);
		else
			text = "";
		
		
	}
	
	public void render() {
		super.render();
		
		//Graphics.setColor(new Color(255,0,255,255));
		//Graphics.drawRect(centerOfMass.x + position.x, centerOfMass.y + position.y, .05f, .05f);
		//Graphics.setColor(Color.clear());
				
		collider.renderAxes(0,1,0,1);
	}
	
	public void setX(float x) {
		this.position.x = x;
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
	
	public void passParentObject(GameObject parentObject) {
		this.parentObject = parentObject;
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
