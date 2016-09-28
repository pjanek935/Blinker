package player;

import java.util.ArrayList;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import main.ResourcesManager;
import particle.ParticleManager;
import projectiles.Projectile;
import scene.BaseScene;
import ui.HpBar;

public abstract class Mech implements Element{
	
	//General
	protected PhysicsWorld physicsWorld;
	protected BaseScene scene;
	
	//Body
	protected Body body;
	protected TiledSprite bodySprite;
	protected Legs legs;
	protected Sprite shield;
	protected int defaultTileIndex = 0;
	protected int destroyedTileIndex = 4;
	protected boolean destroyed = false;
	private int smokeCounter = 0;
	
	//Projectiles
	protected  ArrayList<Projectile> projectilePool;
	protected int projectileCounter = 0;
	protected int projectileLimiter = 10;
	protected int projectilePoolSize = 10;
	protected int projectilePointer = 0;
	
	//States
	private boolean ghostState = false;
	private boolean armorState = false;
	private int armorStateCounter = 0;
	private boolean airKicked = false;
	private boolean airKickDirection = false;
	private boolean hitState = false;
	private int hitCounter = 0;
	private int hitStateTileIndex = 0;
	private boolean spikeState = false;
	protected int spikeDamage = 20;
	private boolean shieldState = false;
	private int shieldCounter = 0;
	public boolean wallClinged = false;
	protected Rectangle moveDestRect;
	protected boolean move = false;
	protected float movementSpeed = 30;
	
	//HP
	private int maxHP = 100;
	private int currentHP = 100;
	private HpBar hpBar;
	
	public Mech(float x, float y, PhysicsWorld physicsWorld, BaseScene scene, ITiledTextureRegion textureRegion, ITiledTextureRegion legTextureRegion,
			int projectilePoolSize, int hitStateTileIndex, int destroyedTileIndex){
		this.hitStateTileIndex = hitStateTileIndex;
		this.projectilePoolSize = projectilePoolSize;
		bodySprite = new TiledSprite(x, y,
				textureRegion, ResourcesManager.getInstance().vbom);
		shield = new Sprite(0, 0, ResourcesManager.getInstance().shield, ResourcesManager.getInstance().vbom);
		this.physicsWorld = physicsWorld;
		legs = new Legs(x, y, scene, legTextureRegion);
		scene.attachChildAtMiddleground(bodySprite);
		scene.attachChildAtForeground(shield);
		shield.setVisible(false);
		shield.setAlpha(0.5f);
		createPhysics(physicsWorld);
		this.scene = scene;
		prepareProjectiles();
		this.destroyedTileIndex = destroyedTileIndex;
		moveDestRect = new Rectangle(0, 0, 32, 32, ResourcesManager.getInstance().vbom);
		moveDestRect.setColor(Color.RED);
	}
	
	public abstract void fire();
	public abstract void prepareProjectiles();
	
	public void setHpBar(HpBar hpBar){ this.hpBar = hpBar;}
	public HpBar getHpBar(){ return hpBar; }
	public int getCurrentHP(){ return currentHP; }
	public int getMaxHp(){ return maxHP; }
	public Body getBody(){ return body; }
	public TiledSprite getBodySprite(){ return bodySprite; }
	@Override
	public float getXInScreenSpace() { return bodySprite.getX(); }
	@Override
	public float getYInScreenSpace() { return bodySprite.getY(); }
	@Override
	public void setPositionInScreenSpace(float x, float y) {
		body.setTransform(new Vector2(x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT), body.getAngle());
	}
	@Override
	public float getXInWorldSpace() { return body.getPosition().x; }
	@Override
	public float getYInWorldSpace() { return body.getPosition().y; }
	@Override
	public void setPositionInWorldSpace(float x, float y) {
		body.setTransform(new Vector2(x, y), body.getAngle());
	}
	
	public void setMaxHP(int maxHP){
		this.maxHP = maxHP;
		currentHP = maxHP;
	}
	
