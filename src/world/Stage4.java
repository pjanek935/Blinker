package world;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import android.provider.Telephony.Mms.Part;
import janowiak.blinker.R;
import main.BaseScene;
import main.ResourcesManager;
import main.UserData;
import particle.ParticleManager;
import ui.DialogBox;

public class Stage4 extends Stage {

	public Stage4(BaseScene scene, PhysicsWorld physicsWorld, IEntity player, IEntity enemy) {
		super(scene, physicsWorld, player, enemy);
	}

	@Override
	public void loadStage() {
		Sprite bg = new Sprite(0, 0, ResourcesManager.getInstance().stage4, ResourcesManager.getInstance().vbom);
		addVisials(bg);
		
		addWall(0, 520, 500, 16);
		addWall(0, -730, 500, 16);
		
		addWall(600, 0, 16, 1000);
		addWall(-650, 0, 16, 1000);
		
		addWall(400, 400, 500, 16, -30);
		addWall(-400, 400, 700, 16, 30);
		
		addWall(400, -600, 500, 16, 30);
		addWall(-420, -620, 500, 16, -30);
		
		
		ParticleManager.getInstance().setScene(getScene());
		ParticleManager.getInstance().enableFootprints();
		ParticleManager.getInstance().startSnow();
		ParticleManager.getInstance().enableBlizzard();
	}

	@Override
	public void stageLogic(World world) {
		if(world.hud.skip){
			world.hud.skip = false;
			skip();
		}
		switch(phase){
		case 0:
			world.hud.showEnemyName("SWARM");
			world.hud.skipButton.setVisible(true);
			phase ++;
			world.hud.isSkipButtonVisible = true;
		case 1:
			if(world.screenPressed){
				world.screenPressed = false;
				box = new DialogBox(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_1), world.hud);
				phase++;
			}
			break;
		case 2:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_2));
				phase++;
			}
			break;
		case 3:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_3));
				phase++;
			}
			break;
		case 4:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_4));
				phase++;
			}
			break;
		case 5:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_5));
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
				world.hud.blackScreen.setVisible(false);
				
			}
			break;
		case 999:
			world.hud.blackScreen.setVisible(false);
			skip(world);
			break;
		case 9999:
				world.player.goTo(enemy.getX()-128, enemy.getY()-128);
				counter = 0;
				phase++;
				UserData.getInstance().setStageAvailable(4, true);
				int newScore = UserData.getInstance().stopTimer();
				if(newScore > UserData.getInstance().getBestStageScore(3)){
					UserData.getInstance().setNewBestScore(newScore, 3);
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
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_6));
				phase++;
				break;
			case 10002:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_7));
					phase++;
				}
				break;
			case 10003:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_8));
					phase++;
				}
				break;
			case 10004:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_9));
					phase++;
				}
				break;
			case 10005:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_10));
					phase++;
				}
				break;
			case 10006:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_11));
					phase++;
				}
				break;
			case 10007:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_12));
					phase++;
				}
				break;
			case 10008:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_13));
					phase++;
				}
				break;
			case 10009:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg4_line_14));
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
			world.hud.blackScreen.setVisible(true);
			world.hud.blackScreen.setAlpha(0.7f);
			world.hud.setControlsVisible(false);
		}
	}

}
