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
	
	public float crossProduct(Point point) {
		return (float)(x * point.x - y * point.y);
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
