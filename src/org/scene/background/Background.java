package org.scene.background;

import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.graphics.Renderer;

public class Background extends GameObject{
	
	public Background(String path) {
		super(0, 0, Renderer.unitsWide * 2, Renderer.unitsTall, path, ID.HUD);	
		
	}
}
