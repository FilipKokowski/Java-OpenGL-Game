package org.scene.entities;

import org.engine.GameLoop;
import org.engine.Handler;
import org.gameobjects.Entities;
import org.gameobjects.GameObject;
import org.gameobjects.ID;

public class Boomerang extends Entities{
	
	private static String animationPath = "res/org/scene/entities/Boomerang/boomer.png";
	
	private float rotationSpeed = 15f;
	
	private float verticalRotationRadius = 1f;
	
	//Angle of the path
	private float angle;
	
	//Starting angle on the path
	private float startingAngle = 4.75f;
	
	//Middle of the circular path
	private float x0, y0;
	
	private boolean facingRight;
	public boolean destroy;
	
	public float cooldown = 5;
	
	//public HUD cooldownBarBottom = new HUD(-(Renderer.unitsWide / 2) + 1.125f, Main.onGround(.25f) + .125f, 2, .25f);
	//public HUD cooldownBarTop = new HUD(-(Renderer.unitsWide / 2) + 1.125f, Main.onGround(.25f) + .125f, 1, .25f);

	public Boomerang(float x, float y, float width, float height) {
		super(x, y, width, height, animationPath);
		
		id = ID.Projectile;
		
		x0 = x;
		y0 = y + verticalRotationRadius;
		
		speed = .1f;
		
		showBounds = true;
		
		//cooldownBarBottom.Color(.25f, .25f, .25f, .8f);
		//cooldownBarTop.Color(.25f, 1, .25f, .8f);
		
		//Handler.addHUD(cooldownBarBottom);
		//Handler.addHUD(cooldownBarTop);
	}
	
	public void update() {
		
		super.update();
		
		rotation += rotationSpeed;
		
		cooldown -= GameLoop.updateDelta();
		
		if(facingRight)
			angle += speed;
		else
			angle -= speed;
		
		position.x = (float) (x0 + Math.cos(angle) * verticalRotationRadius);
		position.y = (float) (y0 + Math.sin(angle) * verticalRotationRadius);
		
		for(int i = 0; i < Handler.gameObjects.size(); i++) {
			//GameObject tempObj = getAt(i);
			
			/*if((tempObj.id == ID.Obstacle || tempObj.id == ID.Entities) && doOverlap(getBounds(), tempObj.getBounds())) {
				destroy = true;
			}
			if((tempObj.id == ID.Player) && doOverlap(getBounds(), tempObj.getBounds())) {
				//System.out.println("Collided with player at " + angle + " degrees");
			}*/
		}
		
		if((angle > 11 || angle < -1)) destroy = true;
		
		System.out.println(cooldown);
	}

	public void setX(float x) { this.x0 = x; }
	public void setY(float y) { this.y0 = y + verticalRotationRadius; }
	
	public void resetAngle() { angle = startingAngle; };
	
	public void setFacing(boolean side) { facingRight = side; }
	
	public void setCooldown(float seconds) { cooldown = seconds; }
	
}
