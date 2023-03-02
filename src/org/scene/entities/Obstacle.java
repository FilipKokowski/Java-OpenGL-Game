package org.scene.entities;

import java.awt.Font;

import org.engine.GameLoop;
import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.graphics.Graphics;

public class Obstacle extends GameObject{

	private static String animationPath = "";
	
	public Obstacle(float x, float y, float width, float height) {
		super(x, y, width, height, animationPath);
		
		id = ID.Obstacle;
		
		setFont(new Font("SansSerif", Font.BOLD, (int)fontSize));
	}
	
	public void update() {
		centerTextHorizontally();
		placeTextAbove();
		
		text = "FPS: " + GameLoop.FPS;
	}
	
	public void render() {
		Graphics.setColor(red, green, blue, alpha);
		Graphics.fillRect(x, y, width, height);
		Graphics.setColor(1, 1, 1, 1);
	}
}
