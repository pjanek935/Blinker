package main;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Vibrator;
import world.StageType;
import world.World;

public class ResourcesManager {

	private static final ResourcesManager INSTANCE = new ResourcesManager();
    public Engine engine;
    public GameActivity activity;
    public MyCamera camera;
    public VertexBufferObjectManager vbom;
    public Random rand;
    
    //Splash
    public ITextureRegion splash_region;
    public ITextureRegion splash2_region;
    private BuildableBitmapTextureAtlas splashTextureAtlas;
    
    //General
    private BuildableBitmapTextureAtlas generalTextureAtlas;
    public ITextureRegion loading;
    public ITextureRegion analog;
    public ITextureRegion mist;
    
    //Stage select scene
    private BuildableBitmapTextureAtlas stageSelectAtlas;
    public ITextureRegion stageSelectTileTutorial;
    public ITextureRegion stageSelectTile1;
    public ITextureRegion stageSelectTile2;
    public ITextureRegion stageSelectTile3;
    public ITextureRegion stageSelectTile4;
    public ITextureRegion stageSelectTile5;
    public ITextureRegion stageSelectTileLock;
    
    //Characters
    private BuildableBitmapTextureAtlas charactersAtlas;
    public TiledTextureRegion player_leg;
    public TiledTextureRegion player_body;
    public TiledTextureRegion mole_leg;
    public TiledTextureRegion mole_body;
    public TiledTextureRegion fist_body;
    public TiledTextureRegion fist_barrel;
    public TiledTextureRegion witch_leg;
    public TiledTextureRegion witch_body;
    public TiledTextureRegion box;
    public TiledTextureRegion swarm_body;
    public TiledTextureRegion swarm_leg;
    public TiledTextureRegion drone;
    public TiledTextureRegion phantom_body;
    public TiledTextureRegion phantom_leg;
    public TiledTextureRegion kid_body;
    public ITextureRegion mound;
    public ITextureRegion player_bullet;
    public ITextureRegion healing_bullet;
    public ITextureRegion big_bullet;
    public ITextureRegion super_bullet;
    public ITextureRegion death_circle;
    public ITextureRegion shield;
    public ITextureRegion homing_missile;
    public ITextureRegion glyph;
    public ITextureRegion fire;
    
    //Interface
    private BuildableBitmapTextureAtlas interfaceAntlas;
    public ITextureRegion analog_bg;
    public ITextureRegion arrow_head;
    public ITextureRegion arrow_body;
    public ITextureRegion border;
    public ITextureRegion blink_bar_bg;
    public ITextureRegion hp_bar_bg;
    public ITextureRegion hp_bar;
    public ITextureRegion hp_bar_middle;
    public ITextureRegion enemy_arrow;
    public ITextureRegion dialog_box;
    
    //StageTutorial
    private BuildableBitmapTextureAtlas stageTutorialAtlas;
    public ITextureRegion stage_tutorial;
    public ITextureRegion player;
    public ITextureRegion uncle;
    
    //Stage1
    private BuildableBitmapTextureAtlas stage1Atlas;
    public ITextureRegion stage1_grass;
    public ITextureRegion stage1_tribune;
    public ITextureRegion stage1_tribune2;
    public ITextureRegion stage1_building;
    public ITextureRegion stage1_wall;
    public ITextureRegion stage1_bottom;
    
    //Stage2
    private BuildableBitmapTextureAtlas stage2Atlas;
    private BuildableBitmapTextureAtlas stage2Atlas02;
    public ITextureRegion stage2;
    public ITextureRegion stage2_pool;
    
    //Stage3
    private BuildableBitmapTextureAtlas stage3Atlas;
    public ITextureRegion stage3;
    
    //Stage 4
    private BuildableBitmapTextureAtlas stage4Atlas;
    public ITextureRegion stage4;
    
    //Stage 5
    private BuildableBitmapTextureAtlas stage5Atlas;
    public ITextureRegion stage5;
    private BuildableBitmapTextureAtlas stage52Atlas;
    public ITextureRegion stage5Shadow;
    public ITextureRegion stage5Roof;
    
    
    //Particles
    public TiledTextureRegion dust;
    public ITextureRegion raindrop;
    public ITextureRegion rain_splash;
    public ITextureRegion sparkle;
    public ITextureRegion shield_particle;
    public ITextureRegion glyph_particle;
    public ITextureRegion footprint;
    public ITextureRegion snow;
    public ITextureRegion blizzard;
    public ITextureRegion boom;
    public ITextureRegion debris;
    public ITextureRegion dirt;
    public ITextureRegion bench;
    public TiledTextureRegion benchParticle;
    public TiledTextureRegion leafs;
    public TiledTextureRegion mechDebris;
    private BuildableBitmapTextureAtlas particlesTextureAtlas;
    
