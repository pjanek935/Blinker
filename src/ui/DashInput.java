package ui;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import main.ResourcesManager;
import player.Player;
import scene.BaseScene;

public class DashInput {
	
	private Sprite arrowBody;
	private Sprite arrowHead;
	
	private float begX = 0;
	private float begY = 0;
	private boolean active = false;
	private float angle;
	private boolean blink = false;
	private float blinkLength = 0;
	
	public DashInput(BaseScene parentScene, MyHUD hud){
		arrowBody = new Sprite(0, 0, ResourcesManager.getInstance().arrow_body,
				ResourcesManager.getInstance().vbom);
		arrowHead = new Sprite(0, 0, ResourcesManager.getInstance().arrow_head,
				ResourcesManager.getInstance().vbom);
		parentScene.attachChildAtBackground2(arrowBody);
		parentScene.attachChildAtBackground2(arrowHead);
		arrowBody.setVisible(false);
		arrowHead.setVisible(false);
		arrowBody.setAlpha(0.25f);
		arrowHead.setAlpha(0.25f);
	}
	
	public void input(TouchEvent pSceneTouchEvent, Camera camera){
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
			arrowBody.setVisible(true);
			arrowHead.setVisible(true);
			arrowHead.setHeight(32);
			arrowBody.setHeight(32);
			begX = pSceneTouchEvent.getX();
			begY = pSceneTouchEvent.getY();
			active = true;
		}
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE){
			if(isActive()){
				float dy = begY - pSceneTouchEvent.getY();
				float dx = begX - pSceneTouchEvent.getX();
				angle = (float)Math.atan2(dy, dx);
				arrowBody.setRotation((float)Math.toDegrees(-angle) - 90);
				arrowHead.setRotation((float)Math.toDegrees(-angle) - 90);
				float length = 1.5f*(float)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
				if(length > 32){
					if(length > 128){
						length = 128;
					}
					arrowBody.setHeight(length);
				}
				
				
			}
		}
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
			if(isActive()){
				arrowBody.setVisible(false);
				arrowHead.setVisible(false);
				blinkLength = arrowBody.getHeight();
				arrowHead.setHeight(32);
				arrowBody.setHeight(32);
				active = false;
				blink = true;
			}
		}
	}
	
	public void updatePosition(Player player){
		float dx = (float)(arrowBody.getHeight()/2 * Math.cos(Math.toRadians(arrowBody.getRotation())));
		float dy = (float)(arrowBody.getHeight()/2 * Math.sin(Math.toRadians(arrowBody.getRotation())));
		arrowBody.setPosition(player.getXInScreenSpace() + dy, player.getYInScreenSpace() + dx);
		
		dx = (float)((arrowBody.getHeight()+arrowHead.getWidth()/2) * Math.cos(Math.toRadians(arrowBody.getRotation())));
		dy = (float)((arrowBody.getHeight()+arrowHead.getWidth()/2) * Math.sin(Math.toRadians(arrowBody.getRotation())));
		arrowHead.setPosition(player.getXInScreenSpace() + dy, player.getYInScreenSpace() + dx);
		
		if(blink){
			float angle = (float)Math.atan2(dx, dy);
			blink = false;
			player.blink(angle, blinkLength);
		}
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void release(){
		active = false;
	}
	
	public float getAngle(){
		return angle;
	}
	
	public void setVisble(boolean visible){
		arrowBody.setVisible(visible);
		arrowHead.setVisible(visible);
	}
	
	

}
