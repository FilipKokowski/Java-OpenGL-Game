package org.scene.background;

import org.gameobjects.GameObject;
import org.graphics.Animation;
import org.graphics.Renderer;
import org.resource.ImageResource;

public class Background extends GameObject {

	public Background(String path) {
		super(0,0, Renderer.unitsWide * 2, Renderer.unitsTall);	
		
		
		animations = new Animation[1];
		animations[0] = new Animation();
		animations[0].frames = new ImageResource[1];
		animations[0].frames[0] = new ImageResource(path);
	}
	
}
