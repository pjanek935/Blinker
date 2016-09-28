package main;

import org.andengine.engine.handler.IUpdateHandler;

import janowiak.blinker.R;
import main.SceneManager.SceneType;
import menu.Button;

public class OptionsScene extends BaseScene {

	private Button clearButton;
	private Button analogAdjustButton;
	
	@Override
	public void createScene() {
		clearButton = new Button(0, 552/2f,  resourcesManager.getResources().getString(R.string.delete_data_button), this){
			@Override
			public void buttonPressed() {
				SceneManager.getInstance().loadAreYouSureScene();
			}
		};
		clearButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(clearButton.getEntity().getX() < 1024/2f){
					clearButton.getEntity().setX(clearButton.getEntity().getX() + 16);
				}else{
					clearButton.getEntity().unregisterUpdateHandler(this);
				}
			}
		});
		analogAdjustButton = new Button(1024, 552/2f - 80,  resourcesManager.getResources().getString(R.string.adjust_analogs_button), this){
			@Override
			public void buttonPressed() {
				SceneManager.getInstance().loadAnalogAdjustScene();
			}
		};
		analogAdjustButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(analogAdjustButton.getEntity().getX() > 1024/2f){
					analogAdjustButton.getEntity().setX(analogAdjustButton.getEntity().getX() - 16);
				}else{
					analogAdjustButton.getEntity().unregisterUpdateHandler(this);
				}
			}
		});
		setTouchAreaBindingOnActionDownEnabled(true);
		setTouchAreaBindingOnActionMoveEnabled(true);
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMainMenuFromOptions();
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

}