    public Font font;
    public Font smallFont;
    
    public Vibrator vibrator;
    public Context context;
    
    private ResourcesManager(){
    	rand = new Random();
    	
    }
    
    public Resources getResources(){
    	return context.getResources();
    }
    
    public void setContext(Context context){
    	this.context = context;
    	vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    
    public void loadMainMenuResources(){
    	loadGeneralResources();
    }
    
    public void loadStageSelectResources(){
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/interface/");
    	stageSelectAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.NEAREST);
    	stageSelectTileTutorial = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stageSelectAtlas, activity, "stage_tile_tutorial.png");
    	stageSelectTile1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stageSelectAtlas, activity, "stage_tile_1.png");
    	stageSelectTile2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stageSelectAtlas, activity, "stage_tile_2.png");
    	stageSelectTile3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stageSelectAtlas, activity, "stage_tile_3.png");
    	stageSelectTile4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stageSelectAtlas, activity, "stage_tile_4.png");
    	stageSelectTile5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stageSelectAtlas, activity, "stage_tile_5.png");
    	stageSelectTileLock = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stageSelectAtlas, activity, "stage_tile_lock.png");
    	
    	try {
			this.stageSelectAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.stageSelectAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}   
    }
    
    public void unloadStageSelectResources(){
    	stageSelectAtlas.unload();
    	stageSelectTileTutorial = null;
    	stageSelectTile1 = null;
    	stageSelectTile2 = null;
    	stageSelectTile3 = null;
    	stageSelectTile4 = null;
    	stageSelectTile5 = null;
    	stageSelectTileLock = null;
    }
    
    public void loadGeneralResources(){
    	loadGameFonts();
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/interface/");
    	generalTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 768, 768, TextureOptions.NEAREST);
    	loading = BitmapTextureAtlasTextureRegionFactory.createFromAsset(generalTextureAtlas, activity, "loading.png");
    	analog = BitmapTextureAtlasTextureRegionFactory.createFromAsset(generalTextureAtlas, activity, "analog.png");
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/particles/");
    	mist = BitmapTextureAtlasTextureRegionFactory.createFromAsset(generalTextureAtlas, activity, "mist.png");
    	
    	try {
			this.generalTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.generalTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}   
    }
    
    public void loadStageTutorial(){
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/stages/");
    	stageTutorialAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024 + 512, 1024, TextureOptions.NEAREST);
    	stage_tutorial = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stageTutorialAtlas, activity, "tutorial.png");
    	player = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stageTutorialAtlas, activity, "character1.png");
    	uncle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stageTutorialAtlas, activity, "character2.png");
    	
    	try {
			this.stageTutorialAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.stageTutorialAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
    }
    
    private void loadStage1(){
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/stages/");

    	stage1Atlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024*2, 1024*2, TextureOptions.NEAREST);
    	stage1_grass = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage1Atlas, activity, "grass3.png");
    	stage1_tribune = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage1Atlas, activity, "tribune.png");
    	stage1_tribune2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage1Atlas, activity, "tribune3.png");
    	stage1_building = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage1Atlas, activity, "building.png");
    	stage1_bottom = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage1Atlas, activity, "bottom.png");
    	stage1_wall = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage1Atlas, activity, "wall.png");
    	
    	try {
			this.stage1Atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.stage1Atlas.load();
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void loadStage2(){
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/stages/");
    	stage2Atlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST);
    	stage2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage2Atlas, activity, "stage2.png");
    	stage2Atlas02 = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST);
    	stage2_pool = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage2Atlas02, activity, "stage2_pool.png");
    	
    	try {
			this.stage2Atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.stage2Atlas.load();
		    this.stage2Atlas02.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		    this.stage2Atlas02.load();
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void loadStage3(){
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/stages/");
    	stage3Atlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024*2, 1024*2, TextureOptions.NEAREST);
    	stage3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage3Atlas, activity, "stage3.png");
    	
    	try {
			this.stage3Atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.stage3Atlas.load();
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void loadStage4(){
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/stages/");
    	stage4Atlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024*2, 1024*2, TextureOptions.NEAREST);
    	stage4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage4Atlas, activity, "stage4.png");
    	
    	try {
			this.stage4Atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.stage4Atlas.load();
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void loadStage5(){
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/stages/");
    	stage5Atlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024*2, 1024*2, TextureOptions.NEAREST);
    	stage5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage5Atlas, activity, "stage5.png");
    	
    	stage52Atlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024*2, 1024*2, TextureOptions.NEAREST);
    	stage5Shadow = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage52Atlas, activity, "stage5_shadow.png");
    	stage5Roof = BitmapTextureAtlasTextureRegionFactory.createFromAsset(stage52Atlas, activity, "stage5_roof.png");
    	
    	try {
			this.stage5Atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.stage5Atlas.load();
			
			this.stage52Atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.stage52Atlas.load();
			
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public void unloadGameResources(){
    	interfaceAntlas.unload();
    	analog_bg = null;
    	arrow_body = null;
    	arrow_head = null;
    	border = null;
    	hp_bar = null;
    	blink_bar_bg = null;
    	hp_bar_bg = null;
    	enemy_arrow = null;
    	dialog_box = null;
    	
    	charactersAtlas.unload();
    	player_leg = null;
    	player_body = null;
    	player_bullet = null;
    	healing_bullet = null;
    	big_bullet = null;
    	super_bullet = null;
    	mole_body = null;
    	mole_leg = null;
    	fist_barrel = null;
    	fist_body = null;
    	witch_body = null;
    	witch_leg = null;
    	mound = null;
    	death_circle = null;
    	box = null;
    	shield = null;
    	homing_missile = null;
    	glyph = null;
    	swarm_body = null;
    	swarm_leg = null;
    	phantom_body = null;
    	phantom_leg = null;
    	kid_body = null;
    	drone = null;
    	fire = null;
    	
    	particlesTextureAtlas.unload();
    	dust = null;
    	raindrop = null;
    	rain_splash = null;
    	sparkle = null;
    	shield_particle = null;
    	leafs = null;
    	glyph_particle = null;
    	footprint = null;
    	snow = null;
    	blizzard = null;
    	boom = null;
    	debris = null;
    	dirt = null;
    	bench = null;
    	benchParticle = null;
    	mechDebris = null;
    	
    	switch(World.stageType){
    	case STAGE_TUTORIAL:
    		stageTutorialAtlas.unload();
        	stage_tutorial = null;
        	player = null;
        	uncle = null;
    		break;
    	case STAGE1:
    		stage1Atlas.unload();
        	stage1_grass = null;
        	stage1_tribune = null;
        	stage1_tribune2 = null;
        	stage1_building = null;
        	stage1_bottom = null;
        	stage1_wall = null;
    		break;
    	case STAGE2:
    		stage2Atlas.unload();
        	stage2Atlas02.unload();
        	stage2 = null;
        	stage2_pool = null;
    		break;
    	case STAGE3:
    		stage3Atlas.unload();
    		stage3 = null;
    		break;
    	case STAGE4:
    		stage4Atlas.unload();
    		stage4 = null;
    		break;
    	case STAGE5:
    		stage5Atlas.unload();
    		stage5 = null;
    		stage52Atlas.unload();
    		stage5Shadow = null;
    		stage5Roof = null;
    		break;
    	case STAGE_EPILOGUE:
    		stage5Atlas.unload();
    		stage5 = null;
    		stage52Atlas.unload();
    		stage5Shadow = null;
    		stage5Roof = null;
    		stageTutorialAtlas.unload();
        	stage_tutorial = null;
        	player = null;
        	uncle = null;
    		break;
    	default:
    		break;
    	}

    }

    public void loadGameResources(StageType stageType){
    	loadGameFonts();
    	
    	//Characters
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/mechs/");
    	charactersAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.NEAREST);
    	player_leg = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "leg_tiled.png", 3, 1);
    	player_body = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "body_tiled.png", 4, 1);
    	healing_bullet = BitmapTextureAtlasTextureRegionFactory.createFromAsset(charactersAtlas, activity, "healing_bullet.png");
    	player_bullet = BitmapTextureAtlasTextureRegionFactory.createFromAsset(charactersAtlas, activity, "bullet2.png");
    	big_bullet = BitmapTextureAtlasTextureRegionFactory.createFromAsset(charactersAtlas, activity, "big_bullet.png");
    	super_bullet = BitmapTextureAtlasTextureRegionFactory.createFromAsset(charactersAtlas, activity, "super_bullet.png");
    	mole_body = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "mole_body.png", 3, 1);
    	mole_leg = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "mole_legs.png", 2, 1);
    	witch_body = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "witch_body.png", 3, 1);
    	witch_leg = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "witch_legs.png", 2, 1);
    	swarm_leg = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "swarm_legs.png", 2, 1);
    	swarm_body = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "swarm_body.png", 3, 1);
    	phantom_leg = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "phantom_legs.png", 2, 1);
    	phantom_body = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "phantom_body.png", 4, 1);
    	kid_body = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "kid_body.png", 1, 1);
    	drone =BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "drone.png", 2, 1);
    	mound = BitmapTextureAtlasTextureRegionFactory.createFromAsset(charactersAtlas, activity, "mound.png");
    	death_circle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(charactersAtlas, activity, "death_circle.png");
    	box = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "box03.png", 2, 1);
    	fist_body = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "fist_body.png", 1, 1);
    	fist_barrel = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "fist_barrel.png", 3, 1);
    	shield = BitmapTextureAtlasTextureRegionFactory.createFromAsset(charactersAtlas, activity, "shield.png");
    	homing_missile = BitmapTextureAtlasTextureRegionFactory.createFromAsset(charactersAtlas, activity, "missile.png");
    	glyph = BitmapTextureAtlasTextureRegionFactory.createFromAsset(charactersAtlas, activity, "glyph.png");
    	fire = BitmapTextureAtlasTextureRegionFactory.createFromAsset(charactersAtlas, activity, "fire.png");

    	//Interface
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/interface/");
    	interfaceAntlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST);
    	analog_bg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(interfaceAntlas, activity, "analog_bg.png");
    	arrow_body = BitmapTextureAtlasTextureRegionFactory.createFromAsset(interfaceAntlas, activity, "arrow_body.png");
    	arrow_head = BitmapTextureAtlasTextureRegionFactory.createFromAsset(interfaceAntlas, activity, "arrow_head.png");
    	border = BitmapTextureAtlasTextureRegionFactory.createFromAsset(interfaceAntlas, activity, "border.png");
    	blink_bar_bg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(interfaceAntlas, activity, "blink_bar_bg.png");
    	hp_bar = BitmapTextureAtlasTextureRegionFactory.createFromAsset(interfaceAntlas, activity, "hp_bar.png");
    	hp_bar_bg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(interfaceAntlas, activity, "hp_bar_bg.png");
    	enemy_arrow = BitmapTextureAtlasTextureRegionFactory.createFromAsset(interfaceAntlas, activity, "enemy_arrow.png");
    	dialog_box = BitmapTextureAtlasTextureRegionFactory.createFromAsset(interfaceAntlas, activity, "dialog_box_fg.png");
    	
    	//Particles
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/particles/");
    	particlesTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST);
    	dust = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(particlesTextureAtlas, activity, "dust2.png", 3, 1);
    	raindrop = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "raindrop.png");
    	rain_splash = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "rain_splash.png");
    	sparkle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "sparkle.png");
    	shield_particle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "shield.png");
    	leafs = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "leafs.png", 5, 2);
    	mechDebris = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "mech_debris.png", 4, 6);
    	glyph_particle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "glyph_particle.png");
    	footprint = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "footprint.png");
    	snow = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "snow.png");
    	blizzard = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "blizzard.png");
    	boom = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "boom.png");
    	debris = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "debris.png");
    	dirt = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "dirt.png");
    	benchParticle = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(charactersAtlas, activity, "bench_particle.png", 4, 1);
    	bench = BitmapTextureAtlasTextureRegionFactory.createFromAsset(particlesTextureAtlas, activity, "bench.png");
    	       
    	try {
    	    this.charactersAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.charactersAtlas.load();
    	    
    	    this.interfaceAntlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.interfaceAntlas.load();
    	    
    	    this.particlesTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.particlesTextureAtlas.load();
    	} catch (final TextureAtlasBuilderException e){
    	        Debug.e(e);
    	}
    	
    	switch(stageType){
    	case STAGE_TUTORIAL:
    		loadStageTutorial();
    		break;
    	case STAGE1:
    		loadStage1();
    		break;
    	case STAGE2:
    		loadStage2();
    		break;
    	case STAGE3:
    		loadStage3();
    		break;
    	case STAGE4:
    		loadStage4();
    		break;
    	case STAGE5:
    		loadStage5();
    		break;
    	default:
    		break;
    	}
    }
    
    private void loadGameFonts(){
    	 FontFactory.setAssetBasePath("gfx/game/fonts/");
    	 final ITexture fontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.NEAREST);
    	 final ITexture smallFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.NEAREST);

    	 font = FontFactory.createFromAsset(activity.getFontManager(), fontTexture, activity.getAssets(), "font.otf", 24, true, Color.WHITE);
    	 font.load();
    	 
    	 smallFont =  FontFactory.createFromAsset(activity.getFontManager(), smallFontTexture, activity.getAssets(), "font.otf", 16, true, Color.WHITE);
    	 smallFont.load();
    }
    
    
    
    public void loadSplashScreen(){
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	splashTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.NEAREST);
    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png");
    	splash2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash2.png");
    	try {
			splashTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			splashTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void unloadSplashScreen(){
    	splashTextureAtlas.unload();
    	splash_region = null;
    	splash2_region = null;
    }
    
    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, GameActivity activity, MyCamera camera, VertexBufferObjectManager vbom){
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static ResourcesManager getInstance(){
        return INSTANCE;
    }
	
}
