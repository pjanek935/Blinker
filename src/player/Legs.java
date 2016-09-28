package player;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import main.ResourcesManager;
import particle.ParticleManager;
import scene.BaseScene;

public class Legs implements Element{
	
	private final static int SPAN = 16;
	public final static int STEP_SIZE = 16;
	private float centerX, centerY;
	private float leftD;
	private float rightD;
	private float rotation = 0; //in degrees
	private TiledSprite leftLeg;
	private TiledSprite rightLeg;
	private int step = 0;
	
	public Legs(float centerX, float centerY, BaseScene parentScene, ITiledTextureRegion textureRegion){
		leftD = -STEP_SIZE;
		rightD = STEP_SIZE;
		leftLeg = new TiledSprite(0, 0, textureRegion, ResourcesManager.getInstance().vbom);
		rightLeg = new TiledSprite(0, 0, textureRegion, ResourcesManager.getInstance().vbom);
		parentScene.attachChildAtMiddleground(leftLeg);
		parentScene.attachChildAtMiddleground(rightLeg);
		setRotation(0);
		
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	public void destroy(){
		leftLeg.detachSelf();
		rightLeg.detachSelf();
	}
	
	public void setRotation(float rotation){
		this.rotation = rotation;
		float dX, dY;
		
		leftLeg.setRotation(-rotation);
		dX = (float)((SPAN/2)*Math.cos(Math.toRadians(rotation - 90)));
		dY = (float)((SPAN/2)*Math.sin(Math.toRadians(rotation - 90)));
		leftLeg.setPosition(centerX + dX, centerY + dY);
		
		dX = (float)((leftD)*Math.cos(Math.toRadians(rotation)));
		dY = (float)((leftD)*Math.sin(Math.toRadians(rotation)));
		leftLeg.setPosition(leftLeg.getX() + dX, leftLeg.getY() + dY);
		
		rightLeg.setRotation(-rotation);
		dX = (float)((SPAN/2)*Math.cos(Math.toRadians(rotation + 90)));
		dY = (float)((SPAN/2)*Math.sin(Math.toRadians(rotation + 90)));
		rightLeg.setPosition(centerX + dX, centerY + dY);
		
		dX = (float)((rightD)*Math.cos(Math.toRadians(rotation)));
		dY = (float)((rightD)*Math.sin(Math.toRadians(rotation)));
		rightLeg.setPosition(rightLeg.getX() + dX, rightLeg.getY() + dY);
		
	}
	
	public void nextAnimationStep(float speed){
		if(!leftLeg.isVisible()){
			return;
		}
		if(step == 0){
			rightLeg.setCurrentTileIndex(0);
			leftLeg.setCurrentTileIndex(1);
			step++;
			ParticleManager.getInstance().addFootPrint(leftLeg.getX(), leftLeg.getY());
		}
		if(step == 1){
			float stepX = (float)((speed)*Math.cos(Math.toRadians(rotation)));
			float stepY = (float)((speed)*Math.sin(Math.toRadians(rotation)));
			rightLeg.setPosition(rightLeg.getX() - stepX, rightLeg.getY() - stepY);
			leftLeg.setPosition(leftLeg.getX() + stepX, leftLeg.getY() + stepY);
			rightD -= speed;
			leftD += speed;
			if(rightD <= -STEP_SIZE){
				step++;
			}
		}
		if(step == 2){
			rightLeg.setCurrentTileIndex(1);
			leftLeg.setCurrentTileIndex(0);
			ParticleManager.getInstance().addFootPrint(rightLeg.getX(), rightLeg.getY());
			step++;
		}
		if(step == 3){
			float stepX = (float)((speed)*Math.cos(Math.toRadians(rotation)));
			float stepY = (float)((speed)*Math.sin(Math.toRadians(rotation)));
			rightLeg.setPosition(rightLeg.getX() + stepX, rightLeg.getY() + stepY);
			leftLeg.setPosition(leftLeg.getX() - stepX, leftLeg.getY() - stepY);
			rightD += speed;
			leftD -= speed;
			if(rightD >= STEP_SIZE){
				step++;
			}
		}
		if(step == 4){
			step = 0;
		}
		
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public void stopAnimation(){
		rightLeg.setCurrentTileIndex(0);
		leftLeg.setCurrentTileIndex(0);
	}
	
	public void startAnimation(){
		if(step >= 0 && step < 2){
			rightLeg.setCurrentTileIndex(0);
			leftLeg.setCurrentTileIndex(1);
		}
		if(step >= 2 && step < 4){
			rightLeg.setCurrentTileIndex(1);
			leftLeg.setCurrentTileIndex(0);
		}
		
	}

	@Override
	public float getXInScreenSpace() {
		return centerX;
	}

	@Override
	public float getYInScreenSpace() {
		return centerY;
	}

	@Override
	public void setPositionInScreenSpace(float x, float y) {
		centerX = x;
		centerY = y;
		setRotation(getRotation());
	}

	@Override
	public float getXInWorldSpace() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getYInWorldSpace() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPositionInWorldSpace(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	
	public void setTileIndex(int index){
		leftLeg.setCurrentTileIndex(index);
		rightLeg.setCurrentTileIndex(index);
	}
	
	public void setVisible(boolean visible){
		if(visible){
			leftLeg.setVisible(true);
			rightLeg.setVisible(true);
		}else{
			leftLeg.setVisible(false);
			rightLeg.setVisible(false);
		}
	}
	
	public void setAlpha(float alpha){
		leftLeg.setAlpha(alpha);
		rightLeg.setAlpha(alpha);
	}
	

}
