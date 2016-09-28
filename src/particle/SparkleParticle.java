package particle;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;

import main.ResourcesManager;

public class SparkleParticle extends Sprite implements Particle {

	public SparkleParticle() {
		super(0, 0, ResourcesManager.getInstance().sparkle, ResourcesManager.getInstance().vbom);
		setVisible(false);
		setAlpha(0.8f);
	}

	@Override
	public void update() {
		if(isVisible()){
			float dx = (float) (4*Math.cos(Math.toRadians(-getRotation())));
			float dy = (float) (4*Math.sin(Math.toRadians(-getRotation())));
			setPosition(getX() + dx, getY() + dy);
			setAlpha(getAlpha() - 0.06f);
			if(getAlpha() <= 0.1){
				setVisible(false);
				setAlpha(1);
			}
		}
	}

}
