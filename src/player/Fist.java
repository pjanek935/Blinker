package player;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.adt.color.Color;

import android.app.backup.SharedPreferencesBackupHelper;
import android.util.Log;
import main.BaseScene;
import main.ResourcesManager;
import projectiles.BigBullet;
import projectiles.HomingBullet;
import projectiles.Projectile;
import projectiles.RoundBullet;
import projectiles.SuperBullet;
import world.World;
import world.World.GameState;

public class Fist extends Enemy {

	private Sprite body;
	private boolean shootTurn = false;
	private Line line;
	
	public Fist(float x, float y, PhysicsWorld physicsWorld, BaseScene scene, Player player) {
		super(x, y, physicsWorld, scene, ResourcesManager.getInstance().fist_barrel, ResourcesManager.getInstance().fist_body, player, 20, 2);
		legs.setVisible(false);
		body = new Sprite(0, 0, ResourcesManager.getInstance().fist_body, ResourcesManager.getInstance().vbom);
		scene.attachChildAtMiddleground(body);
		getBodySprite().detachSelf();
		this.projectileLimiter = 6;
		scene.attachChildAtMiddleground(getBodySprite());
		body.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				body.setPosition(legs.getXInScreenSpace(), legs.getYInScreenSpace());
				body.setRotation(-legs.getRotation() + 90);
			}
		});
		setMovementSpeed(30);
		spikeDamage = 50;
		
		line = new Line(0, 0, 0, 0, ResourcesManager.getInstance().vbom);
		scene.attachChildAtMiddleground(line);
		line.setColor(Color.RED);
		line.setLineWidth(4);
		line.setAlpha(0.3f);
		line.setVisible(false);
	}

	@Override
	public void ai() {
		switch(getPhase()){
		case 0:
			//setPhase(1);
			phase0();
			break;
		case 1:
			//dealDamage(200);
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
			break;
		default:
			break;
		}
	}
	
	public void phase5(){
		if(getCurrentHP() <= 0){
			step = 7;
		}
		float angle = (float) Math.atan2(player.getYInScreenSpace() - getYInScreenSpace(), player.getXInScreenSpace() - getXInScreenSpace());
		float x = getXInScreenSpace() + (float)(600*Math.cos(angle));
		float y = getYInScreenSpace() + (float)(600*Math.sin(angle));
		line.setPosition(getXInScreenSpace(), getYInScreenSpace(),
				x, y);
		switch(step){
		case 0:
			spikeDamage = 25;
			setArmorState(true);
			limiter = ResourcesManager.getInstance().rand.nextInt(100)+30;
			line.setVisible(true);
			line.setLineWidth(32);
			step = 1;
			break;
		case 1:
			chasePlayer();
			counter++;
			if(counter >= limiter){
				counter = 0;
				step = 2;
			}
			break;
		case 2:
			line.setLineWidth(32);
			step = 3;
			break;
		case 3:
			line.setLineWidth(line.getLineWidth() - 0.5f);
			if(line.getLineWidth() <= 1){
				line.setVisible(false);
				step = 4;
			}
			break;
		case 4:
			dashAtPlayer();
			shootAtPlayer();
			step = 5;
			break;
		case 5:
			if(!isBusy()){
				setMovementSpeed(50);
				step = 6;
				setSpikeState(false);
				shootAtPlayer();
			}
			break;
		case 6:
			setArmorState(false);
			counter++;
			if(counter > 150){
				step = 0;
			}
			break;
		case 7:
			line.setVisible(false);
			destroyMech();
			World.gameState = GameState.WON;
			step++;
			break;
		default:
			break;
		}
	}
	
	public void phase4(){
		counter2++;
		if(counter2 >= 1500){
			setArmorState(true);
			setPhase(5);
			getHpBar().refill();
			setMaxHP(100);
			player.getHpBar().refill();
			player.setMaxHP(100);
			step = 0;
			projectileLimiter = 0;
			line.setVisible(false);
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable(){
				@Override
				public void run() {
					destroyAllProjectiles();
					projectilePoolSize = 60;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<projectilePoolSize; i++){
						projectilePool.add(new SuperBullet(physicsWorld, scene, 1));
					}
				}
			});
			
		}
		float angle = (float) Math.atan2(player.getYInScreenSpace() - getYInScreenSpace(), player.getXInScreenSpace() - getXInScreenSpace());
		float x = getXInScreenSpace() + (float)(600*Math.cos(angle));
		float y = getYInScreenSpace() + (float)(600*Math.sin(angle));
		switch(step){
		case 0:
			shootAtPlayer();
			line.setPosition(getXInScreenSpace(), getYInScreenSpace(),
					x, y);
			line.setVisible(true);
			line.setLineWidth(32);
			step = 1;
			limiter = ResourcesManager.getInstance().rand.nextInt(60) + 20;
			break;
		case 1:
			shootAtPlayer();
			line.setPosition(getXInScreenSpace(), getYInScreenSpace(),
					x, y);
			counter++;
			if(counter >= limiter){
				step = 2;
			}
			break;
		case 2:
			line.setLineWidth(line.getLineWidth() - 1);
			if(line.getLineWidth() <= 1){
				step = 3;
				counter = 0;
				limiter = 0;
			}
			break;
		case 3:
			counter++;
			if(counter > 40){
				counter = 0;
				step = 0;
			}
			fire2();
			break;
		default:
			break;
		}
	}
	
	public void phase3(){
		if(getCurrentHP() <= 0 && !isInArmorState()){
			setArmorState(true);
			setPhase(4);
			getHpBar().refill();
			setMaxHP(100);
			player.getHpBar().refill();
			player.setMaxHP(100);
			projectileLimiter = 0;
			goTo(0, 0);
			step = 0;
			setMovementSpeed(30);
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable(){
				@Override
				public void run() {
					destroyAllProjectiles();
					projectilePoolSize = 60;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<projectilePoolSize-3; i++){
						projectilePool.add(new RoundBullet(physicsWorld, scene));
					}
					for(int i=0; i<3; i++){
						projectilePool.add(new HomingBullet(physicsWorld, scene, player.getBodySprite()));
					}
					
				}
			});
			return;
			
		}
		switch(step){
		case 0:
			if(!isBusy()){
				setArmorState(false);
				step = 1;
				setMovementSpeed(15);
			}
			break;
		case 1:
			chasePlayer();
			shootAtPlayer();
			break;
		default:
			break;
		}
		
	}
	
	public void phase2(){
		if(getCurrentHP() <= 0 && !isInArmorState()){
			setArmorState(true);
			setPhase(3);
			getHpBar().refill();
			setMaxHP(100);
			player.getHpBar().refill();
			player.setMaxHP(100);
			projectileLimiter = 40;
			goTo(0, 0);
			step = 0;
			setMovementSpeed(30);
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable(){
				@Override
				public void run() {
					destroyAllProjectiles();
					projectilePoolSize = 5;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<projectilePoolSize; i++){
						projectilePool.add(new HomingBullet(physicsWorld, scene, player.getBodySprite()));
					}
				}
			});
			return;
			
		}
		switch(step){
		case 0:
			if(!isBusy()){
				stopMoving();
				step = 1;
				setMovementSpeed(10);
				setArmorState(false);
			}
			break;
		case 1:
			chasePlayer();
			shootAtPlayer();
			break;
		default:
			break;
		}
	}
	
	public void phase0(){
		walkAndShoot();
		if(getCurrentHP() <= 0 && !isInArmorState()){
			setPhase(1);
			getHpBar().refill();
			setMaxHP(100);
			player.getHpBar().refill();
			player.setMaxHP(100);
		}
	}
	
	public void phase1(){
		if(getCurrentHP() <= 0 && !isInArmorState()){
			setPhase(2);
			getHpBar().refill();
			setMaxHP(100);
			player.getHpBar().refill();
			player.setMaxHP(100);
			setArmorState(true);
			goTo(0, 0);
			step = 0;
			projectileLimiter = 120;
			projectileCounter = projectileLimiter-1;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable(){
				@Override
				public void run() {
					destroyAllProjectiles();
					projectilePoolSize = 40;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<projectilePoolSize; i++){
						projectilePool.add(new SuperBullet(physicsWorld, scene, 1));
					}
				}
			});
			
			return;
		}
		float angle = (float) Math.atan2(player.getYInScreenSpace() - getYInScreenSpace(), player.getXInScreenSpace() - getXInScreenSpace());
		float x = getXInScreenSpace() + (float)(600*Math.cos(angle));
		float y = getYInScreenSpace() + (float)(600*Math.sin(angle));
		line.setPosition(getXInScreenSpace(), getYInScreenSpace(),
				x, y);
		switch(step){
		case 0:
			setArmorState(true);
			limiter = ResourcesManager.getInstance().rand.nextInt(100)+30;
			line.setVisible(true);
			line.setLineWidth(32);
			step = 1;
			break;
		case 1:
			chasePlayer();
			counter++;
			if(counter >= limiter){
				counter = 0;
				step = 2;
			}
			break;
		case 2:
			line.setLineWidth(32);
			step = 3;
			break;
		case 3:
			line.setLineWidth(line.getLineWidth() - 0.5f);
			if(line.getLineWidth() <= 1){
				line.setVisible(false);
				step = 4;
			}
			break;
		case 4:
			dashAtPlayer();
			step = 5;
			break;
		case 5:
			if(!isBusy()){
				setMovementSpeed(50);
				step = 6;
				setSpikeState(false);
			}
			break;
		case 6:
			setArmorState(false);
			counter++;
			if(counter > 60){
				step = 0;
			}
			break;
		default:
			break;
		}
	}
	
	public void dashAtPlayer(){
		setMovementSpeed(300);
		setSpikeState(true);
		stopChasing();
		float x = line.getX2();
		float y = line.getY2();
		if(x > 390){
			x = 390;
		}
		if(x < -460){
			x = -460;
		}
		if(y > 420){
			y = 420;
		}
		if(y < -390){
			y = -390;
		}
		goTo(x, y);
	}
	
	public void walkAndShoot(){
		if(!isBusy()){
			float destX = ResourcesManager.getInstance().rand.nextFloat()*850 - 460;
			float destY = ResourcesManager.getInstance().rand.nextFloat()*810 - 390;
			goTo(destX, destY);
		}
		if(limiter <= 0){
			limiter = ResourcesManager.getInstance().rand.nextInt(50) + 10;
			counter = 0;
		}
		if(counter < limiter){
			shootAtPlayer();
		}
		if(counter > limiter * 2){
			limiter = -1;
		}
		counter++;
	}

	@Override
	public void fire() {
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				
				if(getPhase() == 0 || getPhase() == 1 || getPhase() == 3){
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
				}else if(getPhase() == 2){
					
						for(int j = 0; j<40; j++){
						
							float angle = 9*j;
							projectilePool.get(projectilePointer).fire(bodySprite.getX(), bodySprite.getY(),
									angle);
							projectilePointer++;
							if(projectilePointer >= projectilePoolSize){
								projectilePointer = 0;
							}
						}
					
				}else if(getPhase() == 5){
					for(int j = 0; j<30; j++){
						
						float angle = 12*j;
						projectilePool.get(projectilePointer).fire(bodySprite.getX(), bodySprite.getY(),
								angle);
						projectilePointer++;
						if(projectilePointer >= projectilePoolSize){
							projectilePointer = 0;
						}
					}
				}
				
			}
			
		});
		
		
	}
	
	private void fire2(){
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				float randAngle = ResourcesManager.getInstance().rand.nextFloat()*80 - 40;
				float dx = (float)(bodySprite.getWidth()/2 * Math.cos(Math.toRadians(-bodySprite.getRotation() - 180)));
				float dy = (float)(bodySprite.getHeight()/2 * Math.sin(Math.toRadians(-bodySprite.getRotation() - 180)));
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
