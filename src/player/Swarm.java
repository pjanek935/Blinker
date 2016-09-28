package player;

import java.util.ArrayList;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import main.BaseScene;
import main.ResourcesManager;
import projectiles.BouncingBullet;
import projectiles.FireProjectile;
import projectiles.HomingBullet;
import projectiles.Projectile;
import projectiles.RoundBullet;
import world.World;
import world.World.GameState;

public class Swarm extends Enemy {
	
	private ArrayList<Drone> drones;
	private boolean shootTurn = false;

	public Swarm(float x, float y, PhysicsWorld physicsWorld, BaseScene scene, Player player) {
		super(x, y, physicsWorld, scene, ResourcesManager.getInstance().swarm_body, ResourcesManager.getInstance().swarm_leg, player, 10, 2);
		drones = new ArrayList<Drone>();
		for(int i=0; i<10; i++){
			Drone d = new Drone(x, y, physicsWorld, scene, player, this);
			drones.add(d);
			if(i > 4){
				d.setPhase(1);
				d.destroyAllProjectiles();
				d.prepareHealingProjectiles();
			}
		}
		projectileLimiter = 150;
	}

	@Override
	public void ai() {
		for(Drone d : drones){
			d.ai();
		}
		switch(getPhase()){
		case 0:
			phase0();
			break;
		case 1:
			phase1();
			break;
		case 2:
			phase2();
			break;
		case 3:
			phase3();
			break;
		case 4:
			phase4();
			break;
		default:
			break;
		}
		
	}
	
