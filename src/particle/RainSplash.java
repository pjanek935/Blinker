package particle;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;

import main.ResourcesManager;

public class RainSplash extends Sprite implements Particle{

	public RainSplash() {
		super(0, 0, ResourcesManager.getInstance().rain_splash, ResourcesManager.getInstance().vbom);
	}

	@Override
	public void update() {
		if(isVisible()){
			float dx = (float) (0.5*Math.cos(Math.toRadians(-getRotation())));
			float dy = (float) (0.5*Math.sin(Math.toRadians(-getRotation())));
			setPosition(getX() + dx, getY() + dy);
			setAlpha(getAlpha() - 0.01f);
			if(getAlpha() <= 0.1){
				setVisible(false);
				setAlpha(1);
			}
		}
	}

}
