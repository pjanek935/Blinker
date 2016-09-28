package world;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.adt.color.Color;

import janowiak.blinker.R;
import main.BaseScene;
import main.ResourcesManager;
import main.UserData;
import particle.ParticleManager;
import ui.DialogBox;

public class Stage2 extends Stage {

	public Stage2(BaseScene scene, PhysicsWorld physicsWorld, IEntity player, IEntity enemy) {
		super(scene, physicsWorld, player, enemy);
	}

	@Override
	public void loadStage() {
		
		Sprite bg = addVisuals(0, 0, ResourcesManager.getInstance().stage2);
		final Sprite pool = addVisuals(0, 0, ResourcesManager.getInstance().stage2_pool);
		pool.setAlpha(0f);
		
		pool.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(pool.getAlpha() < 1){
					pool.setAlpha(pool.getAlpha() + 0.0001f);
				}
			}
		});
		
		ParticleManager.getInstance().setScene(getScene());
		ParticleManager.getInstance().startRain();
		
		addWall(0, 488, 1024, 100);
		addWall(-510, 0, 64, 1024);
		addWall(446, 0, 64, 1024);
		addWall(0, -415, 1024, 16);
		
	}

	@Override
	public void stageLogic(World world) {
		if(world.hud.skip){
			world.hud.skip = false;
			skip();
		}
		switch(phase){
		case 0:
			world.hud.showEnemyName("FIST");
			world.hud.skipButton.setVisible(true);
			phase ++;
			world.hud.isSkipButtonVisible = true;
		case 1:
			if(world.screenPressed){
				world.screenPressed = false;
				box = new DialogBox(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_1), world.hud);
				phase++;
			}
			break;
		case 2:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_2));
				phase++;
			}
			break;
		case 3:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_3));
				phase++;
			}
			break;
		case 4:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_4));
				phase++;
			}
			break;
		case 5:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_5));
				phase++;
			}
			break;
		case 6:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_6));
				phase++;
			}
			break;
		case 7:
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
				UserData.getInstance().setStageAvailable(2, true);
				int newScore = UserData.getInstance().stopTimer();
				if(newScore > UserData.getInstance().getBestStageScore(1)){
					UserData.getInstance().setNewBestScore(newScore, 1);
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
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_7));
				phase++;
				break;
		case 10002:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, "...");
					phase++;
				}
				break;
		case 10003:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_8));
					phase++;
				}
				break;
		case 10004:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_9));
					phase++;
				}
				break;
		case 10005:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_10));
					phase++;
				}
				break;
		case 10006:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_11));
					phase++;
				}
				break;
		case 10007:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_12));
					phase++;
				}
				break;
		case 10008:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_13));
					phase++;
				}
				break;
		case 10009:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg2_line_14));
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
