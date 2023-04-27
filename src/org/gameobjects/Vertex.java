package org.gameobjects;

public class Vertex {

	public float x = 0;
	public float y = 0;
	
	public Vertex(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float lenght() {
		return (float)(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
	}
	
	public float dotProduct(Point point) {
		return (float)(x * point.x + y * point.y);
	}
	
	public Vertex clone() {
		return new Vertex(x,y);
    }
}
