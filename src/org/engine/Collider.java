package org.engine;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gameobjects.Color;
import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.gameobjects.Point;
import org.gameobjects.Polygon;
import org.gameobjects.Vector;
import org.graphics.Graphics;

import com.jogamp.graph.geom.Triangle;

public class Collider {

	public static float minColliderPointSpacing = .03275f;
	
	public ArrayList<GameObject> objectsToIgnore = new ArrayList<GameObject>();
	
	public Polygon nonTriangulatedBounds = new Polygon();
	
	public ArrayList<Polygon> trianglesOffsets = new ArrayList<Polygon>();
	private ArrayList<Polygon> triangles = new ArrayList<Polygon>();

	private GameObject parentObject;
	
	private ArrayList<Polygon> closestTriangles = new ArrayList<Polygon>();
	private ArrayList<Polygon> otherClosestTriangles = new ArrayList<Polygon>();
	
	private ArrayList<Polygon> closestTrianglesRender = new ArrayList<Polygon>();
	private ArrayList<Polygon> otherClosestTrianglesRender = new ArrayList<Polygon>();
	
	public Point collisionFieldPosition = new Point(0,0);
	public float collisionFieldWidth = 0;
	public float collisionFieldHeight = 0;
	public ArrayList<Point> extremes = new ArrayList<Point>();
	
	private float minX = 0;
	private float maxX = 0;
	
	private float minY = 0;
	private float maxY = 0;
	
	public Point lastNonCollisionPos = new Point(0,0);
	
	public Polygon lineR = new Polygon();
	
	public Collider(ArrayList<Polygon> trianglesOffsets, GameObject parentObject) {
		this.parentObject = parentObject;
		this.trianglesOffsets = trianglesOffsets;
		
		for(Polygon triangle : trianglesOffsets) {
			Polygon t = new Polygon();
			for(Point pointOffset : triangle.vertices) {
				t.vertices.add(pointOffset.clone());
			}
			triangles.add(t);
		}
	}
	
