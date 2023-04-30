package org.engine;

import java.util.ArrayList;

import org.gameobjects.GameObject;
import org.gameobjects.Point;
import org.gameobjects.Vertex;

public class Collider {

	public ArrayList<Point> pointsOffsets = new ArrayList<Point>();
	private ArrayList<Point> points = new ArrayList<Point>();
	private ArrayList<Vertex> axes = new ArrayList<Vertex>();
	private GameObject parentObject;
	
	public Collider(ArrayList<Point> pointsOffsets, GameObject parentObject) {
		this.parentObject = parentObject;
		this.pointsOffsets = pointsOffsets;
		
		for(Point pointOffset : pointsOffsets) {
			this.points.add(pointOffset.clone());
		}
		
		update();
	}
	
	public void update() {
		for(int point = 0; point < points.size(); point++) {
			points.get(point).x = (float)((pointsOffsets.get(point).x + parentObject.getX()) * Math.cos(Math.toRadians(-parentObject.rotation)) - (pointsOffsets.get(point).y + parentObject.getY()) * Math.sin(Math.toRadians(-parentObject.rotation)) + parentObject.getX());
			points.get(point).y = (float)((pointsOffsets.get(point).x + parentObject.getX()) * Math.sin(Math.toRadians(-parentObject.rotation)) + (pointsOffsets.get(point).y + parentObject.getY()) * Math.cos(Math.toRadians(-parentObject.rotation)) + parentObject.getY());
		}
		
		axes.clear();
		
		for(int point = 0; point < points.size(); point += 2) {
			try {
				axes.add(new Vertex(-(points.get(point + 1).y - points.get(point).y), points.get(point + 1).x - points.get(point).x));
			} catch(IndexOutOfBoundsException e) {
				axes.add(new Vertex(-(points.get(0).y - points.get(point).y), points.get(0).x - points.get(point).x));
			}
		}
		
		for(Vertex axis : axes) {
			float magnitude = (float) Math.sqrt(Math.pow(axis.x, 2) + Math.pow(axis.y, 2));
			
			if(magnitude != 0) {
				axis.x *= 1 / magnitude;
				axis.y *= 1 / magnitude;
			}
			
			//System.out.println(axis.x + " x " + axis.y);
		}
		
	}
	
	public boolean doOverlap(Collider collider) {
		
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
			
			float polyOffset = axis.dotProduct(new Point(parentObject.getX() - collider.parentObject.getX(), parentObject.getY() - collider.parentObject.getY()));
	
			firstPolygonmin += polyOffset;
			firstPolygonmax += polyOffset;
			
			//System.out.println(polyOffset);
			
			System.out.println(firstPolygonmin + ">" + secondPolygonmax + " || " + secondPolygonmin +  ">" + firstPolygonmax + (firstPolygonmin > secondPolygonmax || secondPolygonmin > firstPolygonmax));
			
			if (firstPolygonmin > secondPolygonmax || secondPolygonmin > firstPolygonmax){
		      //return true;
		    }
		}
				
		return false;
	}
}
