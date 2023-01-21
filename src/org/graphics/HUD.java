package org.graphics;

import org.gameobjects.GameObject;

public class HUD extends GameObject {

	public HUD(float x, float y, float width, float height) {
		super(x, y, width, height, "");
		// TODO Auto-generated constructor stub
	}
	
	public void render() {
		Graphics.setColor(red, green, blue, alpha);
		Graphics.fillRect(x, y, width, height);
	}
	
	public void Color(float red, float green, float blue, float alpha) {
		this.red = red;
		this.blue = blue;
		this.green = green;
		this.alpha = alpha;
	}

}