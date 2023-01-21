package org.input;

import org.graphics.Renderer;
import org.scene.entities.Camera;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class MouseInput implements MouseListener {

	public static float x = 0;
	public static float y = 0;
	
	public static boolean pressed;
	public static boolean draggingSmth;
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
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
		x = (e.getX() - Renderer.getWindowWidth() / 2) / (Renderer.getWindowWidth() / Renderer.unitsWide) + Camera.x;
		y = -((e.getY() - Renderer.getWindowHeight() / 2) / (Renderer.getWindowHeight() / Renderer.unitsTall)) + Camera.y;
		
		//System.out.println("x = " + x + " y = " + y);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = false;
		System.out.println("Released");
		
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static float getMouseX() {
		return x;
	}
	
	public static float getMouseY() {
		return y;
	}
}
