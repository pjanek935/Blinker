package player;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import main.ResourcesManager;
import particle.ParticleManager;
import projectiles.BouncingBullet;
import projectiles.DestroyableBouncingBullet;
import projectiles.FollowingBullet;
import projectiles.Projectile;
import projectiles.RoundBullet;
import scene.BaseScene;
import world.World;
import world.World.GameState;

public class Witch extends Enemy{
	
	class Glyph extends Sprite{
		public int glyphCounter = 0;
		public Glyph() {
			super(0, 0, ResourcesManager.getInstance().glyph, ResourcesManager.getInstance().vbom);
			setVisible(false);
			scene.attachChildAtBackground2(this);
			this.registerUpdateHandler(new IUpdateHandler() {
				@Override
				public void reset() {}
				@Override
				public void onUpdate(float pSecondsElapsed) {
					if(isVisible()){
						if(getAlpha() < 0.99){
							setAlpha(getAlpha() + 0.01f);
						}else{
							setAlpha(0.999f);
						}
						if(collidesWith(player.bodySprite) && getAlpha() >= 0.99f && player.blinking == false){
							player.airKick();
							player.dealDamage(40);
							setVisible(false);
							glyphCounter = 0;
							ParticleManager.getInstance().shootGlyphs(getX(), getY());
							return;
						}
						glyphCounter++;
						if(glyphCounter > 300){
							glyphCounter = 0;
							setVisible(false);
							ParticleManager.getInstance().shootGlyphs(getX(), getY());
						}
					}
				}
			});
		}
	}
	
	private ArrayList<Glyph> glyphs;
	private int timer = 0;
	private int timer2 = 0;

