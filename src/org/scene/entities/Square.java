package org.scene.entities;

import org.engine.GameLoop;
import org.gameobjects.Entities;
import org.graphics.Graphics;
import org.graphics.Renderer;
import org.input.KeyInput;

import com.jogamp.newt.event.KeyEvent;

public class Square extends Entities{
	
	public Square() {
		width = 1f;
		height = 1f;
		
		speed = 5;
	
	}
	
	@Override
	public void render() {
		Graphics.setColor(.25f, .75f, .5f, 1);
		Graphics.fillRect(x, y, width, height);
	}
	
	@Override
	public void update() {
		if(KeyInput.getKey(KeyEvent.VK_A)) velocityX -= speed * acceleration;
		if(KeyInput.getKey(KeyEvent.VK_D)) velocityX += speed * acceleration;
		
		if(velocityX <= friction && velocityX >= -friction) velocityX = 0;
		else if(velocityX != 0)	{
			velocityX += (velocityX >= 0) ? (-friction) : (friction);
		}
			
		if(velocityY <= friction && velocityY >= -friction) velocityY = 0;	
		else if(velocityY != 0)
			velocityY += (velocityY >= 0) ? (-friction) : (friction);
		
			
		x += velocityX * GameLoop.updateDelta();
		
		if(y >= (-Renderer.unitsTall + height) / 2) { 
			gravity();
			y += velocityY * GameLoop.updateDelta();
		}
		else {
			if(KeyInput.getKey(KeyEvent.VK_W) || KeyInput.getKey(KeyEvent.VK_SPACE)){
				velocityY = jumpForce;
				y += velocityY * GameLoop.updateDelta();
			}
		}
		
		Camera.x = x;
		//System.out.println(x + "-" + y);
	}
	
}
