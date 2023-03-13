package org.engine;

import org.graphics.Renderer;

public class GameLoop {

	public static boolean running = false;
	
	private static int updates = 0;
	private static final int MAX_UPDATES = 5;
	private static long lastUpdateTime = 0;
	
	private static int targetFPS = 60;
	private static int targetTime = 1000000000 / targetFPS;
	
	public static int FPS = 0;
	public static String FPSString = "";
	
	public static void start() {
		Thread thread = new Thread() {
			public void run() {
				running = true;
				
				lastUpdateTime = System.nanoTime();
			    
				int FPSCounter = 0;
				long lastFPSCheck = System.nanoTime();
				
				while(running) {
					long currentTime = System.nanoTime();
					
					updates = 0;
					
					while(currentTime - lastUpdateTime >= targetTime) {
						Handler.update();
						lastUpdateTime += targetTime;
						updates++;
						
						if(updates > MAX_UPDATES) break;
					}
					
					Renderer.render();
					
					
					FPSCounter++;
					if(System.nanoTime() >= lastFPSCheck + 1000000000) {
						//System.out.println(FPSCounter);
						FPS = FPSCounter;
						FPSString = String.valueOf(FPS);
						FPSCounter = 0;
						lastFPSCheck = System.nanoTime();
					}
					
					long timeTaken = System.nanoTime() - currentTime;
					
					if(targetTime > timeTaken) {
						try {
							Thread.sleep((targetTime -  timeTaken) / 1000000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		thread.setName("GameLoop");
		thread.start();
	}
	
	public static float updateDelta() {
		return 1.0f / 1000000000 * targetTime;
	}

}
