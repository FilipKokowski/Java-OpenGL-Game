package org.resource;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.gameobjects.GameObject;
import org.gameobjects.Point;
import org.graphics.Renderer;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class ImageResource {
	
	private Texture texture = null;
	private BufferedImage img = null;
	private String path = "";
	
	private BufferedImage currentImg = null;
	private BufferedImage mask = null;
	private BufferedImage bounds = null;
	
	private ArrayList<Point> boundsList = new ArrayList<Point>();
	
	public Point centerOfMass = new Point(0,0);
	
	private static HashMap<String, ArrayList<Point>> loadedImages = new HashMap<String, ArrayList<Point>>();
	
	public ImageResource(String path) {
		
		if(path == null) return;
		
		this.path = path;
	 
		InputStream url = getClass().getClassLoader().getResourceAsStream(path);
		
		try {
			img = ImageIO.read(url);
			currentImg = img;
			
            String extension = "";
            
            if(path.lastIndexOf(".") > path.lastIndexOf("/")){
            	extension = path.substring(path.lastIndexOf(".") + 1);
            }
            
            if(loadedImages.containsKey(path)) {
				boundsList = loadedImages.get(path);
				//System.out.println(path + " found in stash");
			} else {
				
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
			                    Point point = new Point((float)(x - mask.getWidth() / 2) / Renderer.pixelsPerUnit, -(float)(y - mask.getHeight() / 2) / Renderer.pixelsPerUnit);
			                    boundsList.add(point);
			                }
	
			            }
			        }
					//System.out.println(boundsList);
					//System.out.println("Width: " + img.getWidth() + " Height: " + img.getHeight());
					
					float centerOfMassX = 0;
					float centerOfMassY = 0;
					
					for(Point point : boundsList) {
						centerOfMassX += (1f / boundsList.size()) * point.x;
						centerOfMassY += (1f / boundsList.size()) * point.y;						
					}
	
					centerOfMass = new Point(centerOfMassX / 1, centerOfMassY / 1);
					
					//System.out.println(centerOfMass.x + " x " + centerOfMass.y);
					
					//Simplifying polygons
					ArrayList<Point> pickedCoords = new ArrayList<Point>();
			
					for(int i=0; i < boundsList.size(); i += 3) {
						pickedCoords.add(boundsList.get(i));
					}
					
					boundsList = pickedCoords;
					
					//Math.sqrt(Math.pow(boundsList.get(closestID).get(1) - boundsList.get(point).get(1), 2) + Math.pow(boundsList.get(closestID).get(0) - boundsList.get(point).get(0), 2))

				}
				//System.out.println(boundsList);
			}
			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				//System.out.println("abdwunawd");
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
	
	public ArrayList<Point> getImageBounds(){
		return boundsList;
	}
	
	public float getWidth() {
		return img.getWidth();
	}
	
	public float getHeight() {
		return img.getHeight();
	}
	
	public static ArrayList<Point> organizePoints(ArrayList<Point> boundsList, GameObject parent) {
		
		ArrayList<Point> checkedPoints = new ArrayList<Point>();
		
		//Organizing points
		int currentPointID = 0;
		
		while(checkedPoints.size() <= boundsList.size()) {
			int closestDistancePointID = 1;
			double closestDistance = 100;
			
			
			for(int pointID = 0; pointID < boundsList.size(); pointID++) {
				if(currentPointID == pointID) continue;
				
				double distance = Math.sqrt(Math.pow(boundsList.get(pointID).y - boundsList.get(currentPointID).y, 2) + Math.pow(boundsList.get(pointID).x - boundsList.get(currentPointID).x, 2));
				
				Point point = new Point(boundsList.get(pointID).x, boundsList.get(pointID).y);

				if(distance < closestDistance){
					boolean pointReckognized = false;
					
					for(Point checkedPoint : checkedPoints) {
						if(checkedPoint.x == point.x && checkedPoint.y == point.y)
							pointReckognized = true;
					}
					
					if(!pointReckognized) {
						closestDistance = distance;
						closestDistancePointID = pointID;
					}
				}
			}
			
			currentPointID = closestDistancePointID;
			
			Point point = new Point(boundsList.get(closestDistancePointID).x, boundsList.get(closestDistancePointID).y);
			checkedPoints.add(point);
		}
		
		return checkedPoints;
	}
	
	public void addToStash() {
		loadedImages.put(path, boundsList);
	}
	
}
