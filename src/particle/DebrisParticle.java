package particle;

import org.andengine.entity.sprite.Sprite;

import main.ResourcesManager;

public class DebrisParticle extends Sprite implements Particle {

	private float speed = 4;
	
	public DebrisParticle() {
		super(0, 0, ResourcesManager.getInstance().debris, ResourcesManager.getInstance().vbom);
		setVisible(false);
		//setScale(2);
	}

	@Override
	public void update() {
		if(isVisible()){
			float dx = (float) (speed*Math.cos(Math.toRadians(-getRotation())));
			float dy = (float) (speed*Math.sin(Math.toRadians(-getRotation())));
			setPosition(getX() + dx, getY() + dy);
			setAlpha(getAlpha() - 0.03f);
			if(getAlpha() <= 0.1){
				setVisible(false);
				setAlpha(1);
			}
		}
	}
	
	public void fire(float x, float y){
		setPosition(x, y);
		setVisible(true);
		float randAngle = ResourcesManager.getInstance().rand.nextFloat()*360;
		setRotation(randAngle);
		speed = ResourcesManager.getInstance().rand.nextFloat()*3 + 2;
	}

}
