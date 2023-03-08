package org.resource;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.graphics.Renderer;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class ImageResource {
	
	private Texture texture = null;
	private BufferedImage img = null;
	
	private BufferedImage currentImg = null;
	private BufferedImage mask = null;
	
	
	public ImageResource(String path) {
		InputStream url = getClass().getClassLoader().getResourceAsStream(path);
		
		try {
			img = ImageIO.read(url);
			currentImg = img;
			
            String extension = "";
            
            if(path.lastIndexOf(".") > path.lastIndexOf("/")){
            	extension = path.substring(path.lastIndexOf(".") + 1);
            }
            
			if(extension.equals("png")) {
				mask = cloneBufferedImage(img);
				
				for(int y = 0; y < img.getHeight(); y++) {
					for(int x = 0; x < img.getWidth(); x++) {
						if(((img.getRGB(x, y) & 0xff000000) >>> 24) == 0){
							mask.setRGB(x, y, -460552);
						} else {
							mask.setRGB(x, y, -16777215);
						}
					}
				}
				//System.out.println("Width: " + img.getWidth() + " Height: " + img.getHeight());
			}
			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("abdwunawd");
			}	
		
		
		if(img != null) {
			img.flush();
		}
	}
	
	public static BufferedImage cloneBufferedImage(BufferedImage bufferImage) {
        ColorModel colorModel = bufferImage.getColorModel();
        WritableRaster raster = bufferImage.copyData(null);
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
	
	public void switchViewMode() {
		if(currentImg == img && mask != null) {
			currentImg = mask;
		}
		else {
			currentImg = img;
		}
		texture = null;
	}
	
	public Texture getTexture() {
		if(currentImg == null) return null;
		
		if(texture == null) {
			texture = AWTTextureIO.newTexture(Renderer.getProfile(), currentImg, true);
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
