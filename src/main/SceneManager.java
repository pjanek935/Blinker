package main;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import particle.ParticleManager;
import scene.AreYouSureScene;
import world.StageType;
import world.World;

public class SceneManager {

	private BaseScene splashScene;
    private BaseScene mainMenuScene;
    private BaseScene gameScene;
    private BaseScene loadingScene;
    private BaseScene stageSelectScene;
    private BaseScene optionsScene;
    private BaseScene areYouSureScene;
    private BaseScene analogAdjustScene;
    private BaseScene exitScene;
    private BaseScene scoreScene;
    private BaseScene setNameScene;
    private BaseScene whatsNewScene;
	
    private static final SceneManager INSTANCE = new SceneManager();
    
    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
    
    private BaseScene currentScene;
    
    private Engine engine = ResourcesManager.getInstance().engine;
    
    public enum SceneType{
        SCENE_SPLASH,
        SCENE_MAIN_MENU,
        SCENE_STAGE_SELECT_MENU,
        SCENE_GAME,
        SCENE_LOADING,
        SCENE_OPTIONS,
        SCENE_ARE_YOU_SURE,
        SCENE_ANALOG_ADJUST,
        SCENE_EXIT,
        SCENE_SCORE,
        SCENE_SET_NAME,
        SCENE_WHATS_NEW
    }
    
    public void setScene(BaseScene scene){
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }
    
    public void setScene(SceneType sceneType){
        switch (sceneType){
            case SCENE_MAIN_MENU:
                setScene(mainMenuScene);
                break;
            case SCENE_GAME:
                setScene(gameScene);
                break;
            case SCENE_SPLASH:
                setScene(splashScene);
                break;
            case SCENE_LOADING:
                setScene(loadingScene);
                break;
            case SCENE_STAGE_SELECT_MENU:
            	setScene(stageSelectScene);
            case SCENE_OPTIONS:
            	setScene(optionsScene);
            case SCENE_ARE_YOU_SURE:
            	setScene(areYouSureScene);
            	break;
            case SCENE_ANALOG_ADJUST:
            	setScene(analogAdjustScene);
            	break;
            case SCENE_SET_NAME:
            	setScene(setNameScene);
            	break;
            case SCENE_EXIT:
            	setScene(exitScene);
            	break;
            case SCENE_SCORE:
            	setScene(scoreScene);
            	break;
            case SCENE_WHATS_NEW:
            	setScene(whatsNewScene);
            	break;
            default:
                break;
        }
    }
    
    public void loadExitScene(){
    	exitScene = new ExitScene();
    	ParticleManager.getInstance().changeMistAtMenuScreen(exitScene);
		setScene(exitScene);
    }
    
    public void loadOptionsScene(){
    	optionsScene = new OptionsScene();
    	ParticleManager.getInstance().changeMistAtMenuScreen(optionsScene);
		setScene(optionsScene);
    }
    
    public void loadMainMenuFromOptions(){
    	mainMenuScene = new MainMenuScene();
    	ParticleManager.getInstance().changeMistAtMenuScreen(mainMenuScene);
		setScene(mainMenuScene);
    }
    
    public void loadScoreScene(){
    	scoreScene = new ScoreScene();
    	ParticleManager.getInstance().changeMistAtMenuScreen(scoreScene);
    	setScene(scoreScene);
    }
    
    public void loadStageSelectSceneFromScoreScene(){
    	stageSelectScene = new StageSelectScene();
    	ParticleManager.getInstance().changeMistAtMenuScreen(stageSelectScene);
    	setScene(stageSelectScene);
    }
    
    public void loadSetNameScene(){
    	setNameScene = new SetNameScene();
    	ParticleManager.getInstance().changeMistAtMenuScreen(setNameScene);
    	setScene(setNameScene);
    }
    
