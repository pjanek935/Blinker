package player;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import main.ResourcesManager;
import main.UserData;
import particle.ParticleManager;
import projectiles.Bullet;
import projectiles.Projectile;
import scene.BaseScene;
import ui.BlinkBar;
import ui.MyHUD;
import world.Wall;
import world.World;
import world.World.GameState;

public class Player extends Mech {

	//Blinking stuff
	protected boolean canBlink = true;
	protected boolean blinking = false;
	protected float lastAngle = 0;
	protected Rectangle destRect;
	protected int blinkCounter = 0;
	protected RayCastCallback rayCastCallback;
	protected Mech enemy;
	private BlinkBar blinkBar;
	
	
	public Player(final PhysicsWorld physicsWorld, final BaseScene scene, float x, float y, final MyHUD hud){
		super(y, y, physicsWorld, scene, ResourcesManager.getInstance().player_body, ResourcesManager.getInstance().player_leg, 20, 2, 3);
		destRect = new Rectangle(0, 0, 32, 32, ResourcesManager.getInstance().vbom);
		//scene.attachChild(destRect);
		body.setLinearDamping(2);
		projectileLimiter = 10;
		rayCastCallback = new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if(fixture.getBody().getUserData() instanceof Wall){
					destRect.setPosition(point.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
							point.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
					destRect.setSize(16, 16);
				}
				return 0;
			}
		};
		
		bodySprite.registerUpdateHandler(new IUpdateHandler(){

			@Override
			public void onUpdate(float pSecondsElapsed) {
				//Log.d("mine", "pos: " + getXInScreenSpace() + ", " + getYInScreenSpace());
				
				if(getCurrentHP() <= 0){
					World.gameState = GameState.LOST;
				}
				/*if(getCurrentHP() < 40){
					dealDamage(-1);
				}*/
				
				if(blinking){
					if(isAirKicked()){
						stopBlinking();
					}else if(bodySprite.collidesWith(destRect)){
						stopBlinking();
						//Log.d("mine", "Pos: " + bodySprite.getX() + ", " + bodySprite.getY());
					}else if(body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0){
						stopBlinking();
					}
				}
				
				if(hud.getLeftAnalog().isActive()){
					
					if(!blinking){
						move(hud.getLeftAnalog().getDeclensionX(), hud.getLeftAnalog().getDeclensionY());
					}
				}else{
					
				}
				if(hud.getLeftAnalog().stop){
					stop();
					hud.getLeftAnalog().stop = false;
				}
				
				float bodyAngle = (float)Math.atan2(hud.getRightAnalog().getDeclensionY(), hud.getRightAnalog().getDeclensionX());
				bodyAngle = (float)Math.toDegrees(bodyAngle);
				if(hud.getRightAnalog().isActive()){
					shoot(hud.getRightAnalog().getDeclensionX(), hud.getRightAnalog().getDeclensionY());
				}else if(hud.getDashInput().isActive()){
					bodySprite.setRotation((float)Math.toDegrees(-hud.getDashInput().getAngle())-90);
				}else{
					
				}
				
				
			}

			@Override
			public void reset() {}
			
		});
	}
	
	public void setBlinkBar(BlinkBar blinkBar){
		this.blinkBar = blinkBar;
	}
	
	public void setEnemy(Mech enemy){
		this.enemy = enemy;
	}
	
	@Override
	public void shoot(float declX, float declY) {
		if(isAirKicked()){
			return;
		}
		float optimumAngle = (float) Math.atan2(enemy.getYInScreenSpace() - getYInScreenSpace(), enemy.getXInScreenSpace() - getXInScreenSpace());
		float bodyAngle = (float)Math.atan2(declY, declX);
		if(Math.abs(optimumAngle - bodyAngle) < Math.PI/8f){
			bodyAngle = optimumAngle;
		}
		bodyAngle = (float)Math.toDegrees(bodyAngle);
		bodySprite.setRotation(-bodyAngle + 90);
		projectileCounter++;
		if(projectileCounter > projectileLimiter){
			projectileCounter = 0;
			fire();
		}
	}
	
	@Override
	public void dealDamage(int damage) {
		if(damage > 0){
			UserData.getInstance().dealDamage(damage);
			ResourcesManager.getInstance().vibrator.vibrate(100 * (int)(damage/8f));
		}
		super.dealDamage(damage);
	}
	
	
	public void blink(float angle, float blinkLength){
		if(!canBlink()){
			return;
		}
		boolean canBlink = blinkBar.blink();
		if(!canBlink){
			return;
		}
		blinkCounter++;
		bodySprite.setCurrentTileIndex(1);
		legs.setTileIndex(2);
		float blinkDestX = bodySprite.getX() + (float)((blinkLength+64)*Math.cos(angle));
		float blinkDestY = bodySprite.getY() + (float)((blinkLength+64)*Math.sin(angle));
		destRect.setPosition(blinkDestX, blinkDestY);
		destRect.setSize(32, 32);
		blinking = true;
		setGhostState(true);
		float speedX = (float)(50*Math.cos(angle));
		float speedY = (float)(50*Math.sin(angle));
		body.setLinearVelocity(speedX, speedY);
		
		float rayX = bodySprite.getX() + (float)((blinkLength+50)*Math.cos(angle));
		float rayY = bodySprite.getY() + (float)((blinkLength+50)*Math.sin(angle));
		physicsWorld.rayCast(rayCastCallback, new Vector2(getXInWorldSpace(), getYInWorldSpace()),
				new Vector2(rayX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, rayY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
	}
	
	public void stopBlinking(){
		body.setLinearVelocity(0, 0);
		blinking = false;
		setGhostState(false);
		bodySprite.setCurrentTileIndex(0);
		legs.setTileIndex(0);
	}
	
	public boolean isBlinking(){
		return blinking;
	}

	@Override
	public void fire() {
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
		    @Override
		    public void run() {
		    	float dx = (float)(bodySprite.getWidth()/2*Math.cos(Math.toRadians(-bodySprite.getRotation())));
				float dy = (float)(bodySprite.getHeight()/2*Math.sin(Math.toRadians(-bodySprite.getRotation())));
				Bullet b = (Bullet)projectilePool.get(projectilePointer);
				projectilePool.get(projectilePointer).fire(bodySprite.getX() + dx, bodySprite.getY() + dy,
						-bodySprite.getRotation() + 90);
				projectilePointer++;
				if(projectilePointer >= projectilePoolSize){
					projectilePointer = 0;
				}
				ParticleManager.getInstance().fireSmoke(bodySprite.getX() + dx, bodySprite.getY() + dy, bodySprite.getRotation() - 90);
				
		    }
		});
	}

	@Override
	public void prepareProjectiles() {
		projectilePool = new ArrayList<Projectile>();
		for(int i=0; i<projectilePoolSize; i++){
			projectilePool.add(new Bullet(physicsWorld, scene));
		}
	}
	
	
	public int getBlinkCounter(){
		return blinkCounter;
	}
	
	public boolean canBlink(){
		return canBlink;
	}
	
	public void setCanBlink(boolean canBlink){
		this.canBlink = canBlink;
	}
	
	public boolean isBusy(){
		if(move || isAirKicked()){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void destroyMech() {
		super.destroyMech();
		ParticleManager.getInstance().firePlayerMechDebris(getXInScreenSpace(), getYInScreenSpace());
	}
	
	@Override
	public void setArmorState(boolean armorState) {
		super.setArmorState(armorState);
		if(armorState){
			ParticleManager.getInstance().startPlayerArmor();
		}else{
			ParticleManager.getInstance().stopPlayerArmor();
		}
	}
	
	

}
