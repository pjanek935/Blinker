package player;

import java.util.ArrayList;

import org.andengine.entity.primitive.Line;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.adt.color.Color;

import main.ResourcesManager;
import projectiles.BigBullet;
import projectiles.PhantomProjectile;
import projectiles.Projectile;
import projectiles.RoundBullet;
import projectiles.SuperBullet;
import scene.BaseScene;
import world.World;
import world.World.GameState;

public class Phantom extends Enemy {
	
	private boolean stealthMode = false;
	private int stealthModeCounter = 0;
	private ArrayList<Phantom> phantoms;
	private float phase4Angle = 0;
	private Line line;

	public Phantom(float x, float y, PhysicsWorld physicsWorld, BaseScene scene, Player player) {
		super(x, y, physicsWorld, scene, ResourcesManager.getInstance().phantom_body,
				ResourcesManager.getInstance().phantom_leg, player, 10, 3);
		projectileLimiter = 15;
		line = new Line(0, 0, 0, 0, ResourcesManager.getInstance().vbom);
		scene.attachChildAtBackground2(line);
		line.setColor(Color.RED);
		line.setVisible(false);
		line.setAlpha(0.3f);
	}

	@Override
	public void ai() {
		if(stealthMode){
			stealthModeCounter++;
			if(stealthModeCounter > 300){
				setStealthMode(false);
			}
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
		case 5:
			phase5();
		case 999:
			phantomPhase();
			break;
		default:
			break;
		}
	}
	
	public void phase5(){
		if(getCurrentHP() <= 0){
			step = 5;
		}
		float angle = (float) Math.atan2(player.getYInScreenSpace() - getYInScreenSpace(), player.getXInScreenSpace() - getXInScreenSpace());
		float x = getXInScreenSpace() + (float)(1000*Math.cos(angle));
		float y = getYInScreenSpace() + (float)(1000*Math.sin(angle));
		line.setPosition(getXInScreenSpace(), getYInScreenSpace(),
				x, y);
		switch(step){
		case 0:
			if(!isBusy()){
				setArmorState(false);
				step = 1;
			}
			break;
		case 1:
			setStealthMode(true);
			setGhostState(true);
			goTo(ResourcesManager.getInstance().rand.nextFloat()*1000 - 500, ResourcesManager.getInstance().rand.nextFloat()*1000 - 500);
			step = 2;
			break;
		case 2:
			counter++;
			if(counter > 15){
				if(wallClinged || !isBusy()){
					stopMoving();
					step = 3;
					line.setVisible(true);
					line.setLineWidth(32);
					counter = 0;
				}
			}
			break;
		case 3:
			aimAtPlayer();
			line.setLineWidth(line.getLineWidth() - 1);
			if(line.getLineWidth() <= 1){
				step = 4;
				setStealthMode(false);
				setGhostState(false);
			}
			break;
		case 4:
			counter++;
			line.setVisible(false);
			if(counter <= 60){
				fire();
			}
			if(counter > 80){
				counter = 0;
				step = 1;
			}
			
			break;
		case 5:
			destroyMech();
			World.gameState = GameState.WON;
			step++;
			line.setVisible(false);
			break;
		default:
			break;
		}
	}
	
