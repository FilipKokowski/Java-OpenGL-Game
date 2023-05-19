package org.scene.entities;

import org.engine.BodyPartsHandler;
import org.engine.GameLoop;
import org.engine.Handler;
import org.gameobjects.Color;
import org.gameobjects.Entities;
import org.gameobjects.ID;
import org.graphics.EventListener;
import org.graphics.Graphics;
import org.input.KeyInput;

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
	
	private static String imagePath = "";
	
	public Player(){
		super(0, 0, initialWidth, initialHeight, imagePath);
		
		HEIGHT = initialHeight;
		WIDTH = initialWidth;
		
		jumpForce = 7;
		mass = 5;
		
		id = ID.Player;
		
		currentAnimation = 0;
	
		speed = 3;
		speedCap = 1.5f;
		
		collisionOn = false;
		
		reloadCrouchHeight();
		reloadCrouchJumpForce();
		reloadCrouchSpeedCap();
		
		bodyPartsHandler = new BodyPartsHandler("res/org/Entities/Skeleton.json");
		
		fontSize = 16;
		setCustomFont("res/org/fonts/pixelmix.ttf");
		setTextColor(0,0,1,1);
	}
	
	public void render() {
		if(imagePath.equals("")) {
			Graphics.Rotate(-rotation);
			Graphics.setColor(0, 0, 0, 0);
			Graphics.drawRect(x, y, width, height);
			Graphics.setColor(Color.clear());
			Graphics.Rotate(0);

		}
		else {
			super.render();
		}
		
		//collider.renderAxes(0, 1, 0, 1);
		
	}
	
	
	@Override
	public void update() {
		
		super.update();
		
		//getChildsVelocity();
		
		centerTextHorizontally();
		placeTextAbove();
		
		text = "("+ x + "/" + y +")";
		
		bodyPartsHandler.passPosition(this);
		
		if(KeyInput.getKey(KeyEvent.VK_G)) {
			//System.out.println("collapse");
			bodyPartsHandler.collapse();
		}
		
		if(KeyInput.getKey(KeyEvent.VK_H)) {
			//System.out.println("collapse");
			bodyPartsHandler.assemble();
		}
		
		float shiftX = 0;
		float shiftY = 0;
		
		if(KeyInput.getKey(KeyEvent.VK_5)) {
			shiftX = .5f;
		}
		else if(KeyInput.getKey(KeyEvent.VK_6)) {
			shiftX = -.5f;
		}

		if(KeyInput.getKey(KeyEvent.VK_7)) {
			shiftY = -.5f;
		}
		else if(KeyInput.getKey(KeyEvent.VK_8)) {
			shiftY = .5f;
		}
		
		bodyPartsHandler.moveJoint(shiftY, shiftX, "1");
		
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
	
		//System.out.println("rotation: " + rotation);
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
