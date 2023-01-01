package org.engine;

import org.scene.background.Background;
import org.scene.entities.Obstacle;
import org.scene.entities.Square;
import org.graphics.Renderer;

public class Main {
	
	public static void main(String[] args) {
		Renderer.init();
		GameLoop.start();
		
		Handler.addGO(new Background("/res/org/scene/background/bg.jpg"));
		
		Handler.addGO(new Square());
		
		Obstacle obstacle = new Obstacle(-3, onGround(1), 2, 1);
		obstacle.setColor(1f, .25f, .58f, .5f);
		Handler.addGO(obstacle);
		
		Obstacle obstacle2 = new Obstacle(-5, onGround(1) + 1, 2, 1);
		obstacle2.setColor(1f, .25f, .58f, .5f);
		Handler.addGO(obstacle2);
	}
	
	public static float onGround(float height) { return(-Renderer.unitsTall + height) / 2; }
}
