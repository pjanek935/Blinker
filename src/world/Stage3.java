package world;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import janowiak.blinker.R;
import main.ResourcesManager;
import main.UserData;
import particle.ParticleManager;
import scene.BaseScene;
import ui.DialogBox;

public class Stage3 extends Stage{

	public Stage3(BaseScene scene, PhysicsWorld physicsWorld, IEntity player, IEntity enemy) {
		super(scene, physicsWorld, player, enemy);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadStage() {
		Sprite bg = new Sprite(0, 0, ResourcesManager.getInstance().stage3, ResourcesManager.getInstance().vbom);
		addVisials(bg);
		
		addWall(-436, 149, 16, 370);
		addWall(615, 0, 16, 700);
		
		addWall(70, 463, 650, 16);
		addWall(300, -600, 100, 16);
		
		addWall(-320, 405, 250, 16, 28);
		addWall(-378, 342, 100, 16, 67);
		addWall(-426, 290, 70, 16, 21);
		
		addWall(-454, -79, 100, 16, 68);
		
		addWall(-250, -301, 600, 16, -45);
		addWall(10, -510, 300, 16, -27);
		addWall(150, -574, 300, 16, -15);
		
		addWall(480, -530, 330, 16, 25);
		addWall(560, -472, 110, 16, 45);
		addWall(590, -410, 160, 16, 72);
		
		addWall(500, 410, 280, 16, -28);
		
		addWall(590, 350, 100, 16, -50);
		
		ParticleManager.getInstance().setScene(getScene());
		ParticleManager.getInstance().enableLeafs();
		ParticleManager.getInstance().enableGlyphParticles();
	}

	@Override
	public void stageLogic(final World world) {
		if(world.hud.skip){
			world.hud.skip = false;
			skip();
		}
		switch(phase){
		case 0:
			world.hud.showEnemyName("WITCH");
			world.hud.skipButton.setVisible(true);
			phase ++;
			world.hud.isSkipButtonVisible = true;
		case 1:
			if(world.screenPressed){
				world.screenPressed = false;
				box = new DialogBox(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg3_line_1), world.hud);
				phase++;
			}
			break;
		case 2:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg3_line_2));
				phase++;
			}
			break;
		case 3:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg3_line_3));
				phase++;
			}
			break;
		case 4:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg3_line_4));
				phase++;
			}
			break;
		case 5:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg3_line_5));
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
				UserData.getInstance().setStageAvailable(3, true);
				int newScore = UserData.getInstance().stopTimer();
				if(newScore > UserData.getInstance().getBestStageScore(2)){
					UserData.getInstance().setNewBestScore(newScore, 2);
				}
				ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						world.enemy.destroyAllProjectiles();
					}
				});
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
				box.change(enemy, "...");
				phase++;
				break;
			case 10002:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg3_line_6));
					phase++;
				}
				break;
			case 10003:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, "...");
					phase++;
				}
				break;
			case 10004:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg3_line_7));
					phase++;
				}
				break;
			case 10005:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(enemy, "...");
					phase++;
				}
				break;
			case 10006:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg3_line_8));
					phase++;
				}
				break;
			case 10007:
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
