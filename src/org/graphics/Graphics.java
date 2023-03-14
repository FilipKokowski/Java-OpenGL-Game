package org.graphics;


import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.resource.ImageResource;
import org.scene.entities.Camera;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;

public class Graphics {
	             
	//From 0 to 1
	private static float red = 1;
	private static float green = 1;
	private static float blue = 1;
	private static float alpha = 1;
	
	private static float textRed = 255;
	private static float textGreen = 255;
	private static float textBlue = 255;
	private static float textAlpha = 255;	
	//In degrees
	private static float rotation = 0;

	public static void drawImage(ImageResource img, float x, float y, float width, float height, ID id) {
		GL2 gl = EventListener.gl;
		
		Texture texture = img.getTexture();
		
		if(texture == null) return;
		
		if((x - width / 2 - Camera.x > Renderer.unitsWide / 2 || x + width / 2 - Camera.x < -Renderer.unitsWide / 2) && !id.equals(ID.HUD)) {
			return;
		}
		
		if((y - height / 2 - Camera.y > Renderer.unitsTall / 2 || y + height / 2 - Camera.y < -Renderer.unitsTall / 2) && !id.equals(ID.HUD)) {
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
	
	public static void fillRect(float x, float y, float width, float height) {
		
		GL2 gl = EventListener.gl;
		
		gl.glTranslatef(x, y, 0);
		gl.glRotatef(-rotation, 0, 0, 1);
		
		gl.glColor4f(red, green, blue, alpha);
		gl.glBegin(GL2.GL_QUADS);
			gl.glVertex2f(-width / 2, -height / 2);
			gl.glVertex2f(width / 2, -height / 2);
			gl.glVertex2f(width / 2, height / 2);
			gl.glVertex2f(-width / 2, height / 2);
		gl.glEnd();
	
		gl.glTranslatef(-x, -y, 0);
		gl.glRotatef(rotation, 0, 0, 1);
	}
	
	public static void drawCircle(float x, float y, float radius, ID id) {
		if((x - radius - Camera.x > Renderer.unitsWide / 2 || x + radius - Camera.x < -Renderer.unitsWide / 2) && !id.equals(ID.HUD)) {
			return;
		}
		
		if((y - radius - Camera.y > Renderer.unitsTall / 2 || y + radius - Camera.y < -Renderer.unitsTall / 2) && !id.equals(ID.HUD)) {
			return;
		}
		
		GL2 gl = EventListener.gl;
		
		gl.glColor4f(red, green, blue, alpha);
	    gl.glBegin(GL2.GL_POINTS);
	    
	    for(double k = 0; k <= 360; k += 0.1){
	    	gl.glVertex2f((float)(x + radius *Math.cos(Math.toRadians(k))),(float)(y - radius * Math.sin(Math.toRadians(k))));
	    }
	    gl.glEnd();
	    
	}
	
	public static void drawLine(float x1, float y1, float x2, float y2, ID id) {
		float furtherX = (x1 > x2) ? x1 : x2;
		float furtherY = (y1 > y2) ? y1 : y2;
		
		float closerX = (x2 > x1) ? x1 : x2;
		float closerY = (y2 > y1) ? y1 : y2;
		
		if((closerX - Camera.x > Renderer.unitsWide / 2 || furtherX - Camera.x < -Renderer.unitsWide / 2) && !id.equals(ID.HUD)) {
			return;
		}
		
		if((closerY - Camera.y > Renderer.unitsTall / 2 || furtherY - Camera.y < -Renderer.unitsTall / 2) && !id.equals(ID.HUD)) {
			return;
		}
		
		GL2 gl = EventListener.gl;
		
		gl.glColor4f(red, green, blue, alpha);
		gl.glBegin(GL2.GL_LINES);
			gl.glVertex2f(x1, y1);
			gl.glVertex2f(x2, y2);
		gl.glEnd();
		gl.glFlush(); 
	}
	
	public static void drawPolygon(float x, float y, ArrayList<ArrayList<Float>> allCoordinates) {
		GL2 gl = EventListener.gl;
		
		gl.glColor4f(red, green, blue, alpha);
		gl.glBegin(GL2.GL_LINE_LOOP);
			for(int coord = 0; coord < allCoordinates.size(); coord++) {
				float x1, y1, x2, y2;
				try {
					x1 = x + allCoordinates.get(coord).get(0);
					y1 = y + allCoordinates.get(coord).get(1);
					x2 = x + allCoordinates.get(coord + 1).get(0);
					y2 = y + allCoordinates.get(coord + 1).get(1);
				} catch(IndexOutOfBoundsException e) {
					x1 = x + allCoordinates.get(coord).get(0);
					y1 = y + allCoordinates.get(coord).get(1);
					x2 = x + allCoordinates.get(0).get(0);
					y2 = y + allCoordinates.get(0).get(1);
				}
				
				gl.glVertex2f(x1, y1);
				gl.glVertex2f(x2, y2);
			}
		gl.glEnd();
		gl.glFlush(); 

	}
	
	public static void setFont(Font font) {
		EventListener.textRenderer = new TextRenderer(font);
	}
	
	public static void drawString(float x, float y, String text, GameObject object, ID id) {
		Rectangle2D textBounds = EventListener.textRenderer.getBounds(text);
		
		object.textBounds = textBounds;
		
		if((x - textBounds.getWidth() / 2 - Camera.x > Renderer.unitsWide / 2 || x + textBounds.getWidth() / 2 - Camera.x < -Renderer.unitsWide / 2) && !id.equals(ID.HUD)) {
			return;
		}
		
		if((y - textBounds.getHeight() / 2 - Camera.y > Renderer.unitsTall / 2 || y + textBounds.getHeight() / 2 - Camera.y < -Renderer.unitsTall / 2) && !id.equals(ID.HUD)) {
			return;
		}
		
		EventListener.textRenderer.setColor(textRed, textGreen, textBlue, textAlpha);
		EventListener.textRenderer.draw(text, (int)(Renderer.getWindowWidth() / Renderer.unitsWide * (x - Camera.x) + Renderer.getWindowWidth() / 2), (int)(Renderer.getWindowHeight() / Renderer.unitsTall * (y - Camera.y) + Renderer.getWindowHeight() / 2));
		//System.out.println(x + "/" + y + ": " + text + " | " + EventListener.textRenderer);
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
	
	public static void setTextColor(float r, float g, float b, float a) {
		textRed = Math.max(0, Math.min(1, r)) * 255;
		textGreen = Math.max(0, Math.min(1, g)) * 255;
		textBlue = Math.max(0, Math.min(1, b)) * 255;
		textAlpha = Math.max(0, Math.min(1, a)) * 255;
	}
}
