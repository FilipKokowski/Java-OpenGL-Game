package org.gameobjects;

import org.engine.GameLoop;
import org.engine.Handler;
import org.graphics.Renderer;
import org.scene.entities.Camera;

public class Entities extends GameObject {
	
	public static boolean physicsOn;
	
	public float HEIGHT;
	public float WIDTH;
	
	public BodyPart mainBodyPart;
	
	public boolean crouched = false;
	public boolean forceCrouch = false;

	public float speed = 3;
	public float crouchSpeedCap = speed / 1.5f;
	public float speedCap = 6;
	public float acceleration = speed / 100;
	public float friction = speed * acceleration / 2;
	
	public float jumpForce = 6;
	public float crouchJumpForce = jumpForce / 2;
	public float mass = 3;
	public float G = 9.81f;
	
	public Camera camera = new Camera();
	public int cameraID = -1;
	
	public boolean collisionR = false;
	public boolean collisionL = false;
	public boolean collisionU = false;
	public boolean collisionD = false;
	
	public Vector dir = new Vector(0,0);
	
	public Entities(float x, float y, float width, float height, String textureSrc, ID id) {
		super(x, y, width, height, textureSrc, id);
	}
	
	public void gravity() { velocityY -= mass/G; };
	
	public void reloadCrouchJumpForce() {
		crouchJumpForce = jumpForce / 2;
	}
	
	public void reloadJumpForce() {
		jumpForce = crouchJumpForce / 2;
	}
	
	public void reloadCrouchSpeedCap() {
		crouchSpeedCap = speedCap / 1.5f;
	}
	
	public void reloadSpeedCap() {
		speedCap = crouchSpeedCap * 1.5f;
	}
	
	public void linkBodyPart(BodyPart bodyPart) {
		mainBodyPart = bodyPart;
	}
	
	public void unlinkBodyPart() {
		mainBodyPart = null;
	}
	
