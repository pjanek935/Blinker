package main;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import com.badlogic.gdx.math.Vector2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserData {
	
	private static UserData instance;
	private Context context;
	
	private String USER_DATA = "USER_DATA";
	private String LEFT_ANALOG_POSITION_X = "LEFT_ANALOG_POSITION_X";
	private String LEFT_ANALOG_POSITION_Y = "LEFT_ANALOG_POSITION_Y";
	private String ANALOG_SIZE = "ANALOG_SIZE";
	private String STAGE_AVAILABLE = "STAGE_AVAILABLE_";
	private String BEST_STAGE_SCORE = "BEST_STAGE_SCORE_";
	private String USER_NAME = "USER_NAME";
	private String WHATS_NEW = "WHATS_NEW_VER_";
	
	private IUpdateHandler scoreTimeHandler;
	private int currentScore = 1000;
	private boolean pauseTimer = false;
	
	private UserData(){
		
	}
	
	public void setContext(Context context){
		this.context = context;
	}
	
	public static UserData getInstance(){
		if(instance == null){
			instance = new UserData();
			return instance;
		}else{
			return instance;
		}
	}
	
	public Vector2 getLeftAnaogPosition(){
		Vector2 pos = new Vector2(0, 0);
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		pos.x = sharedPref.getFloat(LEFT_ANALOG_POSITION_X, 150);
		pos.y = sharedPref.getFloat(LEFT_ANALOG_POSITION_Y, 150);
		return pos;
	}
	
	public void saveLeftAnalogPostion(float x, float y){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putFloat(LEFT_ANALOG_POSITION_X, x);
		editor.putFloat(LEFT_ANALOG_POSITION_Y, y);
		editor.commit();
	}
	
	public boolean isStageAvailable(int stage){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		boolean stageAvailable = sharedPref.getBoolean(STAGE_AVAILABLE + stage, false);
		return stageAvailable;
	}
	
	public void setStageAvailable(int stage, boolean available){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(STAGE_AVAILABLE + stage, available);
		editor.commit();
	}
	
	public void clearAllData(){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.clear();
		editor.commit();
	}
	
	public int getBestStageScore(int stage){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		int score = sharedPref.getInt(BEST_STAGE_SCORE + stage, 0);
		return score;
	}
	
	public String getUserName(){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		String name = sharedPref.getString(USER_NAME, "");
		return name;
	}
	
	public void setUSerName(String name){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(USER_NAME, name);
		editor.commit();
	}
	
	public void setNewBestScore(int newBestScore, int stage){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(BEST_STAGE_SCORE + stage, newBestScore);
		editor.commit();
	}
	
	public void startScoreTimer(){
		Log.d("mine", "starting score timer");
		ResourcesManager.getInstance().engine.unregisterUpdateHandler(scoreTimeHandler);
		currentScore = 1000;
		pauseTimer = false;
		scoreTimeHandler = new IUpdateHandler() {
			int counter = 0;
			@Override
			public void reset() {
				
			}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(!pauseTimer){
					counter++;
					if(counter >= 60){
						counter = 0;
						currentScore = currentScore - 1;
						if(currentScore < 0){
							currentScore = 0;
						}
						Log.d("mine", "score: " + currentScore);
					}
				}
			}
		};
		ResourcesManager.getInstance().engine.registerUpdateHandler(scoreTimeHandler);
	}
	
	public void pauseScoreTimer(){
		pauseTimer = true;
	}
	
	public void resumeScoreTimer(){
		pauseTimer = false;
	}
	
	public int stopTimer(){
		pauseTimer = true;
		ResourcesManager.getInstance().engine.unregisterUpdateHandler(scoreTimeHandler);
		int scoreToReturn = currentScore;
		currentScore = 1000;
		return scoreToReturn;
	}
	
	public int getCurrentScore(){
		return currentScore;
	}
	
	public void dealDamage(int damage){
		currentScore -= damage * 2;
		if(currentScore < 0){
			currentScore = 0;
		}
	}
	
	public boolean isWhatsNewRead(int ver){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		boolean read = sharedPref.getBoolean(WHATS_NEW + ver, false);
		return read;
	}
	
	public void setWhatsNewRead(int ver, boolean read){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(WHATS_NEW + ver, read);
		editor.commit();
	}
	
	public float getAnalogSize(){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		float size = sharedPref.getFloat(ANALOG_SIZE, 3);
		return size;
	}
	
	public void setAnalogSize(float size){
		SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putFloat(ANALOG_SIZE, size);
		editor.commit();
	}

}
