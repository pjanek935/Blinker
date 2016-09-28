package particle;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;

import main.ResourcesManager;

public class BoomParticle extends Sprite implements Particle {

	public BoomParticle() {
		super(0, 0, ResourcesManager.getInstance().boom, ResourcesManager.getInstance().vbom);
		setVisible(false);
	}

	@Override
	public void update() {
		if(isVisible()){
			setScale(getScaleX() + 0.2f);
			setAlpha(getAlpha() - 0.2f);
			if(getAlpha() < 0.05){
				setVisible(false);
			}
		}
	}
	
	public void fire(float x, float y){
		setPosition(x, y);
		setVisible(true);
		setAlpha(1);
		setScale(0.1f);
	}

}
