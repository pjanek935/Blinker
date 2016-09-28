package ui;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;

import main.ResourcesManager;

public class HpBar {

	private boolean visible = true;
	private Sprite hpBarBg;
	private Sprite hpBar;
	private float maxWidth;
	private float ratio = 1;
	private boolean refilled = true;
	
	public HpBar(float x, float y, IEntity hud){
		hpBarBg = new Sprite(x, y, ResourcesManager.getInstance().hp_bar_bg, ResourcesManager.getInstance().vbom);
		hpBar = new Sprite(0, 0, ResourcesManager.getInstance().hp_bar, ResourcesManager.getInstance().vbom);
		hpBar.setPosition(hpBarBg.getWidth()/2, hpBarBg.getHeight()/2);
		hpBar.setAlpha(0.6f);
		hpBarBg.attachChild(hpBar);
		hud.attachChild(hpBarBg);
		hpBar.setWidth(hpBarBg.getWidth()-2);
		maxWidth = (int)hpBar.getWidth();
		
		IUpdateHandler refillUpdateHandler = new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(!refilled){
					setHp(ratio + 0.02f);
					if(ratio >= 1){
						refilled = true;
					}
				}
			}
			@Override
			public void reset() {
				
			}
		};
		hpBar.registerUpdateHandler(refillUpdateHandler);
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
		hpBar.setVisible(visible);
		hpBarBg.setVisible(visible);
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setHp(float ratio){
		if(ratio <= 0){
			hpBar.setWidth(0);
			return;
		}
		this.ratio = ratio;
		hpBar.setWidth(ratio * maxWidth);
		//hpBar.setX(hpBarBg.getWidth()/2 - ((maxWidth) - hpBar.getWidth())/2);
		hpBar.setX(hpBar.getWidth()/2 + 1);
	}
	
	public void refill(){
		refilled = false;
	}
	
	public Sprite getBackground(){
		return hpBarBg;
	}
}
