package main;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.text.Text;

import janowiak.blinker.R;
import main.SceneManager.SceneType;
import menu.Button;

public class AreYouSureScene extends BaseScene {

	private Button yesButton;
	private Button noButton;
	
	@Override
	public void createScene() {
		final Text text = new Text(1024/2f, 552, ResourcesManager.getInstance().font,  resourcesManager.getResources().getString(R.string.are_you_sure),
				ResourcesManager.getInstance().vbom);
		this.attachChild(text);
		text.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(text.getY() > 552/2f + 64){
					text.setY(text.getY() - 8);
				}else{
					text.unregisterUpdateHandler(this);
				}
			}
		});
		
		yesButton = new Button(0, 552/2f,  resourcesManager.getResources().getString(R.string.yes_button), this){
			@Override
			public void buttonPressed() {
				UserData.getInstance().clearAllData();
				SceneManager.getInstance().loadOptionsScene();
			}
		};
		yesButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(yesButton.getEntity().getX() < 1024/2f){
					yesButton.getEntity().setX(yesButton.getEntity().getX() + 16);
				}else{
					yesButton.getEntity().unregisterUpdateHandler(this);
				}
			}
		});
		noButton = new Button(1024, 552/2f - 80,  resourcesManager.getResources().getString(R.string.no_button), this){
			@Override
			public void buttonPressed() {
				SceneManager.getInstance().loadOptionsScene();
			}
		};
		noButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(noButton.getEntity().getX() > 1024/2f){
					noButton.getEntity().setX(noButton.getEntity().getX() - 16);
				}else{
					noButton.getEntity().unregisterUpdateHandler(this);
				}
			}
		});
		setTouchAreaBindingOnActionDownEnabled(true);
		setTouchAreaBindingOnActionMoveEnabled(true);
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadOptionsScene();
		
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