	public Witch(float x, float y, PhysicsWorld physicsWorld, BaseScene scene, final Player player) {
		super(x, y, physicsWorld, scene, ResourcesManager.getInstance().witch_body, ResourcesManager.getInstance().witch_leg,
				player, 20, 2);
		glyphs = new ArrayList<Glyph>();
		glyphs.add(new Glyph());
		
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
			step = 2;
		}
		switch (step) {
		case 0:
			if(!isBusy()){
				shootAtPlayer();
				setMovementSpeed(30);
				timer++;
				if(timer > 200){
					step = 1;
					setArmorState(false);
				}
			}
			break;
		case 1:
			walkAndShoot3();
			break;
		case 2:
			destroyMech();
			World.gameState = GameState.WON;
			step++;
			break;
		default:
			break;
		}
	}
	
	public void phase4(){
		if(getCurrentHP() <= 0){
			setArmorState(true);
			goTo(0, 0);
			step = 0;
			setPhase(5);
			setMovementSpeed(30);
			getHpBar().refill();
			setMaxHP(100);
			player.getHpBar().refill();
			player.setMaxHP(100);
			projectileLimiter = 15;
			projectileCounter = projectileLimiter-2;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable(){
				@Override
				public void run() {
					destroyAllProjectiles();
					projectilePoolSize = 15;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<30; i++){
						if(i%2 == 0){
							projectilePool.add(new BouncingBullet(physicsWorld, scene, 0.6f));
						}else{
							projectilePool.add(new DestroyableBouncingBullet(physicsWorld, scene, 0.6f));
						}
					}
					for(Glyph g : glyphs){
						g.setVisible(false);
						g.detachSelf();
					}
					glyphs.clear();
					for(int i=0; i<6; i++){
						glyphs.add(new Glyph());
					}
				}
			});

		}
		switch(step){
		case 0:
			if(!isBusy()){
				setArmorState(false);
				setMovementSpeed(20);
				step = 1;
				timer = 120;
			}
			break;
		case 1:
			chasePlayer();
			shootAtPlayer();
			timer++;
			if(timer == 200){
				for(int y=0; y<8; y++){
					for(int x=0; x<4; x++){
						int i = y*4+x;
						Glyph g = glyphs.get(i);
						g.setPosition(-300 + x*256, -350 + y*256);
						g.setAlpha(0);
						g.setVisible(true);
						g.glyphCounter = 0;
					}
				}
			}
			if(timer >= 400){
				for(int y=0; y<8; y++){
					for(int x=0; x<4; x++){
						int i = y*4+x;
						Glyph g = glyphs.get(i);
						g.setPosition(-300 + 128 + x*256, -350 + 128 + y*256);
						g.setAlpha(0);
						g.setVisible(true);
						g.glyphCounter = 0;
					}
				}
				timer = 0;
			}
			break;
		default:
			break;
		
		}
	}
	
	public void phase3(){
		switch (step) {
		case 0:
			if(!isBusy()){
				step = 1;
			}
			break;
		case 1:
			for(Projectile p : projectilePool){
				p.getBody().setLinearVelocity(0, 0);
			}
			step = 2;
			timer = 0;
			break;
		case 2:
			timer++;
			if(timer > 30){
				timer = 0;
				step = 3;
			}
			break;
		case 3:
			for(Projectile p : projectilePool){
				float angle = (float) Math.atan2(getYInScreenSpace() - p.getYInScreenSpace(),
						getXInScreenSpace() - p.getXInScreenSpace());
				float dx = (float) (10*Math.cos(angle));
				float dy = (float) (10*Math.sin(angle));
				p.getBody().setLinearVelocity(dx, dy);
			}
			step = 4;
			break;
		case 4:
			timer++;
			if(timer > 60){
				step = 5;
				timer = 0;
			}
			break;
		case 5:
			for(Projectile p : projectilePool){
				p.getBody().setLinearVelocity(0, 0);
			}
			step = 6;
			timer = 0;
			break;
		case 6:
			timer ++;
			if(timer > 60){
				timer = 0;
				step = 7;
			}
			break;
		case 7:
			for(Projectile p : projectilePool){
				float angle = (float) Math.atan2(player.getYInScreenSpace() - p.getYInScreenSpace(),
						player.getXInScreenSpace() - p.getXInScreenSpace());
				float dx = (float) (15*Math.cos(angle));
				float dy = (float) (15*Math.sin(angle));
				p.getBody().setLinearVelocity(dx, dy);
			}
			step = 8;
			break;
		case 8:
			timer2++;
			shootAtPlayer();
			if(timer2 == 120){
				step = 5;
			}
			if(timer2 > 240){
				step = 9;
			}
			break;
		case 9:
			for(Projectile p : projectilePool){
				float angle = (float) Math.atan2(player.getYInScreenSpace() - p.getYInScreenSpace(),
						player.getXInScreenSpace() - p.getXInScreenSpace());
				float dx = (float) (15*Math.cos(angle));
				float dy = (float) (15*Math.sin(angle));
				p.getBody().setLinearVelocity(dx, dy);
				if(p instanceof DestroyableBouncingBullet){
					DestroyableBouncingBullet dbb = (DestroyableBouncingBullet)p;
					dbb.bouncing = false;
				}else if(p instanceof BouncingBullet){
					BouncingBullet bb = (BouncingBullet)p;
					bb.bouncing = false;
				}
			}
			step = 10;
			timer = 0;
			break;
		case 10:
			timer++;
			if(timer > 200){
				timer = 0;
				step = 11;
			}
			break;
		case 11:
			setArmorState(true);
			goTo(0, 0);
			step = 0;
			setPhase(4);
			setMovementSpeed(30);
			getHpBar().refill();
			setMaxHP(100);
			player.getHpBar().refill();
			player.setMaxHP(100);
			final Mech owner = this;
			projectileLimiter = 300;
			projectileCounter = projectileLimiter-2;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable(){
				@Override
				public void run() {
					destroyAllProjectiles();
					projectilePoolSize = 15;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<15; i++){
						projectilePool.add(new FollowingBullet(physicsWorld, scene, owner, player.getBodySprite(), true, 20));
					}
					for(int i=0; i<32; i++){
						glyphs.add(new Glyph());
					}
				}
			});
			break;
		default:
			break;
		}
	}
	
	public void phase2(){
		if(getCurrentHP() <= 0){
			setArmorState(true);
			goTo(0, 0);
			setPhase(3);
			step = 0;
		}
		switch (step) {
		case 0:
			if(!isBusy()){
				shootAtPlayer();
				setMovementSpeed(30);
				timer++;
				if(timer > 200){
					step = 1;
					setArmorState(false);
				}
			}
			break;
		case 1:
			walkAndShoot2();
			break;
		default:
			break;
		}
		
	}
	
	public void phase1(){
		if(getCurrentHP() <= 0 && !isInArmorState()){
			setArmorState(true);
			goTo(0, 100);
			step = 0;
			setPhase(2);
			setMovementSpeed(60);
			getHpBar().refill();
			setMaxHP(100);
			player.getHpBar().refill();
			player.setMaxHP(100);
			projectileLimiter = 35;
			projectileCounter = projectileLimiter-2;
			timer = 0;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable(){
				@Override
				public void run() {
					destroyAllProjectiles();
					
					projectilePoolSize = 30;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<projectilePoolSize; i++){
						if(i%2 == 0){
							projectilePool.add(new BouncingBullet(physicsWorld, scene, 0.6f));
						}else{
							projectilePool.add(new DestroyableBouncingBullet(physicsWorld, scene, 0.6f));
						}
						
					}
					for(Glyph g : glyphs){
						g.setVisible(false);
						g.detachSelf();
					}
					glyphs.clear();
					
				}
			});
			return;
		}
		
		switch(step){
		case 0:
			if(!isBusy()){
				setArmorState(false);
				setMovementSpeed(20);
				step = 1;
				timer = 120;
			}
			break;
		case 1:
			chasePlayer();
			shootAtPlayer();
			timer++;
			if(timer == 200){
				for(int y=0; y<8; y++){
					for(int x=0; x<4; x++){
						int i = y*4+x;
						Glyph g = glyphs.get(i);
						g.setPosition(-300 + x*256, -350 + y*256);
						g.setAlpha(0);
						g.setVisible(true);
						g.glyphCounter = 0;
					}
					
				}
			}
			if(timer >= 400){
				for(int y=0; y<8; y++){
					for(int x=0; x<4; x++){
						int i = y*4+x;
						Glyph g = glyphs.get(i);
						g.setPosition(-300 + 128 + x*256, -350 + 128 + y*256);
						g.setAlpha(0);
						g.setVisible(true);
						g.glyphCounter = 0;
					}
				}
				timer = 0;
			}
			break;
		default:
			break;
		
		}
	}
	
	public void phase0(){
		if(getCurrentHP() <= 0 && !isInArmorState()){
			setArmorState(true);
			goTo(0, 0);
			step = 0;
			setPhase(1);
			setMovementSpeed(60);
			getHpBar().refill();
			setMaxHP(100);
			player.getHpBar().refill();
			player.setMaxHP(100);
			final Mech owner = this;
			projectileLimiter = 200;
			projectileCounter = projectileLimiter-2;
			ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable(){
				@Override
				public void run() {
					destroyAllProjectiles();
					projectilePoolSize = 60;
					projectilePointer = 0;
					projectilePool = new ArrayList<Projectile>();
					for(int i=0; i<30; i++){
						projectilePool.add(new FollowingBullet(physicsWorld, scene, owner, player.getBodySprite(), false, 20));
					}
					for(int i=0; i<30; i++){
						projectilePool.add(new FollowingBullet(physicsWorld, scene, owner, player.getBodySprite(), false, 17));
					}
					for(int i=0; i<32; i++){
						glyphs.add(new Glyph());
					}
				}
			});
			return;
		}
		walkAndShoot();
	}
	
	public void walkAndShoot(){
		if(!isBusy() || wallClinged){
			float destX = ResourcesManager.getInstance().rand.nextFloat()*800 - 400;
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
		if(counter >= limiter){
			if(!glyphs.get(0).isVisible()){
				float rand = ResourcesManager.getInstance().rand.nextFloat();
				if(rand < 0.1f){
					glyphs.get(0).setPosition(player.getBodySprite());
					glyphs.get(0).setAlpha(0);
					glyphs.get(0).setVisible(true);
				}
			}
			
		}
		if(counter > limiter * 2){
			limiter = -1;
		}
		counter++;
	}
	
	public void walkAndShoot2(){
		if(!isBusy() || wallClinged){
			float destX = ResourcesManager.getInstance().rand.nextFloat()*800 - 400;
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
		if(counter >= limiter){
			
		}
		if(counter > limiter * 1.5f){
			limiter = -1;
		}
		counter++;
	}
	
	public void walkAndShoot3(){
		if(!isBusy() || wallClinged){
			float destX = ResourcesManager.getInstance().rand.nextFloat()*800 - 400;
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
		if(counter >= limiter){
			if(!glyphs.get(0).isVisible()){
				float rand = ResourcesManager.getInstance().rand.nextFloat();
				if(rand < 0.1f){
					for(int i=0; i<6; i++){
						float angle = (float)Math.toRadians(60*i);
						float dx = (float) (128*Math.cos(angle));
						float dy = (float) (128*Math.sin(angle));
						glyphs.get(i).setPosition(player.getXInScreenSpace() + dx, player.getYInScreenSpace() + dy);
						glyphs.get(i).setAlpha(0);
						glyphs.get(i).setVisible(true);
					}
				}
			}
		}
		if(counter > limiter * 2f){
			limiter = -1;
		}
		counter++;
	}

	@Override
	public void fire() {
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				if(getPhase() == 0){
					float dx = (float)(bodySprite.getWidth()/2 * Math.cos(Math.toRadians(-bodySprite.getRotation())));
					float dy = (float)(bodySprite.getHeight()/2 * Math.sin(Math.toRadians(-bodySprite.getRotation())));
					projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
							-bodySprite.getRotation() + 90);
					projectilePointer++;
					if(projectilePointer >= projectilePoolSize){
						projectilePointer = 0;
					}
				}else if(getPhase() == 1){
					for(int j = 0; j<projectilePool.size(); j++){
						float angle = 360/(float)projectilePool.size()*j*2;
						projectilePool.get(projectilePointer).fire(bodySprite.getX(), bodySprite.getY(),
								angle);
						projectilePointer++;
						if(projectilePointer >= projectilePoolSize){
							projectilePointer = 0;
						}
					}
				}else if(getPhase() == 4){
					for(int j = 0; j<projectilePool.size(); j++){
						float angle = 360/(float)projectilePool.size()*j;
						projectilePool.get(projectilePointer).fire(bodySprite.getX(), bodySprite.getY(),
								angle);
						projectilePointer++;
						if(projectilePointer >= projectilePoolSize){
							projectilePointer = 0;
						}
					}
				}
				else if(getPhase() == 2 || getPhase() == 3 || getPhase() == 5){
					float dx = (float)(bodySprite.getWidth()/2 * Math.cos(Math.toRadians(-bodySprite.getRotation())));
					float dy = (float)(bodySprite.getHeight()/2 * Math.sin(Math.toRadians(-bodySprite.getRotation())));
					float randAngle = ResourcesManager.getInstance().rand.nextFloat()*30 - 15;
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
