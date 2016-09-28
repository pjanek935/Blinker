package projectiles;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import main.ResourcesManager;
import scene.BaseScene;

public class RoundBullet extends Projectile implements EnemyProjectile{

	public RoundBullet(PhysicsWorld physicsWorld, BaseScene scene) {
		super(ResourcesManager.getInstance().player_bullet, physicsWorld, scene, 1, 1);
		this.damage = 8;
		body.setUserData(this);
		body.getFixtureList().get(0).getFilterData().groupIndex = -8;
	}

}
