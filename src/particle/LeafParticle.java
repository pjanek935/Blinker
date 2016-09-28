package particle;

import java.util.ArrayList;

import org.andengine.entity.sprite.TiledSprite;

import com.badlogic.gdx.math.Vector2;

import main.ResourcesManager;

public class LeafParticle extends TiledSprite implements Particle{
	
	public static ArrayList<Vector2> startPoints;
	
	private float maxSpeed = 1f;
	private float currentSpeed = 0;
	private float rotationSpeed = 0;
	private float angle = -90;
	private float maxScale = 1;
	private float maxRadius = 4;
	private float currentRadius = 0;
	private float radiusChangeSpeed = 0.005f;
	private float radiusCounter = 0;
	private int step = 0;
	private int timer = 0;
	private boolean newPositionSet = false;

	public LeafParticle(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().leafs, ResourcesManager.getInstance().vbom);
		setCurrentTileIndex(ResourcesManager.getInstance().rand.nextInt(10));
		
		maxSpeed = ResourcesManager.getInstance().rand.nextFloat()*1+2;
		angle = -90 + ResourcesManager.getInstance().rand.nextFloat()*40 - 20 - 30;
		angle = (float)Math.toRadians(angle);
		rotationSpeed = ResourcesManager.getInstance().rand.nextFloat()*2 - 1;
		maxScale = ResourcesManager.getInstance().rand.nextFloat()*1.5f + 1;
		radiusChangeSpeed = ResourcesManager.getInstance().rand.nextFloat()/200 + 0.003f;
		step = 3;
	}

	@Override
	public void update() {
		float dx = (float) (currentSpeed*Math.cos(angle));
		float dy = (float) (currentSpeed*Math.sin(angle));
		float dx2 = (float) (currentRadius * Math.cos(radiusCounter));
		float dy2 = (float) (currentRadius * Math.sin(radiusCounter));
		if(getX() < -419 || getX() > 583 || getY() < -473 || getY() > 445){
			if(!newPositionSet){
				if(getAlpha() > 0.01f){
					setAlpha(getAlpha() - 0.01f);
				}else{
					float newX = ResourcesManager.getInstance().rand.nextFloat()*800 - 400 + 200;
					float newY = ResourcesManager.getInstance().rand.nextFloat()*200 + 300;
					setPosition(newX, newY);
					newPositionSet = true;
				}
			}
		}
		if(newPositionSet){
			setAlpha(getAlpha() + 0.01f);
			if(getAlpha() >= 1){
				newPositionSet = false;
			}
		}
		switch(step){
		case 0:
			setPosition(getX() + dx, getY() + dy);
			setRotation(getRotation() + rotationSpeed);
			if(getScaleX() <= maxScale){
				setScale(getScaleX() + 0.01f);
			}
			if(currentSpeed < maxSpeed){
				currentSpeed += 0.05f;
			}
			timer ++;
			if(timer  > 60){
				step = 1;
				timer = 0;
			}
			break;
		case 1:
			setPosition(getX() + dx, getY() + dy);
			setRotation(getRotation() + rotationSpeed);
			if(currentRadius < maxRadius){
				currentRadius += radiusChangeSpeed;
			}
			radiusCounter += 0.1f;
			setPosition(getX() + dx2, getY() + dy2);
			if(getScaleX() <= maxScale){
				setScale(getScaleX() + 0.01f);
			}
			if(currentSpeed < maxSpeed){
				currentSpeed += 0.05f;
			}
			timer ++;
			if(timer  > 120){
				step = 2;
				timer = 0;
			}
			break;
		case 2:
			setPosition(getX() + dx, getY() + dy);
			setRotation(getRotation() + rotationSpeed);
			if(getScaleX() > 1){
				setScale(getScaleX() - 0.01f);
			}
			if(currentRadius > 0){
				currentRadius -= radiusChangeSpeed;
			}
			radiusCounter += 0.1f;
			setPosition(getX() + dx2, getY() + dy2);
			if(currentSpeed > 0){
				currentSpeed -= 0.02f;
			}
			timer ++;
			if(timer > 120){
				timer = 0;
				step = 3;
			}
			break;
		case 3:
			timer ++;
			if(timer > 360){
				step = 0;
				timer = 0;
			}
			break;
		default:
			break;
		}
		
	}

}
