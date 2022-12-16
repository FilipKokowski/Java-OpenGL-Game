package org.gameobjects;

import org.engine.Handler;
import org.scene.entities.Camera;

public class Entities extends GameObject {
	public float velocityX = 0;
	public float velocityY = 0;
	
	public float speed = 3;
	public float speedCap = 6;
	public float acceleration = speed / 100;
	public float friction = speed * acceleration / 2;
	
	public float jumpForce = 6;
	public float mass = 3;
	public float G = 9.81f;
	
	public static Camera camera = new Camera();
	public static int cameraID = -1;
	
	public void gravity() { velocityY -= mass/G; };
}