	public void update() {
		
		//System.out.println(convexPolygons.size() + " " + parentObject.getClass().getSimpleName());
		for(Polygon triangle : triangles) {
			for(int point = 0; point < triangle.vertices.size(); point++) {
				triangle.vertices.get(point).x = (float)((trianglesOffsets.get(triangles.indexOf(triangle)).vertices.get(point).x) * Math.cos(Math.toRadians(-parentObject.rotation)) - (trianglesOffsets.get(triangles.indexOf(triangle)).vertices.get(point).y) * Math.sin(Math.toRadians(-parentObject.rotation)) + parentObject.getX());
				triangle.vertices.get(point).y = (float)((trianglesOffsets.get(triangles.indexOf(triangle)).vertices.get(point).x) * Math.sin(Math.toRadians(-parentObject.rotation)) + (trianglesOffsets.get(triangles.indexOf(triangle)).vertices.get(point).y) * Math.cos(Math.toRadians(-parentObject.rotation)) + parentObject.getY());
			}       
		}
		
		
		maxX = triangles.get(0).vertices.get(0).x;
		minX = triangles.get(0).vertices.get(0).x;
		minY = triangles.get(0).vertices.get(0).y;
		maxY = triangles.get(0).vertices.get(0).y;
		
		for(Polygon triangle : triangles)
			for(Point point : triangle.vertices) {
				
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

		/*Graphics.Rotate(0);
		Graphics.setColor(new Color(255,255,255,255));
		Graphics.drawRect(collisionFieldPosition.x, collisionFieldPosition.y, collisionFieldWidth, collisionFieldHeight);
		Graphics.setColor(Color.clear());*/
		
		for(Point point : extremes) {
			Graphics.setColor(point.color);
			Graphics.drawRect(point.x, point.y, .02f, .02f);
			Graphics.setColor(Color.clear());
		}
		
		if(parentObject.id == ID.Obstacle)
			for(Polygon triangle : triangles) {
				Graphics.setColor(triangle.color);
				for(int point = 0; point < triangle.vertices.size(); point++) 
					Graphics.drawLine(triangle.vertices.get(point).x,triangle.vertices.get(point).y, triangle.vertices.get((point == 2) ? 0 : point + 1).x, triangle.vertices.get((point == 2) ? 0 : point + 1).y, ID.Obstacle);
			}
		
		Graphics.setColor(Color.clear());
		
		
		if(lineR.vertices.size() > 1) {
			for(int line = 0; line < lineR.vertices.size(); line += 4) {
				Graphics.setColor(Color.GREEN);
				Graphics.drawLine(lineR.vertices.get(line).x, lineR.vertices.get(line).y, lineR.vertices.get(line + 1).x, lineR.vertices.get(line + 1).y, ID.Obstacle);
				Graphics.setColor(Color.BLUE);
				//System.out.println(lineR.vertices.get(line + 2).x + " x " + lineR.vertices.get(line + 2).y + "   " + lineR.vertices.get(line + 3).x + " x " + lineR.vertices.get(line + 3).y);
				Graphics.drawLine(lineR.vertices.get(line + 2).x - 10, lineR.vertices.get(line + 2).y, lineR.vertices.get(line + 3).x + 10, lineR.vertices.get(line + 3).y, ID.Obstacle);
			}
		}
		
		//System.out.println(lineR.vertices.size());
		
		Graphics.setColor(Color.clear());
		
		closestTrianglesRender.clear();
		otherClosestTrianglesRender.clear();
		lineR.vertices.clear();
		
		for(Polygon triangle : triangles)
			triangle.color = Color.RED;
	}
	
	public boolean doOverlap(Collider collider) {
				
		closestTriangles.clear();
		otherClosestTriangles.clear();
		
		//Prevents collider from checking collison with itself
		if(this.equals(collider))
			return false;
		
		for(GameObject object : objectsToIgnore)
			if(object.uuid.equals(collider.parentObject.uuid))
				return false;
		
		//Only checks collisions if objects collsionFields are overlaping each other
		if(!doCollisionFieldsCollide(collider)) 
			return false;	
		
		for(Polygon triangle : collider.triangles) {
			for(int side = 0; side < triangle.vertices.size(); side++) {
				Point p1 = triangle.vertices.get(side);
				Point p2 = triangle.vertices.get((side == 2) ? 0 : side + 1);
				
				if(p1.equals(p2))
					continue;
				
				double slope = (p2.y - p1.y) / (p2.x - p1.x);
				double intercept = p1.y - slope * p1.x;
				
				double a = -1 / slope;
				
				for(Polygon otherTriangle : triangles) {
					for(Point point : otherTriangle.vertices) {
						double b = point.y - a *  point.x;

						double x = (b - intercept) / (slope - a);
						double y = a * x + b;
						double dis = Math.sqrt(Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2));
					
						if((p1.y > p2.y) ? (p1.y > y && y > p2.y) : (p2.y > y && y > p1.y))
							if(dis < 0.00625f) {
								closestTriangles.add(triangle);
								otherClosestTriangles.add(otherTriangle);
								
								triangle.color = Color.GREEN;
								otherTriangle.color = Color.GREEN;
							}
						
					}
				}
			}
		}

		
		/*for(Point point : points) {
			for(Point otherColliderPoint : collider.points) {
				float distance = (float) (Math.pow(otherColliderPoint.x - point.x, 2) + Math.pow(otherColliderPoint.y - point.y, 2));
				
				if(distance < .00625f) {
					point.color = new Color(0,255,255,255);
					otherColliderPoint.color = new Color(255,0,255,255);
					
					if(closestPoints.size() > 64) {}
					else if(points.size() > 32)
						closestPoints.add(point);
					else
						closestPoints.addAll(points);
					
					if(otherClosestPoints.size() > 64) {}
					else if(collider.points.size() > 32)
						otherClosestPoints.add(otherColliderPoint);
					else
						otherClosestPoints.addAll(collider.points);
				}
			}
		}*/
		
		
		/*
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
		*/

		
		return false;
	}
	
	
	private boolean doCollisionFieldsCollide(Collider collider) {
		return !(collisionFieldPosition.x + collisionFieldWidth / 2 <= collider.collisionFieldPosition.x - collider.collisionFieldWidth / 2 || collisionFieldPosition.x - collisionFieldWidth / 2 >= collider.collisionFieldPosition.x + collider.collisionFieldWidth / 2 ||
				collisionFieldPosition.y + collisionFieldHeight / 2 <= collider.collisionFieldPosition.y - collider.collisionFieldHeight / 2 || collisionFieldPosition.y - collisionFieldHeight / 2 >= collider.collisionFieldPosition.y + collider.collisionFieldHeight / 2);
	}
	
	public Polygon getCollisonPoints(){
		return (triangles.size() >= 2) ? triangles.get(0) : new Polygon();
	}
}
