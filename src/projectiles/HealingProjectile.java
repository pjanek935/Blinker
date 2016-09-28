package projectiles;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import main.ResourcesManager;
import scene.BaseScene;

public class HealingProjectile extends Projectile {

	public HealingProjectile( PhysicsWorld physicsWorld, BaseScene scene) {
		super(ResourcesManager.getInstance().healing_bullet, physicsWorld, scene, 1f, 1f);
		getSprite().setAlpha(0.4f);
		damage = -1;
		speed = 20;
		body.setUserData(this);
	}
	

}
