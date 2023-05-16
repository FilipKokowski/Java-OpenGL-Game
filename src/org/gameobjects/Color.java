package org.gameobjects;

public class Color {
	public float red = 1;
	public float green = 1;
	public float blue = 1;
	public float alpha = 1;

	public Color(int red, int green, int blue, int alpha) {
		this.red = Math.max(0, Math.min(255, red)) / 255;
		this.green = Math.max(0, Math.min(255, green)) / 255;
		this.blue = Math.max(0, Math.min(255, blue)) / 255;
		this.alpha = Math.max(0, Math.min(255, alpha)) / 255;
	}
	
	public Color(Color color) {
		this.red = Math.max(0, Math.min(255, color.red)) / 255;
		this.green = Math.max(0, Math.min(255, color.green)) / 255;
		this.blue = Math.max(0, Math.min(255, color.blue)) / 255;
		this.alpha = Math.max(0, Math.min(255, color.alpha)) / 255;
	}
	
	public void setColor(int red, int green, int blue, int alpha) {
		this.red = Math.max(0, Math.min(255, red)) / 255;
		this.green = Math.max(0, Math.min(255, green)) / 255;
		this.blue = Math.max(0, Math.min(255, blue)) / 255;
		this.alpha = Math.max(0, Math.min(255, alpha)) / 255;
	}
	
	public void setColor(Color color) {
		this.red = Math.max(0, Math.min(255, color.red)) / 255;
		this.green = Math.max(0, Math.min(255, color.green)) / 255;
		this.blue = Math.max(0, Math.min(255, color.blue)) / 255;
		this.alpha = Math.max(0, Math.min(255, color.alpha)) / 255;
	}
	
	public Color copy() {
		return new Color(this);
	}
	
	public static Color clear() {
		return new Color(255,255,255,255);
	}
}
