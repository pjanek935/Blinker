package world;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import janowiak.blinker.R;
import main.ResourcesManager;
import main.UserData;
import particle.ParticleManager;
import scene.BaseScene;
import scene.SceneManager;
import ui.DialogBox;

public class Stage5 extends Stage{
	
	private Sprite roof;

	public Stage5(BaseScene scene, PhysicsWorld physicsWorld, IEntity player, IEntity enemy) {
		super(scene, physicsWorld, player, enemy);
	}

	@Override
	public void loadStage() {
		addVisuals(0, 0, ResourcesManager.getInstance().stage5);
		Sprite shadow = new Sprite(-390, 248, ResourcesManager.getInstance().stage5Shadow, ResourcesManager.getInstance().vbom);
		addVisialsAtForeground2(shadow);
		roof = new Sprite(-350, 230, ResourcesManager.getInstance().stage5Roof, ResourcesManager.getInstance().vbom);
		addVisialsAtForeground2(roof);
		shadow.setAlpha(0.6f);
		roof.setAlpha(1f);
		
		addWall(0, 500, 1500, 16);
		addWall(0, -470, 1500, 16);
		
		addWall(624, 0, 16, 1000);
		addWall(-688, 0, 16, 1000);
		
		ParticleManager.getInstance().setScene(getScene());
		ParticleManager.getInstance().startMist();
		
	}
	
	public void setUpdateHandler(final IEntity player, final IEntity enemy){
		roof.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(player.getX() < -257 && player.getY() > -154 &&
						enemy.getX() < -257 && enemy.getY() > -154){
					roof.setAlpha(0);
				}else if((player.getX() < -257 && player.getY() > -154) ||
						(enemy.getX() < -257 && enemy.getY() > -154)){
					roof.setAlpha(0f);
				}else{
					roof.setAlpha(1);
				}
			}
		});
	}

	@Override
	public void stageLogic(World world) {
		if(world.hud.skip){
			world.hud.skip = false;
			skip();
		}
		switch(phase){
		case 0:
			
			world.hud.showEnemyName("PHANTOM");
			world.hud.skipButton.setVisible(true);
			world.player.setPositionInScreenSpace(-430, 240);
			world.enemy.setPositionInScreenSpace(-440, 450);
			phase ++;
			world.hud.isSkipButtonVisible = true;
			
		case 1:
			if(world.screenPressed){
				world.screenPressed = false;
				box = new DialogBox(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg5_line_1), world.hud);
				phase++;
			}
			break;
		case 2:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg5_line_2));
				phase++;
			}
			break;
		case 3:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.stg5_line_3));
				phase++;
			}
			break;
		case 4:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg5_line_4));
				phase++;
			}
			break;
		case 5:
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
				int newScore = UserData.getInstance().stopTimer();
				if(newScore > UserData.getInstance().getBestStageScore(4)){
					UserData.getInstance().setNewBestScore(newScore, 4);
				}
				break;
			case 10000:
				counter++;
				if(counter > 60){
					if(!world.player.isBusy() || world.player.wallClinged){
						phase++;
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
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg5_line_5));
					phase++;
					world.player.stopMoving();
				}
				break;
			case 10003:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg5_line_6));
					phase++;
				}
				break;
			case 10004:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg5_line_7));
					phase++;
				}
				break;
			case 10005:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg5_line_8));
					phase++;
				}
				break;
			case 10006:
				if(world.screenPressed){
					world.screenPressed = false;
					box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.stg5_line_9));
					phase++;
				}
				break;
			case 10007:
				if(world.screenPressed){
					world.screenPressed = false;
					phase++;
					box.setVisible(false);
				}
				break;
			case 10008:
				world.hud.blackScreen.setAlpha(0f);
				world.hud.blackScreen.setVisible(true);
				world.hud.holdBlackScreen = true;
				phase++;
				break;
			case 10009:
				world.hud.blackScreen.setAlpha(world.hud.blackScreen.getAlpha() + 0.005f);
				if(world.hud.blackScreen.getAlpha() >= 0.99f){
					phase++;
				}
				break;
			case 10010:
				if(world.screenPressed || world.hud.blackScreen.getAlpha() > 0.99f){
					world.screenPressed = false;
					SceneManager.getInstance().loadEpilogueStage(ResourcesManager.getInstance().engine);
				}
				break;
		default:
			break;
		}
		if(phase <= 5){
			world.hud.setControlsVisible(false);
		}
	}

}
