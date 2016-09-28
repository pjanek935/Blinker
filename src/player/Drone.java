package player;

import java.util.ArrayList;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import main.ResourcesManager;
import particle.ParticleManager;
import projectiles.HealingProjectile;
import projectiles.Projectile;
import projectiles.RoundBullet;
import scene.BaseScene;

public class Drone extends Enemy {
	
	private Enemy owner;
	private float currentRadius = 0;
	private float circleRadius = 64;
	private float circleCounter = 0;

	public Drone(float x, float y, PhysicsWorld physicsWorld, BaseScene scene, Player player, Enemy owner) {
		super(x, y, physicsWorld, scene, ResourcesManager.getInstance().drone, ResourcesManager.getInstance().player_leg, player, 2, 0);
		legs.setVisible(false);
		projectileLimiter = 260;
		projectileCounter = ResourcesManager.getInstance().rand.nextInt(projectileLimiter-1);
		this.owner = owner;
		setMaxHP(32);
	}

	@Override
	public void ai() {
		if(!bodySprite.isVisible()){
			return;
		}
		if(getCurrentHP() <= 0){
			setGhostState(true);
			bodySprite.setVisible(false);
			ParticleManager.getInstance().boom(getXInScreenSpace(), getYInScreenSpace());
		}
		switch(getPhase()){
		case 0:
			walkAndShoot();
			break;
		case 1:
			clingToOwner();
			shootAtOwner();
			break;
		case 2:
			circle();
			break;
		case 3:
			if(limiter >= 0){
				counter++;
			}
			if(counter > 700){
				step = 3;
			}
			switch(step){
			case 0:
				if(!isBusy()){
					setGhostState(false);
					step = 1;
				}
				if(counter > 500){
					setGhostState(false);
					step = 1;
				}
				break;
			case 1:
				getBody().setLinearVelocity(player.getBody().getLinearVelocity().mul(1.1f));
				shootAtPlayer();
				if(counter > 600){
					step = 2;
				}
				break;
			case 2:
				getBody().setLinearVelocity(player.getBody().getLinearVelocity().mul(1.1f));
				shootAtPlayer();
				hit();
				break;
			case 3:
				hit();
				counter = 0;
				limiter = -1;
				setMovementSpeed(90);
				goTo(player.getXInScreenSpace(), player.getYInScreenSpace());
				setSpikeState(true);
				spikeDamage = 20;
				step = 4;
				break;
			case 4:
				hit();
				if(!isBusy()){
					setSpikeState(false);
					setGhostState(true);
					getBodySprite().setVisible(false);
				}
				break;
			}
			break;
		case 4:
			walkAndShoot2();
			break;
		case 5:
			counter++;
			if(counter > 370){
				hit();
			}
			if(counter >= 400){
				counter = 0;
				shootAtPlayer();
			}
			switch(step){
			case 0:
				if(!isBusy()){
					setArmorState(false);
					setGhostState(false);
					step = 1;
				}
				break;
			case 1:
				goTo(moveDestRect.getX(), moveDestRect.getY());
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}
	
	public void startArenaMode(float angle){
		projectileLimiter = 0;
		getBody().setTransform(owner.getXInWorldSpace(), owner.getYInWorldSpace(), 0);
		getBodySprite().setVisible(true);
		counter = 0;
		step = 0;
		limiter = 0;
		spikeDamage = 5;
		setMaxHP(50);
		setPhase(5);
		setMovementSpeed(400);
		setGhostState(true);
		projectileLimiter = 0;
		float dx = (float) (500 * Math.cos(angle));
		float dy = (float) (500 * Math.sin(angle));
		goTo(owner.getXInScreenSpace() + dx, owner.getYInScreenSpace() + dy);
	}
	
	public void circle(){
		if(!isBusy()){
			float d = (float)Math.sqrt(Math.pow(this.getXInScreenSpace() - owner.getXInScreenSpace(), 2) + 
					Math.pow(this.getYInScreenSpace() - owner.getYInScreenSpace(), 2));
			if(d < circleRadius){
				currentRadius += 12f;
			}
			float dx = (float) (currentRadius * Math.cos(circleCounter));
			float dy = (float) (currentRadius * Math.sin(circleCounter));
			circleCounter += 0.5f;
			goTo(owner.getXInScreenSpace() + dx, owner.getYInScreenSpace() + dy);
		}
		if(currentRadius > 50){
			if(circleRadius < 80){
				shootAtOwner();
			}else{
				shootAtPlayer();
			}
		}
	}
	
	public void clingToPlayer(float angle){
		getBody().setTransform(owner.getXInWorldSpace(), owner.getYInWorldSpace(), 0);
		getBodySprite().setVisible(true);
		counter = 0;
		step = 0;
		limiter = 0;
		spikeDamage = 1;
		setMaxHP(50);
		setPhase(3);
		setMovementSpeed(800);
		setGhostState(true);
		projectileLimiter = 100000;
		float dx = (float) (200 * Math.cos(angle));
		float dy = (float) (200 * Math.sin(angle));
		goTo(player.getXInScreenSpace() + dx, player.getYInScreenSpace() + dy);
	}
	
	public void startCircling(float circleRadius){
		this.circleRadius = circleRadius;
		setPhase(2);
	}
	
	public void walkAndShoot(){
		if(!isBusy()){
			float destX = ResourcesManager.getInstance().rand.nextFloat()*500 - 250;
			float destY = ResourcesManager.getInstance().rand.nextFloat()*600 - 300;
			goTo(destX, destY);
		}
		shootAtPlayer();
	}
	
	public void walkAndShoot2(){
		if(!isBusy() || wallClinged){
			float destX = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			float destY = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			goTo(destX, destY);
		}
		shootAtPlayer();
	}
	
	public void clingToOwner(){
		if(!isBusy() || wallClinged){
			float randAngle = ResourcesManager.getInstance().rand.nextFloat()*6.28f;
			float randD = ResourcesManager.getInstance().rand.nextFloat()*128 + 32;
			float rx = (float)(randD*Math.cos(randAngle));
			float ry = (float)(randD*Math.sin(randAngle));
			goTo(owner.getXInScreenSpace() + rx, owner.getYInScreenSpace() + ry);
		}
	}

	@Override
	public void fire() {
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				float dx = (float)((bodySprite.getWidth()/2 + 4) * Math.cos(Math.toRadians(-bodySprite.getRotation() + 90)));
				float dy = (float)((bodySprite.getWidth()/2 + 4) * Math.sin(Math.toRadians(-bodySprite.getRotation() + 90)));
				if(projectilePool == null || projectilePool.isEmpty()){
					return;
				}
				projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
						-bodySprite.getRotation() + 90);
				projectilePointer++;
				if(projectilePointer >= projectilePoolSize){
					projectilePointer = 0;
				}
			}

		});
	}
	
	public void destroy(){
		if(getBodySprite().isVisible()){
			ParticleManager.getInstance().boom(getXInScreenSpace(), getYInScreenSpace());
		}
		legs.destroy();
		destroyAllProjectiles();
		final PhysicsConnector physicsConnector =
				physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(getBodySprite());
		if (physicsConnector != null){
			getBodySprite().setVisible(false);
             physicsWorld.unregisterPhysicsConnector(physicsConnector);
             getBody().setActive(false);
             physicsWorld.destroyBody(getBody());
             getBodySprite().detachSelf();
        }
	}

	@Override
	public void prepareProjectiles() {
		projectilePool = new ArrayList<Projectile>();
		for(int i=0; i<projectilePoolSize; i++){
			projectilePool.add(new RoundBullet(physicsWorld, scene));
		}
	}
	
	public void prepareHealingProjectiles() {
		projectilePoolSize = 10;
		projectileLimiter = 2;
		
		projectilePool = new ArrayList<Projectile>();
		for(int i=0; i<projectilePoolSize; i++){
			projectilePool.add(new HealingProjectile(physicsWorld, scene));
		}
	}
	
	public void shootAtOwner(){
		if(isAirKicked()){
			return;
		}
		float angle = (float)Math.atan2((owner.getYInScreenSpace()+bodySprite.getHeight()/2) - bodySprite.getY(),
				(owner.getXInScreenSpace()+bodySprite.getWidth()/2) - bodySprite.getX());
		angle = (float)Math.toDegrees(angle);
		bodySprite.setRotation(-angle + 90);
		projectileCounter++;
		if(projectileCounter > projectileLimiter){
			projectileCounter = 0;
			fire();
		}
	}

}
