package org.engine;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.gameobjects.Point;
import org.gameobjects.Vertex;
import org.graphics.Graphics;

public class Collider {

	public ArrayList<Point> pointsOffsets = new ArrayList<Point>();
	private ArrayList<Point> points = new ArrayList<Point>();
	private ConcurrentLinkedQueue<Vertex> axes = new ConcurrentLinkedQueue<Vertex>();
	private GameObject parentObject;
	
	public Collider(ArrayList<Point> pointsOffsets, GameObject parentObject) {
		this.parentObject = parentObject;
		this.pointsOffsets = pointsOffsets;
		
		for(Point pointOffset : pointsOffsets) {
			this.points.add(pointOffset.clone());
		}
	}
	
	public void update() {
		for(int point = 0; point < points.size(); point++) {
			points.get(point).x = (float)((pointsOffsets.get(point).x) * Math.cos(Math.toRadians(-parentObject.rotation)) - (pointsOffsets.get(point).y) * Math.sin(Math.toRadians(-parentObject.rotation)) + parentObject.getX());
			points.get(point).y = (float)((pointsOffsets.get(point).x) * Math.sin(Math.toRadians(-parentObject.rotation)) + (pointsOffsets.get(point).y) * Math.cos(Math.toRadians(-parentObject.rotation)) + parentObject.getY());
			//System.out.println(parentObject.getClass().getSimpleName() + " - " + parentObject.getX() + " x " + parentObject.getY());
		}
		
		axes.clear();
		
		for(int point = 0; point < points.size(); point += 1) {
			try {
				axes.offer(new Vertex(-(points.get(point + 1).y - points.get(point).y), points.get(point + 1).x - points.get(point).x));
			} catch(IndexOutOfBoundsException e) {
				axes.offer(new Vertex(-(points.get(0).y - points.get(point).y), points.get(0).x - points.get(point).x));
			}
		}
		
		for(Vertex axis : axes) {
			
			boolean duplicate = false;
			
			for(Vertex checkedAxis : axes) {
				if(axis.equals(checkedAxis))
					continue;
				
				if(checkedAxis.x == axis.x && checkedAxis.y == axis.y) {
					duplicate = true;
					break;
				}
			}
			
			if(duplicate) {
				axes.remove(axis);
				continue;
			}
			
			double magnitude = Math.sqrt(Math.pow(axis.x, 2) + Math.pow(axis.y, 2));
			
			if(magnitude != 0) {
				axis.x *= 1 / magnitude;
				axis.y *= 1 / magnitude;
			}
			
			//System.out.println(axis.x + " x " + axis.y);
		}       
		
	}
	
	public void renderAxes(float red,float green,float blue,float alpha) {
		Graphics.setColor(red, green, blue, alpha);
		
		for(Vertex axis : axes)
		Graphics.drawLine(axis.x * -2, axis.y * -2, axis.x * 2, axis.y * 2, ID.HUD);
		
		for(Point point : points) {
			//System.out.println(parentObject.getClass().getSimpleName() + " - " + points.size() + " " + point.x + "x" + point.y);
			Graphics.drawRect(point.x, point.y, .01f, .01f);
		}
		
		Graphics.setColor(1,1,1,1);
	}
	
	public boolean doOverlap(Collider collider) {
		
		//System.out.println(collider.parentObject.uuid);
		
		ArrayList<Vertex> axes = new ArrayList<Vertex>();
		axes.addAll(this.axes);
		axes.addAll(collider.axes);
		
		for(Vertex axis : axes) {
			//axis.print();
			
			float firstPolygonmin = axis.dotProduct(points.get(0));
			float firstPolygonmax = firstPolygonmin;
			
			float secondPolygonmin = axis.dotProduct(collider.points.get(0));
			float secondPolygonmax = secondPolygonmin;
			
			for(Point point : points) {
				float dot = axis.dotProduct(point);
				
				//System.out.println(parentObject.getClass().getSimpleName() + " - " + dot);
				//System.out.println(axis.x + " * " + point.x + " + " + axis.y + " * " + point.y + " = " + (axis.x * point.x + axis.y * point.y));
				
				firstPolygonmin = Math.min(firstPolygonmin, dot);
				firstPolygonmax = Math.max(firstPolygonmax, dot);
			}
			
			for(Point point : collider.points) {
				float dot = axis.dotProduct(point);
				
				//System.out.println(collider.parentObject.getClass().getSimpleName() + " - " + dot);
				
				//System.out.println(axis.x + " * " + point.x + " + " + axis.y + " * " + point.y + " = " + (axis.x * point.x + axis.y * point.y));
				
				secondPolygonmin = Math.min(secondPolygonmin, dot);
				secondPolygonmax = Math.max(secondPolygonmax, dot);
			}
			
			//System.out.println(polyOffset);
			
			//if(collider.parentObject.uuid.equals("eb"))
				//System.out.println("!(" + secondPolygonmax + " >= " + firstPolygonmin + " && " + firstPolygonmax + " >= " + secondPolygonmin + " " +(!(secondPolygonmax >= firstPolygonmin && firstPolygonmax >= secondPolygonmin)));
			
			if(!(secondPolygonmax >= firstPolygonmin && firstPolygonmax >= secondPolygonmin)) {
				return true;
			}
		}
				
		return false;
	}
}
