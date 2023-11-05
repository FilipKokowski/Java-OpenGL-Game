package org.gameobjects;

import org.graphics.Graphics;

public class Vector {

	public float x = 0;
	public float y = 0;
	
	private Color color = new Color(255, 255, 255, 255);
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float lenght() {
		return (float)(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
	}
	
	public float dotProduct(Point point) {
		return (float)(x * point.x + y * point.y);
	}
	
	public float dotProduct(Vector vertex) {
		return (float)(x * vertex.x + y * vertex.y);
	}
	
	public static double dotProduct(Vector v1, Vector v2) {
		return (v1.x * v2.x + v1.y * v2.y);
	}
	
	public static double crossProduct(Point p1, Point p2, Point p3) {
		return (p2.x - p1.x) * (p3.y - p1.y) * (p2.y - p1.y) * (p3.x - p1.x);
	}
	
	public float determinant(Point point) {
		return (float)(x * point.x - y * point.y);
	}
	
	public static float determinant(Vector v1, Vector v2) {
		return (v1.x * v2.x - v1.y * v2.y);
	}
	
	public static double magnitude(Vector v) {
		return Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2)); 
	}
	
	public static int orientation(Point p, Point q, Point r) {
	    double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

	    if (val == 0.0)
	        return 0;
	    
	    return (val > 0) ? 1 : 2;
	}
	
	public Vector clone() {
		return new Vector(x,y);
    }
	
	public void print() {
		System.out.println("x: " + this.x + " y: " + this.y);
	}
	
	public void setColor(int r, int g, int b, int a) {
		color.setColor(r, g, b, a);
	}
	
	public Color getColor() {
		return color;
	}
	
	public void render() {
		Graphics.setColor(color.red, color.green, color.blue, color.alpha);
		Graphics.drawLine(x * (-2), y * (-2), x * 2, y * 2, ID.HUD);
		Graphics.setColor(1, 1, 1, 1);
	}
}
