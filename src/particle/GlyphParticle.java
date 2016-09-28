package particle;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;

import main.ResourcesManager;

public class GlyphParticle extends Sprite implements Particle{
	
	private float speed = 6;
	
	public GlyphParticle() {
		super(0, 0, ResourcesManager.getInstance().glyph_particle, ResourcesManager.getInstance().vbom);
		speed = ResourcesManager.getInstance().rand.nextFloat()*3 + 2;
		setVisible(false);
	}

	@Override
	public void update() {
		if(isVisible()){
			float dx = (float) (12*Math.cos(getRotation()));
			float dy = (float) (12*Math.sin(getRotation()));
			setPosition(getX() + dx, getY() + dy);
			setScale(getScaleX() + 0.4f);
			setAlpha(getAlpha() - 0.1f);
			if(getAlpha() <= 0.01){
				setVisible(false);
			}
		}
		
	}
	
	public void fire(){
		setAlpha(1);
		setScale(0.1f);
		setVisible(true);
		float angle = ResourcesManager.getInstance().rand.nextFloat()*360;
		setRotation(angle);
	}

}
