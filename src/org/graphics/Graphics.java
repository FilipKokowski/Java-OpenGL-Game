package org.graphics;


import org.resource.ImageResource;
import org.scene.entities.Camera;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class Graphics {
	
	//From 0 to 1
	private static float red = 1;
	private static float green = 1;
	private static float blue = 1;
	private static float alpha = 1;
	
	//In degrees
	private static float rotation = 0;
	
	public static void drawImage(ImageResource img, float x, float y, float width, float height) {
		GL2 gl = EventListener.gl;
		
		Texture texture = img.getTexture();
		
		if(x - width / 2 - Camera.x > Renderer.unitsWide / 2 || x + width / 2 - Camera.x < -Renderer.unitsWide / 2) {
			return;
		}
		
		if(y - height / 2 - Camera.y > Renderer.unitsTall / 2 || y + height / 2 - Camera.y < -Renderer.unitsTall / 2) {
			return;
		}
		
		if(texture != null) {
			gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
		}
		
		gl.glTranslatef(x, y, 0);
		gl.glRotatef(-rotation, 0, 0, 1);
		
		gl.glColor4f(red, green, blue, alpha);
		gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0,1);
			gl.glVertex2f(-width / 2, -height / 2);
			
			gl.glTexCoord2f(1,1);
			gl.glVertex2f(width / 2, -height / 2);
			
			gl.glTexCoord2f(1,0);
			gl.glVertex2f(width / 2, height / 2);
			
			gl.glTexCoord2f(0,0);
			gl.glVertex2f(-width / 2, height / 2);
		gl.glEnd();
		gl.glFlush();
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		
		gl.glRotatef(rotation, 0, 0, 1);
		gl.glTranslatef(-x, -y, 0);
	}
	
	public static void fillRect(float x, float y, float w, float h) {
		GL2 gl = EventListener.gl;
		
		gl.glTranslatef(x, y, 0);
		gl.glRotatef(-rotation, 0, 0, 1);
		
		gl.glColor4f(red, green, blue, alpha);
		gl.glBegin(GL2.GL_QUADS);
			gl.glVertex2f(-w / 2, -h / 2);
			gl.glVertex2f(w / 2, -h / 2);
			gl.glVertex2f(w / 2, h / 2);
			gl.glVertex2f(-w / 2, h / 2);
		gl.glEnd();
		
		gl.glTranslatef(-x, -y, 0);
		gl.glRotatef(rotation, 0, 0, 1);
	}
	
	public static void drawCircle(float x, float y, float radius) {
		GL2 gl = EventListener.gl;
		
		gl.glColor4f(red, green, blue, alpha);
	    gl.glBegin(GL2.GL_POINTS);
	    
	    for(double k = 0; k <= 360; k += 0.1){
	    	gl.glVertex2f((float)(x + radius *Math.cos(Math.toRadians(k))),(float)(y - radius * Math.sin(Math.toRadians(k))));
	    }
	    gl.glEnd();
	    
	}
	
	public static void drawLine(float x1, float y1, float x2, float y2) {
		GL2 gl = EventListener.gl;
		
		gl.glColor4f(red, green, blue, alpha);
		gl.glBegin(GL2.GL_LINES);
			gl.glVertex2f(x1, y1);
			gl.glVertex2f(x2, y2);
		gl.glEnd();
		gl.glFlush();
	}
	
	public static void drawString() {
		
	}
	
	public static void Rotate(float rotate) {
		rotation = -rotate;
	}
	
	public static void setColor(float r, float g, float b, float a) {
		red = Math.max(0, Math.min(1, r));
		green = Math.max(0, Math.min(1, g));
		blue = Math.max(0, Math.min(1, b));
		alpha = Math.max(0, Math.min(1, a));
	}
}
