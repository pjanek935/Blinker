package particle;

import org.andengine.entity.sprite.Sprite;

import com.badlogic.gdx.math.Vector2;

import main.ResourcesManager;

public class SnowParticle extends Sprite implements Particle {

	public float fadeSpeed = 0.02f;
	public float speed = 3;
	public Vector2 linearPosition;
	public float cosTimer = 0;
	
	public SnowParticle(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().snow, ResourcesManager.getInstance().vbom);
		speed = ResourcesManager.getInstance().rand.nextFloat()*1 + 1;
		linearPosition = new Vector2(pX, pY);
		cosTimer = ResourcesManager.getInstance().rand.nextFloat();
	}

	@Override
	public void update() {
		if(getScaleX() > 0.03){
			float dx = (float) (speed*Math.cos(Math.toRadians(-getRotation())));
			float dy = (float) (speed*Math.sin(Math.toRadians(-getRotation())));
			float cosX = (float) (4* Math.cos(cosTimer));
			float cosY = (float) (4*Math.sin(cosTimer));
			linearPosition = linearPosition.sub(dx + cosX, dy + cosY);
			
			cosTimer += 0.001f;
			setPosition(linearPosition.x, linearPosition.y);
			setScale(getScaleX() - fadeSpeed);
		}else{
			double randAngle = ResourcesManager.getInstance().rand.nextDouble()*360;
			double dx = 500*Math.cos(Math.toRadians(randAngle));
			double dy = 450*Math.sin(Math.toRadians(randAngle));
			setPosition((float)dx + 1024/2f, (float)dy + 552/2f);
			setRotation(-(float)randAngle);
			setScale(3);
			fadeSpeed = ResourcesManager.getInstance().rand.nextFloat()/20 + 0.02f;
			linearPosition = new Vector2(this.getX(), this.getY());
			cosTimer = ResourcesManager.getInstance().rand.nextFloat();
		}	
	}
	
}
