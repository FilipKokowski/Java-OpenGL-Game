package org.input;

import org.graphics.Graphics;
import org.graphics.Renderer;
import org.scene.entities.Camera;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class MouseInput implements MouseListener {

	public static float x = 0;
	public static float y = 0;
	
	public static float initialX = 0;
	public static float initialY = 0;
	
	public static float lastCheckedX = 0;
	public static float lastCheckedY = 0;
	
	public static float mouseVelocityX = 0;
	public static float mouseVelocityY = 0;
	
	public static boolean pressed;
	public static boolean draggingSmth;
	
	public static float rotation = 0;
	public static float rotationSpeed = 5;
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		Graphics.drawLine(initialX, initialY, e.getX(), e.getY());
		
		pressed = true;
		mouseMoved(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x = ((Renderer.unitsWide / Renderer.getWindowWidth()) * e.getX() - Renderer.unitsWide/2) + Camera.x;
		
		float unitsTall = Renderer.unitsWide * (float) ((float)Renderer.getWindowHeight() / (float)Renderer.getWindowWidth());
		
		y = -(unitsTall / Renderer.getWindowHeight() * e.getY() - unitsTall/2) + Camera.y;
		
		//x = (e.getX() - Renderer.getWindowWidth() / 2) / (Renderer.getWindowWidth() / Renderer.unitsWide) + Camera.x;
		//y = -((e.getY() - Renderer.getWindowHeight() / 2) / (Renderer.getWindowHeight() / Renderer.unitsTall)) + Camera.y;
		
		mouseVelocityX = (x - lastCheckedX) / .1f;
		mouseVelocityY = (y - lastCheckedY) / .1f;
		
		lastCheckedX = x;
		lastCheckedY = y;
		
		//System.out.println("mouseX: " + x + " mouseY: " + y);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		initialX = e.getX();
		initialY = e.getY();
			
		mouseMoved(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = false;
		rotation = 0;
		//System.out.println("Released");
		
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		// -1 == left, 1 == right
		float wheelRotationDirection = e.getRotation()[1];
		rotation -= wheelRotationDirection * rotationSpeed;
		
		//System.out.println(wheelRotationDirection);
	}
	
	public static float getMouseX() {
		return x;
	}
	
	public static float getMouseY() {
		return y;
	}

}
