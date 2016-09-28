package scene;

import org.andengine.entity.text.Text;

import janowiak.blinker.R;
import scene.SceneManager.SceneType;
import ui.Button;

public class WhatsNewScene extends BaseScene {

	
	@Override
	public void createScene() {
		Text whatsNewText = new Text(1024/2, 500, resourcesManager.font, resourcesManager.getResources().getString(R.string.whats_new_head), vbom);
		Text text = new Text(1024/2, 552/2, resourcesManager.smallFont, resourcesManager.getResources().getString(R.string.whats_new), vbom);
		attachChildAtForeground2(whatsNewText);
		attachChild(text);
		
		Button next = new Button(1024/2, 100, resourcesManager.getResources().getString(R.string.next_button), this, resourcesManager.font) {
			
			@Override
			public void buttonPressed() {
				SceneManager.getInstance().loadStageSelectSceneFromScoreScene();
			}
		};
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_WHATS_NEW;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

}
