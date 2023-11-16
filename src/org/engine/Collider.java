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
					
						if((p1.y > p2.y) ? (p1.y > y && y > p2.y) : (p2.y > y && y > p1.y)) {
							double dis = Math.sqrt(Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2));
							if(dis < 0.00625f) {
								if(closestTriangles.size() > 32)
									break;
								
								closestTriangles.add(triangle);
								otherClosestTriangles.add(otherTriangle);
								
								triangle.color = Color.GREEN;
								otherTriangle.color = Color.GREEN;
							}
						}
					}
				}
			}
		}
		
		Polygon simplex = new Polygon();
		Vector direction = new Vector(1,0);
		
		for(Polygon triangle : closestTriangles) {
			for(Polygon otherTriangle : otherClosestTriangles) {
				//simplex.vertices.clear();
				
				Vector support = support(triangle, otherTriangle, direction);
				simplex.vertices.add(support.toPoint());
				
				direction.mirror(); 
				
				while(true) {
					if(simplex.vertices.size() > 4)
						break;
					
					System.out.println(simplex.vertices.size());
					support = support(triangle,otherTriangle,direction);
					
					if(Vector.dotProduct(support, direction) <= 0) {
						System.out.println("Not collsiion");
						break;
					}
					
					simplex.vertices.add(support.toPoint());
					
					if((simplex.vertices.size() == 2) ? Line(simplex,direction) : (simplex.vertices.size() == 3) ? Triangle(simplex,direction) : (simplex.vertices.size() == 4) ? Tetrahedron(simplex,direction) : false) {
						break;
					}
				}
			}
		}

		
		return false;
	}
	
	private boolean Line(Polygon simplex, Vector direction) {
		Vector ab = Point.substract(simplex.vertices.get(1), simplex.vertices.get(0)).toVector();
		Vector ao = simplex.vertices.get(0).toVector();
		ao.mirror();
		
		if(sameDirection(ab,ao))
			direction = Vector.crossProduct(Vector.crossProduct(ab, ao), ab);
		else {
			Point a = simplex.vertices.get(0);
			
			simplex.vertices.clear();
			simplex.vertices.add(a);
			direction = ao;	
		}
		
		return false;
	}
	
	public boolean Triangle(Polygon simplex, Vector direction) {
		Point a = simplex.vertices.get(0);
		Point b = simplex.vertices.get(1);
		Point c = simplex.vertices.get(2);
		
		Vector ab = Point.substract(b, a).toVector();
		Vector ac = Point.substract(c, a).toVector();
		Vector ao = a.toVector();
		ab.mirror();
		
		Vector abc = Vector.crossProduct(ab, ac);
		
		if(sameDirection(Vector.crossProduct(abc, ac), ao)) {
			if(sameDirection(ac,ao)) {
				simplex.vertices.clear();
				simplex.vertices.add(a);
				simplex.vertices.add(c);
				
				direction = Vector.crossProduct(Vector.crossProduct(ac, ao), ac);
			}else {
				simplex.vertices.clear();
				simplex.vertices.add(a);
				simplex.vertices.add(b);
				return Line(simplex, direction);
			}
		} else {
			if (sameDirection(Vector.crossProduct(ab, abc), ao)) {
				simplex.vertices.clear();
				simplex.vertices.add(a);
				simplex.vertices.add(b);
				return Line(simplex, direction);
			}

			else {
				if (sameDirection(abc, ao)) {
					direction = abc;
				}

				else {
					simplex.vertices.clear();
					simplex.vertices.add(a);
					simplex.vertices.add(c);
					simplex.vertices.add(b);
					
					abc.mirror();
					direction = abc;
				}
			}
		}
		
		return false;
	}
	
	private boolean Tetrahedron(Polygon simplex, Vector direction){
		Point a = simplex.vertices.get(0);
		Point b = simplex.vertices.get(1);
		Point c = simplex.vertices.get(2);
		Point d = simplex.vertices.get(3);

		Vector ab = Point.substract(b, a).toVector();
		Vector ac = Point.substract(c, a).toVector();
		Vector ad =  Point.substract(d, a).toVector();
		Vector ao = a.toVector();
		ab.mirror();
	 
		Vector abc = Vector.crossProduct(ab, ac);
		Vector acd = Vector.crossProduct(ac, ad);
		Vector adb = Vector.crossProduct(ad, ab);
	 
		if (sameDirection(abc, ao)) {
			simplex.vertices.clear();
			simplex.vertices.add(a);
			simplex.vertices.add(b);
			simplex.vertices.add(c);
			return Triangle(simplex, direction);
		}
			
		if (sameDirection(acd, ao)) {
			simplex.vertices.clear();
			simplex.vertices.add(a);
			simplex.vertices.add(c);
			simplex.vertices.add(d);
			return Triangle(simplex, direction);
		}
	 
		if (sameDirection(adb, ao)) {
			simplex.vertices.clear();
			simplex.vertices.add(a);
			simplex.vertices.add(d);
			simplex.vertices.add(b);
			return Triangle(simplex, direction);
		}
	 
		return true;
	}
	
	public boolean sameDirection(Vector direction, Vector ao) {
		return Vector.dotProduct(direction, ao) >  0;
	}
	
	public static Point furthestPoint(Polygon triangle, Vector direction) {
		Point maxPoint = new Point(0,0);
		double maxDistance = 0;
 
		for (Point vertex : triangle.vertices) {
			double distance = Vector.dotProduct(new Vector((float)vertex.x, (float)vertex.y), direction);
			if (distance > maxDistance) {
				maxDistance = distance;
				maxPoint = vertex;
			}
		}
 
		return maxPoint;
	}
	
	private Vector support(Polygon triangle, Polygon secondTriangle, Vector direction) {
		
		return Point.substract(Collider.furthestPoint(triangle, direction), Collider.furthestPoint(secondTriangle, direction)).toVector();
	}
	
	private boolean doCollisionFieldsCollide(Collider collider) {
		return !(collisionFieldPosition.x + collisionFieldWidth / 2 <= collider.collisionFieldPosition.x - collider.collisionFieldWidth / 2 || collisionFieldPosition.x - collisionFieldWidth / 2 >= collider.collisionFieldPosition.x + collider.collisionFieldWidth / 2 ||
				collisionFieldPosition.y + collisionFieldHeight / 2 <= collider.collisionFieldPosition.y - collider.collisionFieldHeight / 2 || collisionFieldPosition.y - collisionFieldHeight / 2 >= collider.collisionFieldPosition.y + collider.collisionFieldHeight / 2);
	}
	
	public Polygon getCollisonPoints(){
		return (triangles.size() >= 2) ? triangles.get(0) : new Polygon();
	}
}
