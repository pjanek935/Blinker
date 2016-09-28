package world;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;

import main.ResourcesManager;
import main.UserData;
import scene.BaseScene;
import ui.DialogBox;

public abstract class Stage {
	
	protected BaseScene parentScene;
	protected PhysicsWorld physicsWorld;
	
	private ArrayList<Sprite> visuals;
	protected IEntity player, enemy;
	protected int phase = 0;
	protected DialogBox box;
	protected int counter = 0;
	
	public Stage(BaseScene scene, PhysicsWorld physicsWorld, IEntity player, IEntity enemy){
		this.parentScene = scene;
		this.physicsWorld = physicsWorld;
		visuals = new ArrayList<Sprite>();
		this.enemy = enemy;
		this.player = player;
	}
	
	public Sprite addVisuals(float x, float y, ITextureRegion textureRegion){
		Sprite sp = new Sprite(x, y, textureRegion, ResourcesManager.getInstance().vbom);
		visuals.add(sp);
		parentScene.attachChildAtBackground(sp);
		return sp;
	}
	
	public BaseScene getScene(){
		return parentScene;
	}
	
	public void addVisials(IEntity entity){
		parentScene.attachChildAtBackground(entity);
	}
	
	public void addVisialsAtForeground2(IEntity entity){
		parentScene.attachChildAtForeground2(entity);
	}

	
	public void addWall(float x, float y, float width, float height){
		new Wall(physicsWorld, parentScene, x, y, width, height);
	}
	
	public void addWall(float x, float y, float width, float height, float rotation){
		new Wall(physicsWorld, parentScene, x, y, width, height, rotation);
	}
	
	public void skip(){
		phase = 999;
	}
	
	public void skip(World world){
		world.hud.skipButton.setVisible(false);
		world.readyToStart = true;
		world.hud.setControlsVisible(true);
		if(box != null){
			box.setVisible(false);
		}
		phase++;
		UserData.getInstance().startScoreTimer();
		world.hud.isSkipButtonVisible = false;
	}
	
	public void playerWon(){
		phase = 9999;
	}
	
	public void playerLost(){
		phase = 20000;
	}
	
	public void exitToSetNameScene(){
		
	}
	
	public abstract void loadStage();
	public abstract void stageLogic(World world);
	
}
