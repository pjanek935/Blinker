package player;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import main.ResourcesManager;
import scene.BaseScene;

public class Box extends Enemy{

	public Box(float x, float y, PhysicsWorld physicsWorld, BaseScene scene, Player player) {
		super(x, y, physicsWorld, scene, ResourcesManager.getInstance().box,
				ResourcesManager.getInstance().player_leg, player, 0, 0);
		legs.setVisible(false);
		setMaxHP(20);
	}

	@Override
	public void ai() {
		
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepareProjectiles() {
		
	}

}
