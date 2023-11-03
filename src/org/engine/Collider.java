package org.engine;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gameobjects.Color;
import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.gameobjects.Point;
import org.gameobjects.Vector;
import org.graphics.Graphics;

public class Collider {

	public static float minColliderPointSpacing = .03275f;
	
	public ArrayList<GameObject> objectsToIgnore = new ArrayList<GameObject>();
	
	public ArrayList<Point> pointsOffsets = new ArrayList<Point>();
	private ArrayList<Point> points = new ArrayList<Point>();

	private GameObject parentObject;
	
	private ArrayList<Point> closestPoints = new ArrayList<Point>();
	private ArrayList<Point> otherClosestPoints = new ArrayList<Point>();
	
	private ArrayList<Point> closestPointsRender = new ArrayList<Point>();
	private ArrayList<Point> otherClosestPointsRender = new ArrayList<Point>();
	
	public Point collisionFieldPosition = new Point(0,0);
	public float collisionFieldWidth = 0;
	public float collisionFieldHeight = 0;
	public ArrayList<Point> extremes = new ArrayList<Point>();
	
	private float minX = 0;
	private float maxX = 0;
	
	private float minY = 0;
	private float maxY = 0;
	
	public Point lastNonCollisionPos = new Point(0,0);
	
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
		}       
		
		maxX = points.get(0).x;
		minX = points.get(0).x;
		minY = points.get(0).y;
		maxY = points.get(0).y;
		
		for(Point point : points) {
			
			extremes.clear();
			
			minX = (point.x < minX) ? point.x : minX;
			maxX = (point.x > maxX) ? point.x : maxX;
			
			minY = (point.y < minY) ? point.y : minY;
			maxY = (point.y > maxY) ? point.y : maxY;
		}
		
		collisionFieldPosition.x = (maxX + minX) / 2;
		collisionFieldPosition.y = (maxY + minY) / 2;
		
		collisionFieldWidth = (collisionFieldPosition.x - minX) * 2;
		collisionFieldHeight = (collisionFieldPosition.y - minY) * 2;
		
		extremes.add(new Point(maxX, maxY)); //Top left
		extremes.add(new Point(minX, maxY)); //Top right
		extremes.add(new Point(minX, minY)); //Bottom right
		extremes.add(new Point(maxX, minY)); //Bottom left
	}
	
	public void renderAxes(float red,float green,float blue,float alpha) {
		//Graphics.setColor(red, green, blue, alpha);
		
		/*for(Vertex axis : axes) {
			Graphics.setColor(axis.getColor());
			Graphics.drawLine(axis.x * -2, axis.y * -2, axis.x * 2, axis.y * 2, ID.HUD);
			Graphics.setColor(Color.clear());
			
		}*/
		
		for(Point point : closestPointsRender) {
			//System.out.println(parentObject.getClass().getSimpleName() + " - " + points.size() + " " + point.x + "x" + point.y);
			Graphics.setColor(point.color);
			Graphics.drawRect(point.x, point.y, .02f, .02f);
			Graphics.setColor(Color.clear());
		}
		
		for(Point point : otherClosestPointsRender) {
			//System.out.println(parentObject.getClass().getSimpleName() + " - " + points.size() + " " + point.x + "x" + point.y);
			Graphics.setColor(point.color);
			Graphics.drawRect(point.x, point.y, .02f, .02f);
			Graphics.setColor(Color.clear());
		}
		
		
		/*Graphics.Rotate(0);
		Graphics.setColor(new Color(255,255,255,255));
		Graphics.drawRect(collisionFieldPosition.x, collisionFieldPosition.y, collisionFieldWidth, collisionFieldHeight);
		Graphics.setColor(Color.clear());*/
		
		for(Point point : extremes) {
			Graphics.setColor(point.color);
			Graphics.drawRect(point.x, point.y, .02f, .02f);
			Graphics.setColor(Color.clear());
		}
		
		closestPointsRender.clear();
		otherClosestPointsRender.clear();
	}
	
	public boolean doOverlap(Collider collider) {
				
		closestPoints.clear();
		otherClosestPoints.clear();
		
		//Prevents collider from checking collison with itself
		if(this.equals(collider))
			return false;
		
		for(GameObject object : objectsToIgnore)
			if(object.uuid.equals(collider.parentObject.uuid))
				return false;
		
		//Only checks collisions if objects collsionFields are overlaping each other
		if(!doCollisionFieldsCollide(collider)) 
			return false;	
		
		
		for(Point point : points) {
			for(Point otherColliderPoint : collider.points) {
				float distance = (float) (Math.pow(otherColliderPoint.x - point.x, 2) + Math.pow(otherColliderPoint.y - point.y, 2));
				
				if(distance < .00625f) {
					point.color = new Color(0,255,255,255);
					otherColliderPoint.color = new Color(255,0,255,255);
					
					if(closestPoints.size() > 128) {}
					else if(points.size() > 32)
						closestPoints.add(point);
					else
						closestPoints.addAll(points);
					
					if(otherClosestPoints.size() > 128) {}
					else if(collider.points.size() > 32)
						otherClosestPoints.add(otherColliderPoint);
					else
						otherClosestPoints.addAll(collider.points);
				}
			}
		}
		
		closestPointsRender.addAll(closestPoints);
		otherClosestPointsRender.addAll(otherClosestPoints);
		
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
	
	
	private boolean doCollisionFieldsCollide(Collider collider) {
		return !(collisionFieldPosition.x + collisionFieldWidth / 2 <= collider.collisionFieldPosition.x - collider.collisionFieldWidth / 2 || collisionFieldPosition.x - collisionFieldWidth / 2 >= collider.collisionFieldPosition.x + collider.collisionFieldWidth / 2 ||
				collisionFieldPosition.y + collisionFieldHeight / 2 <= collider.collisionFieldPosition.y - collider.collisionFieldHeight / 2 || collisionFieldPosition.y - collisionFieldHeight / 2 >= collider.collisionFieldPosition.y + collider.collisionFieldHeight / 2);
	}
	
	public ArrayList<Point> getCollisonPoints(){
		return points;
	}
}
