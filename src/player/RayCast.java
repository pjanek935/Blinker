package player;

import org.andengine.entity.primitive.Line;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import main.BaseScene;
import main.ResourcesManager;

public class RayCast {

	private Line rayCastLine;
	private float length;
	private float rayCastAngle = 0f;
	private RayCastCallback rayCastCallback;
	private float rayFraction = 0;
	
	public RayCast(float x1, float y1, float angle, float length, BaseScene scene, PhysicsWorld physicsWorld, final Fixture parentFixture){
		
		rayCastAngle = angle;
		this.length = length;
		
		float x2 = x1 + (float)Math.sin(Math.toRadians(angle))*length;
		float y2 = y1 + (float)Math.cos(Math.toRadians(angle))*length;
		
		rayCastLine = new Line(x1, y1, x2, y2, ResourcesManager.getInstance().vbom);
		rayCastLine.setColor(0, 1, 0);
		rayCastLine.setLineWidth(2);
		scene.attachChild(rayCastLine);
		
		
		rayCastCallback = new RayCastCallback(){
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if(fixture == parentFixture){
					rayFraction = 0;
					return 0;
				}
				rayCastLine.setPosition(rayCastLine.getX1(), rayCastLine.getY1(),
						point.x*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, point.y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
				rayFraction = fraction;
				return fraction;
			}
		};
		
		physicsWorld.rayCast(rayCastCallback, new Vector2(x1/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, y1/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),
				new Vector2(x2/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, y2/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		
		rayCastLine.setVisible(false);
	}
	
	public void setColor(float r, float g, float b){
		rayCastLine.setColor(r, g, b);
	}
	
	public void move(float newX, float newY){
		float x2 = newX + (float)Math.sin(Math.toRadians(rayCastAngle))*length;
		float y2 = newY + (float)Math.cos(Math.toRadians(rayCastAngle))*length;
		rayCastLine.setPosition(newX, newY, x2, y2);
	}
	
	public void move2(float x1, float y1, float x2, float y2){
		rayCastLine.setPosition(x1, y1, x2, y2);
	}
	
	public void rayCast(PhysicsWorld physicsWorld){
		physicsWorld.rayCast(rayCastCallback, new Vector2(rayCastLine.getX1()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				rayCastLine.getY1()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),
				new Vector2(rayCastLine.getX2()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
						rayCastLine.getY2()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
	}
	
	public RayCastCallback getRayCastCallback(){
		return rayCastCallback;
	}
	
	public void setAngle(float angle){
		rayCastAngle = angle;
	}
	
	public void setVisible(boolean visib){
		rayCastLine.setVisible(visib);
	}
	
	public float getFraction(){
		return rayFraction;
	}
}
