package org.scene.entities;

import org.engine.GameLoop;
import org.engine.Handler;
import org.gameobjects.Entities;
import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.graphics.Animation;
import org.graphics.Graphics;
import org.graphics.Renderer;
import org.input.KeyInput;
import org.resource.ImageResource;

import com.jogamp.newt.event.KeyEvent;

public class Square extends Entities{
	
	
	private static float HEIGHT = 1.25f;
	private static float WIDTH = HEIGHT/2;
	
	//False == left, True == right
	private static boolean lastFacing = false;
	
	public Square() {
		super(0,0,WIDTH, HEIGHT);
		
		reloadCrouchHeight();
		
		jumpForce = 14;
		reloadCrouchJumpForce();
		
		id = ID.Player;
		
		currentAnimation = 0;
	
		animations = new Animation[10];
		
		animations[0] = new Animation();
		animations[0].frames = new ImageResource[1];
		animations[0].frames[0] = new ImageResource("/res/org/scene/entities/Player/Idle/idle.png");
		
		animations[1] = new Animation();
		animations[1].frames = new ImageResource[4];
		animations[1].frames[0] = new ImageResource("/res/org/scene/entities/Player/WalkLeft/walk0.png");
		animations[1].frames[1] = new ImageResource("/res/org/scene/entities/Player/WalkLeft/walk1.png");
		animations[1].frames[2] = new ImageResource("/res/org/scene/entities/Player/WalkLeft/walk2.png");
		animations[1].frames[3] = new ImageResource("/res/org/scene/entities/Player/WalkLeft/walk3.png");
		
		animations[2] = new Animation();
		animations[2].frames = new ImageResource[4];
		animations[2].frames[0] = new ImageResource("/res/org/scene/entities/Player/WalkRight/walk0.png");
		animations[2].frames[1] = new ImageResource("/res/org/scene/entities/Player/WalkRight/walk1.png");
		animations[2].frames[2] = new ImageResource("/res/org/scene/entities/Player/WalkRight/walk2.png");
		animations[2].frames[3] = new ImageResource("/res/org/scene/entities/Player/WalkRight/walk3.png");
		
		animations[3] = new Animation();
		animations[3].frames = new ImageResource[1];
		animations[3].frames[0] = new ImageResource("/res/org/scene/entities/Player/Idle/idleSkate.png");
		
		animations[4] = new Animation();
		animations[4].frames = new ImageResource[1];
		animations[4].frames[0] = new ImageResource("/res/org/scene/entities/Player/Idle/CrouchRightIdle.png");
		
		animations[5] = new Animation();
		animations[5].frames = new ImageResource[1];
		animations[5].frames[0] = new ImageResource("/res/org/scene/entities/Player/Idle/CrouchLeftIdle.png");
		
		animations[6] = new Animation();
		animations[6].frames = new ImageResource[4];
		animations[6].frames[0] = new ImageResource("/res/org/scene/entities/Player/CrouchLeft/CrouchLeft1.png");
		animations[6].frames[1] = new ImageResource("/res/org/scene/entities/Player/CrouchLeft/CrouchLeft2.png");
		animations[6].frames[2] = new ImageResource("/res/org/scene/entities/Player/CrouchLeft/CrouchLeft3.png");
		animations[6].frames[3] = new ImageResource("/res/org/scene/entities/Player/CrouchLeft/CrouchLeft4.png");
		
		animations[7] = new Animation();
		animations[7].frames = new ImageResource[4];
		animations[7].frames[0] = new ImageResource("/res/org/scene/entities/Player/CrouchRight/CrouchRight1.png");
		animations[7].frames[1] = new ImageResource("/res/org/scene/entities/Player/CrouchRight/CrouchRight2.png");
		animations[7].frames[2] = new ImageResource("/res/org/scene/entities/Player/CrouchRight/CrouchRight3.png");
		animations[7].frames[3] = new ImageResource("/res/org/scene/entities/Player/CrouchRight/CrouchRight4.png");
		
		animations[8] = new Animation();
		animations[8].frames = new ImageResource[1];
		animations[8].frames[0] = new ImageResource("/res/org/scene/entities/Player/Idle/idleSkateCrouchLeft.png");
		
		animations[9] = new Animation();
		animations[9].frames = new ImageResource[1];
		animations[9].frames[0] = new ImageResource("/res/org/scene/entities/Player/Idle/idleSkateCrouchRight.png");
		
		speed = 4;
		speedCap = 8;
	}
	
