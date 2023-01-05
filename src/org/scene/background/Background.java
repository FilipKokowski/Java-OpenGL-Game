package org.scene.background;

import org.gameobjects.GameObject;
import org.graphics.Renderer;

public class Background extends GameObject {

	private static String animationPath = "/res/org/animations/Background.txt";
	
	public Background(String path) {
		super(0, 0, Renderer.unitsWide * 2, Renderer.unitsTall, animationPath);	
		
		/*animations = new Animation[1];
		animations[0] = new Animation();
		animations[0].frames = new ImageResource[1];
		animations[0].frames[0] = new ImageResource(path);*/
	}
	
}
