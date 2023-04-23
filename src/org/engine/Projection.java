package org.engine;

public class Projection {

	public float min;
	public float max;
	
	public Projection(float min, float max) {
		this.min = min;
		this.max = max;
	}
	
	public boolean doOverlap(Projection otherProjection) {
		return (!(otherProjection.max < this.min || this.max < otherProjection.min));
	}
	
}
