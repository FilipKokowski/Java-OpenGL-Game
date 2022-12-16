package org.graphics;

import org.resource.ImageResource;

public class Animation {
	
	public ImageResource[] frames;
	
	private int currentFrame = 0;
	
	public int FPS = 8;
	private long lastFrameTime = 0;
	
	public boolean loop = true;
	
	public void play() {
		long currentTime = System.nanoTime();
		
		if(currentTime > lastFrameTime + (1000000000 / FPS)) {
			currentFrame++;
			
			if(currentFrame >= frames.length) {
				if(loop) currentFrame = 0;
				else currentFrame--;
			}
			
			lastFrameTime = currentTime;
		}
	}
	
	public ImageResource getImage() {
		return frames[currentFrame];
	}
}
