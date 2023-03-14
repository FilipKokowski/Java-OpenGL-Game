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
		
		Obstacle obstacle = new Obstacle(-3, onGround(1) + 1, 2, 1, "");
		obstacle.setColor(1f, .25f, .58f, .5f);
		Handler.addGO(obstacle);
		
		Handler.addHUD(new HUD(-Renderer.unitsWide / 2 + .5f,Renderer.unitsTall / 2 - .25f,1,.5f, false));
		Handler.addHUD(new HUD(Renderer.unitsWide / 2 - .5f,Renderer.unitsTall / 2 - .25f,1,.5f, true));
		
		Handler.addHUD(new HUD(0,Renderer.unitsTall / 2 - .25f, 1, .5f, true));
        
		Obstacle obstacle2 = new Obstacle(-5, onGround(1) + 1, 2, 1, "");
		obstacle2.setColor(1f, .25f, .58f, .5f);
		Handler.addGO(obstacle2);
		
		Obstacle obstacle3 = new Obstacle(4.8f, onGround(.8f) + .2f, 8, .8f, "");
		obstacle3.setColor(.89f, .5f, .32f, .5f);
		Handler.addGO(obstacle3);
		
		Obstacle obstacle4 = new Obstacle(-7.5f, onGround(1) + 2.25f, 2, 1, "");
		obstacle4.setColor(1f, .25f, .58f, .5f);
		Handler.addGO(obstacle4);
		
		Obstacle sign = new Obstacle(-1, onGround(.375f), .5f, .375f, "res/org/scene/objects/Sign/sign.png");
		sign.verticallyCenteredText = true;
		sign.offsetText(0, .05f);
		sign.setTextColor(0, 0, 0, 1);
		sign.text = "Ben";
		Handler.addGO(sign);
	
		GameLoop.start();
		
		Entities.physicsOn = true;
	}
	
	public static float onGround(float height) { return(-Renderer.unitsTall + height) / 2; }
}
