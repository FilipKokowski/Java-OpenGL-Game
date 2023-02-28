package org.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.gameobjects.BodyPart;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class BodyPartsHandler {
	
	private float parentX;
	private float parentY;
	private float parentAngle;
	private float parentVelocityX;
	private float parentVelocityY;
	
	public ArrayList<BodyPart> bodyParts = new ArrayList<BodyPart>();
	
	public BodyPartsHandler(String path) {
		InputStream url = getClass().getClassLoader().getResourceAsStream(path);
		
	
		try {
			
			Object jsonFile = new JSONParser().parse(new InputStreamReader(url));
			JSONObject jsonObject = (JSONObject) jsonFile;
			
			iterateThroughJSON(jsonObject);
		
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		for(BodyPart part : bodyParts) {
			if(Integer.valueOf(part.sideID) == 0) {
				Handler.addGO(part);
				//part.printValues();
				
				if(part.partID.equals("0"))
					part.setBasePart();
			}
		}
	}
	
	private void iterateThroughJSON(JSONObject json) {
		
		boolean skip = false;
		
		for(Object side : json.keySet()) {
			
			JSONObject parts = (JSONObject) json.get(side);
			
			for(Object temp : parts.keySet()) {
				
				//System.out.println("\n============= - " + side + " " + temp);
				
				try {
					JSONObject part = (JSONObject) parts.get(temp);
					
					
					for(Object element : part.keySet()) {
						try {
							JSONObject tmp = (JSONObject) part.get(element);
							//System.out.println(tmp.get("src").toString());
							//System.out.println(tmp);
							
							BodyPart newPart = new BodyPart(tmp.get("src").toString(), parts.get("id").toString(), tmp.get("id").toString(), tmp.get("x").toString(), tmp.get("y").toString(), tmp.get("jointWith").toString(), tmp.get("jointX").toString(), tmp.get("jointY").toString());
							bodyParts.add(newPart);
							
						} catch(ClassCastException e) {
							//System.out.println(part.get("src").toString());
							
							try {
								BodyPart newPart = new BodyPart(part.get("src").toString(), parts.get("id").toString(), part.get("id").toString(), part.get("x").toString(), part.get("y").toString(), part.get("jointWith").toString(), part.get("jointX").toString(), part.get("jointY").toString());
								bodyParts.add(newPart);

								
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							//System.out.println(parts.get(temp));
							skip = true;
							break;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(skip) {
						skip = false;
						continue;
					}
					//System.out.println(part);
				} catch(ClassCastException e) {
					//System.out.println(temp + ": " + parts.get(temp));
				}
				
			}
		}
	}
	
	public void passPosition(float x, float y, float velocityX, float velocityY, float rotation) {
		
		parentX = x;
		parentY = y;
		parentAngle = rotation;
		parentVelocityX = velocityX;
		parentVelocityY = velocityY;
		
		BodyPart basePart = null;
		
		for(BodyPart bodyPart : bodyParts) {
			if(bodyPart.getBasePartID().equals(bodyPart.partID)) {
				basePart = bodyPart;
				
				bodyPart.hasJoints = false;
				bodyPart.parentX = x;
				bodyPart.parentY = y;
				bodyPart.parentAngle = rotation;
				bodyPart.isDraggable = false;
			}
		}
		
		if(basePart == null){
			System.out.println("No base part found!");
			return;
		}
		
		for(BodyPart bodyPart : bodyParts) {
			if(!bodyPart.getBasePartID().equals(bodyPart.partID)) {
				for(BodyPart parentPart : bodyParts) {
					if(bodyPart.jointWithID.equals(parentPart.partID)) {
						bodyPart.parentX = parentPart.getX();
						bodyPart.parentY = parentPart.getY();
						bodyPart.parentAngle = parentPart.rotation;
					}
				}
			}
		}
	}
	
	public void collapse() {
		for(BodyPart bodyPart : bodyParts) {
			if(!bodyPart.collapse) {
				bodyPart.setVelocity(parentVelocityX, parentVelocityY);
				bodyPart.collapse = true;
				bodyPart.isDraggable = true;
			}
		}
	}
	
	public void assemble() {
		for(BodyPart bodyPart : bodyParts) {
			if(bodyPart.collapse) {
				bodyPart.collapse = false;
				bodyPart.isDraggable = false;
			}
		}
	}
	
	public void moveJoint(float xShift, float yShift, String ID) {
		
		for(BodyPart bodyPart : bodyParts) {
			if(bodyPart.partID.equals(ID)) {
				bodyPart.jointOffsetX += xShift * GameLoop.updateDelta();
				bodyPart.jointOffsetY += yShift * GameLoop.updateDelta();
			}
		}
	}
	
	public void printPosition() {
		System.out.println("Parent position (" + parentX + "/" + parentY + "/" + parentAngle + ")");
	}
	
}