	public void render() {
		Graphics.setColor(1, 1, 1, 1);
		Graphics.fillRect(x, y, width, height);
	}
	
	
	@Override
	public void update() {
		
		//If player's not moving play idle animation
		if(!KeyInput.getKey(KeyEvent.VK_A) && !KeyInput.getKey(KeyEvent.VK_D)) { 
			if(!crouched)
				currentAnimation = 0;
			else {
				if(lastFacing)
					currentAnimation = 4;
				else
					currentAnimation = 5;
			}
		}
		
	
		
		if(velocityX <= friction && velocityX >= -friction) velocityX = 0;
		else if(velocityX != 0)	{
			velocityX += (velocityX >= 0) ? (-friction) : (friction);
			
			if(!KeyInput.getKey(KeyEvent.VK_A) && !KeyInput.getKey(KeyEvent.VK_D)) {
				if(crouched) {
					if(lastFacing) currentAnimation = 9;
					else currentAnimation = 8;
				}
				else
					currentAnimation = 3;
			}
		}
			
		
		if(velocityY <= friction && velocityY >= -friction) velocityY = 0;	
		else if(velocityY != 0) 
			velocityY += (velocityY >= 0) ? (-friction) : (friction);
		
		
		if(y > (-Renderer.unitsTall + height) / 2) { 
			gravity();
			onGround = false;
			
			y += velocityY * GameLoop.updateDelta();
			
			//Collision detection
			for(int i = 0; i < Handler.gameObjects.size(); i++) {
				
				//Grab one of gameObjects
				GameObject tempObj = getAt(i);
				
				//Check if objects ID is ID.Obstacle and is intersecting with player
				if(tempObj.id == ID.Obstacle && doOverlap(getBounds(), tempObj.getBounds())){
					
					//If player is more to the left side of obstacle, trigger left collision
					if(x < tempObj.getX()) {
							collisionR = true;
					}
					//If player is more to the right side of obstacle, trigger right collision
					else if(x > tempObj.getX()) {
							collisionL = true;
					}

					//If player is beetween X1 and X2 of tempobject, stop falling
					if(y - height / 2 <= tempObj.getY() + tempObj.getHeight() / 2 && y - height / 2 >= tempObj.getY() + tempObj.getHeight() / 2 - .25f) {
						
						//Do not set y to the top of tempObj if neither of the walls of player are colliding with walls of the tempObj
						if(x - width / 2 != tempObj.getX() + tempObj.getWidth() / 2 && x + width / 2 != tempObj.getX() - tempObj.getWidth() / 2) {
							collisionD = true;
							collisionR = false;
							collisionL = false;
							
							velocityY = 0;
							y = tempObj.getY() + tempObj.getHeight() / 2 + height / 2;
						}
					}
					
					if(y + height / 2 >= tempObj.getY() - tempObj.getHeight() / 2 && y + height / 2 <= tempObj.getY() - tempObj.getHeight() / 2 + .25f) {
						
						//When player is under object set forceCrouch and up collision to true
						if(x - width / 2 != tempObj.getX() + tempObj.getWidth() / 2 && x + width / 2 != tempObj.getX() - tempObj.getWidth() / 2) {
							velocityY = 0;	
							y = tempObj.getY() - tempObj.getHeight() / 2 - height / 2;
							
							collisionU = true;
							forceCrouch = true;
							
							//Prevents player from recognizing up collision as left or right collision
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
					}
				
				}
				//When player is below object but not colliding with it, but his standing height is colliding with tempObj trigger forceCrouch
				if(tempObj.id == ID.Obstacle && !collisionU && x - width / 2 < tempObj.getX() + tempObj.getWidth() / 2 && x + width / 2 > tempObj.getX() - tempObj.getWidth() / 2) {
					if(y + HEIGHT / 2 >= tempObj.getY() - tempObj.getHeight() / 2 && y + HEIGHT / 2 <= tempObj.getY() - tempObj.getHeight() / 2 + 1f) {
						forceCrouch = true;
					}
				}

			}
			
			//Jump if player's standing on obstacle
			if((KeyInput.getKey(KeyEvent.VK_W) || KeyInput.getKey(KeyEvent.VK_SPACE)) && collisionD){
				velocityY = jumpForce;
				y += velocityY * GameLoop.updateDelta();
			}
		}
		else {
			
			onGround = true;
			
			//Set y to bottom of the screen
			y = (-Renderer.unitsTall + height) / 2; 
			
			//Jumping 
			if(KeyInput.getKey(KeyEvent.VK_W) || KeyInput.getKey(KeyEvent.VK_SPACE)){
				velocityY = jumpForce;
				y += velocityY * GameLoop.updateDelta();
			}
			
			//Collision detection
			for(int i = 0; i < Handler.gameObjects.size(); i++) {
				GameObject tempObj = getAt(i);
				
				
				if(tempObj.id == ID.Obstacle && doOverlap(getBounds(), tempObj.getBounds())){
					velocityX = 0;
					//System.out.println("Touching");
					
					if(y + height / 2 >= tempObj.getY() - tempObj.getHeight() / 2 && y + height / 2 <= tempObj.getY() - tempObj.getHeight() / 2 + .25f) {
						
						//When player is under object set forceCrouch and up collision to true
						if(x - width / 2 >= tempObj.getX() + tempObj.getWidth() / 2 && x + width / 2 <= tempObj.getX() - tempObj.getWidth() / 2) {
							velocityY = 0;
							y = tempObj.getY() - tempObj.getHeight() / 2 - height / 2;
							
							collisionU = true;
							forceCrouch = true;
							
							//Prevents player from recognizing up collision as left or right collision
							collisionL = false;
							collisionR = false;
							
						}
					}
					
					//If player is more to the left side of obstacle, trigger left collision
					if(x < tempObj.getX() && !collisionU) {
						collisionR = true;
						x = tempObj.getX() - tempObj.getWidth() / 2 - width / 2;
					}
					
					//If player is more to the right side of obstacle, trigger right collision
					else if(x > tempObj.getX() && !collisionU) {
						collisionL = true;
						x = tempObj.getX() + (tempObj.getWidth() + width) / 2;
					}
				}
				
				if(tempObj.id == ID.Obstacle && !collisionU && x - width / 2 <= tempObj.getX() + tempObj.getWidth() / 2 && x + width / 2 >= tempObj.getX() - tempObj.getWidth() / 2) {
					if(y + HEIGHT / 2 >= tempObj.getY() - tempObj.getHeight() / 2 && y + HEIGHT / 2 <= tempObj.getY() - tempObj.getHeight() / 2 + 1f) {
						forceCrouch = true;
					}
				}
			}
			
			
		}
		
		
		//Moving left
		if(KeyInput.getKey(KeyEvent.VK_A) && !collisionL) { 
			lastFacing = false;
			if(velocityX >= -speedCap) {
				velocityX -= speed * acceleration; 
				if(!crouched) {
					currentAnimation = 1;	
				}
				else currentAnimation = 6;
				 
			}else velocityX += .25f;
		}
		
		//Moving right
		if(KeyInput.getKey(KeyEvent.VK_D) && !collisionR) {
			lastFacing = true;
			if(velocityX <= speedCap) {
				velocityX += speed * acceleration; 
				if(!crouched) {
					currentAnimation = 2;	
				}
				else currentAnimation = 7; 
			}
			else velocityX -= .25f;
		}
		
		if((onGround || collisionD) && crouched) speedCap = 2;
		
		//Crouching
		if(KeyInput.getKey(KeyEvent.VK_SHIFT) && !crouched) {
			
			if(onGround || collisionD)
				speedCap = crouchSpeed;
			
			//speed = crouchSpeed;
			jumpForce = crouchJumpForce;
			
			height = crouchHeight;
			y -= height / 4;
			
			crouched = true;
			
			if(lastFacing)
				currentAnimation = 4;
			else 
				currentAnimation = 5;
		}
		else if(!KeyInput.getKey(KeyEvent.VK_SHIFT) && !forceCrouch){
			//Resets speed back to the one assigned before
			//reloadSpeed();
			
			height = (crouchHeight / 2) * 3f;
			
			jumpForce = crouchJumpForce * 2;
			
			if(crouched) { 
				y += height / 6;
				speedCap = 8;
			}
			
			crouched = false;
		}
		
		boolean reset = false;
		
		if(KeyInput.getKey(KeyEvent.VK_X) && !reset) {
			y = 0;
			x = 0;
			velocityX = 0;
			velocityY = 0;
			
			reset = true;
		} else if(!KeyInput.getKey(KeyEvent.VK_X) && reset) reset = false;
		
		//Change x based on velocity
		x += velocityX * GameLoop.updateDelta();
		
		
		//Camera follow player
		Camera.x += (x - Camera.x) * speed * GameLoop.updateDelta();
		
		//System.out.println("forceCrouch = " + forceCrouch);
		
		/*System.out.println(
		"\n\nUp collision: " + collisionU + 
		"\nDown collision: " + collisionD +  
		"\nLeft collision: " + collisionL + 
		"\nRight collision: " + collisionR 	
		);*/
		/*System.out.println(
		"\n\nX: " + x +		
		"\nY: " + y +
		"\nWidth: " + width + 
		"\nHeight: " + height + 
		"\nCrouch height: " + crouchHeight
		);*/
		
		forceCrouch = false;
		
		clearCollision();

		
	}
	
}
