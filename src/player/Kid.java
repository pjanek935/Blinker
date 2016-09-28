package player;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import main.BaseScene;
import main.ResourcesManager;

public class Kid extends Enemy {

	public Kid(float x, float y, PhysicsWorld physicsWorld, BaseScene scene, Player player) {
		super(x, y, physicsWorld, scene, ResourcesManager.getInstance().kid_body, ResourcesManager.getInstance().player_leg, player, 1, 0);
		setMovementSpeed(15);
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
		// TODO Auto-generated method stub
		
	}

}
