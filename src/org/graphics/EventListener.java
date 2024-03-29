package org.graphics;

import java.awt.Font;
import java.util.HashMap;

import org.engine.GameLoop;
import org.engine.Handler;
import org.input.KeyInput;
import org.scene.entities.Camera;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;

public class EventListener implements GLEventListener{

	public static GL2 gl = null;
	
	public static TextRenderer textRenderer;
	
	private static boolean buttonHeld = false;
	
	public static boolean renderBounds = false;
	public static boolean renderJoints = false;
	public static boolean normalViewMode = true;
	
	@Override
	public void display(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		

		/*Graphics.setColor(.5f, 1, .25f, 1);
		Graphics.Rotate(45);
		Graphics.fillRect(0, 0, 1, 1);*/
		
		gl.glTranslatef(-Camera.x, 0, 0);
		Handler.render();
		
		HashMap<String, TextRenderer> textInfo = Handler.getTextInfo();
		
		for (String objectID : textInfo.keySet()) {
			textRenderer = textInfo.get(objectID);

			textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
			Handler.renderText(objectID);
			textRenderer.endRendering();
			//System.out.println(objectID + " " + renderer);
		}
		gl.glTranslatef(Camera.x, 0, 0);
		
		Handler.renderHUD();
		
		HashMap<String, TextRenderer> HUDtextInfo = Handler.getHUDTextInfo();
		
		for (String objectID : HUDtextInfo.keySet()) {
			textRenderer = HUDtextInfo.get(objectID);
			
			textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
			Handler.renderHUDText(objectID);
			textRenderer.endRendering();	
			//System.out.println(objectID + " " + renderer);
		}
		
		if(KeyInput.getKey(KeyEvent.VK_P) && !buttonHeld) {
			Handler.toggleMasks();
			buttonHeld = true;
		}
		
		if(KeyInput.getKey(KeyEvent.VK_I) && !buttonHeld) {
			renderBounds = !renderBounds;
			buttonHeld = true;
		}
	 
		if(KeyInput.getKey(KeyEvent.VK_O) && !buttonHeld) {
			renderJoints = !renderJoints;
			buttonHeld = true;
		}
		
		if(!KeyInput.getKey(KeyEvent.VK_P) && !KeyInput.getKey(KeyEvent.VK_I) && !KeyInput.getKey(KeyEvent.VK_O)) buttonHeld = false;

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GameLoop.running = false;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glClearColor(.1f,.1f,.1f,1);
		
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);
		
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		
		//gl.setSwapInterval(0/1);
		
		textRenderer = new TextRenderer(new Font("Comic Sans MS", Font.BOLD, 12));
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl= drawable.getGL().getGL2();
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		Renderer.unitsTall = Renderer.getWindowHeight() / (Renderer.getWindowWidth() / Renderer.unitsWide);
		
		gl.glOrtho(-Renderer.unitsWide/2, Renderer.unitsWide/2, -Renderer.unitsTall/2, Renderer.unitsTall/2, -1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
	}
	

}
