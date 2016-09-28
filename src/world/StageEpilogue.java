package world;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import janowiak.blinker.R;
import main.BaseScene;
import main.ResourcesManager;
import main.SceneManager;
import ui.DialogBox;

public class StageEpilogue extends Stage {

	public StageEpilogue(BaseScene scene, PhysicsWorld physicsWorld, IEntity player, IEntity enemy) {
		super(scene, physicsWorld, player, enemy);
		
	}

	@Override
	public void loadStage() {
		addVisuals(0, 0, ResourcesManager.getInstance().stage_tutorial);
	}

	@Override
	public void stageLogic(World world) {
		world.hud.setControlsVisible(false);
		switch (phase) {
		case 0:
			world.hud.showEnemyName("EPILOGUE");
			world.hud.skipButton.setVisible(true);
			world.player.setPositionInScreenSpace(-300, -240);
			world.enemy.setPositionInScreenSpace(-300, 300);
			world.hud.skipButton.setVisible(false);
			phase ++;
			world.enemy.goTo(-300, -150);
			world.enemy.shootAtPlayer();
			break;
		case 1:
			if(!world.enemy.isBusy()){
				phase++;
			}
			break;
		case 2:
			box = new DialogBox(player, ResourcesManager.getInstance().getResources().getString(R.string.epg_line_1), world.hud);
			phase++;
			break;
		case 3:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, "...");
				phase++;
			}
			break;
		case 4:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.epg_line_2));
				phase++;
			}
			break;
		case 5:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.epg_line_3));
				phase++;
			}
			break;
		case 6:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, "...");
				phase++;
			}
			break;
		case 7:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.epg_line_4));
				phase++;
			}
			break;
		case 8:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, "...");
				phase++;
			}
			break;
		case 9:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(player, ResourcesManager.getInstance().getResources().getString(R.string.epg_line_5));
				phase++;
			}
			break;
		case 10:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.epg_line_6));
				phase++;
			}
			break;
		case 11:
			if(world.screenPressed){
				world.screenPressed = false;
				box.change(enemy, ResourcesManager.getInstance().getResources().getString(R.string.epg_line_7));
				phase++;
			}
			break;
		case 12:
			if(world.screenPressed){
				world.screenPressed = false;
				world.hud.holdBlackScreen = true;
				world.hud.blackScreen.setAlpha(0);
				world.hud.blackScreen.setVisible(true);
				phase++;
			}
			break;
		case 13:
			world.hud.blackScreen.setAlpha(world.hud.blackScreen.getAlpha() + 0.008f);
			if(world.hud.blackScreen.getAlpha() >= 0.99f){
				world.hud.exit();
			}
			break;
		default:
			break;
		}
	}

}
