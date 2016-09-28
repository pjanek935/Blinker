package scene;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import scene.SceneManager.SceneType;
import world.World;

public class GameScene extends BaseScene implements IOnSceneTouchListener{
	
	World world;
	
	@Override
	public void createScene() {
		this.setBackground(new Background(Color.BLACK));
		world = new World(this);
		this.registerTouchArea(this);
		this.setOnSceneTouchListener(this);
		
		/*DebugRenderer debug = new DebugRenderer(world.physicsWorld, ResourcesManager.getInstance().vbom);
	    attachChild(debug);*/
	}

	@Override
	public void onBackKeyPressed() {
		//SceneManager.getInstance().loadStageSelectSceneFromGameScene(ResourcesManager.getInstance().engine);
		setIgnoreUpdate(true);
		world.onBackKeyPressed();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		world.dispose();
	}
	
	

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		//Log.d("mine", "world input");
		world.input(pScene, pSceneTouchEvent);
		
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
          
           
            
        } 
        if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE){
            
    
           
     
        }
        if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
        
        }
        return true;
	}
	
	
	
	

}
