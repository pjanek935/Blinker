package player;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import android.provider.Telephony.Mms.Part;
import android.util.Log;
import main.BaseScene;
import main.ResourcesManager;
import particle.ParticleManager;
import projectiles.BigBullet;
import projectiles.Projectile;
import projectiles.RoundBullet;
import projectiles.SuperBullet;
import world.World;
import world.World.GameState;

public class Mole extends Enemy {
	
	private Sprite moundEnter;
	private Sprite moundExit;
	private Sprite deathCircle;
	
	private float phase2Angle = -60;

	public Mole(float x, float y, PhysicsWorld physicsWorld, BaseScene scene, Player player) {
		super(x, y, physicsWorld, scene, ResourcesManager.getInstance().mole_body,
				ResourcesManager.getInstance().mole_leg, player, 10, 2);
		this.projectileLimiter = 10;
		moundEnter = new Sprite(0, 0, ResourcesManager.getInstance().mound, ResourcesManager.getInstance().vbom);
		moundExit = new Sprite(0, 0, ResourcesManager.getInstance().mound, ResourcesManager.getInstance().vbom);
		deathCircle = new Sprite(0, 0, ResourcesManager.getInstance().death_circle, ResourcesManager.getInstance().vbom);
		scene.attachChildAtForeground(deathCircle);
		scene.attachChildAtBackground2(moundEnter);
		scene.attachChildAtBackground2(moundExit);
		moundExit.setVisible(false);
		moundEnter.setVisible(false);
		deathCircle.setVisible(false);
		deathCircle.setAlpha(0.2f);
	}

