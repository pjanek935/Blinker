package ui;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;

import android.util.Log;
import janowiak.blinker.R;
import main.GameActivity;
import main.ResourcesManager;
import main.UserData;
import player.Enemy;
import player.Player;
import scene.BaseScene;
import scene.SceneManager;
import world.World;

public class MyHUD extends HUD {
	
	private Entity controlsEntity;
	private AnalogStick leftAnalog;
	private AnalogStick rightAnalog;
	private Rectangle leftTouchArea;
	private Rectangle rightTouchArea;
	private DashInput dashInput;
	private Sprite enemyArrow;
	public Rectangle blackScreen;
	private BlinkBar blinkBar;
	private HpBar playerHpBar;
	private HpBar enemyHpBar;
	public Button skipButton;
	public boolean skip = false;
	public boolean isSkipButtonVisible = false;
	
	//Menu
	private Entity menuEntity;
	private Button resumeButton;
	private Button exitButton;
	private Text score;
	public boolean holdBlackScreen = false;
	
	//Text
	private Entity textEntity;
	private Text theText;
	private Text enemyText;
	private Text count1;
	private Text count2;
	private Text count3;
	private boolean counting = true;
	
	public MyHUD(final BaseScene parentScene, final Camera camera){
		
		blackScreen = new Rectangle(1024/2, 552/2, 1024, 552, ResourcesManager.getInstance().vbom);
		blackScreen.setColor(Color.BLACK);
		blackScreen.registerUpdateHandler(new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(!holdBlackScreen){
					if(blackScreen.getAlpha() > 0){
						blackScreen.setAlpha(blackScreen.getAlpha() - 0.009f);
					}else{
						blackScreen.setVisible(false);
					}
				}
				
			}
			@Override
			public void reset() {}
		});
		this.attachChild(blackScreen);
		
		controlsEntity = new Entity();
		this.attachChild(controlsEntity);
		
		textEntity = new Entity();
		this.attachChild(textEntity);
		
		menuEntity = new Entity();
		this.attachChild(menuEntity);
		
		playerHpBar = new HpBar(100, 500, controlsEntity);
		enemyHpBar = new HpBar(924, 500, controlsEntity);
		blinkBar = new BlinkBar(playerHpBar.getBackground().getX() - playerHpBar.getBackground().getWidth()/2 + 28.5f , 480, controlsEntity);
		
		Vector2 leftPos = UserData.getInstance().getLeftAnaogPosition();
		
		leftAnalog = new AnalogStick(leftPos.x, leftPos.y, controlsEntity);
		rightAnalog = new AnalogStick(GameActivity.WINDOW_WIDTH - leftPos.x, leftPos.y, controlsEntity);
		dashInput = new DashInput(parentScene, this);
		
		enemyArrow = new Sprite(0, 0, ResourcesManager.getInstance().enemy_arrow, ResourcesManager.getInstance().vbom);
		enemyArrow.setVisible(false);
		enemyArrow.setAlpha(0.5f);
		enemyArrow.setScale(2);
		
		leftTouchArea = new Rectangle(0, 0, GameActivity.WINDOW_WIDTH/2, GameActivity.WINDOW_HEIGHT, ResourcesManager.getInstance().vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if(leftAnalog.isVisible() && World.readyToStart){
					leftAnalog.input(pSceneTouchEvent);
				}else{
					leftAnalog.reset();
				}
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
					World.screenPressed = true;
				}
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					World.screenPressed = false;
				}
				if(skipButton.isVisible() && skipButton.getEntity().contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
					skipButton.getEntity().onAreaTouched(pSceneTouchEvent, X, Y);
				}
				return false;
		    };
		};
		leftTouchArea.setVisible(false);
		leftTouchArea.setPosition(GameActivity.WINDOW_WIDTH/4, GameActivity.WINDOW_HEIGHT/2);
		
		rightTouchArea = new Rectangle(0, 0, GameActivity.WINDOW_WIDTH/2, GameActivity.WINDOW_HEIGHT , ResourcesManager.getInstance().vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if(rightAnalog.isVisible() && World.readyToStart){
					rightAnalog.input(pSceneTouchEvent);
					if(!rightAnalog.isActive()){
						dashInput.input(pSceneTouchEvent, camera);
					}
				}else{
					rightAnalog.reset();
				}
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
					World.screenPressed = true;
				}
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					World.screenPressed = false;
				}
				if(skipButton.isVisible() && skipButton.getEntity().contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
					skipButton.getEntity().onAreaTouched(pSceneTouchEvent, X, Y);
				}
				return false;
				
		    };
		};
		rightTouchArea.setColor(Color.GREEN);
		rightTouchArea.setAlpha(0.3f);
		rightTouchArea.setVisible(false);
		rightTouchArea.setPosition(GameActivity.WINDOW_WIDTH - GameActivity.WINDOW_WIDTH/4, GameActivity.WINDOW_HEIGHT/2);
		
		
		
		this.attachChild(new Sprite(GameActivity.WINDOW_WIDTH/2, GameActivity.WINDOW_HEIGHT/2, ResourcesManager.getInstance().border, ResourcesManager.getInstance().vbom));
		controlsEntity.attachChild(leftTouchArea);
		this.registerTouchArea(leftTouchArea);
		controlsEntity.attachChild(rightTouchArea);
		this.registerTouchArea(rightTouchArea);
		controlsEntity.attachChild(enemyArrow);
		
		resumeButton = new Button(1024/2f, 552/2f, ResourcesManager.getInstance().getResources().getString(R.string.return_button), menuEntity, this) {
			@Override
			public void buttonPressed() {
				if(resumeButton.isVisible()){
					parentScene.setIgnoreUpdate(false);
					setControlsVisible(true);
					setMenuVisible(false);
					setSkipButtonVisible(true);
				}
			}
		};
		
		exitButton = new Button(1024/2, 552/2f -80, ResourcesManager.getInstance().getResources().getString(R.string.exit_button), menuEntity, this) {
			@Override
			public void buttonPressed() {
				if(exitButton.isVisible()){
					exit();
				}
			}
		};
		
		score = new Text(1024/2 - 300, 500, ResourcesManager.getInstance().font, "WYNIK: 012456789", ResourcesManager.getInstance().vbom);
		menuEntity.attachChild(score);
		
		resumeButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(resumeButton.getEntity().getX() < 1024/2f){
					resumeButton.getEntity().setX(resumeButton.getEntity().getX() + 16);
				}
			}
		});
		
		exitButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(exitButton.getEntity().getX() > 1024/2f){
					exitButton.getEntity().setX(exitButton.getEntity().getX() - 16);
				}
			}
		});
		
		resumeButton.setVisible(false);
		exitButton.setVisible(false);
		score.setVisible(false);
		
		
		theText = new Text(0, 552/2 + 64, ResourcesManager.getInstance().font, "THE", ResourcesManager.getInstance().vbom);
		theText.setVisible(false);
		textEntity.attachChild(theText);
		theText.registerUpdateHandler(new IUpdateHandler() {
			int timer = 0;
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(theText.isVisible()){
					if(theText.getX() < 1024/2){
						theText.setX(theText.getX() + 16);
					}else{
						timer++;
						if(timer > 60){
							theText.setX(theText.getX() + 16);
						}
						if(theText.getX() > 1024){
							theText.setVisible(false);
							timer = 0;
						}
					}
				}
			}
		});
		
		enemyText = new Text(1024, 552/2, ResourcesManager.getInstance().font, "QWERTYUIOPASDFGHJKLZXCVBNM", ResourcesManager.getInstance().vbom);
		enemyText.setVisible(false);
		textEntity.attachChild(enemyText);
		enemyText.registerUpdateHandler(new IUpdateHandler() {
			int timer = 0;
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(enemyText.isVisible()){
					if(enemyText.getX() > 1024/2){
						enemyText.setX(enemyText.getX() - 16);
					}else{
						timer++;
						if(timer > 60){
							enemyText.setX(enemyText.getX() - 16);
						}
						if(enemyText.getX() < 0){
							enemyText.setVisible(false);
							timer = 0;
						}
					}
				}
				
			}
		});
		
		count3 = new Text(0, 552/2, ResourcesManager.getInstance().font,  "3", ResourcesManager.getInstance().vbom);
		count2 = new Text(0, 552/2, ResourcesManager.getInstance().font,  "2", ResourcesManager.getInstance().vbom);
		count1 = new Text(0, 552/2, ResourcesManager.getInstance().font,  "1", ResourcesManager.getInstance().vbom);
		count3.registerUpdateHandler(new IUpdateHandler() {
			int timer = 0;
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(count3.isVisible()){
					if(count3.getX() < 1024/2){
						count3.setX(count3.getX() + 16);
					}else{
						timer++;
						if(timer > 10){
							count3.setX(count3.getX() + 16);
						}
						if(count3.getX() > 1024){
							count3.setVisible(false);
							timer = 0;
						}
					}
				}
			}
		});
		count2.registerUpdateHandler(new IUpdateHandler() {
			int timer = 0;
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(count3.getX() > 1024/2){
					count2.setVisible(true);
					if(count2.getX() < 1024/2){
						count2.setX(count2.getX() + 16);
					}else{
						timer++;
						if(timer > 10){
							count2.setX(count2.getX() + 16);
						}
						if(count2.getX() > 1024){
							count2.setVisible(false);
							timer = 0;
						}
					}
				}
			}
		});
		count1.registerUpdateHandler(new IUpdateHandler() {
			int timer = 0;
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(count2.getX() > 1024/2){
					count1.setVisible(true);
					if(count1.getX() < 1024/2){
						count1.setX(count1.getX() + 16);
					}else{
						timer++;
						if(timer > 10){
							count1.setX(count1.getX() + 16);
						}
						if(count1.getX() > 1024 && counting){
							count1.setVisible(false);
							count2.setVisible(false);
							count3.setVisible(false);
							counting = false;
							timer = 0;
							World.readyToStart = true;
							Log.d("mine", "!!!!!");
							UserData.getInstance().startScoreTimer();
							isSkipButtonVisible = false;
						}
					}
				}
			}
		});
		textEntity.attachChild(count1);
		textEntity.attachChild(count2);
		textEntity.attachChild(count3);
		count1.setVisible(false);
		count2.setVisible(false);
		count3.setVisible(false);
		
		skipButton = new Button(1024/2f + 300, 500, ResourcesManager.getInstance().getResources().getString(R.string.skip_button), controlsEntity, this) {
			@Override
			public void buttonPressed() {
				skip = true;
			}
		};
		skipButton.setVisible(false);
	}
	
	public void setPlayer(final Player player){
		player.getBodySprite().registerUpdateHandler(new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) {
				dashInput.updatePosition(player);
			}
			@Override
			public void reset() {
				
			}
		});
	}
	
	public void exit(){
		World.readyToStart = false;
		SceneManager.getInstance().loadStageSelectSceneFromGameScene(ResourcesManager.getInstance().engine);
	}
	
	public void setEnemy(final Enemy enemy){
		enemyArrow.registerUpdateHandler(new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) {
				float[] cameraSceneCoordinates = 
						ResourcesManager.getInstance().camera.getCameraSceneCoordinatesFromSceneCoordinates(
								enemy.getBodySprite().getX(), enemy.getBodySprite().getY());
				
				if(cameraSceneCoordinates[0] > 1024 || cameraSceneCoordinates[0] < 0 || cameraSceneCoordinates[1] > 552
						|| cameraSceneCoordinates[1] < 0){
					enemyArrow.setVisible(true);
					float x = cameraSceneCoordinates[0];
					float y = cameraSceneCoordinates[1];
					if(cameraSceneCoordinates[0] > 1024) x = 1024;
					if(cameraSceneCoordinates[0] < 0) x = 0;
					if(cameraSceneCoordinates[1] > 552) y = 552;
					if(cameraSceneCoordinates[1] < 0) y = 0;
					float angle = (float)Math.atan2(552/2f - y, 1024/2f - x);
					angle = (float)Math.toDegrees(angle);
					enemyArrow.setRotation(angle);
					enemyArrow.setPosition(x, y);
				}else{
					enemyArrow.setVisible(false);
				}
			}
			@Override
			public void reset() {
				
			}
		});
	}
	
	public AnalogStick getLeftAnalog(){
		return leftAnalog;
	}
	
	public AnalogStick getRightAnalog(){
		return rightAnalog;
	}
	
	public DashInput getDashInput(){
		return dashInput;
	}
	
	public BlinkBar getBlinkBar(){
		return blinkBar;
	}
	
	public HpBar getPlayerHpBar(){
		return playerHpBar;
	}
	
	public HpBar getEnemyHpBar(){
		return enemyHpBar;
	}
	
	public Rectangle getRightTouchArea(){
		return rightTouchArea;
	}
	
	public Rectangle getLeftTouchArea(){
		return leftTouchArea;
	}
	
	
	public void setControlsVisible(boolean visible){
		getEnemyHpBar().setVisible(visible);
		getPlayerHpBar().setVisible(visible);
		getLeftAnalog().setVisible(visible);
		getRightAnalog().setVisible(visible);
		getBlinkBar().setVisible(visible);
		//getDashInput().setVisble(visible);
	}
	
	public void setSkipButtonVisible(boolean visible){
		if(isSkipButtonVisible){
			skipButton.setVisible(visible);
		}
	}
	
	public void setMenuVisible(boolean visible){
		if(visible){
			resumeButton.setVisible(visible);
			exitButton.setVisible(visible);
			resumeButton.getEntity().setX(0);
			exitButton.getEntity().setX(1024);
			unregisterTouchArea(leftTouchArea);
			unregisterTouchArea(rightTouchArea);
			this.registerTouchArea(resumeButton.getEntity());
			this.registerTouchArea(exitButton.getEntity());
			blackScreen.setAlpha(0.6f);
			blackScreen.setVisible(true);
			holdBlackScreen = true;
			UserData.getInstance().pauseScoreTimer();
			score.setText("WYNIK: " + UserData.getInstance().getCurrentScore());
			score.setVisible(true);
		}else{
			resumeButton.setVisible(visible);
			exitButton.setVisible(visible);
			this.unregisterTouchArea(resumeButton.getEntity());
			this.unregisterTouchArea(exitButton.getEntity());
			registerTouchArea(leftTouchArea);
			registerTouchArea(rightTouchArea);
			blackScreen.setAlpha(0);
			blackScreen.setVisible(false);
			holdBlackScreen = false;
			score.setVisible(false);
			UserData.getInstance().resumeScoreTimer();
		}
		
	}
	
	public Sprite getEnemArrow(){
		return enemyArrow;
	}
	
	public void showEnemyName(String enemyName){
		enemyText.setText(enemyName);
		enemyText.setX(1024);
		theText.setX(0);
		enemyText.setVisible(true);
		theText.setVisible(true);
	}
	
	public void startCounting(){
		count3.setX(0);
		count2.setX(0);
		count1.setX(0);
		count3.setVisible(true);
		counting = true;
	}

}
