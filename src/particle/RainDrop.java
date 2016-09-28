package particle;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;

import main.ResourcesManager;

public class RainDrop extends Sprite implements Particle{
	
	public float fadeSpeed = 0.02f;
	public boolean createRainSplash = false;

	public RainDrop(float pX, float pY) {
		super(pX, pY, ResourcesManager.getInstance().raindrop, ResourcesManager.getInstance().vbom);
	}

	@Override
	public void update() {
		if(getScaleX() > 0.03){
			float dx = (float) (6*Math.cos(Math.toRadians(-getRotation())));
			float dy = (float) (6*Math.sin(Math.toRadians(-getRotation())));
			setPosition(getX() - dx, getY() - dy);
			setScaleX(getScaleX() - fadeSpeed);
		}else{
			double randAngle = ResourcesManager.getInstance().rand.nextDouble()*360;
			double dx = 500*Math.cos(Math.toRadians(randAngle));
			double dy = 450*Math.sin(Math.toRadians(randAngle));
			setPosition((float)dx + 1024/2f, (float)dy + 552/2f);
			setRotation(-(float)randAngle);
			setScaleX(3);
			fadeSpeed = ResourcesManager.getInstance().rand.nextFloat()/20 + 0.04f;
			createRainSplash = true;
			
		}	
		
	}

}