	public void phase4(){
		if(getCurrentHP() <= 0){
			step = 3;
		}
		switch(step){
		case 0:
			if(!isBusy()){
				ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						for(int i=0; i<drones.size(); i++){
							Drone d = drones.get(i);
							float angle = (float)Math.toRadians((360f/(float)drones.size())*i);
							d.startArenaMode(angle);
						}
					}
				});
				step = 1;
			}
			
			break;
		case 1:
			setArmorState(false);
			setMovementSpeed(40);
			step = 2;
			break;
		case 2:
			shootAtPlayer();
			chasePlayer();
			break;
		case 3:
			destroyMech();
			World.gameState = GameState.WON;
			step++;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					for(Drone d : drones){
						d.destroy();
					}
				}
			});
			break;
		default:
			break;
		}
	}
	
	public void phase3(){
		if(getCurrentHP() <= 0){
			setArmorState(true);
			setMaxHP(100);
			getHpBar().refill();
			player.setMaxHP(100);
			player.getHpBar().refill();
			goTo(0, -200);
			step = 0;
			setPhase(4);
			final Enemy owner = this;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					destroyAllProjectiles();
					for(Drone d : drones){
						d.destroy();
					}
					drones.clear();
					projectileLimiter = 0;
					projectilePoolSize = 30;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<projectilePoolSize; i++){
						projectilePool.add(new FireProjectile(physicsWorld, scene));
					}
					for(int i=0; i<16; i++){
						Drone d = new Drone(0, 0, physicsWorld, scene, player, owner);
						drones.add(d);
						d.setGhostState(true);
						d.getBodySprite().setVisible(false);
					}
				}
			});
			return;
		}
		switch(step){
		case 0:
			if(!isBusy()){
				ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
					
					@Override
					public void run() {
						counter ++;
						if(counter % 60 == 0){
							if(counter2 < drones.size()){
								drones.get(counter2).getBodySprite().setVisible(true);
								drones.get(counter2).setGhostState(false);
								drones.get(counter2).setPhase(4);
								counter2++;
							}else{
								step = 1;
							}
						}else if(counter % 60 == 30){
							if(counter2 < drones.size()){
								drones.get(counter2).getBodySprite().setVisible(true);
								drones.get(counter2).setGhostState(false);
								drones.get(counter2).destroyAllProjectiles();
								drones.get(counter2).prepareHealingProjectiles();
								drones.get(counter2).setPhase(1);
								drones.get(counter2).setMovementSpeed(100);
								counter2++;
							}else{
								step = 1;
							}
						}
					}
				});
				
			}
			break;
		case 1:
			setMovementSpeed(30);
			setArmorState(false);
			step = 2;
			break;
		case 2:
			walkAndShoot();
			break;
		default:
			break;
		}
	}
	
	public void phase2(){
		if(getCurrentHP() <= 0){
			setArmorState(true);
			setPhase(3);
			step = 0;
			setMovementSpeed(60);
			goTo(0, -200);
			setMaxHP(100);
			getHpBar().refill();
			player.setMaxHP(100);
			player.getHpBar().refill();
			final Enemy owner = this;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					destroyAllProjectiles();
					for(Drone d : drones){
						d.destroy();
					}
					drones.clear();
					projectileLimiter = 60;
					projectilePoolSize = 5;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<projectilePoolSize; i++){
						projectilePool.add(new HomingBullet(physicsWorld, scene, player.getBodySprite()));
					}
					for(int i=0; i<10; i++){
						Drone d = new Drone(0, 0, physicsWorld, scene, player, owner);
						drones.add(d);
						d.setGhostState(true);
						d.getBodySprite().setVisible(false);
					}
				}
			});
			return;
		}
		switch(step){
		case 0:
			setArmorState(false);
			step = 1;
			break;
		case 1:
			walkAndShoot2();
			break;
		default:
			break;
		}
	}
	
	public void phase1(){
		if(getCurrentHP() <= 0){
			setArmorState(true);
			setPhase(2);
			step = 0;
			setMaxHP(100);
			getHpBar().refill();
			player.setMaxHP(100);
			player.getHpBar().refill();
			projectileLimiter = 10;
			final Enemy owner = this;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					destroyAllProjectiles();
					for(Drone d : drones){
						d.destroy();
					}
					drones.clear();
					projectilePoolSize = 20;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<projectilePoolSize; i++){
						projectilePool.add(new RoundBullet(physicsWorld, scene));
					}
					for(int i=0; i<10; i++){
						Drone d = new Drone(0, 0, physicsWorld, scene, player, owner);
						drones.add(d);
						d.setGhostState(true);
						d.getBodySprite().setVisible(false);
					}
				}
			});
			return;
		}
		switch(step){
		case 0:
			if(!isBusy()){
				step = 1;
				setMovementSpeed(30);
				projectileLimiter = 10;
			}
			break;
		case 1:
			shootAtPlayer();
			if(projectilePointer == projectilePoolSize-1){
				step = 2;
			}
			break;
		case 2:
			counter++;
			final Enemy owner = this;
			if(counter % 60 == 0){
				ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						Drone d = new Drone(getXInScreenSpace(), getYInScreenSpace(), physicsWorld, scene, player, owner);
						d.startCircling(64);
						drones.add(d);
						d.destroyAllProjectiles();
						d.prepareHealingProjectiles();
					}
				});
			}else if(counter % 60 == 30){
				ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						Drone d = new Drone(getXInScreenSpace(), getYInScreenSpace(), physicsWorld, scene, player, owner);
						d.startCircling(128);
						drones.add(d);
					}
				});
			}
			if(drones.size() >= 10){
				step = 3;
			}
			break;
		case 3:
			setArmorState(false);
			step = 4;
			break;
		case 4:
			int destroyedDrones = 0;
			for(Drone d : drones){
				if(!d.getBodySprite().isVisible()){
					destroyedDrones++;
				}
			}
			if(destroyedDrones > 7){
				projectileLimiter = 30;
				for(Projectile p : projectilePool){
					BouncingBullet bb = (BouncingBullet)p;
					bb.bouncing = false;
				}
				step = 5;
			}
			
			break;
		case 5:
			shootAtPlayer();
			break;
		default:
			break;
		}
	}
	
	public void phase0(){
		if(getCurrentHP() <= 0){
			setArmorState(true);
			goTo(0,  -200);
			setMovementSpeed(60);
			setPhase(1);
			setMaxHP(100);
			getHpBar().refill();
			player.setMaxHP(100);
			player.getHpBar().refill();
			step = 0;
			
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					destroyAllProjectiles();
					for(Drone d : drones){
						d.destroy();
					}
					drones.clear();
					projectilePoolSize = 30;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<30; i++){
						projectilePool.add(new BouncingBullet(physicsWorld, scene, 1));
						projectilePool.get(i).setSpeed(5);
					}
				}
			});
			
			return;
		}
		int destroyedDrones = 0;
		for(Drone d : drones){
			if(!d.getBodySprite().isVisible()){
				destroyedDrones++;
			}
		}
		if(destroyedDrones >= 7){
			projectileLimiter = 8;
			walkAndShoot();
		}else if(destroyedDrones >= 4){
			projectileLimiter = 30;
			shootAtPlayer();
		}else{
			shootAtPlayer();
		}
	}
	
	public  void walkAndShoot() {
		if(wallClinged){
			float destX = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			float destY = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			goTo(destX, destY);
		}
		if(!isBusy()){
			float destX = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			float destY = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			goTo(destX, destY);
		}
		if(limiter <= 0){
			limiter = ResourcesManager.getInstance().rand.nextInt(100) + 30;
			counter = 0;
		}
		if(counter < limiter){
			shootAtPlayer();
		}
		if(counter > limiter * 1*3f){
			limiter = -1;
		}
		counter++;
	}
	
	public  void walkAndShoot2() {
		if(wallClinged){
			float destX = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			float destY = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			goTo(destX, destY);
		}
		if(!isBusy()){
			float destX = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			float destY = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			goTo(destX, destY);
		}
		if(limiter <= 0){
			limiter = ResourcesManager.getInstance().rand.nextInt(100) + 30;
			counter = 0;
		}
		if(counter < limiter){
			shootAtPlayer();
		}
		if(counter > limiter){
			float rand = ResourcesManager.getInstance().rand.nextFloat();
			if(rand < 0.01f){
				int visibleDrones = 0;
				for(Drone d : drones){
					if(d.getBodySprite().isVisible()) visibleDrones ++;
				}
				if(visibleDrones < 5){
					shootDronesAtPlayer();
				}
				
			}
		}
		if(counter > limiter * 2f){
			limiter = -1;
		}
		counter++;
	}
	
	public void shootDronesAtPlayer(){
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				int i=0;
				for(Drone d : drones){
					float angle = (float)Math.toRadians((360f/10f)*i);
					d.clingToPlayer(angle);
					i++;
				}
			}
		});
	}

	@Override
	public void fire() {
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				if(getPhase() == 0 || getPhase() == 2 || getPhase() == 3){
					float dx = (float)(bodySprite.getWidth()/2 * Math.cos(Math.toRadians(-bodySprite.getRotation() - 180)));
					float dy = (float)(bodySprite.getHeight()/2 * Math.sin(Math.toRadians(-bodySprite.getRotation() - 180)));
					if(shootTurn){
						dx = -dx;
						dy = -dy;
					}
					shootTurn = !shootTurn;
					projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
							-bodySprite.getRotation() + 90);
					projectilePointer++;
					if(projectilePointer >= projectilePoolSize){
						projectilePointer = 0;
					}
				}else if(getPhase() == 1){
					float dx = (float)(bodySprite.getWidth()/2 * Math.cos(Math.toRadians(-bodySprite.getRotation() - 180)));
					float dy = (float)(bodySprite.getHeight()/2 * Math.sin(Math.toRadians(-bodySprite.getRotation() - 180)));
					float randAngle = ResourcesManager.getInstance().rand.nextFloat()*100 - 50;
					if(shootTurn){
						dx = -dx;
						dy = -dy;
					}
					shootTurn = !shootTurn;
					projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
							-bodySprite.getRotation() + 90 + randAngle);
					projectilePointer++;
					if(projectilePointer >= projectilePoolSize){
						projectilePointer = 0;
					}
				}else if(getPhase() == 4){
					float dx = (float)(bodySprite.getWidth()/2 * Math.cos(Math.toRadians(-bodySprite.getRotation() - 180)));
					float dy = (float)(bodySprite.getHeight()/2 * Math.sin(Math.toRadians(-bodySprite.getRotation() - 180)));
					float randAngle = ResourcesManager.getInstance().rand.nextFloat()*80 - 40;
					if(shootTurn){
						dx = -dx;
						dy = -dy;
					}
					shootTurn = !shootTurn;
					projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
							-bodySprite.getRotation() + 90 + randAngle);
					projectilePointer++;
					if(projectilePointer >= projectilePoolSize){
						projectilePointer = 0;
					}
					
					/*randAngle = ResourcesManager.getInstance().rand.nextFloat()*100 - 50;
					projectilePool.get(projectilePointer).fire(bodySprite.getX() - dx, bodySprite.getY() - dy,
							-bodySprite.getRotation() + 90 + randAngle);
					projectilePointer++;
					if(projectilePointer >= projectilePoolSize){
						projectilePointer = 0;
					}*/
				}
			}
		});
		
	}

	@Override
	public void prepareProjectiles() {
		projectilePool = new ArrayList<Projectile>();
		for(int i=0; i<projectilePoolSize; i++){
			projectilePool.add(new RoundBullet(physicsWorld, scene));
		}
	}

}
