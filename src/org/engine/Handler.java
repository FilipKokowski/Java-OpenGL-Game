package org.engine;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.gameobjects.GameObject;
import org.scene.background.Background;
import org.scene.entities.Camera;

public class Handler {
	public static ConcurrentLinkedQueue<GameObject> gameObjects = new ConcurrentLinkedQueue<GameObject>();
	public static ConcurrentLinkedQueue<Background> backgrounds = new ConcurrentLinkedQueue<Background>();
	public static ConcurrentLinkedQueue<Camera> cameras = new ConcurrentLinkedQueue<Camera>();
	
	public static void render() {
		for(GameObject go : gameObjects){
			go.render();
		}
	}
	
	public static void update() {
		for(GameObject go : gameObjects){
			go.update();
		}
	}
	
	public static void addGO(GameObject go) { gameObjects.offer(go); }
	public static void removeGO(GameObject go) { gameObjects.remove(go); }
	
	public static void addCam(Camera cam) { cameras.offer(cam); };
	public static void removeCam(Camera cam) { cameras.offer(cam); };
	
}
