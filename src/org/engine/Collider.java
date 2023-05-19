package org.engine;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gameobjects.Color;
import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.gameobjects.Point;
import org.gameobjects.Vertex;
import org.graphics.Graphics;

public class Collider {

	public static float minColliderPointSpacing = .25f;
	
	public ArrayList<Point> pointsOffsets = new ArrayList<Point>();
	private ArrayList<Point> points = new ArrayList<Point>();
	private ConcurrentLinkedQueue<Vertex> axes = new ConcurrentLinkedQueue<Vertex>();
	private GameObject parentObject;
	
	private ArrayList<Point> closestPoints = new ArrayList<Point>();
	private ArrayList<Point> otherClosestPoints = new ArrayList<Point>();
	
	public Collider(ArrayList<Point> pointsOffsets, GameObject parentObject) {
		this.parentObject = parentObject;
		this.pointsOffsets = pointsOffsets;
		
		for(Point pointOffset : pointsOffsets) {
			this.points.add(pointOffset.clone());
		}
	}
	
	public void update() {
		
		//System.out.println(convexPolygons.size() + " " + parentObject.getClass().getSimpleName());
		
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
		//Graphics.setColor(red, green, blue, alpha);
		
		/*for(Vertex axis : axes) {
			Graphics.setColor(axis.getColor());
			Graphics.drawLine(axis.x * -2, axis.y * -2, axis.x * 2, axis.y * 2, ID.HUD);
			Graphics.setColor(Color.clear());
			
		}*/
		
		for(Point point : closestPoints) {
			//System.out.println(parentObject.getClass().getSimpleName() + " - " + points.size() + " " + point.x + "x" + point.y);
			Graphics.setColor(point.color);
			Graphics.drawRect(point.x, point.y, .01f, .01f);
			Graphics.setColor(Color.clear());
		}
		
		for(Point point : otherClosestPoints) {
			//System.out.println(parentObject.getClass().getSimpleName() + " - " + points.size() + " " + point.x + "x" + point.y);
			Graphics.setColor(point.color);
			Graphics.drawRect(point.x, point.y, .01f, .01f);
			Graphics.setColor(Color.clear());
		}
		
		closestPoints.clear();
		otherClosestPoints.clear();
	}
	
	public boolean doOverlap(Collider collider) {
		//Prevents collider from checking collison with itself
		if(this.equals(collider))
			return false;
		
		for(Point point : points) {
			for(Point otherColliderPoint : collider.points) {
				float distance = (float) (Math.pow(otherColliderPoint.x - point.x, 2) + Math.pow(otherColliderPoint.y - point.y, 2));
				
				if(distance < .005f) {
					point.color = new Color(0,255,255,255);
					otherColliderPoint.color = new Color(255,0,255,255);
					
					if(closestPoints.size() > 64) {}
					else if(points.size() > 16)
						closestPoints.add(point);
					else
						closestPoints.addAll(points);
					
					if(otherClosestPoints.size() > 64) {}
					else if(collider.points.size() > 16)
						otherClosestPoints.add(otherColliderPoint);
					else
						otherClosestPoints.addAll(collider.points);
				}
			}
		}
		
		//System.out.println(closestDistance + " x " + closestPoints.size());
		//System.out.println(points.get(closestPointFirstPolygonID).x + " x " + points.get(closestPointFirstPolygonID).y);
		//System.out.println(collider.points.get(closestPointSecondPolygonID).x + " x " + collider.points.get(closestPointSecondPolygonID).y);
		
		for(Point point : closestPoints) {
			
			Point diagStart = new Point(parentObject.getX(), parentObject.getY());
			Point diagEnd = point;

			for(Point otherColliderPoint : otherClosestPoints) {
				Point edgeStart = new Point(otherColliderPoint.x, otherColliderPoint.y);
				Point egdeEnd = new Point(collider.points.get((collider.points.indexOf(otherColliderPoint) + 1) % collider.points.size()).x, collider.points.get((collider.points.indexOf(otherColliderPoint) + 1) % collider.points.size()).y);

				// Standard "off the shelf" line segment intersection
				float h = (egdeEnd.x - edgeStart.x) * (diagStart.y - diagEnd.y) - (diagStart.x - diagEnd.x) * (egdeEnd.y - edgeStart.y);
				float t1 = ((edgeStart.y - egdeEnd.y) * (diagStart.x - edgeStart.x) + (egdeEnd.x - edgeStart.x) * (diagStart.y - edgeStart.y)) / h;
				float t2 = ((diagStart.y - diagEnd.y) * (diagStart.x - edgeStart.x) + (diagEnd.x - diagStart.x) * (diagStart.y - edgeStart.y)) / h;

				if(t1 >= 0 && t1 < 1 && t2 >= 0 && t2 < 1) {
					return true;
				}
			}
		}
		
		return false;
	}
}
