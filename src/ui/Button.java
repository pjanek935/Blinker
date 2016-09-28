package ui;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;

import main.ResourcesManager;
import scene.BaseScene;

public abstract class Button {

	private IEntity entity;
	private float scale = 1f;
	public String name = "";
	
	public Button(float x, float y, String string, BaseScene scene){
		Text text = new Text(x, y, ResourcesManager.getInstance().font, string, ResourcesManager.getInstance().vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					if(entity.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
						ResourcesManager.getInstance().vibrator.vibrate(80);
						buttonPressed();
					}
				}
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_OUTSIDE ||
						pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					setScale(1f);
				}else{
					setScale(1.2f);
				}
				return true;
			}
		};
		
		entity = text;
		scene.attachChildAtForeground(text);
		scene.registerTouchArea(text);
	}
	
	public Button(float x, float y, String string, BaseScene scene, Font font){
		Text text = new Text(x, y, font, string, ResourcesManager.getInstance().vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					if(entity.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
						buttonPressed();
					}
				}
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_OUTSIDE ||
						pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					setScale(1f);
				}else{
					setScale(1.2f);
				}
				return true;
			}
		};
		
		entity = text;
		scene.attachChildAtForeground(text);
		scene.registerTouchArea(text);
	}
	
	public Button(float x, float y, String string, Entity parentEntity, MyHUD hud){
		Text text = new Text(x, y, ResourcesManager.getInstance().font, string, ResourcesManager.getInstance().vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					if(entity.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
						buttonPressed();
					}
				}
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_OUTSIDE ||
						pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					setScale(1f);
				}else{
					setScale(1.2f);
				}
				return true;
			}
		};
		
		entity = text;
		parentEntity.attachChild(text);
		hud.registerTouchArea(text);
	}
	
	public Button(float x, float y, ITextureRegion textureRegion, BaseScene scene){
		Sprite sprite = new Sprite(x, y, textureRegion, ResourcesManager.getInstance().vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					buttonPressed();
				}
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_OUTSIDE ||
						pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					setScale(scale);
				}else{
					setScale(scale + scale/4f);
				}
				onButtonTouched(pSceneTouchEvent);
				return true;
			}
		};
		
		entity = sprite;
		scene.attachChildAtForeground(sprite);
		scene.registerTouchArea(sprite);
	}
	
	public void setVisible(boolean visible){
		entity.setVisible(visible);
	}
	
	public boolean isVisible(){
		return entity.isVisible();
	}
	
	
	public void setScale(float scale){
		this.scale = scale;
		entity.setScale(scale);
	}
	public float getScale(){
		return scale;
	}
	
	public IEntity getEntity(){ return entity; }
	
	public abstract void buttonPressed();
	public void onButtonTouched(TouchEvent pSceneTouchEvent){
		
	}
	
}
