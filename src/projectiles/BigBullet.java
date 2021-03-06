package projectiles;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import main.ResourcesManager;
import scene.BaseScene;

public class BigBullet extends Projectile implements EnemyProjectile {

	public BigBullet(PhysicsWorld physicsWorld, BaseScene scene, float elasticty) {
		super(ResourcesManager.getInstance().big_bullet, physicsWorld, scene, 0.25f, elasticty);
		this.damage = 20;
		body.setUserData(this);
		body.getFixtureList().get(0).getFilterData().groupIndex = -8;
		speed = 10;
	}

}
