package projectiles;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Fixture;

import main.BaseScene;
import player.Player;

public class DestroyableBouncingBullet extends BigBullet {

	public boolean bouncing = true;
	
	public DestroyableBouncingBullet(PhysicsWorld physicsWorld, BaseScene scene, float elasticity) {
		super(physicsWorld, scene, elasticity);
	}
	
	@Override
	public void destroy(Fixture whatDestroyed) {
		if(bouncing){
			if(whatDestroyed.getBody().getUserData() instanceof Player ||
					whatDestroyed.getBody().getUserData() instanceof Bullet){
				super.destroy(whatDestroyed);
			}
		}else{
			super.destroy(whatDestroyed);
		}
		
		
	}

}
