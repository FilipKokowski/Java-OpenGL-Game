package org.engine;

import java.util.ArrayList;

import org.gameobjects.GameObject;
import org.gameobjects.Vertex;

public class Collider {


 ArrayList<Vertex> vertices;
	GameObject parentObject;
	
	public Collider(ArrayList<Vertex> vertices, GameObject parentObject) {
		this.vertices = vertices;
		this.parentObject = parentObject;
	}
	
	public boolean doCollide(Collider otherCollider){
		ArrayList<Vertex> combinedVertices = new ArrayList<Vertex>();
		combinedVertices.addAll(vertices);
		combinedVertices.addAll(otherCollider.vertices);
		
		for(Vertex axis : combinedVertices) {
			Projection projectionA = project(this, axis);
			Projection projectionB = project(otherCollider, axis);
			
			//System.out.println(projectionB.max + " < " + projectionA.min + " || "  +  projectionA.max + " < " + projectionB.min);
			
			if(!projectionA.doOverlap(projectionB)) {
				return false;
			}
		}
		return true;
	}

    private Projection project(Collider collider, Vertex axis) {
        // Project the shapes along the axis
        float min = axis.dotProduct(collider.vertices.get(0), parentObject, collider.parentObject); // Get the first min
        float max = min;
        for (int i = 1; i < collider.vertices.size(); i++) {
            float p = axis.dotProduct(collider.vertices.get(i), parentObject, collider.parentObject); // Get the dot product between the axis and the node
            if (p < min) {
                min = p;
            } else if (p > max) {
                max = p;
            }
        }
        return new Projection(min, max);
    }
}