	@Override
	public void ai() {
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
				break;
			default:
				break;
		}
		if(moundEnter.isVisible()){
			moundEnter.setAlpha(moundEnter.getAlpha() - 0.001f);
			if(moundEnter.getAlpha() <= 0){
				moundEnter.setVisible(false);
				moundEnter.setAlpha(1);
			}
		}
		if(moundExit.isVisible()){
			moundExit.setAlpha(moundExit.getAlpha() - 0.001f);
			if(moundExit.getAlpha() <= 0){
				moundExit.setVisible(false);
				moundExit.setAlpha(1);
			}
		}
	}
	
	public void phase0(){
		switch(step){
		case 0:
			walkAndShoot();
			break;
		case 1:
			waitForJumpOut();
			break;
		case 2:
			jumpingOut();
			break;
		case 3:
			jumpOut();
			break;
		case 4:
			goToPhase1();
			break;
		}
		if(getCurrentHP() <= 0 && !isInArmorState()){
			setArmorState(true);
			goTo(700, 0);
			step = 4;
			setMovementSpeed(60);
		}
	}
	
	public void phase1(){
		switch (step) {
		case 0:
			shootAtPlayer();
			break;
		case 1:
			goToPhase2();
			break;
		default:
			break;
		}
		if(getCurrentHP() <= 0 && !isInArmorState()){
			setArmorState(true);
			goTo(300, 0);
			step = 1;
			setMovementSpeed(60);
		}
	}
	
	public void phase2(){
		switch (step) {
		case 0:
			float x = (float)Math.cos(Math.toRadians(phase2Angle));
			float y = (float)Math.sin(Math.toRadians(phase2Angle));
			shoot(x, y);
			phase2Angle += 3;
			break;
		case 1:
			goToPhase3();
			break;
		default:
			break;
		}
		if(getCurrentHP() <= 0 && !isInArmorState()){
			setArmorState(true);
			goTo(0, 0);
			step = 1;
			setMovementSpeed(60);
		}
	}
	
	public void phase3(){
		if(getCurrentHP() <= 0){
			step = 10;
		}
		switch(step){
		case 0:
			walkAndShoot();
			break;
		case 1:
			waitForJumpOut();
			break;
		case 2:
			jumpingOut();
			break;
		case 3:
			jumpOut();
			break;
		case 4:
			goToPhase1();
			break;
		case 10:
			destroyMech();
			step++;
			World.gameState = GameState.WON;
			break;
		case 11:
			break;
		default:
			break;
		}
	}
	
	public void goToPhase1(){
		if(!isBusy()){
			getHpBar().refill();
			player.getHpBar().refill();
			player.setMaxHP(100);
			setArmorState(false);
			setPhase(1);
			step = 0;
			Log.d("mine", "Phase 1");
			setMovementSpeed(30);
			setMaxHP(100);
			projectileLimiter = 90;
			projectileCounter = 80;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable(){
				@Override
				public void run() {
					destroyAllProjectiles();
					projectilePoolSize = 45;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<30; i++){
						projectilePool.add(new BigBullet(physicsWorld, scene, 1));
					}
					for(int i=0; i<15; i++){
						projectilePool.add(new SuperBullet(physicsWorld, scene, 1));
					}
				}
			});
		}
	}
	
	public void goToPhase2(){
		if(!isBusy()){
			getHpBar().refill();
			player.getHpBar().refill();
			player.setMaxHP(100);
			setArmorState(false);
			setPhase(2);
			step = 0;
			Log.d("mine", "Phase 2");
			setMovementSpeed(30);
			setMaxHP(100);
			projectileLimiter = 2;
			phase2Angle = -60;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable(){
				@Override
				public void run() {
					destroyAllProjectiles();
					projectilePoolSize = 30;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<projectilePoolSize; i++){
						projectilePool.add(new SuperBullet(physicsWorld, scene, 1));
						projectilePool.get(i).setSpeed(40);
					}
				}
			});
		}
	}
	
	public void goToPhase3(){
		if(!isBusy()){
			getHpBar().refill();
			player.getHpBar().refill();
			player.setMaxHP(100);
			setArmorState(false);
			setPhase(3);
			step = 0;
			Log.d("mine", "Phase 3");
			setMovementSpeed(80);
			setMaxHP(100);
			projectileLimiter = 10;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable(){
				@Override
				public void run() {
					destroyAllProjectiles();
					projectilePoolSize = 30;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<projectilePoolSize; i++){
						projectilePool.add(new BigBullet(physicsWorld, scene, 1));
						projectilePool.get(i).setSpeed(8);
					}
				}
			});
		}
	}
	
	public void jumpOut(){
		ParticleManager.getInstance().fireDirt(deathCircle.getX(), deathCircle.getY());
		float d = (float)Math.sqrt(Math.pow(player.getBodySprite().getX() - deathCircle.getX(), 2) + 
				Math.pow(player.getBodySprite().getY() - deathCircle.getY(), 2));
		if(d < deathCircle.getHeightScaled()/2){
			player.airKick();
			player.dealDamage(25);
		}
		airKick();
		limiter = 0;
		counter = 0;
		setVisible(true);
		deathCircle.setVisible(false);
		step = 0;
		getBody().setTransform(deathCircle.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				deathCircle.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, getBody().getAngle());
		setGhostState(false);
		moundExit.setPosition(deathCircle);
		moundExit.setVisible(true);
		moundExit.setAlpha(1);
	}
	
	public void jumpingOut(){
		deathCircle.setScale(deathCircle.getScaleX() + 0.18f);
		if(deathCircle.getScaleX() > 6){
			jumpOut();
		}
	}
	
	public void waitForJumpOut(){
		counter++;
		if(counter > limiter){
			prepareJumpOut();
		}
	}
	
	public void walkAndShoot(){
		if(!isBusy()){
			float destX = ResourcesManager.getInstance().rand.nextFloat()*1000 - 200;
			float destY = ResourcesManager.getInstance().rand.nextFloat()*600 - 300;
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
			float dig = ResourcesManager.getInstance().rand.nextFloat();
			if(dig < 0.01){
				dig();
				step = 1;
			}
		}
		if(counter > limiter * 2){
			limiter = -1;
		}
		counter++;
	}
	
	public void dig(){
		setGhostState(true);
		setVisible(false);
		moundEnter.setPosition(bodySprite);
		ParticleManager.getInstance().fireDirt(moundEnter.getX(), moundEnter.getY());
		moundEnter.setVisible(true);
		moundEnter.setAlpha(1);
		counter = 0;
		limiter = ResourcesManager.getInstance().rand.nextInt(180) + 60;
	}
	
	public void prepareJumpOut(){
		deathCircle.setPosition(player.getBodySprite());
		deathCircle.setVisible(true);
		deathCircle.setScale(0);
		limiter = 0;
		counter = 0;
		step = 2;
	}

	@Override
	public void fire() {
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				
				if(getPhase() == 0 || getPhase() == 2){
					float dx = (float)(bodySprite.getWidth()/2 * Math.cos(Math.toRadians(-bodySprite.getRotation() - 180)));
					float dy = (float)(bodySprite.getHeight()/2 * Math.sin(Math.toRadians(-bodySprite.getRotation() - 180)));
					projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
							-bodySprite.getRotation() + 90);
					projectilePointer++;
					if(projectilePointer >= projectilePoolSize){
						projectilePointer = 0;
					}
				}else if(getPhase() == 1){
					
					for(int j = 0; j<15; j++){
						
						float a = -20 + j*2.67f;
						float dx = (float)((a*2) * Math.cos(Math.toRadians(-bodySprite.getRotation() - 180)));
						float dy = (float)((a*2) * Math.sin(Math.toRadians(-bodySprite.getRotation() - 180)));
						
						projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
								-bodySprite.getRotation() + 90 + a);
						projectilePointer++;
						if(projectilePointer >= projectilePoolSize){
							projectilePointer = 0;
						}
					}
					
				}else if(getPhase() == 3){
					float dx = (float)(bodySprite.getWidth()/2 * Math.cos(Math.toRadians(-bodySprite.getRotation() - 180)));
					float dy = (float)(bodySprite.getHeight()/2 * Math.sin(Math.toRadians(-bodySprite.getRotation() - 180)));
					float randAngle = ResourcesManager.getInstance().rand.nextFloat() * 60 - 30;
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

	@Override
	public void prepareProjectiles() {
		projectilePool = new ArrayList<Projectile>();
		for(int i=0; i<projectilePoolSize; i++){
			projectilePool.add(new RoundBullet(physicsWorld, scene));
		}
	}

}
