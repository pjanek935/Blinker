package projectiles;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Fixture;

import main.BaseScene;
import main.ResourcesManager;
import player.Mech;

public class FollowingBullet extends SuperBullet {
	
	private Mech owner;
	private int timer = 0;
	private int step = 0;
	private int radius = 10;
	private boolean updateHandlerRegistered = false;
	private boolean shootAtTarget = false;
	private IEntity target;

	public FollowingBullet(PhysicsWorld physicsWorld, BaseScene scene, Mech owner, IEntity target, boolean shootAtTarget, int radius) {
		super(physicsWorld, scene, 1);
		this.owner = owner;
		this.shootAtTarget = shootAtTarget;
		this.target = target;
		this.radius = radius;
		if(!shootAtTarget){
			sprite.setColor(1f, 0.5f, 0.5f);
			this.damage = 40;
		}
		
	}
	
	@Override
	public void destroy(Fixture whatDestroyed) {
		super.destroy(whatDestroyed);
		timer = 0;
		step = 0;
	}

	@Override
	public void fire(float x, float y, float angle) {
		super.fire(x, y, angle);
		timer = 0;
		step = 0;
		if(!updateHandlerRegistered){
			updateHandlerRegistered = true;
			getSprite().registerUpdateHandler(new IUpdateHandler() {
				@Override
				public void reset() {	}
				@Override
				public void onUpdate(float pSecondsElapsed) {
					if(isFired()){
						switch(step){
						case 0:
							timer++;
							if(timer > radius){
								timer = 0;
								step = 1;
							}
							break;
						case 1:
							body.setLinearVelocity(0, 0);
							step = 2;
							break;
						case 2:
							body.setLinearVelocity(owner.getBody().getLinearVelocity().mul(0.8f));
							if(shootAtTarget){
								timer++;
								if(timer > 120){
									step = 3;
								}
							}
							break;
						case 3:
							float angle = (float)Math.atan2(target.getY() - getYInScreenSpace(), target.getX() - getXInScreenSpace());
							body.setLinearVelocity(1.4f*speed*(float)Math.cos(angle), 1.4f*speed*(float)Math.sin(angle));
							step = 4;
							break;
						case 4:
							break;
						default:
							break;
						}
					}
				}
			});
		}
	}
}
