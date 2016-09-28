package ui;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;

import main.ResourcesManager;

public class BlinkBar {
	
	private boolean visible = true;
	private Sprite blinkBarBg;
	private Sprite blinkBar;
	private float maxWidth;
	private float ratio = 1;
	private float maxBlink = 90;
	private float currentBlink = 90;
	
	public BlinkBar(float x, float y, Entity hud){
		blinkBarBg = new Sprite(x, y, ResourcesManager.getInstance().blink_bar_bg, ResourcesManager.getInstance().vbom);
		blinkBar = new Sprite(0, 0, ResourcesManager.getInstance().hp_bar, ResourcesManager.getInstance().vbom);
		blinkBar.setPosition(blinkBarBg.getWidth()/2, blinkBarBg.getHeight()/2);
		blinkBar.setAlpha(0.6f);
		blinkBarBg.attachChild(blinkBar);
		hud.attachChild(blinkBarBg);
		blinkBar.setWidth(blinkBarBg.getWidth()-2);
		maxWidth = (int)blinkBar.getWidth();
		
		IUpdateHandler refillUpdateHandler = new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(currentBlink < maxBlink){
					currentBlink += 0.5f;
					setBlink();
				}
			}
			@Override
			public void reset() {
				
			}
		};
		blinkBar.registerUpdateHandler(refillUpdateHandler);
	}
	
	public void setBlink(){
		blinkBar.setWidth(currentBlink/maxBlink * maxWidth);
		blinkBar.setX(blinkBar.getWidth()/2 + 1);
	}
	
	public boolean blink(){
		if(currentBlink >= 30){
			currentBlink -= 30;
			setBlink();
			return true;
		}else{
			return false;
		}
	}
	
	public Sprite getBackground(){
		return blinkBarBg;
	}
	
	public void setVisible(boolean visible){
		blinkBarBg.setVisible(visible);
		blinkBar.setVisible(visible);
	}

}
