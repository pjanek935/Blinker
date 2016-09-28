package scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;
import main.ResourcesManager;
import scene.SceneManager.SceneType;

public abstract class BaseScene extends Scene {

	protected Engine engine;
    protected Activity activity;
    protected ResourcesManager resourcesManager;
    protected VertexBufferObjectManager vbom;
    protected ZoomCamera camera;
    
    private Entity background;
    private Entity background2;
    private Entity middleground;
    private Entity foreground;
    private Entity foreground2;
    
    public BaseScene(){
        this.resourcesManager = ResourcesManager.getInstance();
        this.engine = resourcesManager.engine;
        this.activity = resourcesManager.activity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        background = new Entity();
        background2 = new Entity();
        middleground = new Entity();
        foreground = new Entity();
        foreground2 = new Entity();
        attachChild(background);
        attachChild(background2);
        attachChild(middleground);
        attachChild(foreground);
        attachChild(foreground2);
        createScene();
    }
    
    public void attachChildAtBackground(IEntity pEntity){
    	background.attachChild(pEntity);
    }
    public void attachChildAtBackground2(IEntity pEntity){
    	background2.attachChild(pEntity);
    }
    public void attachChildAtMiddleground(IEntity pEntity){
    	middleground.attachChild(pEntity);
    }
    public void attachChildAtForeground(IEntity pEntity){
    	foreground.attachChild(pEntity);
    }
    public void attachChildAtForeground2(IEntity pEntity){
    	foreground2.attachChild(pEntity);
    }
    
    public IEntity getBackgroundLayer(){
    	return background;
    }
    public IEntity getMiddlegroundLayer(){
    	return middleground;
    }
    public IEntity getForegroundLayer(){
    	return foreground;
    }
    
    public abstract void createScene();
    
    public abstract void onBackKeyPressed();
    
    public abstract SceneType getSceneType();
    
    public abstract void disposeScene();
}
