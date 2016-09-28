package main;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import android.graphics.Rect;
import android.util.Log;
import janowiak.blinker.R;
import main.SceneManager.SceneType;
import menu.Button;
import particle.ParticleManager;
import world.StageType;

public class StageSelectScene extends BaseScene {
	
	private ArrayList<Button> stageTiles;
	private Rectangle rect;
	private Text stageName;
	private Text scoreText;
	private Text totalScoreText;
	private Button scoreSceneButton;
	private float moveX = 0;
	private boolean moved = false;
	private Boolean[] stageAvailable;

	@Override
	public void createScene() {
		if(!ParticleManager.getInstance().mistAtMenuEnabled()){
			ParticleManager.getInstance().setScene(this);
			ParticleManager.getInstance().startMistAtMenu();
		}
		
		stageAvailable = new Boolean[5];
		for(int i=0; i<5; i++){
			stageAvailable[i] = UserData.getInstance().isStageAvailable(i);
		}
		
		scoreText = new Text(0, 0, ResourcesManager.getInstance().smallFont, "(WYNIK: 123456789)", vbom);
		attachChild(scoreText);
		scoreText.setVisible(false);
		
		scoreSceneButton = new Button(1024 - 150, 552 + 20,  resourcesManager.getResources().getString(R.string.high_scores_table), this, resourcesManager.smallFont) {
			@Override
			public void buttonPressed() {
				SceneManager.getInstance().loadScoreScene();
				//SceneManager.getInstance().loadSetNameScene();
			}
		};
		scoreSceneButton.getEntity().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(scoreSceneButton.getEntity().getY() > 500){
					scoreSceneButton.getEntity().setY(scoreSceneButton.getEntity().getY() - 4);
				}else{
					scoreSceneButton.getEntity().unregisterUpdateHandler(this);
				}
			}
		});
		
		int totalScore = 0;
		for(int i=0; i<5; i++){
			totalScore += UserData.getInstance().getBestStageScore(i);
		}
		if(totalScore > 0){
			totalScoreText = new Text(150, 552  + 20, resourcesManager.smallFont,  resourcesManager.getResources().getString(R.string.total_score) + totalScore, vbom);
			attachChild(totalScoreText);
			totalScoreText.registerUpdateHandler(new IUpdateHandler() {
				@Override
				public void reset() {}
				@Override
				public void onUpdate(float pSecondsElapsed) {
					if(totalScoreText.getY() > 500){
						totalScoreText.setY(totalScoreText.getY() - 4);
					}else{
						totalScoreText.unregisterUpdateHandler(this);
					}
				}
			});
		}
		
		stageName = new Text(0, 0, ResourcesManager.getInstance().font, "QWERTYUIOPASDFGHJKLZXCVBNM", ResourcesManager.getInstance().vbom);
		attachChild(stageName);
		stageName.setVisible(false);
		
		rect = new Rectangle(1024/2f, 552/2f, 1024, 552, ResourcesManager.getInstance().vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
					moveX = pSceneTouchEvent.getX();
				}
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE){
					float dx = pSceneTouchEvent.getX() - moveX;
					if(Math.abs(dx) > 4){
						moved = true;
					}
					if(!(stageTiles.get(stageTiles.size()-1).getEntity().getX() < 1024/2f) &&
							!(stageTiles.get(0).getEntity().getX() > 1024/2f)){
						for(Button b : stageTiles){
							b.getEntity().setX(b.getEntity().getX() + dx);
						}
						moveX = pSceneTouchEvent.getX();
						stageName.setPosition(stageName.getX() + dx, stageName.getY());
						scoreText.setPosition(scoreText.getX() + dx, scoreText.getY());
					}
					if(stageTiles.get(0).getEntity().getX() > 1024/2f){
						for(int i=0; i<6; i++){
							stageTiles.get(i).setScale(4);
							stageTiles.get(i).getEntity().setPosition(1024/2f + (stageTiles.get(i).getEntity().getWidthScaled()*2)*i, 552/2f);
						}
					}
					if(stageTiles.get(stageTiles.size()-1).getEntity().getX() < 1024/2f){
						for(int i=0; i<6; i++){
							stageTiles.get(i).setScale(4);
							stageTiles.get(i).getEntity().setPosition(-768 + (stageTiles.get(i).getEntity().getWidthScaled()*2)*i, 552/2f);
						}
					}
					//Log.d("mine", "FT: " + stageTiles.get(0).getEntity().getX() + ", LT: " + stageTiles.get(stageTiles.size()-1).getEntity().getX());
				}
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					moved = false;
				}
				return true;
			}
		};
		
		
		
		final Text stageText = new Text(0, 552/2f + 128, ResourcesManager.getInstance().font,  resourcesManager.getResources().getString(R.string.stage_select_1),
				ResourcesManager.getInstance().vbom);
		IUpdateHandler stageTextUpdater = new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(stageText.getX() < 1024/2f){
					stageText.setX(stageText.getX() + 16);
				}else{
					stageText.unregisterUpdateHandler(this);
				}
				
			}
			@Override
			public void reset() {}
		};
		stageText.registerUpdateHandler(stageTextUpdater);
		attachChild(stageText);
		
		final Text selectText = new Text(1024, 552/2f + 128 - 24, ResourcesManager.getInstance().font,  resourcesManager.getResources().getString(R.string.stage_select_2),
				ResourcesManager.getInstance().vbom);
		IUpdateHandler selectTextUpdater = new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(selectText.getX() > 1024/2f){
					selectText.setX(selectText.getX() - 16);
				}else{
					selectText.unregisterUpdateHandler(this);
				}
				
			}
			@Override
			public void reset() {}
		};
		selectText.registerUpdateHandler(selectTextUpdater);
		attachChild(selectText);
		
		stageTiles = new ArrayList<Button>();
		
		Button tile0 = new Button(0, 0, ResourcesManager.getInstance().stageSelectTileTutorial, this){
			@Override
			public void onButtonTouched(TouchEvent pSceneTouchEvent) {
				onTileTouched(pSceneTouchEvent, this, -1);
			}
			@Override
			public void buttonPressed() {
				loadStage(0);
			}
		};
		tile0.name = "TUTORIAL";
		stageTiles.add(tile0);
		Button tile1 = new Button(0, 0,
				(stageAvailable[0] ? ResourcesManager.getInstance().stageSelectTile1 : ResourcesManager.getInstance().stageSelectTileLock), this){
			@Override
			public void onButtonTouched(TouchEvent pSceneTouchEvent) {
				onTileTouched(pSceneTouchEvent, this, 0);
			}
			@Override
			public void buttonPressed() {
				loadStage(1);
			}
		};
		tile1.name = (stageAvailable[0] ? "THE\nMOLE" : "???");
		stageTiles.add(tile1);
		Button tile2 = new Button(0, 0,
				(stageAvailable[1] ? ResourcesManager.getInstance().stageSelectTile2 : ResourcesManager.getInstance().stageSelectTileLock), this){
			@Override
			public void onButtonTouched(TouchEvent pSceneTouchEvent) {
				onTileTouched(pSceneTouchEvent, this, 1);
			}
			@Override
			public void buttonPressed() {
				loadStage(2);
			}
		};
		tile2.name = (stageAvailable[1] ? "THE\nFIST" : "???");
		stageTiles.add(tile2);
		Button tile3 = new Button(0, 0,
				(stageAvailable[2] ? ResourcesManager.getInstance().stageSelectTile3 : ResourcesManager.getInstance().stageSelectTileLock), this){
			@Override
			public void onButtonTouched(TouchEvent pSceneTouchEvent) {
				onTileTouched(pSceneTouchEvent, this, 2);
			}
			@Override
			public void buttonPressed() {
				loadStage(3);
			}
		};
		tile3.name = (stageAvailable[2] ? "THE\nWITCH" : "???");
		stageTiles.add(tile3);
		Button tile4 = new Button(0, 0,
				(stageAvailable[3] ? ResourcesManager.getInstance().stageSelectTile4 : ResourcesManager.getInstance().stageSelectTileLock), this){
			@Override
			public void onButtonTouched(TouchEvent pSceneTouchEvent) {
				onTileTouched(pSceneTouchEvent, this, 3);
			}
			@Override
			public void buttonPressed() {
				loadStage(4);
			}
		};
		tile4.name = (stageAvailable[3] ? "THE\nSWARM" : "???");
		stageTiles.add(tile4);
		Button tile5 = new Button(0, 0,
				(stageAvailable[4] ? ResourcesManager.getInstance().stageSelectTile5 : ResourcesManager.getInstance().stageSelectTileLock), this){
			@Override
			public void onButtonTouched(TouchEvent pSceneTouchEvent) {
				onTileTouched(pSceneTouchEvent, this, 4);
			}
			@Override
			public void buttonPressed() {
				loadStage(5);
			}
		};
		tile5.name = (stageAvailable[4] ? "THE\nPHANTOM" : "???");
		stageTiles.add(tile5);
		
		attachChild(rect);
		rect.setVisible(false);
		registerTouchArea(rect);
		
		for(int i=0; i<6; i++){
			stageTiles.get(i).setScale(4);
			stageTiles.get(i).getEntity().setPosition(1024/2f + (stageTiles.get(i).getEntity().getWidthScaled()*2)*i, 552/2f);
		}
		
		setTouchAreaBindingOnActionDownEnabled(true);
		setTouchAreaBindingOnActionMoveEnabled(true);
	}
	
	private void onTileTouched(TouchEvent pSceneTouchEvent, Button tile, int stage){
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
			moveX = pSceneTouchEvent.getX();
		}
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE){
			float dx = pSceneTouchEvent.getX() - moveX;
			if(Math.abs(dx) > 4){
				moved = true;
			}
			if(!(stageTiles.get(stageTiles.size()-1).getEntity().getX() < 1024/2f) &&
					!(stageTiles.get(0).getEntity().getX() > 1024/2f)){
				for(Button b : stageTiles){
					b.getEntity().setX(b.getEntity().getX() + dx);
				}
				moveX = pSceneTouchEvent.getX();
			}
			if(stageTiles.get(0).getEntity().getX() > 1024/2f){
				for(int i=0; i<6; i++){
					stageTiles.get(i).setScale(4);
					stageTiles.get(i).getEntity().setPosition(1024/2f + (stageTiles.get(i).getEntity().getWidthScaled()*2)*i, 552/2f);
				}
			}
			if(stageTiles.get(stageTiles.size()-1).getEntity().getX() < 1024/2f){
				for(int i=0; i<6; i++){
					stageTiles.get(i).setScale(4);
					stageTiles.get(i).getEntity().setPosition(-768 + (stageTiles.get(i).getEntity().getWidthScaled()*2)*i, 552/2f);
				}
			}
			
		}
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
			moved = false;
		}
		stageName.setText(tile.name);
		stageName.setVisible(true);
		stageName.setPosition(tile.getEntity().getX(), 552/2 - 128);
		
		if(stage != -1){
			int score = UserData.getInstance().getBestStageScore(stage);
			if(score > 0){
				scoreText.setText("(" + resourcesManager.getResources().getString(R.string.score) + score + ")");
				scoreText.setVisible(true);
				scoreText.setPosition(tile.getEntity().getX(), 552/2 - 190);
			}else{
				scoreText.setVisible(false);
			}
		}else{
			scoreText.setVisible(false);
		}
		
		
	}
	
	private void loadStage(int index){
		if(index != 0){
			if(!stageAvailable[index-1]){
				return;
			}
		}
		
		if(!moved){
			switch(index){
			case 0:
				SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, StageType.STAGE_TUTORIAL);
				break;
			case 1:
				SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, StageType.STAGE1);
				break;
			case 2:
				SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, StageType.STAGE2);
				break;
			case 3:
				SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, StageType.STAGE3);
				break;
			case 4:
				SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, StageType.STAGE4);
				break;
			case 5:
				SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, StageType.STAGE5);
				break;
			default:
				break;
			}
		}	
	}

	@Override
	public void onBackKeyPressed() {
		Log.d("mine", "back pressed");
		SceneManager.getInstance().loadMainMenuFromStageSelect(ResourcesManager.getInstance().engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_STAGE_SELECT_MENU;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

}
