package world;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;

import android.graphics.Color;
import janowiak.blinker.R;
import main.ResourcesManager;
import main.UserData;
import scene.BaseScene;
import ui.DialogBox;

public class StageTutorial extends Stage {
	
	private Sprite kid;
	private Sprite uncle;
	private Vector2 pos;
	private Rectangle destRect;
	
	//private int timer = 0;
	//private int phase = 0;
	

	public StageTutorial(BaseScene scene, PhysicsWorld physicsWorld, IEntity player, IEntity enemy) {
		super(scene, physicsWorld, player, enemy);
		
		destRect = new Rectangle(enemy.getX() + 44, enemy.getY() - 44, 32, 32, ResourcesManager.getInstance().vbom);
		scene.attachChildAtBackground2(destRect);
		destRect.setColor(Color.GREEN);
		destRect.setAlpha(0.3f);
		destRect.setVisible(false);
		
		uncle = new Sprite(120, -110, ResourcesManager.getInstance().player, ResourcesManager.getInstance().vbom);
		kid = new Sprite(140, -110, ResourcesManager.getInstance().uncle, ResourcesManager.getInstance().vbom);
		uncle.setRotation(90);
		kid.setRotation(-90);
		scene.attachChildAtMiddleground(kid);
		scene.attachChildAtMiddleground(uncle);
	}

	@Override
	public void loadStage() {
		addVisuals(0, 0, ResourcesManager.getInstance().stage_tutorial);
		addWall(395, 0, 8, 950);
		addWall(-438, 0, 8, 950);
		
		addWall(-20, 370, 810, 8);
		addWall(-20, -486, 810, 8);
		
		addWall(0, -114, 138, 94);
		addWall(128, -293, 394, 192);
		
		addWall(84, -54, 8, 8);
	}

