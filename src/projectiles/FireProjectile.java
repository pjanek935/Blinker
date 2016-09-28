package projectiles;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import main.ResourcesManager;
import scene.BaseScene;

public class FireProjectile extends Projectile implements EnemyProjectile {
	
	private boolean updateHandlerRegistered = false;

	public FireProjectile(PhysicsWorld physicsWorld, BaseScene scene) {
		super(ResourcesManager.getInstance().fire, physicsWorld, scene, 0.25f, 1);
		damage = 10;
		speed = 10;
	}
	
	@Override
	public void fire(float x, float y, float angle) {
		getSprite().setScale(0.5f);
		super.fire(x, y, angle);
		if(!updateHandlerRegistered){
			updateHandlerRegistered = true;
			getSprite().registerUpdateHandler(new IUpdateHandler() {
				@Override
				public void reset() {}
				@Override
				public void onUpdate(float pSecondsElapsed) {
					if(getSprite().getScaleX() < 2){
						getSprite().setScale(getSprite().getScaleX() + 0.1f);
					}
				}
			});
		}
	}

}
