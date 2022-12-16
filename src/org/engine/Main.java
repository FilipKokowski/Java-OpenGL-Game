package org.engine;

import org.scene.background.Background;
import org.scene.entities.Square;
import org.graphics.Renderer;

public class Main {
	
	public static void main(String[] args) {
		Renderer.init();
		GameLoop.start();
		
		Handler.addGO(new Background("/res/org/scene/background/bg.jpg"));
		Handler.addGO(new Square());
	}
}
