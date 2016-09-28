package particle;

import org.andengine.entity.sprite.Sprite;

import com.badlogic.gdx.math.Vector2;

import main.ResourcesManager;

public class MistParticle extends Sprite implements Particle {
	
	public Vector2 bounds = new Vector2(-250, -350);
	public Vector2 startPoint = new Vector2(600, 200);
	public Vector2 randRange = new Vector2(400, 300);
	public float maxAlpha = 0.5f;
	public Vector2 direction = new Vector2(-0.4f, -0.3f);

	public MistParticle() {
		super(0, 0, ResourcesManager.getInstance().mist, ResourcesManager.getInstance().vbom);
		float x = ResourcesManager.getInstance().rand.nextFloat() * 1000 - 500;
		float y = ResourcesManager.getInstance().rand.nextFloat() * 1000 - 500;
		setAlpha(0.5f);
		setPosition(x, y);
		setFlippedHorizontal(ResourcesManager.getInstance().rand.nextBoolean());
		setFlippedVertical(ResourcesManager.getInstance().rand.nextBoolean());
	}

	@Override
	public void update() {
		setPosition(getX() + direction.x , getY() + direction.y);
		if(getX() < bounds.x || bounds.y < -350){
			setAlpha(getAlpha() - 0.02f);
			if(getAlpha() < 0.02f){
				setFlippedHorizontal(ResourcesManager.getInstance().rand.nextBoolean());
				setFlippedVertical(ResourcesManager.getInstance().rand.nextBoolean());
				float randX = ResourcesManager.getInstance().rand.nextFloat()*randRange.x - randRange.x/2f;
				float randY = ResourcesManager.getInstance().rand.nextFloat()*randRange.y - randRange.y/2f;
				setPosition(startPoint.x + randX, startPoint.y + randY);
			}
		}else{
			if(getAlpha() < maxAlpha){
				setAlpha(getAlpha() + 0.02f);
			}
		}
	}

}