	@Override
	public void stageLogic(final World world) {
		if(phase > 10){
			float angle = (float)Math.atan2(uncle.getY() - world.player.getYInScreenSpace(),
					uncle.getX() - world.player.getXInScreenSpace());
			angle = -(float)Math.toDegrees(angle);
			uncle.setRotation(angle - 90);
		}
		switch (phase) {
		case 0:
			world.player.setVisible(false);
			world.player.setPositionInWorldSpace(2.93125f, -3.4375f);
			world.hud.getEnemyHpBar().setVisible(false);
			world.hud.getPlayerHpBar().setVisible(false);
			world.hud.getLeftAnalog().setVisible(false);
			world.hud.getRightAnalog().setVisible(false);
			world.hud.getBlinkBar().setVisible(false);
			world.hud.getDashInput().setVisble(false);
			world.player.setCanBlink(false);
			//world.hud.canBlink = false;
			phase = 1;
			break;
		case 1:
			if(world.screenPressed){
				world.screenPressed = false;
				box = new DialogBox(kid, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_1), world.hud);
				phase = 2;
			}
			break;
		case 2:
			//timer++;
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_2));
				phase++;
			}
			break;
		case 3:
			if(world.screenPressed){
				world.screenPressed = false;
				box.setVisible(false);
				uncle.setRotation(-90);
				phase++;
			}
			
			break;
		case 4:
			if(uncle.getX() > 74){
				uncle.setX(uncle.getX() - 0.6f);
			}else{
				world.hud.blackScreen.setVisible(true);
				world.hud.blackScreen.setAlpha(1);
				world.player.setPositionInScreenSpace(uncle.getX()+20, uncle.getY());
				world.player.setVisible(true);
				uncle.setPosition(uncle.getX()+10, uncle.getY()+20);
				uncle.setRotation(90);
				box.setVisible(true);
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_3));
				phase++;
			}
			break;
		case 5:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(kid, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_4));
				phase++;
			}
			break;
		case 6:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_5));
				phase++;
			}
			break;
		case 7:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_6));
				phase++;
			}
			break;
		case 8:
			if(world.screenPressed){
				String name = UserData.getInstance().getUserName();
				name = name.toLowerCase();
				name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_7) + name +
						ResourcesManager.getInstance().getResources().getString(R.string.tut_line_8));
				phase++;
			}
			break;
		case 9:
			if(world.screenPressed){
				world.screenPressed = false;
				box.setVisible(false);
				phase++;
			}
			break;
		case 10:
			if(kid.getX() > 110){
				kid.setX(kid.getX() - 0.5f);
				uncle.setY(uncle.getY() + 0.6f);
				uncle.setRotation(0);
			}else{
				box.setVisible(false);
				kid.setVisible(false);
				world.hud.blackScreen.setVisible(true);
				world.hud.setAlpha(2);
				phase++;
			}
			break;
		case 11:
			world.screenPressed = false;
			box.setVisible(true);
			box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_9));
			world.hud.getLeftAnalog().setVisible(true);
			pos = new Vector2(world.player.getXInScreenSpace(), world.player.getYInScreenSpace());
			world.readyToStart = true;
			phase++;
			break;
		case 12:
			float d = (float)Math.sqrt(Math.pow(world.player.getXInScreenSpace() - pos.x, 2) + 
					Math.pow(world.player.getYInScreenSpace() - pos.y, 2));
			if(d > 220){
				phase++;
			}
			break;
		case 13:
			box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_10));
			world.readyToStart = false;
			phase++;
			break;
		case 14:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_11));
				phase++;
			}
			break;
		case 15:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_12));
				phase++;
			}
			break;
		case 16:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_13));
				world.hud.getRightAnalog().setVisible(true);
				world.hud.getPlayerHpBar().setVisible(true);
				world.hud.getEnemyHpBar().setVisible(true);
				world.readyToStart = true;
				phase++;
			}
			break;
		case 17:
			if(world.enemy.getCurrentHP() <= 0){
				phase++;
			}
			break;
		case 18:
			box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_14));
			world.readyToStart = false;
			phase++;
			break;
		case 19:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_15));
				phase++;
			}
			break;
		case 20:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_16));
				world.hud.getRightTouchArea().setVisible(true);
				world.hud.getBlinkBar().setVisible(true);
				world.hud.getDashInput().setVisble(true);
				world.player.setCanBlink(true);
				world.readyToStart = true;
				phase++;
			}
			break;
		case 21:
			if(world.player.getBlinkCounter() >= 1){
				phase++;
			}
			break;
		case 22:
			box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_17));
			world.readyToStart = false;
			world.hud.getRightTouchArea().setVisible(false);
			world.readyToStart = false;
			phase++;
			break;
		case 23:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_18));
				phase++;
			}
			break;
		case 24:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_19));
				destRect.setVisible(true);
				world.readyToStart = true;
				phase++;
				ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						world.enemy.setPositionInScreenSpace(0, 200);
					}
				});
				/*world.screenPressed = false;
				phase++;
				box.change(uncle, "Chyba znasz ju¿ wszystkie podstawy...");
				world.hud.holdBlackScreen = true;
				world.hud.blackScreen.setVisible(true);
				world.hud.blackScreen.setAlpha(0);
				UserData.getInstance().setStageAvailable(0, true);*/
			}
			break;
		case 25:
			if(destRect.collidesWith(world.player.getBodySprite())){
				phase++;
			}
			/*world.hud.blackScreen.setAlpha(world.hud.blackScreen.getAlpha() + 0.004f);
			if(world.hud.blackScreen.getAlpha() >= 0.99f){
				world.hud.exit();
			}*/
			break;
		case 26:
			world.readyToStart = false;
			box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_20));
			destRect.setPosition(world.enemy.getXInScreenSpace() - 44, world.enemy.getYInScreenSpace() + 44);
			phase++;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					world.enemy.setPositionInScreenSpace(0, 200);
				}
			});
			break;
		case 27:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_21));
				phase++;
				world.readyToStart = true;
				world.hud.getLeftAnalog().setVisible(false);
			}
			break;
		case 28:
			if(destRect.collidesWith(world.player.getBodySprite())){
				phase++;
				world.readyToStart = false;
			}
			break;
		case 29:
			phase++;
			box.change(uncle, ResourcesManager.getInstance().getResources().getString(R.string.tut_line_22));
			world.hud.holdBlackScreen = true;
			world.hud.blackScreen.setVisible(true);
			world.hud.blackScreen.setAlpha(0);
			UserData.getInstance().setStageAvailable(0, true);
			break;
		case 30:
			world.hud.blackScreen.setAlpha(world.hud.blackScreen.getAlpha() + 0.004f);
			if(world.hud.blackScreen.getAlpha() >= 0.99f){
				world.hud.exit();
			}
			break;
		default:
			break;
		}
		if(phase < 11){
			world.hud.getEnemyHpBar().setVisible(false);
			world.hud.getPlayerHpBar().setVisible(false);
			world.hud.getLeftAnalog().setVisible(false);
			world.hud.getRightAnalog().setVisible(false);
			world.hud.getBlinkBar().setVisible(false);
			world.hud.getDashInput().setVisble(false);
			world.player.setCanBlink(false);
		}
		if(phase >= 11 && phase < 16){
			world.hud.getEnemyHpBar().setVisible(false);
			world.hud.getPlayerHpBar().setVisible(false);
			world.hud.getRightAnalog().setVisible(false);
			world.hud.getBlinkBar().setVisible(false);
			world.hud.getDashInput().setVisble(false);
			world.player.setCanBlink(false);
		}
		if(phase >= 16 && phase < 20){
			world.hud.getBlinkBar().setVisible(false);
			world.hud.getDashInput().setVisble(false);
			world.player.setCanBlink(false);
		}
		if(phase >= 20 && phase < 27){
			//world.hud.getLeftAnalog().setVisible(false);
			//world.hud.getRightAnalog().setVisible(false);
		}
		if(phase >= 27){
			world.hud.getLeftAnalog().setVisible(false);
		}
		
	}

}
