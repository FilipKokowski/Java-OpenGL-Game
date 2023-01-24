package org.graphics;

import java.awt.Color;
import java.awt.Font;

import org.engine.GameLoop;
import org.engine.Handler;
import org.scene.entities.Camera;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class EventListener implements GLEventListener{

	public static GL2 gl = null;
	
	@Override
	public void display(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		

		/*Graphics.setColor(.5f, 1, .25f, 1);
		Graphics.Rotate(45);
		Graphics.fillRect(0, 0, 1, 1);*/
		
		gl.glTranslatef(-Camera.x, 0, 0);
		Handler.render();
		gl.glTranslatef(Camera.x, 0, 0);
		
		Handler.renderHUD();
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
