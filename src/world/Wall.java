package world;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import main.ResourcesManager;
import scene.BaseScene;

public class Wall {

	private Body body;
	private Rectangle rect;
	
	public Wall(PhysicsWorld physicsWorld, BaseScene scene, float x, float y, float width, float height){
		rect = new Rectangle(x, y, width, height, ResourcesManager.getInstance().vbom);
		createPhysics(physicsWorld);
		
	}
	
	public Wall(PhysicsWorld physicsWorld, BaseScene scene, float x, float y, float width, float height, float rotation){
		rect = new Rectangle(x, y, width, height, ResourcesManager.getInstance().vbom);
		rect.setRotation(rotation);
		createPhysics(physicsWorld);
		
	}
	
	private void createPhysics(PhysicsWorld physicsWorld){
	    body = PhysicsFactory.createBoxBody(physicsWorld, rect, BodyType.StaticBody,
	    		PhysicsFactory.createFixtureDef(1, 0f, 3f));
	    body.setUserData(this);
	    /*physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect, body, true, true){
	        @Override
	        public void onUpdate(float pSecondsElapsed){
	           super.onUpdate(pSecondsElapsed);
	           sprite.setX(body.getPosition().x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
	           sprite.setY(body.getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
	           sprite.setRotation(-1 * (float)Math.toDegrees(body.getAngle()));
	        }
	    });*/
	}
}
