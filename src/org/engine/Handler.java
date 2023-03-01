package org.engine;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gameobjects.GameObject;
import org.graphics.HUD;
import org.scene.background.Background;
import org.scene.entities.Camera;

import com.jogamp.opengl.util.awt.TextRenderer;

public class Handler {
	public static ConcurrentLinkedQueue<GameObject> gameObjects = new ConcurrentLinkedQueue<GameObject>();
	public static ConcurrentLinkedQueue<HUD> HUDs = new ConcurrentLinkedQueue<HUD>();
	public static ConcurrentLinkedQueue<Background> backgrounds = new ConcurrentLinkedQueue<Background>();
	public static ConcurrentLinkedQueue<Camera> cameras = new ConcurrentLinkedQueue<Camera>();
	
	public static void render() {
		for(GameObject go : gameObjects){
			go.render();
		}
	}
	
	public static void renderText(String uuid) {
		for(GameObject go : gameObjects){
			if(go.uuid == uuid) {
				go.renderText();
			}
		}
	}
	
	public static void renderHUD() {
		for(HUD hud : HUDs){
			hud.render();
		}
	}
	
	public static void renderHUDText(String uuid) {
		for(HUD hud : HUDs){
			if(hud.uuid == uuid) {
				hud.renderText();
			}
		}
	}
	
	public static void update() {
		for(GameObject go : gameObjects){
			go.update();
		}
		
		for(HUD hud : HUDs){
			hud.update();
		}
	}
	
	public static HashMap<String, TextRenderer> getTextInfo(){
		HashMap<String, TextRenderer> textInfo = new HashMap<String, TextRenderer>();
		
		for(GameObject go : gameObjects){
			textInfo.put(go.uuid, go.textRenderer);
		}
		
		return textInfo;
	}
	
	public static HashMap<String, TextRenderer> getHUDTextInfo(){
		HashMap<String, TextRenderer> textInfo = new HashMap<String, TextRenderer>();
		
		for(HUD hud : HUDs){
			textInfo.put(hud.uuid, hud.textRenderer);
		}
		
		return textInfo;
	}
	
	public static void addGO(GameObject go) { gameObjects.offer(go); }
	public static void removeGO(GameObject go) { gameObjects.remove(go); }
	
	public static void addCam(Camera cam) { cameras.offer(cam); };
	public static void removeCam(Camera cam) { cameras.offer(cam); };
	
	public static void addHUD(HUD hud) { HUDs.offer(hud); };
	public static void removeHUD(HUD hud) { HUDs.offer(hud); };
	
}
