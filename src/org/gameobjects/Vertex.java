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
	
	public float dotProduct(Vertex vertex, GameObject parentObject, GameObject otherParentObject) {
		//System.out.println("parentX: " + parentObject.getX() + "parentY: " + parentObject.getY());
		//System.out.println("otherParentObjectX: " + otherParentObject.getX() + "otherParentObjectY: " + otherParentObject.getY() + "\n\n\n");
		
		//System.out.println(parentObject.getClass().getSimpleName() + " " +  (float)((x) * (vertex.x) + (y) * (vertex.y)));
		
		return (float)((x + parentObject.getX()) * (vertex.x + otherParentObject.getX()) + (y + parentObject.getY()) * (vertex.y + otherParentObject.getY()));
	}
	
	public Vertex clone() {
		return new Vertex(x,y);
    }
}
