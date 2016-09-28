package scene;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import janowiak.blinker.R;
import main.ResourcesManager;
import scene.SceneManager.SceneType;

public class LoadingScene extends BaseScene {

	@Override
	public void createScene() {
		final Sprite loadingSprite = new Sprite(1024/2f, 552/2f, ResourcesManager.getInstance().loading,
				ResourcesManager.getInstance().vbom);
		attachChild(loadingSprite);
		loadingSprite.setAlpha(0.5f);
		loadingSprite.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				loadingSprite.setRotation(loadingSprite.getRotation() + 8f);
			}
		});
		Text loadingText = new Text(1024/2f, 552/2f - 128, ResourcesManager.getInstance().font,
				 resourcesManager.getResources().getString(R.string.loading), ResourcesManager.getInstance().vbom);
		attachChild(loadingText);
		
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
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
