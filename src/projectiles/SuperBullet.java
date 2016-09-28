package projectiles;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import main.BaseScene;
import main.ResourcesManager;

public class SuperBullet extends Projectile implements EnemyProjectile{
	
	public SuperBullet(PhysicsWorld physicsWorld, BaseScene scene, float elasticity) {
		super(ResourcesManager.getInstance().super_bullet, physicsWorld, scene, 0.25f, elasticity);
		this.damage = 20;
		body.setUserData(this);
		body.getFixtureList().get(0).getFilterData().groupIndex = -8;
		speed = 10;
	}

}
