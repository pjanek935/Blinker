package scene;

import org.andengine.engine.handler.IUpdateHandler;

import janowiak.blinker.R;
import main.ResourcesManager;
import main.UserData;
import particle.ParticleManager;
import scene.SceneManager.SceneType;
import ui.Button;

public class MainMenuScene extends BaseScene{

	private Button playButton;
	private Button optionsButton;
	
	@Override
	public void createScene() {
		if(!ParticleManager.getInstance().mistAtMenuEnabled()){
			ParticleManager.getInstance().setScene(this);
			ParticleManager.getInstance().startMistAtMenu();
		}
		
		/*this.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				ParticleManager.getInstance().updateMistAtMenu();
			}
		});*/
		playButton = new Button(0, 552/2f,  resourcesManager.getResources().getString(R.string.play_button), this){
			@Override
			public void buttonPressed() {
				if(UserData.getInstance().getUserName().equals("")){
					SceneManager.getInstance().loadSetNameForTheFirstTime(resourcesManager.engine);
				}else if(!UserData.getInstance().isWhatsNewRead(3)){
					UserData.getInstance().setWhatsNewRead(3, true);
					SceneManager.getInstance().loadWhatsNewScene(resourcesManager.engine);
				}else{
					SceneManager.getInstance().loadStageSelectSceneFromMainMenu(ResourcesManager.getInstance().engine);
				}
				
			}
		};
		playButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(playButton.getEntity().getX() < 1024/2f){
					playButton.getEntity().setX(playButton.getEntity().getX() + 16);
				}else{
					playButton.getEntity().unregisterUpdateHandler(this);
				}
			}
		});
		optionsButton = new Button(1024, 552/2f - 80,  resourcesManager.getResources().getString(R.string.options_button), this){
			@Override
			public void buttonPressed() {
				SceneManager.getInstance().loadOptionsScene();
			}
		};
		optionsButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(optionsButton.getEntity().getX() > 1024/2f){
					optionsButton.getEntity().setX(optionsButton.getEntity().getX() - 16);
				}else{
					optionsButton.getEntity().unregisterUpdateHandler(this);
				}
			}
		});
		setTouchAreaBindingOnActionDownEnabled(true);
		setTouchAreaBindingOnActionMoveEnabled(true);
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadExitScene();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MAIN_MENU;
	}

	@Override
	public void disposeScene() {
		playButton.getEntity().detachSelf();
		playButton.getEntity().dispose();
		optionsButton.getEntity().detachSelf();
		optionsButton.getEntity().dispose();
		this.detachSelf();
		this.dispose();
		
	}

}
