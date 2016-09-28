package particle;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;

import main.ResourcesManager;

public class FootprintParticle extends Sprite implements Particle{

	public FootprintParticle() {
		super(0, 0, ResourcesManager.getInstance().footprint, ResourcesManager.getInstance().vbom);
		setVisible(false);
	}

	@Override
	public void update() {
		if(isVisible()){
			setAlpha(getAlpha() - 0.0005f);
			if(getAlpha() <= 0.01f){
				setVisible(false);
			}
		}
	}
	
	public void fire(float x, float y){
		setPosition(x, y);
		setAlpha(0.34f);
		setVisible(true);
	}

}
