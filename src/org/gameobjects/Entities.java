package org.gameobjects;

import org.scene.entities.Camera;

public class Entities extends GameObject {
	public Entities(float x, float y, float width, float height, String animationPath) {
		super(x, y, width, height, animationPath);
		// TODO Auto-generated constructor stub
	}
	
	public boolean crouched = false;
	public boolean forceCrouch = false;
	public boolean onGround = false;

	public float velocityX = 0;
	public float velocityY = 0;
	
	public float speed = 3;
	public float crouchSpeedCap = speed / 1.5f;
	public float speedCap = 6;
	public float acceleration = speed / 100;
	public float friction = speed * acceleration / 2;
	
	public float jumpForce = 6;
	public float crouchJumpForce = jumpForce / 2;
	public float mass = 3;
	public float G = 9.81f;
	
	public static Camera camera = new Camera();
	public static int cameraID = -1;
	
	public static boolean collisionR = false;
	public static boolean collisionL = false;
	public static boolean collisionU = false;
	public static boolean collisionD = false;
	
	public void gravity() { velocityY -= mass/G; };
	
	public void reloadCrouchJumpForce() {
		crouchJumpForce = jumpForce / 2;
	}
	
	public void reloadJumpForce() {
		jumpForce = crouchJumpForce / 2;
	}
	
	public void reloadCrouchSpeedCap() {
		crouchSpeedCap = speedCap / 1.5f;
	}
	
	public void reloadSpeedCap() {
		speedCap = crouchSpeedCap * 1.5f;
	}
	
	public void clearCollision() {
		collisionR = false;
		collisionL = false;
		collisionU = false;
		collisionD = false;
	}
}
