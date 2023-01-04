package org.graphics;

import org.input.KeyInput;
import org.input.MouseInput;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

public class Renderer {
	
	private static GLWindow window = null;
	private static GLProfile profile = null;
	
	private static int screenW = 1280;
	private static int screenH = 720;

	public static float unitsWide = 10;
	public static float unitsTall = 0;
	
	
	
	public static void init() {
		GLProfile.initSingleton();
		profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities caps = new GLCapabilities(profile);
		
		window = GLWindow.create(caps);
		
		window.setSize(screenW, screenH);
		window.setTitle("JOGL");
		window.setVisible(true);
		window.addGLEventListener(new EventListener());
		window.addMouseListener(new MouseInput());
		window.addKeyListener(new KeyInput());
		
		window.display();
	}
	
	public static void render() {
		if(window == null) return;
		
		window.display();
	}
	
	public static int getWindowWidth() { return window.getWidth(); }
	public static int getWindowHeight() { return window.getHeight(); }
	public static GLProfile getProfile() { return profile; }
	
	
}
