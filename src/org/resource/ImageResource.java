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
	
	private static HashMap<String, ArrayList<ArrayList<Float>>> loadedImages = new HashMap<String, ArrayList<ArrayList<Float>>>();
	
	public ImageResource(String path) {
		
		if(path == null) return;
		
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
				
				// Perform edge detection on the mask
				for (int y = 0; y < mask.getHeight() - 1; y++) {
		            for (int x = 0; x < mask.getWidth() - 1; x++) {

		                if (Math.abs(mask.getRGB(x, y) - mask.getRGB(x + 1, y + 1)) > 0 || ((y == 0 || y == mask.getHeight() - 2) && mask.getRGB(x, y) == 0xFF000000) || ((x == 0 || x == mask.getWidth() - 2) && mask.getRGB(x, y) == 0xFF000000)) {
		                    bounds.setRGB(x, y, 0xFFFF0000);
		                    ArrayList<Float> coordinates = new ArrayList<Float>();
		                    coordinates.add(((float) x - mask.getWidth() / 2) / Renderer.pixelsPerUnit);
		                    coordinates.add(-((float) y - mask.getHeight() / 2) / Renderer.pixelsPerUnit);
		                    boundsList.add(coordinates);
		                }

		            }
		        }
				//System.out.println(boundsList);
				//System.out.println("Width: " + img.getWidth() + " Height: " + img.getHeight());
				
				//Simplifying polygons
				ArrayList<ArrayList<Float>> pickedCoords = new ArrayList<ArrayList<Float>>();
		
				for(int i=0; i < boundsList.size(); i += 4) {
					pickedCoords.add(boundsList.get(i));
				}
				
				boundsList = pickedCoords;
				
				//Math.sqrt(Math.pow(boundsList.get(closestID).get(1) - boundsList.get(point).get(1), 2) + Math.pow(boundsList.get(closestID).get(0) - boundsList.get(point).get(0), 2))
				
				if(loadedImages.containsKey(path)) {
					boundsList = loadedImages.get(path);
				} else {
					System.out.println(path);
					ArrayList<ArrayList<Float>> checkedPoints = new ArrayList<ArrayList<Float>>();
					
					//Organizing points
					int currentPointID = 0;
					
					while(checkedPoints.size() <= boundsList.size()) {
						int closestDistancePointID = 1;
						double closestDistance = 100;
						
						
						for(int pointID = 0; pointID < boundsList.size(); pointID++) {
							if(currentPointID == pointID) continue;
							
							double distance = Math.sqrt(Math.pow(boundsList.get(pointID).get(1) - boundsList.get(currentPointID).get(1), 2) + Math.pow(boundsList.get(pointID).get(0) - boundsList.get(currentPointID).get(0), 2));
							
							ArrayList<Float> point = new ArrayList<Float>();
							point.add(boundsList.get(pointID).get(0));
							point.add(boundsList.get(pointID).get(1));
							
							if(distance < closestDistance && !checkedPoints.contains(point)){
								closestDistance = distance;
								closestDistancePointID = pointID;
								//System.out.println(pointID);
							}
						}
						
						currentPointID = closestDistancePointID;
						
						ArrayList<Float> point = new ArrayList<Float>();
						point.add(boundsList.get(closestDistancePointID).get(0));
						point.add(boundsList.get(closestDistancePointID).get(1));
						checkedPoints.add(point);
						
						//System.out.println(closestDistancePointID);
					}
					boundsList = checkedPoints;
					
					loadedImages.put(path, boundsList);
				}
				//System.out.println(boundsList);
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