    public void loadSetNameForTheFirstTime(final Engine mEngine){
    	setScene(loadingScene);
    	ParticleManager.getInstance().changeMistAtMenuScreen(loadingScene);
    	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().loadStageSelectResources();
				setNameScene = new SetNameScene();
		    	ParticleManager.getInstance().changeMistAtMenuScreen(setNameScene);
		    	setScene(setNameScene);
				/*stageSelectScene = new StageSelectScene();
				setScene(stageSelectScene);
				ParticleManager.getInstance().changeMistAtMenuScreen(stageSelectScene);*/
				
			}
		}));
    }
    
    public void loadWhatsNewScene(final Engine mEngine){
    	setScene(loadingScene);
    	ParticleManager.getInstance().changeMistAtMenuScreen(loadingScene);
    	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().loadStageSelectResources();
				whatsNewScene = new WhatsNewScene();
		    	ParticleManager.getInstance().changeMistAtMenuScreen(whatsNewScene);
		    	setScene(whatsNewScene);
				
			}
		}));
    }
    
    public void loadAreYouSureScene(){
    	areYouSureScene = new AreYouSureScene();
    	ParticleManager.getInstance().changeMistAtMenuScreen(areYouSureScene);
		setScene(areYouSureScene);
    }
    
    public void loadAnalogAdjustScene(){
    	analogAdjustScene = new AnalogAdjustScene();
    	ParticleManager.getInstance().changeMistAtMenuScreen(analogAdjustScene);
		setScene(analogAdjustScene);
    }
    
    public void loadEpilogueStage(final Engine mEngine){
    	setScene(loadingScene);
    	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().loadStageTutorial();
				World.stageType = StageType.STAGE_EPILOGUE;
				gameScene.disposeScene();
				gameScene = new GameScene();
				setScene(gameScene);
			}
		}));
    }
    
    public void loadGameScene(final Engine mEngine, final StageType stageType){
    	setScene(loadingScene);
    	ParticleManager.getInstance().disableMistAtMenu();
    	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().loadGameResources(stageType);
				ResourcesManager.getInstance().unloadStageSelectResources();
				World.stageType = stageType;
				gameScene = new GameScene();
				setScene(gameScene);
			}
		}));
    }
    
    public void loadStageSelectSceneFromMainMenu(final Engine mEngine){
    	setScene(loadingScene);
    	ParticleManager.getInstance().changeMistAtMenuScreen(loadingScene);
    	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().loadStageSelectResources();
				stageSelectScene = new StageSelectScene();
				setScene(stageSelectScene);
				ParticleManager.getInstance().changeMistAtMenuScreen(stageSelectScene);
				
			}
		}));
    }
    
    public void loadMainMenuFromStageSelect(final Engine mEngine){
    	setScene(loadingScene);
    	ParticleManager.getInstance().changeMistAtMenuScreen(loadingScene);
    	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().unloadStageSelectResources();
				mainMenuScene = new MainMenuScene();
				setScene(mainMenuScene);
				ParticleManager.getInstance().changeMistAtMenuScreen(mainMenuScene);
			}
		}));
    }
    
    public void loadStageSelectSceneFromGameScene(final Engine mEngine){
    	disposeGameScene();
    	setScene(loadingScene);
    	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().loadStageSelectResources();
				ResourcesManager.getInstance().unloadGameResources();
				stageSelectScene = new StageSelectScene();
				setScene(stageSelectScene);
			}
		}));
    }
    
    public static SceneManager getInstance(){
        return INSTANCE;
    }
    
    public SceneType getCurrentSceneType(){
        return currentSceneType;
    }
    
    public BaseScene getCurrentScene(){
        return currentScene;
    }
    
    public void createMainMenuScene(){
    	ResourcesManager.getInstance().loadMainMenuResources();
    	mainMenuScene = new MainMenuScene();
    	loadingScene = new LoadingScene();
    	setScene(mainMenuScene);
    	disposeSplashScene();
    }
    
    
    public void disposeMainMenuScene(){
    	mainMenuScene.disposeScene();
    	mainMenuScene = null;
    }
    
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback){
        ResourcesManager.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    
    private void disposeSplashScene(){
        splashScene.disposeScene();
        splashScene = null;
    }
    
    
    public void disposeGameScene(){
    	gameScene.disposeScene();
    	gameScene = null;
    }
}