	public void phase4(){
		switch(step){
		case 0:
			float x = (float)Math.cos(Math.toRadians(phase4Angle));
			float y = (float)Math.sin(Math.toRadians(phase4Angle));
			shoot(x, y);
			phase4Angle += 4;
			counter++;
			if(counter > 100){
				fire2();
				counter = 0;
			}
			counter2++;
			if(counter2 > 1000){
				step = 1;
				counter = 0;
				counter2 = 0;
			}
			break;
		case 1:
			counter++;
			if(counter > 60){
				step = 2;
			}
			break;
		case 2:
			step = 0;
			setPhase(5);
			counter = 0;
			counter2 = 0;
			setMaxHP(100);
			getHpBar().refill();
			player.setMaxHP(100);
			player.getHpBar().refill();
			goTo(0, 0);
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					destroyAllProjectiles();
					projectilePoolSize = 120;
					projectileLimiter = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<projectilePoolSize; i++){
						projectilePool.add(new RoundBullet(physicsWorld, scene));
					}
				}
			});
			break;
		default:
			break;
		}
	}
	
	public void phase3(){
		switch(step){
		case 0:
			if(!isBusy()){
				step = 1;
				counter = 0;
			}
			break;
		case 1:
			counter++;
			if(counter > 1600){
				step = 2;
				counter = 0;
			}
			shootAtPlayer();
			break;
		case 2:
			goTo(0, 0);
			step = 3;
			break;
		case 3:
			if(!isBusy()){
				setPhase(4);
				step = 0;
				counter = 0;
				counter2 = 0;
				player.setMaxHP(100);
				player.getHpBar().refill();
				ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						destroyAllProjectiles();
						projectileLimiter = 1;
						projectileCounter = 0;
						projectilePoolSize = 100;
						projectilePool = new ArrayList<Projectile>();
						for(int i=0; i<projectilePoolSize; i++){
							projectilePool.add(new SuperBullet(physicsWorld, scene, 1));
						}
						for(int i=0; i<projectilePoolSize-20; i++){
							projectilePool.get(i).setSpeed(35);
						}
						for(int i=projectilePoolSize-20; i<projectilePoolSize; i++){
							projectilePool.get(i).setSpeed(10);
						}
					}
				});
			}
			break;
		default:
			break;
		}
	}
	
	public void phase2(){
		switch(step){
		case 0:
			boolean busy = false;
			for(Phantom p : phantoms){
				if(p.isBusy()){
					busy = true;
				}
			}
			if(!isBusy() && !busy){
				ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						for(Phantom p : phantoms){
							p.destroy();
						}
						phantoms.clear();
						destroyAllProjectiles();
						projectilePoolSize = 60;
						projectileLimiter = 40;
						projectileCounter = projectileLimiter-2;
						projectilePool = new ArrayList<Projectile>();
						for(int i=0; i<projectilePoolSize; i++){
							projectilePool.add(new SuperBullet(physicsWorld, scene, 1));
						}
					}
				});
				step = 1;
				counter = 0;
			}
			break;
		case 1:
			counter++;
			if(counter > 1000){
				step = 2;
			}
			shootAtPlayer();
			break;
		case 2:
			goTo(-350, 0);
			setPhase(3);
			step = 0;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					destroyAllProjectiles();
					projectileLimiter = 5;
					projectileCounter = projectileLimiter - 2;
					projectilePoolSize = 100;
					for(int i=0; i<80; i++){
						projectilePool.add(new BigBullet(physicsWorld, scene, 1));
					}
					for(int i=0; i<20; i++){
						projectilePool.add(new SuperBullet(physicsWorld, scene, 1));
					}
					for(int i=0; i<100; i++){
						projectilePool.get(i).setSpeed(14);
					}
				}
			});
			break;
		default:
			break;
		}
	}
	
	public void phantomPhase(){
		switch(step){
		case 0:
			if(!isBusy() || wallClinged){
				float randx = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
				float randy = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
				goTo(randx, randy);
			}
			if(limiter <= 0){
				limiter = ResourcesManager.getInstance().rand.nextInt(100) + 30;
				counter = 0;
			}
			if(counter < limiter){
				shootAtPlayer();
			}
			if(counter > limiter * 1.5){
				limiter = -1;
			}
			counter++;
			break;
		case 1:
			bodySprite.setAlpha(bodySprite.getAlpha() - 0.01f);
			legs.setVisible(false);
			if(bodySprite.getAlpha() <= 0.01f){
				bodySprite.setVisible(false);
			}
			break;
		default:
			break;
		}
		
	}
	
	public void phase1(){
		if(getCurrentHP() <= 0){
			setArmorState(true);
			for(Phantom p : phantoms){
				p.step = 1;
				p.goTo(0, 0);
			}
			goTo(0, 0);
			setMaxHP(100);
			getHpBar().refill();
			player.setMaxHP(100);
			player.getHpBar().refill();
			step = 0;
			setPhase(2);
			return;
		}
		switch(step){
		case 0:
			if(!isBusy()){
				step = 1;
			}
			break;
		case 1:
			for(Phantom p : phantoms){
				p.setPhase(999);
				p.setVisible(true);
				p.projectileLimiter = 40;
				p.getBodySprite().setAlpha(0.75f);
				p.legs.setAlpha(0.75f);
			}
			projectileLimiter = 15;
			setArmorState(false);
			step = 2;
			break;
		case 2:
			if(!isBusy() || wallClinged){
				float randx = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
				float randy = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
				goTo(randx, randy);
			}
			if(limiter <= 0){
				limiter = ResourcesManager.getInstance().rand.nextInt(100) + 30;
				counter = 0;
			}
			if(counter < limiter){
				shootAtPlayer();
			}
			if(counter > limiter * 1.5){
				limiter = -1;
			}
			counter++;
			break;
		default:
			break;
		}
	}
	
	public void phase0(){
		if(getCurrentHP() <= 0){
			setArmorState(true);
			goTo(0, 0);
			setMaxHP(100);
			step = 0;
			setPhase(1);
			getHpBar().refill();
			player.setMaxHP(100);
			getHpBar().refill();
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					phantoms = new ArrayList<Phantom>();
					for(int i=0; i<7; i++){
						Phantom p = new Phantom(0, 0, physicsWorld, scene, player);
						p.setGhostState(true);
						p.setVisible(false);
						p.setPhase(1000);
						p.destroyAllProjectiles();
						p.projectilePoolSize = 20;
						p.projectilePointer = 0;
						p.projectilePool = new ArrayList<Projectile>();
						projectileLimiter = 4;
						for(int j=0; j<p.projectilePoolSize; j++){
							p.projectilePool.add(new PhantomProjectile(physicsWorld, scene));
						}
						phantoms.add(p);
					}
				}
			});
			return;
		}
		if(!isBusy() || wallClinged){
			float randx = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			float randy = ResourcesManager.getInstance().rand.nextFloat()*1000 - 500;
			goTo(randx, randy);
		}
		if(limiter <= 0){
			limiter = ResourcesManager.getInstance().rand.nextInt(100) + 30;
			counter = 0;
		}
		if(counter < limiter){
			shootAtPlayer();
		}
		if(counter == limiter){
			if(!stealthMode){
				float rand = ResourcesManager.getInstance().rand.nextFloat();
				if(rand < 0.4f){
					setStealthMode(true);
				}
			}
		}
		if(counter > limiter * 1.5){
			limiter = -1;
		}
		counter++;
	}
	
	public void setStealthMode(boolean stealthMode){
		if(stealthMode){
			this.stealthMode = true;
			setMovementSpeed(60);
			getBodySprite().setCurrentTileIndex(2);
			defaultTileIndex = 2;
			stealthModeCounter = 0;
			legs.setAlpha(0.1f);
			bodySprite.setAlpha(0.3f);
		}else{
			this.stealthMode = false;
			setMovementSpeed(30);
			defaultTileIndex = 0;
			getBodySprite().setCurrentTileIndex(0);
			stealthModeCounter = 0;
			legs.setAlpha(1);
			bodySprite.setAlpha(1);
		}
	}

	@Override
	public void fire() {
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				float dx = (float)((bodySprite.getWidth()/2 + 4) * Math.cos(Math.toRadians(-bodySprite.getRotation() + 45)));
				float dy = (float)((bodySprite.getWidth()/2 + 4) * Math.sin(Math.toRadians(-bodySprite.getRotation() + 45)));
				if(getPhase() == 0 || getPhase() == 1 || getPhase() == 999){
					projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
							-bodySprite.getRotation() + 90);
					projectilePointer++;
					if(projectilePointer >= projectilePoolSize){
						projectilePointer = 0;
					}
				}else if(getPhase() == 2){
					for(int i=0; i<20; i++){
						float angle = 360f/20f * i;
						float randAngle = ResourcesManager.getInstance().rand.nextFloat()*3f - 1.5f;
						projectilePool.get(projectilePointer).fire(bodySprite.getX(), bodySprite.getY(),
								-bodySprite.getRotation() + 90 + angle + randAngle);
						projectilePointer++;
						if(projectilePointer >= projectilePoolSize){
							projectilePointer = 0;
						}
					}
				}else if(getPhase() == 3){
					if(projectilePointer == 0){
						projectileLimiter = 5;
						projectileCounter = 0;
					}
					for(int j = 0; j<20; j++){
						float randAngle = ResourcesManager.getInstance().rand.nextFloat()*2 - 1;
						float a = -60 + j*6f;
						dx = (float)((a*2) * Math.cos(Math.toRadians(-bodySprite.getRotation())));
						dy = (float)((a*2) * Math.sin(Math.toRadians(-bodySprite.getRotation())));
						projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
								-bodySprite.getRotation() + 90 + a + randAngle);
						projectilePointer++;
						if(projectilePointer >= projectilePoolSize){
							projectileLimiter = 100;
							projectileCounter = 0;
							projectilePointer = 0;
							return;
						}
					}
				}else if(getPhase() == 4){
					projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
							-bodySprite.getRotation() + 90);
					projectilePointer++;
					if(projectilePointer >= projectilePoolSize-10){
						projectilePointer = 0;
					}
				}else if(getPhase() == 5){
					float randAngle = ResourcesManager.getInstance().rand.nextFloat()*80 - 40;
					projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
							-bodySprite.getRotation() + 90 + randAngle);
					projectilePointer++;
					if(projectilePointer >= projectilePoolSize){
						projectilePointer = 0;
					}
					
				}
			}
			
		});
		
	}
	
	public void fire2() {
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=projectilePoolSize-10; i<projectilePoolSize; i++){
					float angle = 360f/10f * i;
					projectilePool.get(i).fire(bodySprite.getX(), bodySprite.getY(),
							-bodySprite.getRotation() + 90 + angle);
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
	
	public void destroy(){
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

}
