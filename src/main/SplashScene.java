package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import main.SceneManager.SceneType;

public class SplashScene extends BaseScene {

	private Sprite splash;
	private Sprite splash2;
	
	@Override
	public void createScene() {
		splash = new Sprite(1024/2, 552/2, resourcesManager.splash_region, vbom);
		splash.setScale(3f);
		
		splash2 = new Sprite(1024/2, 552/2, ResourcesManager.getInstance().splash2_region, vbom);
		splash2.setScale(3);
		splash2.registerUpdateHandler(new IUpdateHandler() {
			int counter = 0;
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				counter++;
				if(counter > 5){
					counter = 0;
					splash2.setPosition(1024/2 + ResourcesManager.getInstance().rand.nextFloat()*20 - 10,
							552/2 + ResourcesManager.getInstance().rand.nextFloat()*20 - 10);
				}
			}
		});
		attachChild(splash2);
		attachChild(splash);
		
		
		
		///
		
		
		
		
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		 return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene() {
		splash.detachSelf();
	    splash.dispose();
	    splash2.detachSelf();
	    splash2.dispose();
	    this.detachSelf();
	    this.dispose();
	}

}
