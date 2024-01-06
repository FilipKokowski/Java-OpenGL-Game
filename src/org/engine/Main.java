package org.engine;

import org.scene.background.Background;
import org.scene.entities.Obstacle;
import org.scene.entities.Player;

import org.gameobjects.Entities;
import org.graphics.HUD;
import org.graphics.Renderer;

public class Main {
	
	public static void main(String[] args) {
		Renderer.init();
		
		Handler.addGO(new Background("res/org/scene/background/bg.jpg"));
		
		Handler.addGO(new Player());
		
		Obstacle obstacle = new Obstacle(-3, onGround(1) + 1, 2, 1, false, "");
		obstacle.setColor(255, 64, 130, 127);
		Handler.addGO(obstacle);
	
		Handler.addHUD(new HUD(-Renderer.unitsWide / 2 + .5f,Renderer.unitsTall / 2 - .25f,1,.5f, false));
		Handler.addHUD(new HUD(Renderer.unitsWide / 2 - .5f,Renderer.unitsTall / 2 - .25f,1,.5f, true));
		
		Handler.addHUD(new HUD(0,Renderer.unitsTall / 2 - .25f, 1, .5f, true));
        
		Obstacle obstacle2 = new Obstacle(-5, onGround(1) + 1, 2, 1, false, "");
		obstacle2.setColor(255, 64, 135, 127);
		Handler.addGO(obstacle2);
	
		for(float i = 0; i < 4; i += .6f) {
			Obstacle obstacle3 = new Obstacle(1.5f + i, onGround(.8f) + .2f, .4f, .8f, false, "");
			obstacle3.setColor(220, 127, 80, 127);
			Handler.addGO(obstacle3);
		}
		
		Obstacle obstacle4 = new Obstacle(-7.5f, onGround(1) + 2.25f, 2, 1, false, "");
		obstacle4.setColor(255, 64, 135, 127);
		Handler.addGO(obstacle4);
		
		/*Obstacle sign = new Obstacle(-3, onGround(3.75f), 5.f, 3.75f, false, "res/org/scene/objects/Sign/sign.png");
		sign.verticallyCenteredText = true;
		sign.offsetText(0, .05f);
		sign.setTextColor(0, 0, 0, 1);
		sign.text = sign.uuid;
		Handler.addGO(sign);*/

		GameLoop.start();
		
		Entities.physicsOn = true;
	}
	
	public static float onGround(float height) { return(-Renderer.unitsTall + height) / 2; }
}
