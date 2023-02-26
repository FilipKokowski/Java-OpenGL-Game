package org.resource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.graphics.Renderer;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class ImageResource {
	
	private Texture texture = null;
	private BufferedImage img = null;
	
	String trest = null;
	
	public ImageResource(String path) {
		InputStream url = getClass().getClassLoader().getResourceAsStream(path);
		
		try {
			img = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(img != null) {
			img.flush();
		}
	}
	
	public Texture getTexture() {
		if(img == null) return null;
		
		if(texture == null) {
			texture = AWTTextureIO.newTexture(Renderer.getProfile(), img, true);
		}
		
		return texture;
	}
	
	public float getWidth() {
		return img.getWidth();
	}
	
	public float getHeight() {
		return img.getHeight();
	}
	
}
