package main;

import java.util.ArrayList;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;

import com.badlogic.gdx.math.Vector2;

import android.util.Log;


public class MyCamera extends ZoomCamera {
	
	private IEntity chaseEntity;
	private IEntity enemy;
	private ArrayList<Vector2> pos;
	private int bufforSize = 60;
	public boolean follow = false;
	public IUpdateHandler cameraUpdateHandler;
	
	public MyCamera(float pX, float pY, float pWidth, float pHeight){
		super(pX, pY, pWidth, pHeight);
		pos = new ArrayList<Vector2>();
		
		cameraUpdateHandler = new IUpdateHandler(){

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(!follow){
					return;
				}
				float distance = 1;
				
				
				
				distance = (float)Math.sqrt(  Math.pow(chaseEntity.getX() - enemy.getX(), 2) + 
						Math.pow(chaseEntity.getY() - enemy.getY(), 2));
				float scaleX = distance/2500;
				float scaleY = distance/1500;
				
				float[] cameraSceneCoordinates = 
						ResourcesManager.getInstance().camera.getCameraSceneCoordinatesFromSceneCoordinates(
								chaseEntity.getX(), chaseEntity.getY());
				if(cameraSceneCoordinates[0] > 900) scaleX = distance/4000;
				if(cameraSceneCoordinates[0] < 32) scaleX = distance/4000;
				if(cameraSceneCoordinates[1] > 500) scaleY = distance/4000;
				if(cameraSceneCoordinates[1] < 32)   scaleY = distance/4000;
				
				float posX = (chaseEntity.getX() + scaleX*enemy.getX()) / (1 + scaleX);
				float posY = (chaseEntity.getY() + scaleY*enemy.getY()) / (1 + scaleY);
				pos.add(new Vector2(posX, posY));
				
				
				if(pos.size() > bufforSize){
					pos.remove(0);
				}
				if(pos.size() >= bufforSize){
					Vector2 meanPos = new Vector2(0, 0);
					for(Vector2 p : pos){
						meanPos = meanPos.add(p);
					}
					
					meanPos = meanPos.mul(1/(float)bufforSize);
					setCenter(meanPos.x, meanPos.y);
					
					
					
					
				}
			}

			@Override
			public void reset() {}
		};
		
		registerUpdateHandler(cameraUpdateHandler);
	}
	
	@Override
	public void setChaseEntity(IEntity chaseEntity){
		this.chaseEntity = chaseEntity;
		for(int i= 0; i<bufforSize; i++){
			Vector2 p = new Vector2();
			p.x = chaseEntity.getX();
			p.y = chaseEntity.getY();
			pos.add(p);
		}
	}
	
	public void setEnemy(IEntity enemy){
		this.enemy = enemy;
	}
	
	public void dispose(){
		chaseEntity = null;
		enemy = null;
		pos.clear();
	}
	
}
