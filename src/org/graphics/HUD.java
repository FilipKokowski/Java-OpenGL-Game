package org.graphics;

import org.engine.GameLoop;
import org.gameobjects.GameObject;
import org.gameobjects.ID;
import org.input.MouseInput;
import org.scene.entities.Camera;

public class HUD extends GameObject {
	
	public boolean interactable;
	
	private float textRed = 0;
	private float textGreen = 0;
	private float textBlue = 0;
	private float textAlpha = 1;
	
	private float textRedSelect = 0;
	private float textGreenSelect = 0;
	private float textBlueSelect = 1;
	private float textAlphaSelect = 1;
	
	private boolean focused;
	private boolean clicked;

	public HUD(float x, float y, float width, float height, boolean interactable) {
		super(x, y, width, height, "");
		// TODO Auto-generated constructor stub
		
		this.interactable = interactable;
		
		red = 1;
		green = 1;
		blue = 1;
		alpha = 1;
		
		id = ID.HUD;
		
		fontSize = 24;
		setCustomFont("res/org/fonts/pixelmix.ttf");
		setTextColor(0,0,0,1);
	}
	
	/*public void render() {
		Graphics.setColor(red, green, blue, alpha);
		Graphics.fillRect(x, y, width, height);
	}*/
	
	public void update() {
		centerTextHorizontally();
		centerTextVertically();
		textOffsetX += Camera.x;
		textOffsetY += Camera.y;
		text = "FPS: " + GameLoop.FPS;
		
		//System.out.println(MouseInput.getMouseX() + "/" + MouseInput.getMouseY() + ": (" + x + "/" + y + ")");
		
		if(interactable && onClick()) {
			System.out.println("clicked: " + clicked);
			if(clicked) {
				setTextColor(textRed, textGreen, textBlue, textAlpha);
				System.out.println("Unselect");
			}
			else	
				setTextColor(textRedSelect, textGreenSelect, textBlueSelect, textAlphaSelect);
			
			clicked = !clicked;
			MouseInput.pressed = false;
		}
		else if(interactable && hover()) {
			drawBounds();
			focused = true;
		}
		else {
			hideBounds();
			focused = false;
		}
	}
	
	public void setTextColorOnSelect(){
		
	}
	
	public void Color(float red, float green, float blue, float alpha) {
		this.red = red;
		this.blue = blue;
		this.green = green;
		this.alpha = alpha;
	}

}
