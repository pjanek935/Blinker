package particle;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;

import main.ResourcesManager;

public class DirtParticle extends Sprite implements Particle {

	private float speed = 4;
	private float scaleSpeed = 0.05f;
	private float rotationSpeed = 0.01f;
	
	public DirtParticle() {
		super(0, 0, ResourcesManager.getInstance().dirt, ResourcesManager.getInstance().vbom);
		setVisible(false);
	}

	@Override
	public void update() {
		if(isVisible()){
			float dx = (float) (speed*Math.cos(Math.toRadians(-getRotation())));
			float dy = (float) (speed*Math.sin(Math.toRadians(-getRotation())));
			setPosition(getX() + dx, getY() + dy);
			setAlpha(getAlpha() - 0.01f);
			setRotation(getRotation() + rotationSpeed);
			speed -= 0.12f;
			if(speed <= 0){
				speed = 0;
			}
			if(getAlpha() > 0.8){
				setScale(getScaleX() + scaleSpeed);
			}else{
				if(getScaleX() > 1){
					setScale(getScaleX() - scaleSpeed);
				}
				
			}
			if(getAlpha() <= 0.1){
				setVisible(false);
				setAlpha(1);
			}
		}
	}
	
	public void fire(float x, float y){
		setScale(1);
		setPosition(x, y);
		setVisible(true);
		float randAngle = ResourcesManager.getInstance().rand.nextFloat()*360;
		setRotation(randAngle);
		speed = ResourcesManager.getInstance().rand.nextFloat()*4 + 2;
		scaleSpeed = ResourcesManager.getInstance().rand.nextFloat()*0.05f;
		rotationSpeed = ResourcesManager.getInstance().rand.nextFloat() * 5f - 2.5f;
	}

}
