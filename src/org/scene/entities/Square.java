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
	
	
	private static float height = 1.5f;
	private static float width = height/2;
	
	public Square() {
		super(0,0,width, height);
		
		id = ID.Player;
		
		currentAnimation = 0;
	
		animations = new Animation[4];
		
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
		
		speed = 4;
		speedCap = 8;
	
	}
	
	@Override
	public void update() {
		
		if(!KeyInput.getKey(KeyEvent.VK_A) && !KeyInput.getKey(KeyEvent.VK_D)) { currentAnimation = 0; }
		
		if(KeyInput.getKey(KeyEvent.VK_A)) { 
			if(velocityX >= -speedCap)
				velocityX -= speed * acceleration; currentAnimation = 1; 
		}
		if(KeyInput.getKey(KeyEvent.VK_D)) {
			if(velocityX <= speedCap)
				velocityX += speed * acceleration; currentAnimation = 2; 
		}
		
	
		
		if(velocityX <= friction && velocityX >= -friction) velocityX = 0;
		else if(velocityX != 0)	{
			velocityX += (velocityX >= 0) ? (-friction) : (friction);
			
			if(!KeyInput.getKey(KeyEvent.VK_A) && !KeyInput.getKey(KeyEvent.VK_D))
				currentAnimation = 3;
		}
			
		if(velocityY <= friction && velocityY >= -friction) velocityY = 0;	
		else if(velocityY != 0) 
			velocityY += (velocityY >= 0) ? (-friction) : (friction);
		
			
		x += velocityX * GameLoop.updateDelta();
		
		
		if(y > (-Renderer.unitsTall + height) / 2) { 
			gravity();
			y += velocityY * GameLoop.updateDelta();
		}
		else {
			for(int i = 0; i < Handler.gameObjects.size(); i++) {
				GameObject tempObj = getAt(i);
				
				if(doOverlap(getBounds(), tempObj.getBounds()) && tempObj.id == ID.Entities){
					System.out.println("Overlap");
				}
			}
			y = (-Renderer.unitsTall + height) / 2;
			if(KeyInput.getKey(KeyEvent.VK_W) || KeyInput.getKey(KeyEvent.VK_SPACE)){
				velocityY = jumpForce;
				y += velocityY * GameLoop.updateDelta();
			}
		}
		
		Camera.x += (x - Camera.x) * speed * GameLoop.updateDelta();
		//System.out.println(x + "-" + y);
	}
	
}
