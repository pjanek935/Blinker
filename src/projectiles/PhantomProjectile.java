package projectiles;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import main.BaseScene;

public class PhantomProjectile extends RoundBullet{

	public PhantomProjectile(PhysicsWorld physicsWorld, BaseScene scene) {
		super(physicsWorld, scene);
		damage = 0;
		getSprite().setAlpha(0.7f);
	}
	
}
