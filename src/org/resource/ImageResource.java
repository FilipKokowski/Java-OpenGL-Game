package org.resource;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.graphics.Renderer;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class ImageResource {
	
	private Texture texture = null;
	private BufferedImage img = null;
	
	private BufferedImage currentImg = null;
	private BufferedImage mask = null;
	private BufferedImage bounds = null;
	
	private ArrayList<ArrayList<Float>> boundsList = new ArrayList<ArrayList<Float>>();
	ArrayList<Float> point = new ArrayList<Float>();
	
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
							mask.setRGB(x, y, 0xFFFFFFFF);
						} else {
							mask.setRGB(x, y, 0xFF000000);
						}
					}
				}

				bounds = new BufferedImage(mask.getWidth(), mask.getHeight(), BufferedImage.TYPE_INT_ARGB);
				
				ArrayList<ArrayList<Float>> boundsListNotSorted = new ArrayList<ArrayList<Float>>();
				
				// Perform edge detection on the mask
				for (int y = 0; y < mask.getHeight() - 1; y++) {
		            for (int x = 0; x < mask.getWidth() - 1; x++) {

		                if (Math.abs(mask.getRGB(x, y) - mask.getRGB(x + 1, y + 1)) > 0 || ( y == 0 && mask.getRGB(x, y) == 0xFF000000) || ((x == 0 || x == mask.getWidth() - 1) && mask.getRGB(x, y) == 0xFF000000)) {
		                    bounds.setRGB(x, y, 0xFFFF0000);
		                    ArrayList<Float> coordinates = new ArrayList<Float>();
		                    coordinates.add(((float) x - mask.getWidth() / 2) / Renderer.pixelsPerUnit);
		                    coordinates.add(-((float) y - mask.getHeight() / 2) / Renderer.pixelsPerUnit);
		                    boundsListNotSorted.add(coordinates);
		                }

		            }
		        }
				
				ArrayList<Integer> checkedPoints = new ArrayList<Integer>();
				
				for(int coord = 0; coord < boundsListNotSorted.size(); coord++) {
					int closestID = 0;
					float closestDistance = 100;
					for(int otherCoord = 0; otherCoord < boundsListNotSorted.size(); otherCoord++) {
						
						if(coord == otherCoord && !checkedPoints.contains(otherCoord)) continue;
						
						float x1 = boundsListNotSorted.get(coord).get(0);
						float y1 = boundsListNotSorted.get(coord).get(1);
						float x2 = boundsListNotSorted.get(otherCoord).get(0);
						float y2 = boundsListNotSorted.get(otherCoord).get(1);
						
						float distance = (float) Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
						
						//System.out.println("ClosestID: " + closestID + " Distance: " +  Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2)));
						if(distance < closestDistance) {
							closestDistance = distance;
							closestID = otherCoord;
						}
					}
					
					System.out.println(closestDistance);
					checkedPoints.add(closestID);
					
					point.clear();
					//System.out.println("ClosestID: " + closestID + " Distance: " + closestDistance);
					point.add(boundsListNotSorted.get(closestID).get(0));
					point.add(boundsListNotSorted.get(closestID).get(1));
					boundsList.add(point);
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
			currentImg = bounds;
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
	
	public ArrayList<ArrayList<Float>> getImageBounds(){
		return boundsList;
	}
	
	public float getWidth() {
		return img.getWidth();
	}
	
	public float getHeight() {
		return img.getHeight();
	}
	
}
