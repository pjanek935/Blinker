package projectiles;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import main.BaseScene;
import main.ResourcesManager;

public class Bullet extends Projectile{

	public Bullet(PhysicsWorld physicsWorld, BaseScene scene) {
		super(ResourcesManager.getInstance().player_bullet, physicsWorld, scene, 1.5f, 1);
		this.damage = 8;
		//fire(20);
		body.setUserData(this);
		
		/*getSprite().registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(isFired() && follow){
					float d = (float) Math.sqrt(Math.pow(x - getXInScreenSpace(), 2) + Math.pow(y - getYInScreenSpace(), 2));
					float diffX = x - getXInScreenSpace();
					float diffY = y - getYInScreenSpace();
					float directionX = (diffX/d)*10;
					float directionY = (diffY/d)*10;
					
					Vector2 desireVel = new Vector2(directionX*speed, directionY*speed);
					Vector2 finalVel = new Vector2();
					finalVel = desireVel.sub(body.getLinearVelocity()).mul(0.4f);
					Vector2 finalForce = finalVel;
					finalForce = finalForce.mul(body.getMass());
					body.applyForce(finalForce, body.getWorldCenter());
				}
				
				
			}
		});*/
	}

}
