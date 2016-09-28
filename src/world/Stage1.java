package world;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import android.provider.Telephony.Mms.Part;
import android.util.Log;
import janowiak.blinker.R;
import main.BaseScene;
import main.ResourcesManager;
import main.UserData;
import particle.ParticleManager;
import ui.DialogBox;

public class Stage1 extends Stage {
	
	public Stage1(BaseScene scene, PhysicsWorld physicsWorld, IEntity player, IEntity enemy) {
		super(scene, physicsWorld, player, enemy);
	}
	
	@Override
	public void loadStage(){
		
		Sprite bg3 = addVisuals(0, 0 + 382, ResourcesManager.getInstance().stage1_tribune);
		bg3.setRotation(90);
		
		Sprite bg4 = addVisuals(510, 0 + 382, ResourcesManager.getInstance().stage1_tribune);
		bg4.setRotation(90);
		bg4.setFlippedVertical(true);
		
		Sprite bg5 = addVisuals(0, 0 - 382, ResourcesManager.getInstance().stage1_wall);
		bg5.setRotation(90);
		
		Sprite bg1 = addVisuals(0, 0, ResourcesManager.getInstance().stage1_grass);
		bg1.setRotation(90);
		
		Sprite bg2 = addVisuals(0 + 510, 0, ResourcesManager.getInstance().stage1_grass);
		bg2.setRotation(-90);
		
		Sprite bg6 = addVisuals(510, 0 - 382, ResourcesManager.getInstance().stage1_wall);
		bg6.setRotation(90);
		
		Sprite bg7 = addVisuals(510 + 382, 254, ResourcesManager.getInstance().stage1_tribune2);
		bg7.setRotation(90);
		
		Sprite bg8 = addVisuals(510 + 382, -254, ResourcesManager.getInstance().stage1_building);
		bg8.setRotation(90);
		
		Sprite bg9 = addVisuals(0 - 256 - 62, 0, ResourcesManager.getInstance().stage1_bottom);
		bg9.setRotation(90);
		
		addWall(-284, 0, 16, 800);
		addWall(850, 0, 16, 800);
		addWall(280, 350, 1100, 16);
		addWall(-5, -384, 485, 16);
		addWall(512 + 30, -384, 480 + 80, 16);
		addWall(0, -500, 1000, 16);
		
		ParticleManager.getInstance().setScene(getScene());
		ParticleManager.getInstance().startMist();
		ParticleManager.getInstance().enableDirtParticles();
		
	}

	@Override
	public void stageLogic(World world) {
		if(world.hud.skip){
			world.hud.skip = false;
			skip();
		}
		switch(phase){
		case 0:
			world.hud.showEnemyName("MOLE");
			world.hud.skipButton.setVisible(true);
			phase ++;
			world.hud.isSkipButtonVisible = true;
		case 1:
			if(world.screenPressed){
				world.screenPressed = false;
				box = new DialogBox(player,  ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_1), world.hud);
				phase++;
			}
			break;
		case 2:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_2));
				phase++;
			}
			break;
		case 3:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_3));
				phase++;
			}
			break;
		case 4:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_4));
				phase++;
			}
			break;
		case 5:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_5));
				phase++;
			}
			break;
		case 6:
			if(world.screenPressed){
				world.screenPressed = false;
				box.setVisible(false);
				world.hud.setControlsVisible(true);
				phase++;
				world.hud.startCounting();
				world.hud.skipButton.setVisible(false);
			}
			break;
		case 999:
			skip(world);
			break;
		case 9999:
			world.player.goTo(enemy.getX()-128, enemy.getY()-128);
			counter = 0;
			phase++;
			UserData.getInstance().setStageAvailable(1, true);
			int newScore = UserData.getInstance().stopTimer();
			Log.d("mine", "new score: " + newScore);
			if(newScore > UserData.getInstance().getBestStageScore(0)){
				UserData.getInstance().setNewBestScore(newScore, 0);
			}
			break;
		case 10000:
			counter++;
			if(counter > 60){
				if(!world.player.isBusy() || world.player.wallClinged){
					phase++;
					world.player.stopMoving();
				}
			}
			break;
		case 10001:
			box.setVisible(true);
			box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_6));
			phase++;
			break;
		case 10002:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_7));
				phase++;
			}
			break;
		case 10003:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_8));
				phase++;
			}
			break;
		case 10004:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_9));
				phase++;
			}
			break;
		case 10005:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_10));
				phase++;
			}
			break;
		case 10006:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_11));
				phase++;
			}
			break;
		case 10007:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_12));
				phase++;
			}
			break;
		case 10008:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_13));
				phase++;
			}
			break;
		case 10009:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg1_line_14));
				phase++;
			}
			break;
		case 10010:
			if(world.screenPressed){
				world.screenPressed = false;
				world.hud.exit();
			}
			break;
		default:
			
			break;
		}
		if(phase <= 6 || phase >= 9999){
			world.hud.setControlsVisible(false);
		}
		
		
	}

}
