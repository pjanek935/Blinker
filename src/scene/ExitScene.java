package scene;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.text.Text;

import janowiak.blinker.R;
import main.ResourcesManager;
import scene.SceneManager.SceneType;
import ui.Button;

public class ExitScene extends BaseScene{

	private Button yesButton;
	private Button noButton;
	
	@Override
	public void createScene() {
		final Text text = new Text(1024/2f, 552, ResourcesManager.getInstance().font, resourcesManager.getResources().getString(R.string.exit_statement),
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
		
		yesButton = new Button(0, 552/2f, resourcesManager.getResources().getString(R.string.yes_button), this){
			@Override
			public void buttonPressed() {
				System.exit(TAG_DEFAULT);
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
		noButton = new Button(1024, 552/2f - 80, resourcesManager.getResources().getString(R.string.no_button), this){
			@Override
			public void buttonPressed() {
				SceneManager.getInstance().loadMainMenuFromOptions();
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
		SceneManager.getInstance().loadMainMenuFromOptions();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_EXIT;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

}
