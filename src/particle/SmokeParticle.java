package particle;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;

import main.ResourcesManager;

public class SmokeParticle extends TiledSprite implements Particle{

	private float speed = 4;
	private float rotationSpeed = 0.01f;
	private float fadeSpeed = 0.05f;
	
	public SmokeParticle() {
		super(0, 0, ResourcesManager.getInstance().dust, ResourcesManager.getInstance().vbom);
		setCurrentTileIndex(ResourcesManager.getInstance().rand.nextInt(3));
		setVisible(false);
	}

	@Override
	public void update() {
		if(isVisible()){
			float dx = (float) (speed*Math.cos(Math.toRadians(-getRotation())));
			float dy = (float) (speed*Math.sin(Math.toRadians(-getRotation())));
			setPosition(getX() + dx, getY() + dy);
			setAlpha(getAlpha() - fadeSpeed);
			setScale(getScaleX() + 0.03f); 
			setRotation(getRotation() + rotationSpeed);
			if(getAlpha() < 0.01f){
				setVisible(false);
			}
		}
	}
	
	public void fire(float x, float y, float angle){
		fadeSpeed = 0.05f;
		setScale(1f);
		setPosition(x, y);
		setVisible(true);
		setAlpha(1);
		float randAngle = ResourcesManager.getInstance().rand.nextFloat()*30 - 15;
		if(angle == -999){
			randAngle = ResourcesManager.getInstance().rand.nextFloat()*360;
		}
		setRotation(angle + randAngle);
		speed = ResourcesManager.getInstance().rand.nextFloat()*5 + 2f;
		rotationSpeed = ResourcesManager.getInstance().rand.nextFloat() * 3f - 1.5f;
	}
	
	public void fire2(float x, float y){
		fadeSpeed = 0.02f;
		setScale(1f);
		setPosition(x, y);
		setVisible(true);
		setAlpha(1);
		float randAngle = ResourcesManager.getInstance().rand.nextFloat()*360;
		setRotation(randAngle);
		speed = ResourcesManager.getInstance().rand.nextFloat()*0.5f + 0.5f;
		rotationSpeed = ResourcesManager.getInstance().rand.nextFloat() * 3f - 1.5f;
	}

}
