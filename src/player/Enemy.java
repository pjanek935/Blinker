package player;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;

import android.provider.Telephony.Mms.Part;
import android.util.Log;
import main.BaseScene;
import main.ResourcesManager;
import particle.ParticleManager;
import world.World;

public abstract class Enemy extends Mech {
	
	private int phase = 0;
	protected int step = 0;
	protected Player player;
	protected boolean start = true;
	protected int startCounter = 0;
	
	private boolean chasePlayer = false;
	
	protected int counter = 0;
	protected int limiter = 0;
	protected int counter2 = 0;
	protected int limiter2 = 0;
	
	public Enemy(float x, float y, PhysicsWorld physicsWorld, BaseScene scene, ITiledTextureRegion bodyTextureRegion,
			ITiledTextureRegion legsTextureRegion, final Player player, int projectilePoolSize, int destroyedTileIndex) {
		super(x, y, physicsWorld, scene, bodyTextureRegion, legsTextureRegion, projectilePoolSize, 1, destroyedTileIndex);
		body.setUserData(this);
		body.getFixtureList().get(0).getFilterData().groupIndex = -8;
		
		//scene.attachChild(destRect);
		this.player = player;
		bodySprite.registerUpdateHandler(new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) {
				
				if(chasePlayer){
					float angle = chasePlayer();
					move(angle);
				}
				if(World.readyToStart && (!destroyed)){
					if(start){
						startCounter++;
						if(startCounter >= 60){
							startCounter = 0;
							start = false;
						}
					}else{
						ai();
					}
					
				}
				
			}
			@Override
			public void reset() {}
		});
		body.setLinearDamping(10);
	}
	
	public boolean isBusy(){
		if(move || chasePlayer || isAirKicked()){
			return true;
		}else{
			return false;
		}
	}
	
	public int getPhase(){ return phase; }
	public void setPhase(int phase){ this.phase = phase; }
	public float getMovementSpeed(){ return movementSpeed; }
	public void setMovementSpeed(float movementSpeed){ this.movementSpeed = movementSpeed; }
	
	public abstract void ai();
	
	@Override
	public float goTo(float x, float y) {
		chasePlayer = false;
		return super.goTo(x, y);
	}
	
	public void shootAtPlayer(){
		if(isAirKicked()){
			return;
		}
		float angle = (float)Math.atan2((player.getYInScreenSpace()+bodySprite.getHeight()/2) - bodySprite.getY(),
				(player.getXInScreenSpace()+bodySprite.getWidth()/2) - bodySprite.getX());
		angle = (float)Math.toDegrees(angle);
		bodySprite.setRotation(-angle + 90);
		projectileCounter++;
		if(projectileCounter > projectileLimiter){
			projectileCounter = 0;
			fire();
		}
	}
	
	public void aimAtPlayer(){
		if(isAirKicked()){
			return;
		}
		float angle = (float)Math.atan2((player.getYInScreenSpace()+bodySprite.getHeight()/2) - bodySprite.getY(),
				(player.getXInScreenSpace()+bodySprite.getWidth()/2) - bodySprite.getX());
		angle = (float)Math.toDegrees(angle);
		bodySprite.setRotation(-angle + 90);
	}
	
	public float chasePlayer(){
		move = false;
		chasePlayer = true;
		moveDestRect.setPosition(player.getXInScreenSpace(), player.getYInScreenSpace());
		float angle = (float)Math.atan2(player.getYInScreenSpace() - bodySprite.getY(),
				player.getXInScreenSpace() - bodySprite.getX());
		angle = (float)Math.toDegrees(angle);
		return angle;
	}
	
	public void stopChasing(){
		chasePlayer = false;
	}
	
	
	
	@Override
	public void setArmorState(boolean armorState) {
		super.setArmorState(armorState);
		if(armorState){
			ParticleManager.getInstance().startEnemyArmor();
		}else{
			ParticleManager.getInstance().stopEnemyArmor();
		}
	}
	
	@Override
	public void destroyMech() {
		super.destroyMech();
		stopChasing();
		stopMoving();
		ParticleManager.getInstance().fireEnemyMechDebris(getXInScreenSpace(), getYInScreenSpace());
	}
	
	

}
