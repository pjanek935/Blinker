package main;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import android.annotation.SuppressLint;
import android.view.KeyEvent;

@SuppressLint("NewApi")
public class GameActivity extends BaseGameActivity implements IOnSceneTouchListener {

	public static MyCamera camera;	
	public static int WINDOW_WIDTH = 1024;
	public static int WINDOW_HEIGHT = 552;
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		UserData.getInstance().setContext(this);
		ResourcesManager.getInstance().setContext(this);
		camera = new MyCamera(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
	    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
	    		new RatioResolutionPolicy(WINDOW_WIDTH, WINDOW_HEIGHT), this.camera);
	    engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
	    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	    engineOptions.getTouchOptions().setNeedsMultiTouch(true);
	    return engineOptions;
	}
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
	    return new LimitedFPSEngine(pEngineOptions, 60);
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
		ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
	    ResourcesManager.getInstance();
	    pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
		 mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
		            public void onTimePassed(final TimerHandler pTimerHandler) {
		            	 mEngine.unregisterUpdateHandler(pTimerHandler);
		                 SceneManager.getInstance().createMainMenuScene();
		            }
		    }));
		    pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if (keyCode == KeyEvent.KEYCODE_BACK){
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}

}
