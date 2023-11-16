package org.gameobjects;

import java.util.ArrayList;

public class Point {

	public float x = 0;
	public float y = 0;
	
	public Color color = new Color(255,0,0,255);
	
	public Point() {}
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public static float getDistance(Point p1, Point p2) {
		return (float) Math.sqrt(Math.pow(p2.y - p1.y, 2) - Math.pow(p2.x - p1.x, 2));
	}
	
	public Point clone() {
		return new Point(x,y);
	}
	
	public static Point findClosestPoint(ArrayList<Point> points) {
		float smallestDistance = (float) Math.sqrt(Math.pow(points.get(0).x, 2) + Math.pow(points.get(0).y, 2));
		Point closestPoint = points.get(0);
		
		for(Point point : points) {
			float distance = (float) Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.x, 2));
			
			if(distance < smallestDistance) {
				closestPoint = point;
				smallestDistance = distance;
			}
		}
		
		return closestPoint;
	}
	
	public static Point findFurthestPoint(ArrayList<Point> points) {
		float longesttDistance = (float) Math.sqrt(Math.pow(points.get(0).x, 2) + Math.pow(points.get(0).y, 2));
		Point furthestPoint = points.get(0);
		
		for(Point point : points) {
			float distance = (float) Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.x, 2));
			
			if(distance > longesttDistance) {
				furthestPoint = point;
				longesttDistance = distance;
			}
		}
		
		return furthestPoint;
	}
	
	public static Point substract(Point p1, Point p2) {
		return new Point(p2.x - p1.x, p2.y - p1.y);
	}
	
	public Vector toVector() {
		return new Vector(x,y);
	}
	
}