	public void applyPhysics(boolean GravityEnabled, boolean CollisionEnabled){
		if(physicsOn) {

			if(velocityX <= friction && velocityX >= -friction) velocityX = 0;
			else if(velocityX != 0 && onGround)	
				velocityX += (velocityX >= 0) ? (-friction) : (friction);

			if(position.y > (-Renderer.unitsTall + height) / 2) { 
				if(GravityEnabled)
					gravity();
				
				onGround = false;
				
				position.y += velocityY * GameLoop.updateDelta();
				
				if(CollisionEnabled) {
					//Collision detection
					for(int i = 0; i < Handler.gameObjects.size(); i++) {
						
						//Grab one of gameObjects
						GameObject tempObj = getAt(i);
						
						//System.out.println(this.getClass().getSimpleName() + " " + collider.minkowskiDiff(tempObj.collider, dir).x  +  " x " + collider.minkowskiDiff(tempObj.collider, dir).y);
						
						//System.out.println(collider.doCollide(tempObj.collider) + " " + tempObj.id);
						
						//Check if objects ID is ID.Obstacle and is intersecting with entity
						if(collisionOn && tempObj.collisionOn && collider.doOverlap(tempObj.collider)){
							
							//System.out.println("Collide with " + tempObj.uuid);
							
							//velocityY = 5;
							//velocityX = (velocityX > 0) ? (-velocityX * 2) : velocityX;
							//System.out.println("Collision with " + tempObj.getClass().getSimpleName() + " " + tempObj.uuid);
							/*//If entity is more to the left side of obstacle, trigger left collisionW
							if(x < tempObj.getX()) {
									collisionR = true;
							}
							//If entity is more to the right side of obstacle, trigger right collision
							else if(x > tempObj.getX()) {
									collisionL = true;
							}
		
							//If entity is beetween X1 and X2 of tempobject, stop falling
							if(y - height / 2 <= tempObj.getY() + tempObj.getHeight() / 2 && y - height / 2 >= tempObj.getY() + tempObj.getHeight() / 2 - .25f) {
								
								//Do not set y to the top of tempObj if neither of the walls of player are colliding with walls of the tempObj
								if(x - width / 2 != tempObj.getX() + tempObj.getWidth() / 2 && x + width / 2 != tempObj.getX() - tempObj.getWidth() / 2) {
									collisionD = true;
									onGround = true;
									collisionR = false;
									collisionL = false;
									
									velocityY = 0;
									y = tempObj.getY() + tempObj.getHeight() / 2 + height / 2;
								}
							}
							
							if(y + height / 2 >= tempObj.getY() - tempObj.getHeight() / 2 && y + height / 2 <= tempObj.getY() - tempObj.getHeight() / 2 + .125f) {
								
								//When entity is under object set forceCrouch and up collision to true
								if(x + width / 2 > tempObj.getX() - tempObj.getWidth() / 2 && x - width / 2 < tempObj.getX() + tempObj.getWidth() / 2) {
									velocityY = 0;	
									y = tempObj.getY() - tempObj.getHeight() / 2 - height / 2;
									
									collisionU = true;
									forceCrouch = true;
									
									//Prevents entity from recognizing up collision as left or right collision
									collisionL = false;
									collisionR = false;
									
								}
							}
							
							if(collisionR) {
								velocityX = 0;
								//System.out.println("tempObj.getX() - (tempObj.getWidth() + width) / 2 = " + (tempObj.getX() - (tempObj.getWidth() + width) / 2) + collisionR);
								x = tempObj.getX() - (tempObj.getWidth() + width) / 2;
							}
							if(collisionL) {
								velocityX = 0;
								//System.out.println("tempObj.getX() + (tempObj.getWidth() + width) / 2 = " + (tempObj.getX() + (tempObj.getWidth() + width) / 2) + collisionL);
								x = tempObj.getX() + (tempObj.getWidth() + width) / 2;
							}*/
						
						}
						
						//When player is below object but not colliding with it, but his standing height is colliding with tempObj trigger forceCrouch
						if(tempObj.id == ID.Obstacle && !collisionU && position.x - width / 2 < tempObj.getX() + tempObj.getWidth() / 2 && position.x + width / 2 > tempObj.getX() - tempObj.getWidth() / 2) {
							if(position.y - height / 2 + HEIGHT >= tempObj.getY() - tempObj.getHeight() / 2 && position.y - height / 2 + HEIGHT <= tempObj.getY() - tempObj.getHeight() / 2 + .5f) {
								forceCrouch = true;
							}
						}
		
					}
				}
				
			}
			else {
				onGround = true;
				//System.out.println(onGround + " " + this.getClass().getSimpleName());
				
				//Set y to bottom of the screen
				position.y = (-Renderer.unitsTall + height) / 2; 
				velocityY = 0;
				
				
				if(CollisionEnabled) {
					//Collision detection
					for(int i = 0; i < Handler.gameObjects.size(); i++) {
						GameObject tempObj = getAt(i);
						
						//System.out.println(this.getClass().getSimpleName() + " " + collider.minkowskiDiff(tempObj.collider, dir).x  +  " x " + collider.minkowskiDiff(tempObj.collider, dir).y);
							
						//System.out.println("Player: " + collider.parentObject.getX() + "/nObstacle: " + tempObj.collider.parentObject.getX() + ": " + collider.doCollide(tempObj.collider));
						if(collisionOn && tempObj.collisionOn && collider.doOverlap(tempObj.collider)){
							//System.out.println("Collide with " + tempObj.uuid);
							//velocityY = 5;
							//velocityX = (velocityX > 0) ? (-velocityX * 2) : velocityX;
							/*velocityX = 0;
							//System.out.println("Touching");
							
							if(y + height / 2 >= tempObj.getY() - tempObj.getHeight() / 2 && y + height / 2 <= tempObj.getY() - tempObj.getHeight() / 2 + .125f) {
								//When entity is under object set forceCrouch and up collision to true
								if(x + width / 2 > tempObj.getX() - tempObj.getWidth() / 2 && x - width / 2 < tempObj.getX() + tempObj.getWidth() / 2) {
									velocityY = 0;
									y = tempObj.getY() - tempObj.getHeight() / 2 - height / 2;
									collisionU = true;
									forceCrouch = true;
									
									//Prevents entity from recognizing up collision as left or right collision
									collisionL = false;
									collisionR = false;
									
								}
							}
							
							//If entity is more to the left side of obstacle, trigger left collision
							if(x < tempObj.getX() && !collisionU) {
								collisionR = true;
								x = tempObj.getX() - tempObj.getWidth() / 2 - width / 2;
							}
							
							//If entity is more to the right side of obstacle, trigger right collision
							else if(x > tempObj.getX() && !collisionU) {
								collisionL = true;
								x = tempObj.getX() + (tempObj.getWidth() + width) / 2;
							}
							*/
						}
						
						if(tempObj.id == ID.Obstacle && !collisionU && position.x - width / 2 < tempObj.getX() + tempObj.getWidth() / 2 && position.x + width / 2 > tempObj.getX() - tempObj.getWidth() / 2) {
							if(position.y - height / 2 + HEIGHT >= tempObj.getY() - tempObj.getHeight() / 2 && position.y - height / 2 + HEIGHT <= tempObj.getY() - tempObj.getHeight() / 2 + .5f) {
								forceCrouch = true;
							}
							else {
								//System.out.println(y - height / 2 + HEIGHT + " >= " + (tempObj.getY() - tempObj.getHeight() / 2));
								//System.out.println(y - height / 2 + HEIGHT + " <= "  + (tempObj.getY() - tempObj.getHeight() / 2 + .5f) + "\n\n");
						
							}
						}
						
						
					}
				}
				
				
			}
		}
	}
	
	public void clearCollision() {
		collisionR = false;
		collisionL = false;
		collisionU = false;
		collisionD = false;
	}
}
