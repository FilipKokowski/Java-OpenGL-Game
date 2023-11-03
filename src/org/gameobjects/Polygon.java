package org.gameobjects;

import java.util.ArrayList;
import java.util.Arrays;

public class Polygon {

	public ArrayList<Point> vertices = new ArrayList<Point>();
	
	public Polygon(ArrayList<Point> vertices) {
		this.vertices = vertices;
	}
	
	public Polygon(Point[] vertices) {
		for(Point vertex : vertices) {
			this.vertices.add(vertex);
		}
	}
	
	public Polygon() {
		this.vertices = new ArrayList<Point>();
	}
	
}
