package particle;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import main.ResourcesManager;

public class BenchParticle extends TiledSprite implements Particle{

	private float speed = 4;
	private float rotationSpeed = 0.01f;
	private boolean destroyed = false;
	
	public BenchParticle() {
		super(0, 0, ResourcesManager.getInstance().benchParticle, ResourcesManager.getInstance().vbom);
		setVisible(false);
		setCurrentTileIndex(ResourcesManager.getInstance().rand.nextInt(4));
	}

	@Override
	public void update() {
		if(isVisible()){
			float dx = (float) (speed*Math.cos(Math.toRadians(-getRotation())));
			float dy = (float) (speed*Math.sin(Math.toRadians(-getRotation())));
			setPosition(getX() + dx, getY() + dy);
			setAlpha(getAlpha() - 0.0001f);
			speed -= 0.08f;
			if(speed <= 0){
				speed = 0;
			}else{
				setRotation(getRotation() + rotationSpeed);
				if(rotationSpeed < 0){
					rotationSpeed += 0.01f;
				}else if(rotationSpeed > 0){
					rotationSpeed -= 0.01f;
				}
			}
		}
		if(isVisible() && getAlpha() < 0.01f){
			destroy();
		}
	}
	
	public void destroy(){
		destroyed = true;
		setVisible(false);
		detachSelf();
	}
	
	public boolean isDestroyed(){
		return destroyed;
	}
	
	public void fire(float x, float y){
		setScale(1);
		setPosition(x, y);
		setVisible(true);
		float randAngle = ResourcesManager.getInstance().rand.nextFloat()*360;
		setRotation(randAngle);
		speed = ResourcesManager.getInstance().rand.nextFloat()*2 + 1;
		rotationSpeed = ResourcesManager.getInstance().rand.nextFloat() * 5f - 2.5f;
	}

}
