package projectiles;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

import main.ResourcesManager;
import particle.ParticleManager;
import scene.BaseScene;

public class HomingBullet extends Projectile implements EnemyProjectile{

	private IEntity target;
	private boolean updateHandlerSet = false;
	private int counter = 0;
	
	public HomingBullet(PhysicsWorld physicsWorld, BaseScene scene, IEntity target) {
		super(ResourcesManager.getInstance().homing_missile, physicsWorld, scene, 0.25f, 1f);
		this.damage = 15;
		body.setUserData(this);
		body.getFixtureList().get(0).getFilterData().groupIndex = -8;
		this.target = target;
		
	}
	
	public void setTarget(IEntity target){
		this.target = target;
	}
	
	@Override
	public void fire(float x, float y, float angle) {
		counter = 0;
		body.setActive(true);
		body.setTransform(x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (float)Math.toRadians(angle));
		sprite.setVisible(true);
		setFired(true);
		body.setLinearVelocity(2*(float)Math.cos(body.getAngle()), 2*(float)Math.sin(body.getAngle()));
		
		if(!updateHandlerSet){
			updateHandlerSet = true;
			getSprite().registerUpdateHandler(new IUpdateHandler() {
				@Override
				public void reset() {}
				@Override
				public void onUpdate(float pSecondsElapsed) {
					if(isFired()){
						counter++;
						if(counter > 150){
							destroy(null);
						}
						float d = (float) Math.sqrt(Math.pow(target.getX() - getXInScreenSpace(), 2) + 
								Math.pow(target.getY() - getYInScreenSpace(), 2));
						float diffX = target.getX() - getXInScreenSpace();
						float diffY = target.getY() - getYInScreenSpace();
						float directionX = (diffX/d)*10;
						float directionY = (diffY/d)*10;
						
						Vector2 desireVel = new Vector2(directionX*5, directionY*5);
						Vector2 finalVel = new Vector2();
						finalVel = desireVel.sub(body.getLinearVelocity()).mul(0.6f);
						Vector2 finalForce = finalVel;
						finalForce = finalForce.mul(body.getMass());
						body.applyForce(finalForce, body.getWorldCenter());
						float angle = (float) Math.toDegrees(Math.atan2(finalVel.y, finalVel.x));
						getSprite().setRotation(-angle);
					}
				}
			});
		}
	}
	
	@Override
	public void destroy(Fixture whatDestroyed) {
		super.destroy(whatDestroyed);
		counter = 0;
		ParticleManager.getInstance().boom(getXInScreenSpace(), getYInScreenSpace());
	}
	
	
	
}
