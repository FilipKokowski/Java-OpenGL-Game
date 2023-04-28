package org.engine;

import java.util.ArrayList;

import org.gameobjects.GameObject;
import org.gameobjects.Point;
import org.gameobjects.Vertex;

public class Collider {

	private ArrayList<Point> pointsOffsets = new ArrayList<Point>();
	private ArrayList<Point> points = new ArrayList<Point>();
	private ArrayList<Vertex> axes = new ArrayList<Vertex>();
	private GameObject parentObject;
	
	public Collider(ArrayList<Point> pointsOffsets, GameObject parentObject) {
		this.parentObject = parentObject;
		this.pointsOffsets = pointsOffsets;
		
		for(Point pointOffset : pointsOffsets) {
			this.points.add(pointOffset.clone());
		}
		
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
		}
	}
	
	public void update() {
		for(int point = 0; point < points.size(); point++) {
			points.get(point).x = pointsOffsets.get(point).x + parentObject.getX();
			points.get(point).y = pointsOffsets.get(point).y + parentObject.getY();
		}
	}
	
	public boolean doOverlap(Collider collider) {

		for(Vertex axis : axes) {
			float firstPolygonmin = axis.dotProduct(points.get(0));
			float firstPolygonmax = firstPolygonmin;
			
			float secondPolygonmin = axis.dotProduct(collider.points.get(0));
			float secondPolygonmax = secondPolygonmin;
			
			for(Point point : points) {
				float dot = axis.dotProduct(new Point(point.x + parentObject.getX(), point.y + parentObject.getY()));
				
				firstPolygonmin = Math.min(firstPolygonmin, dot);
				firstPolygonmax = Math.max(firstPolygonmax, dot);
			}
			
			for(Point point : collider.points) {
				float dot = axis.dotProduct(new Point(point.x + collider.parentObject.getX(), point.y + collider.parentObject.getY()));
				
				secondPolygonmin = Math.min(secondPolygonmin, dot);
				secondPolygonmax = Math.max(secondPolygonmax, dot);
			}
			
			float polyOffset = axis.dotProduct(new Point(parentObject.getX() - collider.parentObject.getX(), parentObject.getY() - collider.parentObject.getY()));
			
			firstPolygonmin += polyOffset;
			firstPolygonmax += polyOffset;
			
			System.out.println(firstPolygonmin + ">" + secondPolygonmax + " || " + secondPolygonmax +  ">" + firstPolygonmax);
			
			if (firstPolygonmin > secondPolygonmax || secondPolygonmax > firstPolygonmax){
		      return true;
		    }
		}
				
		return false;
	}
}
