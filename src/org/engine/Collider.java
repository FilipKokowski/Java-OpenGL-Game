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

	public ArrayList<ArrayList<Point>> convexPolygons = new ArrayList<ArrayList<Point>>();
	
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
		
		convexPolygons.add(points);
		
		int lastPoint = 0;
		
		for(int point = 1; point < points.size() - 1; point++) {
			Vertex v1 = new Vertex(points.get(point).x - points.get(point - 1).x, points.get(point).y - points.get(point - 1).y);
			Vertex v2 = new Vertex(points.get(point + 1).x - points.get(point).x, points.get(point + 1).y - points.get(point).y);

			double cos = Math.cos(v1.dotProduct(v2) / (Math.sqrt(Math.pow(v1.x, 2) + Math.pow(v1.y, 2)) * Math.sqrt(Math.pow(v2.x, 2) + Math.pow(v2.y, 2))));
			double tg = Math.atan(v2.x - v1.x / v2.y - v1.y);
			
			if(Math.signum(cos) == Math.signum(tg)) {
				//System.out.println(cos + " " + tg);
				ArrayList<Point> newPolygon = new ArrayList<Point>();
				
				points.get(point).color = new Color(255,0,255,255);
				
				for(int p = lastPoint; p < points.size(); p++) {
					if(points.get(p).equals(points.get(point))) {
						lastPoint = p;
						break;
					}
					newPolygon.add(points.get(p));
				}
				
				convexPolygons.add(newPolygon);
				
			} else 
				points.get(point).color = new Color(255,0,255,0);
				
		}
		
		ArrayList<Point> convexPart = new ArrayList<Point>();
		
		for(int point = lastPoint; point < points.size(); point++) {
			convexPart.add(points.get(point));
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
		Graphics.setColor(red, green, blue, alpha);
		
		for(Vertex axis : axes) {
			Graphics.setColor(axis.getColor());
			Graphics.drawLine(axis.x * -2, axis.y * -2, axis.x * 2, axis.y * 2, ID.HUD);
			Graphics.setColor(Color.clear());
			
		}
		
		for(Point point : points) {
			//System.out.println(parentObject.getClass().getSimpleName() + " - " + points.size() + " " + point.x + "x" + point.y);
			Graphics.setColor(point.color);
			Graphics.drawRect(point.x, point.y, .01f, .01f);
			Graphics.setColor(Color.clear());
		}
	}
	
	public boolean doOverlap(Collider collider) {
		//Prevents collider from checking collison with itself
		
		for(Point point : points) {
			
			Point diagStart = new Point(parentObject.getX(), parentObject.getY());
			Point diagEnd = point;

			for(Point otherColliderPoint : collider.points) {
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
