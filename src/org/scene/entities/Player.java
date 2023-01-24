package org.scene.entities;


import java.io.IOException;

import org.engine.GameLoop;
import org.engine.Handler;
import org.gameobjects.BodyParts;
import org.gameobjects.Entities;
import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.graphics.Graphics;
import org.graphics.Renderer;
import org.input.KeyInput;
import org.input.MouseInput;

import com.jogamp.newt.event.KeyEvent;

public class Player extends Entities{
	
	private static final float WHRatio = 2.3f;
	
	//w 1 h 2,3
	private static final float initialHeight = 1.15f;
	private static final float initialWidth = initialHeight/WHRatio;
	
	//False == left, True == right
	private static boolean lastFacing = false;
	
	private Boomerang boomer = new Boomerang(x, y, .1875f, .375f);
	private boolean boomerDeployed;
	
	private static String animationPath = "/res/org/animations/Player.txt";
	
	public Player(){
		super(0, 0, initialWidth, initialHeight, animationPath);
		
		HEIGHT = initialHeight;
		WIDTH = initialWidth;
		
		jumpForce = 7;
		mass = 3;
		
		id = ID.Player;
		
		currentAnimation = 0;
	
		speed = 3;
		speedCap = 1.5f;
		
		reloadCrouchHeight();
		reloadCrouchJumpForce();
		reloadCrouchSpeedCap();
		
		String paths[] = new String[14];
		paths[0] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontBody.png";
		paths[1] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontHead.png";
		paths[2] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontLeftArmDown.png";
		paths[3] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontLeftArmUp.png";
		paths[4] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontLeftHand.png";
		paths[5] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontLeftFoot.png";
		paths[6] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontLeftLegDown.png";
		paths[7] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontLeftLegUp.png";
		paths[8] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontRightArmDown.png";
		paths[9] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontRightArmUp.png";
		paths[10] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontRightHand.png";
		paths[11] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontRightLegDown.png";
		paths[12] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontRightLegUp.png";
		paths[13] = "/res/org/scene/entities/Skeleton/Parts/Front/FrontRightFoot.png";
		
		try {
			for(int i=0; i < 14; i++) {
				BodyParts bodyPart = new BodyParts(paths[i]);
				bodyPart.setX(i);
				bodyPart.currentAnimation = i;
				Handler.addGO(bodyPart);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*public void render() {
		Graphics.setColor(1, 1, 1, 1);
		Graphics.fillRect(x, y, width, height);
	}*/
	
	
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
		
		//Applying gravity and collision detection
		applyPhysics(true, true);
		
		//Jump if player's standing on obstacle
		if((KeyInput.getKey(KeyEvent.VK_W) || KeyInput.getKey(KeyEvent.VK_SPACE)) && onGround){
			velocityY = jumpForce;
			y += velocityY * GameLoop.updateDelta();
			currentAnimation = 10;
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
		
		if((onGround || collisionD) && crouched) speedCap = crouchSpeedCap;
		
		//Crouching
		if((KeyInput.getKey(KeyEvent.VK_SHIFT) && !crouched) ) {
			
			if(onGround || collisionD)
				speedCap = crouchSpeedCap;
			
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
				reloadSpeedCap();
			}
			
			crouched = false;
		}
		
		if(KeyInput.getKey(KeyEvent.VK_T) && !boomerDeployed) {
			boomer.setX(x);
			boomer.setY(y);
			boomer.resetAngle();
			boomer.setFacing(lastFacing);
			boomer.setCooldown(5);
			
			//System.out.println("x: " + boomer.getX());
			//System.out.println("y: " + boomer.getY());
			
			Handler.addGO(boomer);
			boomerDeployed = true;
		}
		
		boolean reset = false;
		
		if(KeyInput.getKey(KeyEvent.VK_X) && !reset) {
			y = 0;
			x = 0;
			velocityX = 0;
			velocityY = 0;
			
			reset = true;
		} else if(!KeyInput.getKey(KeyEvent.VK_X) && reset) reset = false;
		
		if(boomer.destroy) {
			Handler.removeGO(boomer);
			boomer.destroy = false;
			
			boomerDeployed = false;
		}
		
		
		//Change x based on velocity
		x += velocityX * GameLoop.updateDelta();
		
		boomer.cooldown -= GameLoop.updateDelta();
		
		
		//Camera follow player
		Camera.x += (x - Camera.x) * speed * GameLoop.updateDelta();
		
		draggable();
		drawBounds();
		
		//System.out.println("x = " + x + " y = " + y);
		//System.out.println("mouseX = " + MouseInput.getMouseX() + " mouseY = " + MouseInput.getMouseY());
		
		//System.out.println("Player down collision = " + collisionD);
		//System.out.println("Player onGround = " + onGround);
		
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
		"\nCrouch height: " + crouchHeight +
		"\nSpeed cap: " + speedCap +
		"\nCrouch speed cap: " + crouchSpeedCap 
		);*/
		
		forceCrouch = false;
		
		//drawBounds();
		
		clearCollision();

		
	}
	
}
