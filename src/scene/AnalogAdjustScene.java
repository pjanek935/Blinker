package scene;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.math.Vector2;

import android.util.Log;
import janowiak.blinker.R;
import main.ResourcesManager;
import main.UserData;
import scene.SceneManager.SceneType;
import ui.Button;

public class AnalogAdjustScene extends BaseScene {

	private Button resetButton, leftButton, rightButton;
	private Sprite leftAnalog;
	private Sprite rightAnalog;
	
	@Override
	public void createScene() {
		resetButton = new Button(1024/2, 552 + 10, resourcesManager.getResources().getString(R.string.reset_button), this){
			@Override
			public void buttonPressed() {
				leftAnalog.setPosition(150, 150);
				rightAnalog.setPosition(1024 - 150, 150);
				leftAnalog.setScale(3);
				rightAnalog.setScale(3);
			}
		};
		resetButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(resetButton.getEntity().getY() > 500){
					resetButton.getEntity().setY(resetButton.getEntity().getY() - 4);
				}else{
					resetButton.getEntity().unregisterUpdateHandler(this);
				}
			}
		});
		
		leftButton = new Button(0, 552/2, resourcesManager.getResources().getString(R.string.shrink_button), this) {
			
			@Override
			public void buttonPressed() {
				if(leftAnalog.getScaleX() > 1 && leftAnalog.getScaleX() < 4.6f){
					leftAnalog.setScale(leftAnalog.getScaleX() - 0.2f);
					rightAnalog.setScale(rightAnalog.getScaleX() - 0.2f);
					Log.d("mine", "scale:" + leftAnalog.getScaleX());
				}
				if(leftAnalog.getScaleX() <= 1){
					leftAnalog.setScale(1.01f);
					rightAnalog.setScale(1.01f);
				}
				if(leftAnalog.getScaleX() >= 4.6){
					leftAnalog.setScale(4.55f);
					rightAnalog.setScale(4.55f);
				}
			}
		};
		
		leftButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {
			}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(leftButton.getEntity().getX() < 1024/2 - 150){
					leftButton.getEntity().setX(leftButton.getEntity().getX() + 16);
				}else{
					leftButton.getEntity().unregisterUpdateHandler(this);
				}
			}
		});
		
		rightButton = new Button(1024, 552/2, resourcesManager.getResources().getString(R.string.enlarge_button), this) {
			
			@Override
			public void buttonPressed() {
				if(leftAnalog.getScaleX() > 1 && leftAnalog.getScaleX() < 4.6f){
					leftAnalog.setScale(leftAnalog.getScaleX() + 0.2f);
					rightAnalog.setScale(rightAnalog.getScaleX() + 0.2f);
					Log.d("mine", "scale:" + leftAnalog.getScaleX());
				}
				if(leftAnalog.getScaleX() <= 1){
					leftAnalog.setScale(1.01f);
					rightAnalog.setScale(1.01f);
				}
				if(leftAnalog.getScaleX() >= 4.6){
					leftAnalog.setScale(4.55f);
					rightAnalog.setScale(4.55f);
				}
			}
		};
		
		rightButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {
			}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(rightButton.getEntity().getX() > 1024/2 + 150){
					rightButton.getEntity().setX(rightButton.getEntity().getX() - 16);
				}else{
					rightButton.getEntity().unregisterUpdateHandler(this);
				}
			}
		});
		
		Vector2 leftPos = UserData.getInstance().getLeftAnaogPosition();
		float scale = UserData.getInstance().getAnalogSize();
		
		leftAnalog = new Sprite(leftPos.x, leftPos.y, ResourcesManager.getInstance().analog, ResourcesManager.getInstance().vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionMove()){
					leftAnalog.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					if(leftAnalog.getX() > 350){
						leftAnalog.setX(350);
					}
					if(leftAnalog.getX() < leftAnalog.getWidthScaled()/2){
						leftAnalog.setX(leftAnalog.getWidthScaled()/2);
					}
					if(leftAnalog.getY() > 300){
						leftAnalog.setY(300);
					}
					if(leftAnalog.getY() < leftAnalog.getHeightScaled()/2){
						leftAnalog.setY(leftAnalog.getHeightScaled()/2);
					}
					rightAnalog.setPosition(1024 - leftAnalog.getX(), leftAnalog.getY());
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		rightAnalog = new Sprite(1024 - leftPos.x, leftPos.y, ResourcesManager.getInstance().analog, ResourcesManager.getInstance().vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionMove()){
					rightAnalog.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					if(rightAnalog.getX() < 1024 - 350){
						rightAnalog.setX(1024 - 350);
					}
					if(rightAnalog.getX() > 1024 - rightAnalog.getWidthScaled()/2){
						rightAnalog.setX(1024 - rightAnalog.getWidthScaled()/2);
					}
					if(rightAnalog.getY() > 300){
						rightAnalog.setY(300);
					}
					if(rightAnalog.getY() < rightAnalog.getHeightScaled()/2){
						rightAnalog.setY(rightAnalog.getHeightScaled()/2);
					}
					leftAnalog.setPosition(1024 - rightAnalog.getX(), rightAnalog.getY());
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		leftAnalog.setScale(scale);
		rightAnalog.setScale(scale);
		this.registerTouchArea(leftAnalog);
		this.registerTouchArea(rightAnalog);
		this.attachChild(leftAnalog);
		this.attachChild(rightAnalog);
	}

	@Override
	public void onBackKeyPressed() {
		UserData.getInstance().saveLeftAnalogPostion(leftAnalog.getX(), leftAnalog.getY());
		UserData.getInstance().setAnalogSize(leftAnalog.getScaleX());
		SceneManager.getInstance().loadOptionsScene();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_ANALOG_ADJUST;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

}
