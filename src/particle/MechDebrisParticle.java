package particle;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import main.ResourcesManager;

public class MechDebrisParticle extends TiledSprite implements Particle {

	private float speed = 4;
	private float rotationSpeed = 0.01f;
	
	public MechDebrisParticle(int x, int y) {
		super(0, 0, ResourcesManager.getInstance().mechDebris, ResourcesManager.getInstance().vbom);
		setCurrentTileIndex(y*4 + x);
		setVisible(false);
	}

	@Override
	public void update() {
		if(isVisible()){
			float dx = (float) (speed*Math.cos(Math.toRadians(-getRotation())));
			float dy = (float) (speed*Math.sin(Math.toRadians(-getRotation())));
			setPosition(getX() + dx, getY() + dy);
			speed -= 0.06f;
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

	}
	
	public void fire(float x, float y){
		setScale(1);
		setPosition(x, y);
		setVisible(true);
		float randAngle = ResourcesManager.getInstance().rand.nextFloat()*360;
		setRotation(randAngle);
		speed = ResourcesManager.getInstance().rand.nextFloat()*3 + 2;
		rotationSpeed = ResourcesManager.getInstance().rand.nextFloat() * 5f - 2.5f;
	}

}
