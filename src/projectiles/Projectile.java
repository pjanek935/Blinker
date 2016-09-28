package projectiles;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.FixtureDef;

import main.BaseScene;
import main.ResourcesManager;
import player.Element;

public abstract class Projectile implements Element {
	
	protected Sprite sprite;
	protected Body body;
	protected int damage = 5;
	protected float speed = 20;
	private boolean fired = false;
	private float elasticity = 1f;
	
	public Projectile(ITextureRegion textureRegion, PhysicsWorld physicsWorld, BaseScene scene, float hitboxScale, float elasticity){
		sprite = new Sprite(0, 0, textureRegion, ResourcesManager.getInstance().vbom);
		sprite.setRotation(0);
		sprite.setCullingEnabled(true);
		sprite.setScale(hitboxScale);
		this.elasticity = elasticity;
		createPhysics(physicsWorld);
		scene.attachChildAtMiddleground(sprite);
		sprite.setVisible(false);
		sprite.setScale(1);
	}
	
	public void setFired(boolean fired){ this.fired = fired; }
	public boolean isFired(){ return fired; }
	
	private void createPhysics(PhysicsWorld physicsWorld){
		FixtureDef fixDef = PhysicsFactory.createFixtureDef(100, elasticity, 0.1f);
	    body = PhysicsFactory.createCircleBody(physicsWorld, sprite, BodyType.DynamicBody, fixDef);
	    body.setFixedRotation(true);
	    body.setUserData(this);
	    physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, false){
	        @Override
	        public void onUpdate(float pSecondsElapsed){
	           super.onUpdate(pSecondsElapsed);
	           sprite.setX(body.getPosition().x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
	           sprite.setY(body.getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
	           sprite.setRotation(-1 * (float)Math.toDegrees(body.getAngle()));
	        }
	    });
	    body.setActive(false);
	}
	
	public void destroy(Fixture whatDestroyed){
		setFired(false);
		sprite.setVisible(false);
		body.setLinearVelocity(0, 0);
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				body.setActive(false);
				
			}
		});
		
		
	}
	
	public void fire(final float x, final float y, final float angle){
		body.setActive(true);
		body.setTransform(x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (float)Math.toRadians(angle));
		body.setLinearVelocity(speed*(float)Math.cos(body.getAngle()), speed*(float)Math.sin(body.getAngle()));
		sprite.setVisible(true);
		setFired(true);
	}
	
	public int getDamage(){
		return damage;
	}

	@Override
	public float getXInScreenSpace() {
		return sprite.getX();
	}

	@Override
	public float getYInScreenSpace() {
		return sprite.getY();
	}

	@Override
	public void setPositionInScreenSpace(float x, float y) {
		sprite.setX(x);
		sprite.setY(y);
		body.setTransform(sprite.getX() * 1/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				sprite.getY() * 1/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, body.getAngle());
		
	}

	@Override
	public float getXInWorldSpace() {
		return body.getPosition().x;
	}

	@Override
	public float getYInWorldSpace() {
		return body.getPosition().y;
	}

	@Override
	public void setPositionInWorldSpace(float x, float y) {
		body.setTransform(x, y, body.getAngle());
	}
	
	public Sprite getSprite(){
		return sprite;
	}
	
	public Body getBody(){
		return body;
	}
	
	public void setSpeed(float speed){
		this.speed = speed;
	}

}