	public void dealDamage(int damage){
		if(damage > 0){
			hit();
		}
		currentHP -= damage;
		if(currentHP < 0){
			currentHP = 0;
		}
		if(currentHP > maxHP){
			currentHP = maxHP;
		}
		if(hpBar != null){
			hpBar.setHp((float)currentHP/(float)maxHP);
		}
		
	}
	
	private void createPhysics(PhysicsWorld physicsWorld){
		FixtureDef fixDef = PhysicsFactory.createFixtureDef(100, 0.001f, 0.1f);
		
	    body = PhysicsFactory.createBoxBody(physicsWorld, bodySprite, BodyType.DynamicBody, fixDef);
	    body.setFixedRotation(true);
	    body.setUserData(this);
	    physicsWorld.registerPhysicsConnector(new PhysicsConnector(bodySprite, body, true, false){
	        @Override
	        public void onUpdate(float pSecondsElapsed){
	           super.onUpdate(pSecondsElapsed);
	           bodySprite.setX(body.getPosition().x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
	           bodySprite.setY(body.getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
	           legs.setPositionInScreenSpace(bodySprite.getX(), bodySprite.getY());
	           shield.setPosition(bodySprite);
	           
	           if(move){
					float angle = goTo(moveDestRect.getX(), moveDestRect.getY());
					move(angle);
					if(bodySprite.collidesWith(moveDestRect)){
						stopMoving();
					}
				}
	           
	           if(airKicked){
	        	   if(!airKickDirection){
	        		   bodySprite.setScale(bodySprite.getScaleX() + 0.1f);
	        		   if(bodySprite.getScaleX() > 3){
	        			   airKickDirection = true;
	        		   }
	        	   }else{
	        		   bodySprite.setScale(bodySprite.getScaleX() - 0.1f);
	        		   if(bodySprite.getScaleX() < 1){
	        			   bodySprite.setScale(1);
	        			   airKicked = false;
	        			   body.setLinearVelocity(0, 0);
	        			   setGhostState(false);
	        		   }
	        	   }
	           }
	           
	           if(isInArmorState()){
	        	   //TO DO
	           }
	           
	           if(!destroyed){
	        	   if(hitState){
						hitCounter++;
						bodySprite.setCurrentTileIndex(hitStateTileIndex);
						if(hitCounter >= 5){
							bodySprite.setCurrentTileIndex(defaultTileIndex);
							hitState = false;
							hitCounter = 0;
						}
					}
		           
		           if(shieldState){
		        	   shield.setVisible(true);
		        	   shieldCounter++;
		        	   if(shieldCounter > 5){
		        		   shieldCounter = 0;
		        		   shield.setVisible(false);
		        		   shieldState = false;
		        	   }
		           }
	           }else{
	        	   smokeCounter++;
	        	   if(smokeCounter>10){
	        		   smokeCounter=0;
	        		   ParticleManager.getInstance().fireSmoke2(getXInScreenSpace(), getYInScreenSpace());
	        	   }
	        	   
	           }
	           
	        }
	        
	    });
	}
	
	public void move(float declX, float declY){
		if(isAirKicked()){
			return;
		}
		float legsAngle = (float)Math.atan2(declY, declX);
		legsAngle = (float)Math.toDegrees(legsAngle);
		legs.setRotation(legsAngle);
		legs.startAnimation();
		float d = (float)Math.sqrt(Math.pow(declX, 2) 
				+ Math.pow(declY, 2));
		legs.nextAnimationStep(d/30);
		body.setLinearVelocity(new Vector2(declX/10, declY/10));
	}
	
	public void stop(){
		legs.stopAnimation();
		body.setLinearVelocity(0, 0);
	}
	
	public void stopMoving(){
		move = false;
		stop();
	}
	
	public void shoot(float declX, float declY){
		if(isAirKicked()){
			return;
		}
		float bodyAngle = (float)Math.atan2(declY, declX);
		bodyAngle = (float)Math.toDegrees(bodyAngle);
		bodySprite.setRotation(-bodyAngle + 90);
		projectileCounter++;
		if(projectileCounter > projectileLimiter){
			projectileCounter = 0;
			fire();
		}
	}
	
	public void destroyMech(){
		destroyed = true;
		bodySprite.setCurrentTileIndex(destroyedTileIndex);
		ParticleManager.getInstance().boom(getXInScreenSpace(), getYInScreenSpace());
		for(int i= 0; i<20; i++){
			ParticleManager.getInstance().sparkle(getXInScreenSpace(), getYInScreenSpace());
			ParticleManager.getInstance().fireSmoke(getXInScreenSpace(), getYInScreenSpace(), -999);
		}
	}
	
	public void destroyAllProjectiles(){
		for(int i=0; i<projectilePool.size(); i++){
			Projectile p = projectilePool.get(i);
			p.setFired(false);
			p.getSprite().setVisible(false);
			final PhysicsConnector physicsConnector =
					physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(p.getSprite());
			if (physicsConnector != null){
				p.getSprite().setVisible(false);
	             physicsWorld.unregisterPhysicsConnector(physicsConnector);
	             p.getBody().setActive(false);
	             physicsWorld.destroyBody(p.getBody());
	             scene.detachChild(p.getSprite());
	        }
		}
		projectilePool.clear();
	}
	
	public void setVisible(boolean visible){
		if(visible){
			bodySprite.setVisible(true);
			legs.setVisible(true);
		}else{
			bodySprite.setVisible(false);
			legs.setVisible(false);
		}
	}
	
	public boolean isInGhostState(){ return ghostState; }
	public void setGhostState(boolean ghostState){ this.ghostState = ghostState; }
	public boolean isInArmorState(){ return armorState; }
	public void setArmorState(boolean armorState){
		this.armorState = armorState;
		if(!armorState){
			bodySprite.setCurrentTileIndex(defaultTileIndex);
		}
	}
	
	public boolean isInSpikeState(){ return spikeState; }
	public void setSpikeState(boolean spikeState){ this.spikeState = spikeState; }
	public int getSpikeDamage(){ return spikeDamage; }
	public boolean isAirKicked(){ return airKicked; }
	public void setAirKicked(boolean airKicked){ this.airKicked = airKicked; }
	
	public void airKick(){
		setAirKicked(true);
		airKickDirection = false;
		setGhostState(true);
		body.setLinearVelocity(ResourcesManager.getInstance().rand.nextFloat()*20 - 10, ResourcesManager.getInstance().rand.nextFloat()*20 - 10);
	}
	
	public void hit(){
		hitCounter = 0;
		hitState = true;
	}
	public boolean isInHitState(){
		return hitState;
	}
	
	public void showShield(float bulletX, float bulletY){
		float angle = (float) Math.atan2(bulletY - getYInScreenSpace(), bulletX - getXInScreenSpace());
		angle = (float) Math.toDegrees(angle);
		shield.setRotation(-angle + 90);
		shieldState = true;
		shieldCounter = 0;
	}
	
	public float goTo(float x, float y){ //in pixels
		move = true;
		moveDestRect.setPosition(x, y);
		float angle = (float)Math.atan2(y - bodySprite.getY(), x - bodySprite.getX());
		angle = (float)Math.toDegrees(angle);
		return angle;
	}
	
	public void move(float angle){ //in degrees
		if(isAirKicked()){
			return;
		}
		legs.setRotation(angle);
		legs.startAnimation();
		float speedX = movementSpeed*(float)Math.cos(Math.toRadians(angle));
		float speedY = movementSpeed*(float)Math.sin(Math.toRadians(angle));
		legs.nextAnimationStep(movementSpeed/30);
		body.setLinearVelocity(new Vector2(speedX/10, speedY/10));
	}
	
}
