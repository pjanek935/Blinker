package particle;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;

import main.ResourcesManager;

public class ShieldParticle extends Sprite implements Particle {

	private float timerX = 0;
	private float timerY = 0;
	private IEntity owner;
	
	public ShieldParticle(IEntity owner) {
		super(0, 0, ResourcesManager.getInstance().shield_particle, ResourcesManager.getInstance().vbom);
		this.owner = owner;
		setVisible(false);
		setAlpha(0.7f);
	}

	@Override
	public void update() {
		timerX += 0.1f;
		timerY += 0.1f;
		float dx = (float) (20*Math.cos(timerX));
		float dy = (float) (20*Math.sin(timerY));
		setPosition(owner.getX() + dx, owner.getY() + dy);
	}
	
	public void start(){
		setVisible(true);
		timerX = ResourcesManager.getInstance().rand.nextFloat()*3.14f;
		timerY  = ResourcesManager.getInstance().rand.nextFloat()*3.14f;
	}

}
