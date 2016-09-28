package ui;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import main.GameActivity;
import main.ResourcesManager;
import main.UserData;

public class AnalogStick {

	private boolean visible = true;
	private Sprite sprite;
	private Sprite bgSprite;
	private float x, y;
	private float declensionX = 0, declensionY = 0;
	private boolean active = false;
	public boolean stop = false;
	
	public float prevActionDownX = 0;
	public float prevActionDownY = 0;

	public AnalogStick(float x, float y, Entity hud){
		this.x = x;
		this.y = y;
		
		float scale = UserData.getInstance().getAnalogSize();
		sprite = new Sprite(x, y, ResourcesManager.getInstance().analog, ResourcesManager.getInstance().vbom);
		sprite.setScale(scale);
		bgSprite = new Sprite(x, y, ResourcesManager.getInstance().analog_bg, ResourcesManager.getInstance().vbom);
		bgSprite.setScale(scale);
		
		hud.attachChild(bgSprite);
		hud.attachChild(sprite);
	}
	
	public void reset(){
		declensionX = 0;
		declensionY = 0;
		prevActionDownX = 0;
		prevActionDownY = 0;
		release();
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
		sprite.setVisible(visible);
		bgSprite.setVisible(visible);
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void input(TouchEvent pSceneTouchEvent){
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
			setActive(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
		}
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE){
			float dx = pSceneTouchEvent.getX() - prevActionDownX;
			float dy = pSceneTouchEvent.getY() - prevActionDownY;
			if(isActive()){
				tilt(dx, dy);
			}
			prevActionDownX = pSceneTouchEvent.getX();
			prevActionDownY = pSceneTouchEvent.getY();
		}
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
			if(isActive()){
				release();
			}
		}
	}
	
	public float getDeclensionX(){
		return declensionX;
	}
	
	public float getDeclensionY(){
		return declensionY;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void tilt(float dx, float dy){
		if(isActive()){
			float nx = sprite.getX() + dx;
			float ny = sprite.getY() + dy;
			float nd = (float)Math.sqrt( Math.pow(this.x - nx, 2) + Math.pow(this.y - ny, 2));
			if(nd < sprite.getWidth()*0.75f){
				sprite.setPosition(nx, ny);
				declensionX = nx - this.x;
				declensionY = ny - this.y;
			}
		}
	}
	
	public void release(){
		active = false;
		sprite.setPosition(x, y);
		stop = true;
		declensionX = 0;
		declensionY = 0;
	}
	
	public void setActive(float x, float y){
		float d = (float)Math.sqrt( Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
		if(d < sprite.getWidthScaled()/2){
			active = true;
		}
	}
	
	public boolean isActive(){
		return active;
	}
	
	
	
	
}
