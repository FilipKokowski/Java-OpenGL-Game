package org.input;

import org.gameobjects.ID;
import org.graphics.Graphics;
import org.graphics.Renderer;
import org.scene.entities.Camera;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class MouseInput implements MouseListener {

	public static float x = 0;
	public static float y = 0;
	
	public static float worldX = 0;
	public static float worldY = 0;
	
	public static float initialX = 0;
	public static float initialY = 0;
	
	public static boolean pressed;
	public static boolean draggingSmth;
	
	public static float rotation = 0;
	public static float rotationSpeed = 5;
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		Graphics.drawLine(initialX, initialY, e.getX(), e.getY(), ID.GameObject);
		
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
	
		worldX = ((Renderer.unitsWide / Renderer.getWindowWidth()) * e.getX() - Renderer.unitsWide/2) + Camera.x;
		x = ((Renderer.unitsWide / Renderer.getWindowWidth()) * e.getX() - Renderer.unitsWide/2);
		
		float unitsTall = Renderer.unitsWide * (float) ((float)Renderer.getWindowHeight() / (float)Renderer.getWindowWidth());
		worldY = -(unitsTall / Renderer.getWindowHeight() * e.getY() - unitsTall/2) + Camera.y;
		y = -(unitsTall / Renderer.getWindowHeight() * e.getY() - unitsTall/2);
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		initialX = e.getX();
		initialY = e.getY();
		
		pressed = true;
			
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
	
	public static float getMouseWorldX() {
		return worldX;
	}
	
	public static float getMouseWorldY() {
		return worldY;
	}
	
	public static float getMouseX() {
		return x;
	}
	
	public static float getMouseY() {
		return y;
	}

}
