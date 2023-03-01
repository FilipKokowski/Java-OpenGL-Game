package org.graphics;

import org.engine.GameLoop;
import org.gameobjects.GameObject;
import org.scene.entities.Camera;

public class HUD extends GameObject {

	public HUD(float x, float y, float width, float height) {
		super(x, y, width, height, "");
		// TODO Auto-generated constructor stub
		
		red = .5f;
		green = .5f;
		blue = .5f;
		alpha = .75f;
	}
	
	public void render() {
		Graphics.setColor(red, green, blue, alpha);
		Graphics.fillRect(x, y, width, height);
	}
	
	public void update() {
		textOffsetX = Camera.x;
		textOffsetY = Camera.y;
		text = "FPS: " + GameLoop.FPS;
	}
	
	public void Color(float red, float green, float blue, float alpha) {
		this.red = red;
		this.blue = blue;
		this.green = green;
		this.alpha = alpha;
	}

}
